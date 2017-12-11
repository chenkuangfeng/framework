package com.ubsoft.framework.metadata.model.form;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (name="SelectForm")
public class SelectFormMeta extends FormMeta{

	@XmlAttribute
	private String orderBy;
	
	@XmlAttribute
	private Integer maxRowCount;

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getMaxRowCount() {
		return maxRowCount;
	}

	public void setMaxRowCount(Integer maxRowCount) {
		this.maxRowCount = maxRowCount;
	}

		
	
}
