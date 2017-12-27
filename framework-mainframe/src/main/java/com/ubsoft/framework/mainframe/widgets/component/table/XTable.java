package com.ubsoft.framework.mainframe.widgets.component.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.borland.dbswing.JdbTable;
import com.borland.dbswing.TableScrollPane;
import com.borland.dx.dataset.StorageDataSet;
import com.ubsoft.framework.mainframe.widgets.renderer.IRenderer;
import com.ubsoft.framework.mainframe.widgets.renderer.RendererUtil;
import com.ubsoft.framework.metadata.model.widget.WidgetMeta;
import com.ubsoft.framework.metadata.model.widget.table.TableMeta;

public class XTable extends JdbTable implements IRenderer {


	public XTable() {
		
		gridColor = Color.LIGHT_GRAY;
		setRowHeaderVisible(false);	
//		TableRowHeader rowHeader=new TableRowHeader(this);
//		TableRowNoRenderer rowCellRenderer= new TableRowNoRenderer();
//		rowCellRenderer.setText("888888888");
//		rowCellRenderer.setSize(new Dimension(100, 40));
//		rowHeader.setCellRenderer(rowCellRenderer);
//		rowHeader.setResizeTableWhileSizing(false);
//		this.setRowHeader(rowHeader);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		this.setCellSelectionEnabled(true);
		//this.setEditableFocusedCellBackground(Color.LIGHT_GRAY);
		this.setSelectionBackground(new Color(199,218,255));
		this.setSelectionForeground(Color.black);
	//	this.setFont(new Font("宋体", Font.PLAIN, 12));
	
		this.setSelectionMode(2);
		
		//this.setDefaultRenderer(columnClass, renderer);
		
		if (this.getDataSet() == null) {
			StorageDataSet dataSet = new StorageDataSet();			
			dataSet.open();
			this.setDataSet(dataSet);
		}

	}

	protected boolean isCellValid(TableCellEditor paramTableCellEditor)
	  {
		return true;
	  }
//	 protected JTableHeader createDefaultTableHeader()
//	  {
//		 JTableHeader th = new JTableHeader() {
//	      public boolean isFocusTraversable() {
//	        return true;
//	      }
//	      public boolean isRequestFocusEnabled() {
//	        return true;
//	      }
//	    };
//	    //DefaultTableCellRenderer
//	    th.setDefaultRenderer(new TableHeaderRenderer());
//	    return th;
//	  }
//
//	int i=0;
	public void addColumn(XColumn col) {
		if (this.getDataSet() != null) {
			StorageDataSet ds = (StorageDataSet) this.getDataSet();
			ds.addColumn(col);
		}
//		TableColumn tc = getColumnModel().getColumn(i);
//		tc.setHeaderRenderer(new TableHeaderRendererWrapper(new DefaultTableCellHeaderRenderer()));
//			i++;
//			TableColumn tc1 = getColumnModel().getColumn(1);
//			tc1.setHeaderRenderer(new TableHeaderRendererWrapper(new DefaultTableCellHeaderRenderer()));
		}

		

	

	@Override
	public Component render(WidgetMeta meta, Container parent, Map<String, Object> params) {
		TableMeta tableMeta = (TableMeta) meta;
		this.meta = tableMeta;
		TableScrollPane lstTablePane = new TableScrollPane();
		lstTablePane.setBorder(null);
		this.setBorder(null);
		
		lstTablePane.setViewportView(this);
		// table特殊处理，只添加scrollPanel;
		RendererUtil.addComponent(tableMeta, lstTablePane, parent,params);
		return this;
	}
//	 public void mouseClicked(MouseEvent e)
//	  {
//	    int tableColumn;
//	    String ss="";
//	    if ((e.getSource() == getTableHeader()) && ((tableColumn = getTableHeader().columnAtPoint(e.getPoint())) != -1))
//	    {
//	    	
//	    	int modelColumn = convertColumnIndexToModel(tableColumn);
//	    	MessageBox.showInfo(e.getSource()+"");
//	    	JTableHeader th=getTableHeader();
//	    	TableColumn col=th.getColumnModel().getColumn(modelColumn);
//	    	//col.setCellEditor(col.getHeaderRenderer());
//	    	//th.getColumnModel().getColumn(modelColumn).setCellEditor(cellEditor)
////	      int modelColumn = convertColumnIndexToModel(tableColumn);
////	      TableModel tableModel = getModel();
////	      if ((modelColumn >= 0) && (modelColumn < tableModel.getColumnCount()) && (this.dataSet != null) && ((tableModel instanceof DBTableModel)))
////	      {
////	        Frame frame = DBUtilities.getFrame(this);
////	        try {
////	          Column column = ((DBTableModel)tableModel).getColumn(modelColumn);
////	          if ((this.dataSet.isSortable(column)) && (!isEditing()) && (!this.dataSet.isEditing())) {
////	            boolean resetAutoSelection = this.autoSelection;
////	            this.autoSelection = false;
////	            frame.setCursor(Cursor.getPredefinedCursor(3));
////	            this.dataSet.toggleViewOrder(column.getColumnName());
////	            frame.setCursor(Cursor.getPredefinedCursor(0));
////	            this.autoSelection = resetAutoSelection;
////	          }
////	        }
////	        catch (Exception x) {
////	          DBExceptionHandler.handleException(this.dataSet, this, x);
////	          frame.setCursor(Cursor.getPredefinedCursor(0));
////	        }
////	      }
//	    }
//	  }
//	public void initializeLocalVars(){
//		super.initializeLocalVars();
//	
//		//TableColumn tc1 = getColumnModel().getColumn(0);
//		//tc1.setHeaderRenderer(new TableHeaderRendererWrapper(new DefaultTableCellHeaderRenderer()));
//		
//
//
//		
//	}
	public void mousePressed(MouseEvent paramMouseEvent) {

	}

	public void mouseEntered(MouseEvent paramMouseEvent) {
	}

	public void mouseExited(MouseEvent paramMouseEvent) {
	}

	public void mouseReleased(MouseEvent paramMouseEvent) {

	}

	TableMeta meta = null;

	@Override
	public TableMeta getMeta() {
		return meta;
	}

}
