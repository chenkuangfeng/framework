package com.ubsoft.framework.system.entity;

import java.io.Serializable;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;

/**
 * 用户界面方案配置
 * @author chenkf
 *
 */

@Table(name="SA_UI_SETTING")  
public class UISetting implements Serializable{
		
	@Column(name="ID",length=32,nullable=false)  
	protected String id;
	@Column(name="USERKEY",length=32,nullable=false) 
	private String userKey;		
	@Column(name="UIKEY",length=32,nullable=false) 
	private String uiKey;
	@Column(name="FIELD",length=32,nullable=false) 
	private String field;
	
	@Column(name="WIDTH") 
	private Integer width;
	@Column(name="SEQ") 
	private Integer seq;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	public String getUiKey() {
		return uiKey;
	}
	public void setUiKey(String uiKey) {
		this.uiKey = uiKey;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	
	
}
