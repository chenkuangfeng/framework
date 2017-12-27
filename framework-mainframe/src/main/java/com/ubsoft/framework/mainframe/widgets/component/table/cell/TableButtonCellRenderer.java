package com.ubsoft.framework.mainframe.widgets.component.table.cell;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.ubsoft.framework.mainframe.widgets.util.IconFactory;
import com.ubsoft.framework.mainframe.widgets.util.MessageBox;

public  class TableButtonCellRenderer extends DefaultTableCellRenderer implements ActionListener {

	private JButton btnLookup = new JButton();
	
	public TableButtonCellRenderer() {

		btnLookup.addActionListener(this);
		btnLookup.setFocusable(false); 
		btnLookup.setFocusPainted(false);
		btnLookup.setIcon(IconFactory.getImageIcon("icon/find.png"));

		btnLookup.setMargin(new Insets(0, 0, 0, 0));
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		

		// label.setBorder(BorderFactory.createEtchedBorder());
		return btnLookup;

	}

	public final void actionPerformed(ActionEvent paramActionEvent) {
		MessageBox.showError("fff");
	}
}
