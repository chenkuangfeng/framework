package com.ubsoft.framework.metadata.model.form.fdm;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (name="query")
public class QueryMeta implements Serializable{
	@XmlAttribute
	private String id;
	@XmlValue
	private String text;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
	
	
	
	
}
