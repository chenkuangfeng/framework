package com.ubsoft.framework.system.role;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.borland.dx.dataset.StorageDataSet;
import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.mainframe.formbase.ExplorerForm;
import com.ubsoft.framework.mainframe.widgets.component.XButton;
import com.ubsoft.framework.mainframe.widgets.component.XTextField;
import com.ubsoft.framework.mainframe.widgets.component.tree.TreeNodeModel;
import com.ubsoft.framework.mainframe.widgets.component.tree.XCheckBoxTree;
import com.ubsoft.framework.mainframe.widgets.util.FormUtil;
import com.ubsoft.framework.mainframe.widgets.util.MessageBox;
import com.ubsoft.framework.rpc.model.SessionContext;
import com.ubsoft.framework.rpc.proxy.RpcProxy;
import com.ubsoft.framework.system.common.SelectDialog;
import com.ubsoft.framework.system.entity.Permission;
import com.ubsoft.framework.system.entity.RoleDimension;
import com.ubsoft.framework.system.entity.RolePermission;
import com.ubsoft.framework.system.service.IRoleService;
import com.ubsoft.framework.system.service.IUserService;

public class RoleExplorer extends ExplorerForm {
	private XTextField txtRoleKey;
	private XCheckBoxTree menuTree;
	private XCheckBoxTree dimTree;
	private XButton btnSelectUser;
	private XButton btnDelUser;
	private XButton btnSaveSecurity;
	private boolean isModifiedSecurity = false;

	private IRoleService roleService = RpcProxy.getProxy(IRoleService.class);
	private IUserService userService = RpcProxy.getProxy(IUserService.class);

	@Override
	public void initForm() {
		renderer(getClass().getSimpleName());
	}

	protected void afterRenderer() {
		txtRoleKey = (XTextField) this.getComponent("roleKey");
		menuTree = (XCheckBoxTree) this.getComponent("menuTree");
		dimTree = (XCheckBoxTree) this.getComponent("dimTree");
		loadLoginUserPermission();
		loadLoginUserDimension();
		// 增加角色
		btnSelectUser = (XButton) this.getComponent("btnSelectUser");
		btnDelUser = (XButton) this.getComponent("btnDelUser");
		btnSaveSecurity = (XButton) this.getComponent("btnSaveSecurity");
		btnSelectUser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (StringUtil.isNotEmpty(txtRoleKey.getText())) {
					List<Bio> result = SelectDialog.select("UserSelect", btnSelectUser, null, true);
					if (result != null) {
						addUserDetail(result);
					}
				} else {
					MessageBox.showInfo("用户代码不能为空");
				}
			}
		});
		
		btnSaveSecurity.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (StringUtil.isNotEmpty(txtRoleKey.getText())) {
					doSaveSecurity();
				} else {
					MessageBox.showInfo("用户代码不能为空");
				}
			}
		});

		btnDelUser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doDelLine("roleUser");
			}
		});

		menuTree.getCheckBoxTreeSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				isModifiedSecurity = true;
				setToolbarStatus();
			}
		});
		dimTree.getCheckBoxTreeSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				isModifiedSecurity = true;
				setToolbarStatus();
			}
		});
	}

	protected void afterRefresh(){
		menuTree.getCheckBoxTreeSelectionModel().clearSelection();
		dimTree.getCheckBoxTreeSelectionModel().clearSelection();
		this.isModifiedSecurity=false;
		this.setToolbarStatus();
	}

	private void addUserDetail(List<Bio> selectBios) {
		for (Bio bio : selectBios) {
			StorageDataSet roleDataSet = detailDataSets.get("roleUser");
			roleDataSet.insertRow(false);
			roleDataSet.setString("userKey", bio.getString("USERKEY"));
			roleDataSet.setString("userName", bio.getString("USERNAME"));	
		}
	}	
	protected void afterLoad() {
		// 加载当前设置用户的权限，并选中		
		checkedRoleDimension();
		checkedRolePermission();	
		//期初设置认为不是修改，修改会enable保存权限按钮
		this.isModifiedSecurity=false;
		this.txtRoleKey.setEditable(false);
	}

	protected void setToolbarStatus() {
		super.setToolbarStatus();
		if (this.isModifiedSecurity && masterDataSet != null && masterDataSet.rowCount() > 0) {
			btnSaveSecurity.setEnabled(true);
		} else {
			btnSaveSecurity.setEnabled(false);
		}
		
	}

	/**
	 * 加载登陆用户所有功能权限
	 */
	private void loadLoginUserPermission() {
		List<Permission> permissions = userService.getPermission(SessionContext.getContext().getUserKey());
		TreeNodeModel root = FormUtil.loadTreeModel("permKey", "permName", "permModule", "parentPermKey", permissions, "ROOT", "系统菜单");
		menuTree.setRootVisible(true);
		menuTree.setModel(root);
	}

	private void checkedRoleDimension(){
		dimTree.getCheckBoxTreeSelectionModel().clearSelection();
		List<Bio> dimBios = roleService.getRoleDimension(txtRoleKey.getText());//getUserDimension(txtUserKey.getText());
		List<TreePath>  paths=new ArrayList<TreePath>();		
		for (Bio bio : dimBios) {
			TreePath path =findDimension(bio.getString("DIMVALUE"),bio.getString("DIMKEY"));		
			if (path != null) {
				paths.add(path);				
			}
		}
		TreePath []  treePaths=new TreePath[paths.size()];
		int i=0;
		for(TreePath path:paths){
			treePaths[i]=path;
			i++;
		}
		//treePaths=(TreePath[])paths.toArray();
		dimTree.getCheckBoxTreeSelectionModel().setSelectionPaths(treePaths);
	
	}
	
	private void doSaveSecurity(){		
		this.setBusy(true);
		setStatusMessage("正在保存......");
		SwingWorker worker = new SwingWorker() {
			@Override
			protected Object doInBackground() throws Exception {
				try {
					//获取所有check节点并转换成bean传到后台，后台先删除，然后保存
					List<TreeNodeModel> permModel=menuTree.getCheckedTreeNodes();
					List<RolePermission> permList= new ArrayList<RolePermission>();
					for(TreeNodeModel model:permModel){
						//去掉根节点
						if(model.getId().equals("ROOT")){
							continue;
						}
						RolePermission perm=new RolePermission();
						perm.setPermKey(model.getId());
						perm.setRoleKey(txtRoleKey.getText());
						permList.add(perm);
					}
					List<TreeNodeModel> dimModel=dimTree.getCheckedTreeNodes();
					List<RoleDimension> dimList= new ArrayList<RoleDimension>();					
					for(TreeNodeModel model:dimModel){			
						//去掉根节点
						if(model.getModule()==null){
							continue;
						}
						RoleDimension dim=new RoleDimension();
						dim.setRoleKey(txtRoleKey.getText());
						dim.setDimValue(model.getId());
						dim.setDimKey(model.getModule());					
						dimList.add(dim);
					}
					roleService.saveSecurity(txtRoleKey.getText(), permList, dimList);
					return 1;// userService.saveForm(meta.getFdm(), getChanges());
				} catch (Exception e) {
					MessageBox.showException(e);
					return null;
				}
			}

			@Override
			public void done() {
				try {
					Object result = get();
					if (result != null) {
						isModifiedSecurity=false;
						btnSaveSecurity.setEnabled(false);
					
					}
					setStatusMessage("就绪");

					setBusy(false);
				} catch (Exception e) {
					setBusy(false);
					loading = false;
					setStatusMessage("就绪");
					MessageBox.showException(e);
				}
			}

		};
		worker.execute();
	}
	protected void afterNew(){
		menuTree.getCheckBoxTreeSelectionModel().clearSelection();
		dimTree.getCheckBoxTreeSelectionModel().clearSelection();
		this.isModifiedSecurity=false;
		this.setToolbarStatus();
	}
	
	protected void setFormEditable(boolean enabled){
		super.setFormEditable(enabled);
		menuTree.setEnabled(enabled);
		dimTree.setEnabled(enabled);
		this.btnDelUser.setEnabled(enabled);
		this.btnSelectUser.setEnabled(enabled);

	}
	private void checkedRolePermission(){
		menuTree.getCheckBoxTreeSelectionModel().clearSelection();
		List<Permission> permissions = roleService.getRolePermission(txtRoleKey.getText());
		List<TreePath>  paths=new ArrayList<TreePath>();		
		for (Permission permission : permissions) {
			TreePath path = menuTree.findById(permission.getPermKey());		
			if (path != null) {
				paths.add(path);				
			}
		}
		TreePath []  treePaths=new TreePath[paths.size()];
		int i=0;
		for(TreePath path:paths){
			treePaths[i]=path;
			i++;
		}
		//treePaths=(TreePath[])paths.toArray();
		menuTree.getCheckBoxTreeSelectionModel().setSelectionPaths(treePaths);
		//this.isModifiedSecurity=false;
	}
	/**
	 * 加载登陆用户所有数据权限
	 */
	private void loadLoginUserDimension() {
		List<Bio> dimBios = userService.getDimension(SessionContext.getContext().getUserKey());
		TreeNodeModel root = FormUtil.loadTreeModelFormBio("DIMVALUE", "DIMTEXT", "DIMKEY", "PID", dimBios, "ROOT", "数据维度");
		dimTree.setRootVisible(true);
		dimTree.setModel(root);

	}
	
	
	private TreePath findDimension(TreePath treePath, String id,String module) {

		Object object = treePath.getLastPathComponent();
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) object;// 返回最后选定的节点
		if (selectedNode.getUserObject() == null)
			return null;
		String value = ((TreeNodeModel) selectedNode.getUserObject()).getId();
		String mod=((TreeNodeModel) selectedNode.getUserObject()).getModule();
		if (id.equals(value)&&mod.equals(module)) {
			return treePath;
		} else {

			TreeModel model = dimTree.getModel();
			int n = model.getChildCount(object);

			for (int i = 0; i < n; i++) {

				Object child = model.getChild(object, i);

				TreePath path = treePath.pathByAddingChild(child);

				path = findDimension(path, id,module);

				if (path != null) {

					return path;
				}

			}

			return null;
		}

	}
	private TreePath findDimension(String id,String module) {
		Object root = dimTree.getModel().getRoot();

		TreePath treePath = new TreePath(root);

		treePath = findDimension(treePath, id,module);

		if (treePath != null) {			
			//scrollPathToVisible(treePath);
			//getCheckBoxTreeSelectionModel().setSelectionPath(treePath);
			return treePath;

		}
		return null;

	}
}
