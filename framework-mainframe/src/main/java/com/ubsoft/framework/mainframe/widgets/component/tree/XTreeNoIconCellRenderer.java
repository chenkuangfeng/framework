package com.ubsoft.framework.mainframe.widgets.component.tree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.ubsoft.framework.system.entity.Permission;

public class XTreeNoIconCellRenderer extends DefaultTreeCellRenderer {

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
		setLeafIcon(null);
		setClosedIcon(null);
		setOpenIcon(null);
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

		setLeafIcon(null);
		setClosedIcon(null);
		setOpenIcon(null);
		// setBorderSelectionColor(new Color(199,218,255));

	}
}
