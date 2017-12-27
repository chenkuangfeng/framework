package com.ubsoft.framework.mainframe.widgets.component.table.cell;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.EventObject;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.text.JTextComponent;

import com.borland.dbswing.JdbTable;
import com.borland.dx.dataset.Variant;
import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.mainframe.widgets.component.table.XColumn;
import com.ubsoft.framework.mainframe.widgets.util.DataSetUtil;
import com.ubsoft.framework.mainframe.widgets.util.IconFactory;
import com.ubsoft.framework.metadata.model.widget.table.TableColumnMeta;
import com.ubsoft.framework.system.common.SelectDialog;

public class TableLookupCellEditor implements TableCellEditor, ActionListener {
	private TableCellEditor cellEditor;
	private JButton btnLookup = new JButton();
	protected JdbTable table;
	protected int row;
	protected int col;
	private XColumn column;

	public TableLookupCellEditor(XColumn column, TableCellEditor cellEditor) {
		this.column = column;
		this.cellEditor = cellEditor;
		btnLookup.addActionListener(this);
		btnLookup.setFocusable(false);
		btnLookup.setFocusPainted(false);
		btnLookup.setIcon(IconFactory.getImageIcon("icon/find.png"));
		btnLookup.setMargin(new Insets(0, 0, 0, 0));
	}

	public Component getTableCellEditorComponent(JTable table, Object object, boolean paramBoolean, int row, int column) {
		Component com = cellEditor.getTableCellEditorComponent(table, object, paramBoolean, row, column);
		JPanel panel = new JPanel(new BorderLayout(), true) {
			protected boolean processKeyBinding(KeyStroke paramKeyStroke, KeyEvent paramKeyEvent, int paramInt, boolean paramBoolean) {
				boolean bool = super.processKeyBinding(paramKeyStroke, paramKeyEvent, paramInt, paramBoolean);
				if ((paramInt == 0) && (paramBoolean) && (paramKeyEvent.getKeyChar() >= ' ') && (paramKeyEvent.getKeyChar() <= '~'))
					if ((cellEditor instanceof JTextComponent))
						((JTextComponent) cellEditor).setText(Character.toString(paramKeyEvent.getKeyChar()));
					else if (((cellEditor instanceof JComboBox)) && (((JComboBox) cellEditor).isEditable()))
						((JComboBox) cellEditor).getEditor().setItem(Character.toString(paramKeyEvent.getKeyChar()));
				return bool;
			}
		};
		panel.add(com);
		panel.add(btnLookup, "East");
		this.table = (JdbTable)table;
		this.row = row;
		this.col = column;
		return panel;
	}

	public TableCellEditor getEditor() {
		return cellEditor;
	}

	public Object getCellEditorValue() {
		return cellEditor.getCellEditorValue();
	}

	public boolean isCellEditable(EventObject paramEventObject) {
		return cellEditor.isCellEditable(paramEventObject);
	}

	public boolean shouldSelectCell(EventObject paramEventObject) {
		return cellEditor.shouldSelectCell(paramEventObject);
	}

	public boolean stopCellEditing() {
		return cellEditor.stopCellEditing();
	}

	public void cancelCellEditing() {
		cellEditor.cancelCellEditing();
	}

	public void addCellEditorListener(CellEditorListener paramCellEditorListener) {
		cellEditor.addCellEditorListener(paramCellEditorListener);
	}

	public void removeCellEditorListener(CellEditorListener paramCellEditorListener) {
		cellEditor.removeCellEditorListener(paramCellEditorListener);
	}

	public final void actionPerformed(ActionEvent paramActionEvent) {
		cellEditor.cancelCellEditing();
		TableColumnMeta meta=column.getMeta();
		if(meta!=null&&meta.getSelect()!=null){
			List<Bio> result=SelectDialog.select(meta.getSelect(), table, column.getCondiitionNode(), false);
			Bio bio=result.get(0);
			if(result!=null){
				String fromKey=meta.getSelectField()==null?meta.getField():meta.getSelectField();
				Variant varField=new Variant();
				DataSetUtil.setVariant(varField, column, bio.getObject(fromKey.toUpperCase()));						
				table.getDataSet().setVariant(column.getColumnName(),varField);					
				if(meta.getFromFields()!=null&&meta.getToFields()!=null){
					String [] fFields=meta.getFromFields().split(",");
					String [] tFields=meta.getToFields().split(",");
					for(int i=0;i<fFields.length;i++){
						Variant var=new Variant();
						DataSetUtil.setVariant(var, column, bio.getObject(fFields[i].toUpperCase()));						
						table.getDataSet().setVariant(tFields[i],var);
					}							
				}
				if(column.getLookupListener()!=null){
					column.getLookupListener().lookup(column, result);
				}
			}
		}
		// Variant var = null;
		// if (column.getLookupListener() != null) {
		// column.getLookupListener().lookup(column,var);
		// if (var != null) {
		// try {
		// column.getDataSet().setVariant(column.getColumnName(), var);
		// } catch (Exception localException) {
		// table.requestFocusInWindow();
		// DBExceptionHandler.handleException(column.getDataSet(),
		// localException);
		// }
		// }
		//
		// }
	}

}
