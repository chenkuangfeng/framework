package com.ubsoft.framework.mainframe.widgets.component.table.cell;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.borland.dbswing.JdbTable;
import com.borland.dx.dataset.Column;
import com.ubsoft.framework.mainframe.widgets.component.table.XColumn;
import com.ubsoft.framework.mainframe.widgets.util.MessageBox;

public  class TableLinkCellRenderer extends DefaultTableCellRenderer implements ActionListener {
	private JLabel linkButton = new JLabel();
	protected JdbTable table;
	private XColumn column;
	protected int row;
	protected int col;
	private Cursor handCursor = new Cursor(Cursor.HAND_CURSOR); 
	public TableLinkCellRenderer(Column column) {
		this.column=(XColumn) column;
		linkButton.setFocusable(true);

		linkButton.addMouseListener(new MouseAdapter() {   
			 public void mouseClicked(MouseEvent e) {
				 
				 
			 }
			 public void mouseEntered(MouseEvent e) { 
				 linkButton.setCursor(handCursor);    

			 }
			 public void mouseExited(MouseEvent e) {
				 linkButton.setCursor(null); 
			 }
			 
		 });
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
		Component com=super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		if(com instanceof JLabel){
			final JLabel label=(JLabel)com;
			label.setText("<html><font color=blue>" + value  + "</font></html>");
			label.addMouseListener(new MouseAdapter() {   
				 public void mouseClicked(MouseEvent e) {
					 MessageBox.showInfo("fdfdf");
					  
				 }
				 public void mouseEntered(MouseEvent e) { 
					 label.setCursor(handCursor);    

				 }
				 public void mouseExited(MouseEvent e) {
					 label.setCursor(null); 
				 }
				 
			 });
		}
		this.table = (JdbTable)table;
		this.row = row;
		this.col = col;
		//linkButton.setText("<html><font color=blue>" + value  + "</font></html>");
		// label.setBorder(BorderFactory.createEtchedBorder());
		return com;

	}

	public final void actionPerformed(ActionEvent paramActionEvent) {
		customLink(table,col,row);
	}
	
	protected  void customLink(JdbTable table, int col, int row){
		if (column.getLinkListener() != null) {
			table.getDataSet().goToRow(row);
			column.getLinkListener().link(column,table.getDataSet().getRow());
		}
	}

}
