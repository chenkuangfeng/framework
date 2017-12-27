package com.ubsoft.framework.core.dal.entity;

import java.io.Serializable;
import java.util.Date;

import com.ubsoft.framework.core.dal.annotation.Column;

public abstract class BaseEntity implements Serializable {
	@Column(name = "ID", length = 32, nullable = false)
	protected String id;
	@Column(name = "VERSION")
	protected int version;
	@Column(name = "CREATEDBY", length = 32)
	protected String createdBy;

	@Column(name = "STATUS", length = 10)
	protected String status;

	@Column(name = "CREATEDDATE")
	protected Date createdDate;

	@Column(name = "UPDATEDBY", length = 32)
	protected String updatedBy;

	@Column(name = "UPDATEDDATE")
	protected Date updatedDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
