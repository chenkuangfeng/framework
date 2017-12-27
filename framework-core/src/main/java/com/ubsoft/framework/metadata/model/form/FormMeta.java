package com.ubsoft.framework.metadata.model.form;

import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.ubsoft.framework.metadata.model.widget.WidgetMeta;
import com.ubsoft.framework.metadata.model.form.fdm.FdmMeta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (name="Form")
public class FormMeta extends WidgetMeta implements Serializable{
	private static final long serialVersionUID = 1L;
	@XmlAttribute
	private String unitName;
	@XmlTransient  
	private Map<String,Boolean> permision;
	
	@XmlAttribute
	private String fdm;
	
	@XmlTransient
	private FdmMeta fdmMeta;
	
	
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	public Map<String, Boolean> getPermision() {
		return permision;
	}
	public void setPermision(Map<String, Boolean> permision) {
		this.permision = permision;
	}
	public String getFdm() {
		return fdm;
	}
	public void setFdm(String fdm) {
		this.fdm = fdm;
	}
	public FdmMeta getFdmMeta() {
		return fdmMeta;
	}
	public void setFdmMeta(FdmMeta fdmMeta) {
		this.fdmMeta = fdmMeta;
	}
	
		
	
	
	
	
	
	
	
}
