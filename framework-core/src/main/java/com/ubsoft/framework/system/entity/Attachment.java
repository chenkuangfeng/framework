package com.ubsoft.framework.system.entity;

import java.io.Serializable;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;


@Table(name="SC_ATTACHMENT") 
public class Attachment extends BaseEntity implements Serializable {
	/**
	 * 附件名称
	 */
	@Column(name="ATTNAME",length=255,nullable=false) 	

	private String attName;
	/**
	 * 附件所在服务器地址
	 */
	@Column(name="SERVER",length=40) 	

	private String server;
	/**
	 * 附件类型
	 */
	@Column(name="ATTTYPE",length=20,nullable=false) 	

	private String attType;
	/**
	 * 附件大小
	 */
	@Column(name="ATTSIZE") 	

	private long attSize;
	/**
	 * 附件所属的模块
	 */
	@Column(name="ATTMODULE",length=80,nullable=false) 	

	private String attModule;
	/**
	 * 附件所关联的单据号
	 */
	@Column(name="REFID",length=64,nullable=false) 	

	private String refId;
	/**
	 * 附件存储的路径
	 */
	@Column(name="PATH",length=255) 	

	private String path;
	public String getAttName() {
		return attName;
	}
	public void setAttName(String attName) {
		this.attName = attName;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getAttType() {
		return attType;
	}
	public void setAttType(String attType) {
		this.attType = attType;
	}
	
	public long getAttSize() {
		return attSize;
	}
	public void setAttSize(long attSize) {
		this.attSize = attSize;
	}
	public String getAttModule() {
		return attModule;
	}
	public void setAttModule(String attModule) {
		this.attModule = attModule;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}	
}
