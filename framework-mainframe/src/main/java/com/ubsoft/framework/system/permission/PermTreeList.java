package com.ubsoft.framework.system.permission;

import java.util.List;
import java.util.Map;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.borland.dx.dataset.DataSetView;
import com.ubsoft.framework.core.dal.model.ConditionLeafNode;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.mainframe.formbase.ListForm;
import com.ubsoft.framework.mainframe.widgets.component.tree.TreeNodeModel;
import com.ubsoft.framework.mainframe.widgets.component.tree.XTree;
import com.ubsoft.framework.mainframe.widgets.util.FormUtil;
import com.ubsoft.framework.rpc.proxy.RpcProxy;
import com.ubsoft.framework.system.entity.Permission;
import com.ubsoft.framework.system.service.IPermService;

public class PermTreeList extends ListForm {
	private XTree menuTree;
	/** 是否点击刷新按钮，如果点击，刷新tree和list **/
	private boolean bClickRefreshButton = true;
	/** 是否tree选中刷新 **/
	private boolean bSelectedTreeNode = true;

	@Override
	public void initForm() {
		renderer(getClass().getSimpleName());
	}

	@Override
	protected void afterRenderer() {
		this.initTree();

	}

	private void initTree() {
		menuTree = (XTree) this.getComponent("menuTree");
		loadTreeModel();
		// 添加tree事件
		menuTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent evt) {
				
				if (menuTree.getLastSelectedPathComponent() == null) {
					return;
				}
				bClickRefreshButton = false;
				// 用户点击treenode才刷新，保存后选中原来的node不刷新
				if (bSelectedTreeNode) {
					doRefresh();
				}
				bClickRefreshButton=true;
			}

		});
	}

	private void loadTreeModel() {
		IPermService permService = RpcProxy.getProxy(IPermService.class);
		List<Permission> permissions = permService.getMenus();
		TreeNodeModel root = FormUtil.loadTreeModel("permKey", "permName", "permModule", "parentPermKey", permissions, "ROOT", "功能权限");
		menuTree.setModel(root);		
		menuTree.setRootVisible(true);
	}

	

	protected boolean beforeRefresh() {
		// 只有selecttree时候才增加过滤条件
		if (!bClickRefreshButton) {
			if (menuTree.getLastSelectedPathComponent() != null) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) menuTree.getLastSelectedPathComponent();// 返回最后选定的节点
				if (selectedNode.getUserObject() != null) {
					TreeNodeModel nodeModel = (TreeNodeModel) selectedNode.getUserObject();
					ConditionLeafNode leafNode = new ConditionLeafNode("T", "parentPermKey", "=", nodeModel.getId(), null, "and");
					queryModel.getConditionTree().getNodes().add(leafNode);
				}
			}
		} else {
			this.loadTreeModel();
		}
		bClickRefreshButton = true;
		return true;
	}

	protected boolean beforeSave() {
		String parentPermKey = this.getParentPermKey();
		if (parentPermKey != null) {
			mainDataSet.post();
			DataSetView updatedView = new DataSetView();
			DataSetView insertView = new DataSetView();
			mainDataSet.getUpdatedRows(updatedView);
			mainDataSet.getInsertedRows(insertView);
			if (insertView.getRowCount() > 0) {
				mainDataSet.goToRow(insertView.getInternalRow());
				if (StringUtil.isEmpty(mainDataSet.getString("parentPermKey"))) {
					mainDataSet.setString("parentPermKey", parentPermKey);
				}
			}
			if (updatedView.getRowCount() > 0) {
				mainDataSet.goToRow(updatedView.getRow());
				if (mainDataSet.getString("parentPermKey") == null) {
					mainDataSet.setString("parentPermKey", parentPermKey);
					mainDataSet.setString("status", "1");

				}
			}
		}
		return true;
	}

	private String getParentPermKey() {
		if (menuTree.getLastSelectedPathComponent() != null) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) menuTree.getLastSelectedPathComponent();// 返回最后选定的节点
			if (selectedNode.getUserObject() != null) {
				TreeNodeModel nodeModel = (TreeNodeModel) selectedNode.getUserObject();
				return nodeModel.getId();

			}
		}
		return null;
	}

	protected void afterSave(Map result) {
		String selectId=this.getParentPermKey();
	
		this.loadTreeModel();
		// 重新选中
		if (selectId!=null) {
			this.bSelectedTreeNode = false;
			menuTree.findById(selectId);
			bSelectedTreeNode = true;
		}
	}

	protected void afterDelete() {
		String selectId=this.getParentPermKey();
		
		this.loadTreeModel();
		// 重新选中
		if (selectId!=null) {
			this.bSelectedTreeNode = false;
			menuTree.findById(selectId);
			bSelectedTreeNode = true;
		}
	}

}
