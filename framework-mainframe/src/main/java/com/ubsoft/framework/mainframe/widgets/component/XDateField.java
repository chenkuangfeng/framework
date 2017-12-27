package com.ubsoft.framework.mainframe.widgets.component;

import java.awt.Component;
import java.awt.Container;
import java.util.Map;

import com.borland.dbswing.JdbDatePicker;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;
import com.ubsoft.framework.mainframe.widgets.component.table.XColumn;
import com.ubsoft.framework.mainframe.widgets.renderer.IRenderer;
import com.ubsoft.framework.mainframe.widgets.renderer.RendererUtil;
import com.ubsoft.framework.metadata.model.widget.DateFieldMeta;
import com.ubsoft.framework.metadata.model.widget.WidgetMeta;

@SuppressWarnings("serial")
public class XDateField extends JdbDatePicker implements IRenderer {

	@Override
	public Component render(WidgetMeta meta, Container parent, Map<String, Object> params) {
		DateFieldMeta compMeta = (DateFieldMeta) meta;
		this.meta = compMeta;
		RendererUtil.addComponent(compMeta, this, parent, params);
		this.setDate(null);
		return this;
	}

	DateFieldMeta meta = null;

	@Override
	public DateFieldMeta getMeta() {
		return meta;
	}

	/**
	 * 重写基类，默认自动增加列
	 * 
	 * @param dataSet
	 */
	public void setDateSet(DataSet dataSet) {
		if (this.getColumnName() != null) {
			if (dataSet.getColumn(this.getColumnName()) == null) {
				XColumn xcol = new XColumn();
				xcol.setColumnName(this.getColumnName());
				xcol.setDataType(Variant.DATE);
				dataSet.getStorageDataSet().addColumn(xcol);
			}
		}
	}
}
