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
}

