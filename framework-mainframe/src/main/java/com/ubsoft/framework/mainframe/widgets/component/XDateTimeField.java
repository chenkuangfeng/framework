package com.ubsoft.framework.mainframe.widgets.component;

import java.awt.Component;
import java.awt.Container;
import java.util.Map;

import com.borland.dbswing.JdbDateTimePicker;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;
import com.ubsoft.framework.mainframe.widgets.component.table.XColumn;
import com.ubsoft.framework.mainframe.widgets.renderer.IRenderer;
import com.ubsoft.framework.mainframe.widgets.renderer.RendererUtil;
import com.ubsoft.framework.metadata.model.widget.DateTimeFieldMeta;
import com.ubsoft.framework.metadata.model.widget.WidgetMeta;

@SuppressWarnings("serial")
public class XDateTimeField extends JdbDateTimePicker implements IRenderer {
	
	
	@Override
	public Component render(WidgetMeta meta, Container parent,Map<String,Object> params) {
		DateTimeFieldMeta compMeta = (DateTimeFieldMeta) meta;
		this.meta=compMeta;	
		RendererUtil.addComponent(compMeta, this, parent, params);
		return this;
	}
	
	DateTimeFieldMeta meta =null;
	@Override
	public DateTimeFieldMeta getMeta() {
		
		return meta;
	}
	/**
	 * 重写基类，默认自动增加列
	 * @param dataSet
	 */
	public void setDateSet(DataSet dataSet) {
		if (this.getColumnName() != null) {
			if (dataSet.getColumn(this.getColumnName()) == null) {
				XColumn xcol = new XColumn();
		
				xcol.setColumnName(this.getColumnName());
				xcol.setDataType(Variant.TIMESTAMP);
				dataSet.getStorageDataSet().addColumn(xcol);
			}
		}
	}
}
