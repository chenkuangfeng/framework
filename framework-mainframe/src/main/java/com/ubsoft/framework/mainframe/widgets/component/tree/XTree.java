package com.ubsoft.framework.mainframe.widgets.component.tree;

import java.awt.Component;
import java.awt.Container;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.borland.dbswing.JdbTree;
import com.ubsoft.framework.mainframe.widgets.renderer.IRenderer;
import com.ubsoft.framework.mainframe.widgets.renderer.RendererUtil;
import com.ubsoft.framework.metadata.model.widget.WidgetMeta;
import com.ubsoft.framework.metadata.model.widget.tree.TreeMeta;

public class XTree extends JdbTree implements IRenderer {
	public XTree() {
		super();
		// XTreeCellRenderer cellRenderer= new XTreeCellRenderer();
		// cellRenderer.setBackgroundSelectionColor(new Color(199,218,255));
		this.setCellRenderer(new XTreeCellRenderer());

		// this.setCellRenderer(x);

	}

	TreeMeta meta = null;

	@Override
	public TreeMeta getMeta() {
		return meta;
	}

	@Override
	public Component render(WidgetMeta meta, Container parent, Map<String, Object> params) {
		TreeMeta treeMeta = (TreeMeta) meta;
		this.meta = treeMeta;
		JScrollPane sbar = new JScrollPane(this);

		RendererUtil.addComponent(treeMeta, sbar, parent, params);
		return this;
	}

	public void setModel(TreeNodeModel node) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(node);
		if (node.getChildren() != null) {
			addChildren(node, root);
		}
		DefaultTreeModel model = new DefaultTreeModel(root);
		this.setModel(model);

	}

	private void addChildren(TreeNodeModel node, DefaultMutableTreeNode parent) {
		for (TreeNodeModel cNode : node.getChildren()) {
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(cNode);
			parent.add(childNode);
			if (cNode.getChildren() != null) {
				addChildren(cNode, childNode);
			}

		}
	}

	public TreePath findById(String id) {
		Object root = getModel().getRoot();

		TreePath treePath = new TreePath(root);
		treePath = findById(treePath, id);
		if (treePath != null) {			
			//scrollPathToVisible(treePath);
			return treePath;
		}
		return null;

	}
	
	public TreePath findByText(String text) {

		Object root = getModel().getRoot();

		TreePath treePath = new TreePath(root);

		treePath = findByText(treePath, text);

		if (treePath != null) {
			// setSelectionPath(treePath);
			//scrollPathToVisible(treePath);
			return treePath;

		}
		return null;

	}



	private TreePath findById(TreePath treePath, String id) {

		Object object = treePath.getLastPathComponent();
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) object;// 返回最后选定的节点
		if (selectedNode.getUserObject() == null)
			return null;
		String value = ((TreeNodeModel) selectedNode.getUserObject()).getId();

		if (id.equals(value)) {

			return treePath;
		} else {

			TreeModel model = getModel();
			int n = model.getChildCount(object);

			for (int i = 0; i < n; i++) {

				Object child = model.getChild(object, i);

				TreePath path = treePath.pathByAddingChild(child);

				path = findById(path, id);

				if (path != null) {

					return path;
				}

			}

			return null;
		}

	}
	
	private TreePath findByText(TreePath treePath, String text) {

		Object object = treePath.getLastPathComponent();
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) object;// 返回最后选定的节点
		if (selectedNode.getUserObject() == null)
			return null;
		String value = ((TreeNodeModel) selectedNode.getUserObject()).getText();

		if (text.equals(value)) {

			return treePath;
		} else {

			TreeModel model = getModel();
			int n = model.getChildCount(object);

			for (int i = 0; i < n; i++) {

				Object child = model.getChild(object, i);

				TreePath path = treePath.pathByAddingChild(child);
				path = findByText(path, text);
				if (path != null) {
					return path;
				}

			}

			return null;
		}

	}

}
