package com.ubsoft.framework.mainframe.widgets.component.table.cell;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class TableHeaderRendererWrapper  implements TableCellRenderer {

	 private TableCellRenderer delegate;
	 
	 public TableHeaderRendererWrapper(TableCellRenderer delegate){
		 this.delegate=delegate;
	 }
	 @Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		 Component c = delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		 
		 JPanel headerPanel=new JPanel();
		 headerPanel.setLayout(new BorderLayout());
		 headerPanel.add(c,BorderLayout.CENTER);
		 headerPanel.add(new JTextField(),BorderLayout.NORTH);
		 
		 //label.setBorder(BorderFactory.createEtchedBorder());
		 return headerPanel;
		 
		 
	}

}
