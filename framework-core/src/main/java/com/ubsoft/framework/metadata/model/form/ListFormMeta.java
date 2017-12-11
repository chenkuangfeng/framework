package com.ubsoft.framework.metadata.model.form;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (name="ListForm")
public class ListFormMeta extends FormMeta{
	@XmlAttribute
	private String editForm;
	@XmlAttribute
	private String editFormTitle;

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

	public String getEditForm() {
		return editForm;
	}

	public void setEditForm(String editForm) {
		this.editForm = editForm;
	}

	public String getEditFormTitle() {
		return editFormTitle;
	}

	public void setEditFormTitle(String editFormTitle) {
		this.editFormTitle = editFormTitle;
	}

	public Integer getMaxRowCount() {
		return maxRowCount;
	}

	public void setMaxRowCount(Integer maxRowCount) {
		this.maxRowCount = maxRowCount;
	}

	
}
