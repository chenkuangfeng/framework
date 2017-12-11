package com.ubsoft.framework.metadata.model.widget;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (name="combobox")
public class ComboBoxMeta extends InputFieldMeta{

	
	@XmlAttribute
	private String valueField;
	@XmlAttribute
	private String textField;
	
	@XmlAttribute
	private String code;
	
	
	/** 是否邦定dataSet**/
	@XmlAttribute
	private Boolean bind;
	public String getValueField() {
		return valueField;
	}
	public void setValueField(String valueField) {
		this.valueField = valueField;
	}
	public String getTextField() {
		return textField;
	}
	public void setTextField(String textField) {
		this.textField = textField;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Boolean getBind() {
		return bind;
	}
	public void setBind(Boolean bind) {
		this.bind = bind;
	}
	
		
	
}
