package com.ubsoft.framework.mainframe.widgets.component.table;

import java.awt.Component;
import java.awt.Container;
import java.util.List;
import java.util.Map;

import com.borland.dbswing.TableCustomCellEditor;
import com.borland.dbswing.TableMaskCellEditor;
import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.PickListDescriptor;
import com.borland.dx.dataset.Variant;
import com.ubsoft.framework.core.dal.model.ConditionNode;
import com.ubsoft.framework.core.dal.util.TypeUtil;
import com.ubsoft.framework.mainframe.widgets.component.table.cell.TableCellDateTimePicker;
import com.ubsoft.framework.mainframe.widgets.component.table.cell.TableLinkCellRenderer;
import com.ubsoft.framework.mainframe.widgets.component.table.cell.TableLookupCellEditor;
import com.ubsoft.framework.mainframe.widgets.event.LinkColumnListener;
import com.ubsoft.framework.mainframe.widgets.event.LookupColumnListener;
import com.ubsoft.framework.mainframe.widgets.renderer.IRenderer;
import com.ubsoft.framework.mainframe.widgets.util.LookupManager;
import com.ubsoft.framework.mainframe.widgets.util.MessageBox;
import com.ubsoft.framework.metadata.model.widget.WidgetMeta;
import com.ubsoft.framework.metadata.model.widget.model.ColumnType;
import com.ubsoft.framework.metadata.model.widget.table.TableColumnMeta;

public class XColumn extends Column implements IRenderer {

	transient LookupColumnListener lookupListener;
	transient LinkColumnListener linkListener;
	/** 弹出框默认过滤条件 **/
	private List<ConditionNode> condiitionNode;
	TableColumnMeta meta = null;

	public LinkColumnListener getLinkListener() {
		return linkListener;
	}

	public void addLinkListener(LinkColumnListener linkListener) {
		this.linkListener = linkListener;
	}

	public LookupColumnListener getLookupListener() {
		return lookupListener;
	}

	public void addLookupListener(LookupColumnListener lookupListener) {
		this.lookupListener = lookupListener;
	}

	public Component render(WidgetMeta meta, Container parent, Map<String, Object> params) {
		TableColumnMeta colMeta = (TableColumnMeta) meta;
		this.meta = colMeta;
		XTable table = (XTable) parent;
		this.setColumnName(colMeta.getField());
		this.setCaption(colMeta.getLabel());
		if (meta.getWidth() != null) {
			this.setWidth(Integer.parseInt(meta.getWidth()));
		}
		// 设置隐藏
		if (meta.getVisible() != null) {
			if (!meta.getVisible())
				this.setVisible(0);

		}
		// // 设置是否编辑
		if (meta.getEditable() != null) {
			this.setEditable(meta.getEditable());
		}
		if (meta.getAlign() != null) {
			String align = meta.getAlign();
			if (align.equals("center")) {
				this.setAlignment(2);
			} else if (align.equals("right")) {
				this.setAlignment(3);

			}
		}
		String dataType = colMeta.getDataType();

		if (dataType == null) {
			dataType = TypeUtil.STRING;
		}

		String type = colMeta.getType();
		if (type != null) {
			if (type.equals(ColumnType.DATE)) {
				dataType = TypeUtil.DATE;
			} else if (type.equals(ColumnType.DATETIME)) {
				dataType = TypeUtil.TIMESTAMP;
			}
			// 设置列类型
			this.genColumnByType(type);
		}
		// 设置数据类型
		dataType = dataType.toLowerCase();
		this.setColumnDataType(dataType);
		// 下拉框数据
		String code = colMeta.getCode();
		if (code != null) {
			try {
				DataSet ds = LookupManager.getLookupDataSet(code);
				PickListDescriptor pd = new PickListDescriptor(ds, new String[] { "value" }, new String[] { "text" },
						new String[] { colMeta.getField() }, "text", true);
				this.setPickList(pd);
			} catch (Exception ex) {
				MessageBox.showException(ex);
			}

		}
		table.addColumn(this);
		return null;
	}

	private void setColumnDataType(String dataType) {
		int variantDataType = -1;
		if (dataType.equals(TypeUtil.STRING)) {
			variantDataType = Variant.STRING;
		} else if (dataType.equals(TypeUtil.INTEGER)||dataType.equals(TypeUtil.INT)) {
			variantDataType = Variant.INT;
		} else if (dataType.equals(TypeUtil.DOUBLE)) {
			variantDataType = Variant.DOUBLE;
		} else if (dataType.equals(TypeUtil.BIGDECIMAL)) {
			variantDataType = Variant.BIGDECIMAL;
		} else if (dataType.equals(TypeUtil.LONG)) {
			variantDataType = Variant.LONG;
		} else if (dataType.equals(TypeUtil.SHORT)) {
			variantDataType = Variant.SHORT;
		} else if (dataType.equals(TypeUtil.FLOAT)) {
			variantDataType = Variant.FLOAT;
		} else if (dataType.equals(TypeUtil.DATE)) {
			variantDataType = Variant.DATE;
		} else if (dataType.equals(TypeUtil.TIMESTAMP)) {
			variantDataType = Variant.TIMESTAMP;
		} else if (dataType.equals(TypeUtil.BYTE_ARRAY)) {
			variantDataType = Variant.BYTE_ARRAY;
		} else {
			MessageBox.showError("不支持数据类型:" + dataType);
		}
		setDataType(variantDataType);

	}

	private void genColumnByType(String type) {
		if (type.equals(ColumnType.LINK)) {
			this.setItemPainter(new TableLinkCellRenderer(this));
		} else if (type.equals(ColumnType.DATE)) {
			TableCellDateTimePicker cell = new TableCellDateTimePicker(this);
			TableCustomCellEditor cellEdite = new TableCustomCellEditor(cell);
			this.setItemEditor(cellEdite);
			setDataType(Variant.DATE);
			this.setWidth(15);
			this.setDisplayMask("yyyy-MM-dd");
		} else if (type.equals(ColumnType.DATETIME)) {
			TableCellDateTimePicker cell = new TableCellDateTimePicker(this);
			TableCustomCellEditor cellEdite = new TableCustomCellEditor(cell);
			this.setItemEditor(cellEdite);
			setDataType(Variant.TIMESTAMP);
			this.setDisplayMask("yyyy-MM-dd HH:mm:ss");
			this.setWidth(20);
		} else if (type.equals(ColumnType.COMBOBOX)) {

		} else if (type.equals(ColumnType.SEARCH)) {
			TableMaskCellEditor maskCell = new TableMaskCellEditor();
			this.setDataType(Variant.STRING);
			maskCell.setVariantType(Variant.STRING);
			TableLookupCellEditor cellEditor = new TableLookupCellEditor(this, maskCell);
			this.setItemEditor(cellEditor);
		}
	}

	@Override
	public TableColumnMeta getMeta() {
		return meta;
	}

	public List<ConditionNode> getCondiitionNode() {
		return condiitionNode;
	}

	public void setCondiitionNode(List<ConditionNode> condiitionNode) {
		this.condiitionNode = condiitionNode;
	}

}
