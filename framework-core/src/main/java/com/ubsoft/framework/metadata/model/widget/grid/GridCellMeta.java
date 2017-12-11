package com.ubsoft.framework.metadata.model.widget.grid;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.ubsoft.framework.metadata.model.widget.WidgetMeta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (name="cell")
public class GridCellMeta extends WidgetMeta{

	@XmlAttribute
	private Integer colSpan;
	@XmlAttribute
	private Integer rowSpan;
	public Integer getColSpan() {
		return colSpan;
	}
	public void setColSpan(Integer colSpan) {
		this.colSpan = colSpan;
	}
	public Integer getRowSpan() {
		return rowSpan;
	}
	public void setRowSpan(Integer rowSpan) {
		this.rowSpan = rowSpan;
	}
	
	
	
}


