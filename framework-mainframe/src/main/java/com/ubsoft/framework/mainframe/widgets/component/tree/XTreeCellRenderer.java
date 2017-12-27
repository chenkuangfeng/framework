package com.ubsoft.framework.mainframe.widgets.component.tree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.ubsoft.framework.mainframe.widgets.util.IconFactory;
import com.ubsoft.framework.system.entity.Permission;

public class XTreeCellRenderer extends DefaultTreeCellRenderer {

	 protected void customizeStyledLabel(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
    	 DefaultMutableTreeNode treeNode=(DefaultMutableTreeNode)value;  
    	  Object object=treeNode.getUserObject();  
    	Object text=null;
    	if(object instanceof Permission){
    		text=((Permission)object).getPermName();
        }else if(object instanceof TreeNodeModel){
        	text=((TreeNodeModel)object).getText();
        }else{
        	text=value;
        }
    	String stringValue = tree.convertValueToText(text, sel,
                expanded, leaf, row, hasFocus);
      
        setText(stringValue);
    }
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		setLeafIcon(IconFactory.getImageIcon("icon/leaf.gif"));
		setClosedIcon(IconFactory.getImageIcon("icon/folder.gif"));
		setOpenIcon(IconFactory.getImageIcon("icon/folder-open.gif"));
		String stringValue = tree.convertValueToText(value, sel, expanded, leaf, row, hasFocus);

		super.getTreeCellRendererComponent(tree, stringValue, sel, expanded, leaf, row, hasFocus);
		
		customizeStyledLabel(tree, value, sel, expanded, leaf, row, hasFocus);
		return this;
	}

	public void updateUI() {
        super.updateUI();
        updateUIDefaults();
	}
	private void updateUIDefaults() {

		setLeafIcon(IconFactory.getImageIcon("icon/leaf.gif"));
		setClosedIcon(IconFactory.getImageIcon("icon/folder.gif"));
		setOpenIcon(IconFactory.getImageIcon("icon/folder-open.gif"));
		// setBorderSelectionColor(new Color(199,218,255));

	}
}
