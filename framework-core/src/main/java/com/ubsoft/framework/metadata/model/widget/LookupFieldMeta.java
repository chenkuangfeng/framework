package com.ubsoft.framework.metadata.model.widget;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (name="lookup")
public class LookupFieldMeta extends InputFieldMeta{
	/** 窗体ID**/
	@XmlAttribute
	private String select;
	/** 窗体目标返回字段**/
	@XmlAttribute
	private String selectField;
	/** 窗体目标额外返回字段**/
	@XmlAttribute
	private String fromFields;
	/** 填充到目标字段**/
	@XmlAttribute
	private String toFields;
	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public String getFromFields() {
		return fromFields;
	}

	public void setFromFields(String fromFields) {
		this.fromFields = fromFields;
	}

	public String getToFields() {
		return toFields;
	}

	public void setToFields(String toFields) {
		this.toFields = toFields;
	}

	public String getSelectField() {
		return selectField;
	}

	public void setSelectField(String selectField) {
		this.selectField = selectField;
	}
	
	
}
