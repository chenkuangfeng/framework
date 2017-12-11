package com.ubsoft.framework.metadata.model.widget.table;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.ubsoft.framework.metadata.model.widget.WidgetMeta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (name="column")
public class TableColumnMeta extends WidgetMeta{
	
	@XmlAttribute
	private String type;
	
	@XmlAttribute
	private String dataType;
	
	@XmlAttribute
	private String field;

	@XmlAttribute
	private String code;	
	/**链接界面的module**/
	@XmlAttribute
	private String linkModule;
	
	/** 传递给module参数对应的key值**/
	@XmlAttribute
	private String linkKey;
	/** 弹出框ID**/
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
	
	public String getLinkModule() {
		return linkModule;
	}

	public void setLinkModule(String linkModule) {
		this.linkModule = linkModule;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLinkKey() {
		return linkKey;
	}

	public void setLinkKey(String linkKey) {
		this.linkKey = linkKey;
	}

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
