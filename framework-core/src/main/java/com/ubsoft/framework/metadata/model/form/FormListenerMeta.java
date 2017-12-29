package com.ubsoft.framework.metadata.model.form;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 服务器端扩展服务配置，用于二次开发
 * 
 * @author Administrator
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "listeners")
public class FormListenerMeta implements Serializable {
	@XmlAttribute
	private String name;
	/**
	 * 事件名 BEFORESAVE,AFTERSAVE,BEFOREDELETE,AFTERDELTE,AFTERLOAD
	 */
	@XmlAttribute
	private String event;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEvent(){
		return event;
	}

	public void setEvent(String event){
		this.event = event;
	}
}
