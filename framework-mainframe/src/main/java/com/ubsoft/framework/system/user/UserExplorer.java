package com.ubsoft.framework.system.user;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
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
import com.ubsoft.framework.system.entity.UserDimension;
import com.ubsoft.framework.system.entity.UserPermission;
import com.ubsoft.framework.system.service.IUserService;

public class UserExplorer extends ExplorerForm {	
	private XTextField txtUserKey;
	private XCheckBoxTree menuTree;
	private XCheckBoxTree roleTree;
	private XCheckBoxTree dimTree;
	private XButton btnSelectRole;
	private XButton btnDelRole;
	private XButton btnSetPassword;
	private XButton btnSaveSecurity;
	private boolean isModifiedSecurity = false;

	private IUserService userService = RpcProxy.getProxy(IUserService.class);

	@Override
	public void initForm() {
		renderer(getClass().getSimpleName());
	}

	protected void afterRenderer() {
		txtUserKey = (XTextField) this.getComponent("userKey");
		menuTree = (XCheckBoxTree) this.getComponent("menuTree");
		dimTree = (XCheckBoxTree) this.getComponent("dimTree");

		loadLoginUserPermission();
		loadLoginUserDimension();
		// 增加角色
		btnSelectRole = (XButton) this.getComponent("btnSelectRole");
		btnDelRole = (XButton) this.getComponent("btnDelRole");
		btnSaveSecurity = (XButton) this.getComponent("btnSaveSecurity");
		btnSetPassword = (XButton) this.getComponent("btnSetPwd");

		btnSelectRole.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (StringUtil.isNotEmpty(txtUserKey.getText())) {
					List<Bio> result = SelectDialog.select("RoleSelect", btnSelectRole, null, true);
					if (result != null) {
						addRoleDetail(result);
					}
				} else {
					MessageBox.showInfo("用户代码不能为空");
				}
			}
		});
		
		btnSaveSecurity.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (StringUtil.isNotEmpty(txtUserKey.getText())) {
					doSaveSecurity();
				} else {
					MessageBox.showInfo("用户代码不能为空");
				}
			}
		});

		btnDelRole.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doDelLine("userRole");
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

	private void addRoleDetail(List<Bio> selectBios) {
		for (Bio bio : selectBios) {
			StorageDataSet roleDataSet = detailDataSets.get("userRole");
			roleDataSet.insertRow(false);
			roleDataSet.setString("roleKey", bio.getString("ROLEKEY"));
			roleDataSet.setString("roleName", bio.getString("ROLENAME"));	
		}
	}	
	protected void afterLoad() {
		// 加载当前设置用户的权限，并选中		
		checkedUserDimension();
		checkedUserPermission();	
		//期初设置认为不是修改，修改会enable保存权限按钮
		this.isModifiedSecurity=false;
		this.txtUserKey.setEditable(false);
	}

	protected void setToolbarStatus() {
		super.setToolbarStatus();
		if (this.isModifiedSecurity && masterDataSet != null && masterDataSet.rowCount() > 0) {
			btnSaveSecurity.setEnabled(true);
		} else {
			btnSaveSecurity.setEnabled(false);
		}
		if (masterDataSet != null && masterDataSet.rowCount() > 0) {
			btnSetPassword.setEnabled(true);
		} else {
			btnSetPassword.setEnabled(false);
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

	private void checkedUserDimension(){
		dimTree.getCheckBoxTreeSelectionModel().clearSelection();
		List<Bio> dimBios = userService.getUserDimension(txtUserKey.getText());
		List<TreePath>  paths=new ArrayList<TreePath>();		
		for (Bio bio : dimBios) {
			TreePath path = dimTree.findById(bio.getString("DIMVALUE"));
		
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
					List<UserPermission> permList= new ArrayList<UserPermission>();
					for(TreeNodeModel model:permModel){
						//去掉根节点
						if(model.getId().equals("ROOT")){
							continue;
						}
						UserPermission perm=new UserPermission();
						perm.setPermKey(model.getId());
						perm.setUserKey(txtUserKey.getText());
						permList.add(perm);
					}
					List<TreeNodeModel> dimModel=dimTree.getCheckedTreeNodes();
					List<UserDimension> dimList= new ArrayList<UserDimension>();					
					for(TreeNodeModel model:dimModel){			
						//去掉根节点
						if(model.getModule()==null){
							continue;
						}
						UserDimension dim=new UserDimension();
						dim.setUserKey(txtUserKey.getText());
						dim.setDimValue(model.getId());
						dim.setDimKey(model.getModule());					
						dimList.add(dim);
					}
					userService.saveSecurity(txtUserKey.getText(), permList, dimList);
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
		this.btnDelRole.setEnabled(enabled);
		this.btnSelectRole.setEnabled(enabled);

	}
	private void checkedUserPermission(){
		menuTree.getCheckBoxTreeSelectionModel().clearSelection();
		List<Permission> permissions = userService.getUserPermission(txtUserKey.getText());
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

}
