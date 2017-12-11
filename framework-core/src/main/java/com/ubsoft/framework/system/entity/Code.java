package com.ubsoft.framework.system.entity;

import java.io.Serializable;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;


@Table(name = "SA_CODE")
public class Code extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(length = 255, name = "REMARKS")
	private String remarks;

	@Column(length = 32, unique = true, nullable = false, name = "CODEKEY")
	private String codeKey;
	@Column(length = 100, nullable = false, name = "CODENAME")
	private String codeName;

	public String getCodeKey() {
		return codeKey;
	}

	public void setCodeKey(String codeKey) {
		this.codeKey = codeKey;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}