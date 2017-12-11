package com.ubsoft.framework.metadata.model.widget;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (name="inputfield")
public class InputFieldMeta extends WidgetMeta{
	
	
	@XmlAttribute
	
	private String op ;
	
	@XmlAttribute
	private String as ;
	
	
	@XmlAttribute
	private String field ;
	
	@XmlAttribute
	private String join ;
	
	@XmlAttribute
	private String dataType ;

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getAs() {
		return as;
	}

	public void setAs(String as) {
		this.as = as;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getJoin() {
		return join;
	}

	public void setJoin(String join) {
		this.join = join;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
