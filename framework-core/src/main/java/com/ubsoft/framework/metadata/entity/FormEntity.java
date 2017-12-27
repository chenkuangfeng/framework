package com.ubsoft.framework.metadata.entity;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;

import java.io.Serializable;

@Table(name="META_FORM")
public class FormEntity extends  BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column
	private String formKey;
	@Column
	private String formName;
	@Column
	private String formXml;	
	
	@Column
	private String fdmKey;

	
	@Column
	private String remarks;
	

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public String getFormName(){
		return formName;
	}

	public void setFormName(String formName){
		this.formName = formName;
	}

	public String getFormXml(){
		return formXml;
	}

	public void setFormXml(String formXml){
		this.formXml = formXml;
	}

	public String getFdmKey() {
		return fdmKey;
	}

	public void setFdmKey(String fdmKey) {
		this.fdmKey = fdmKey;
	}
	
	
}

