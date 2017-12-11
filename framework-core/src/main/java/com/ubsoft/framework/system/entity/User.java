package com.ubsoft.framework.system.entity;

import java.io.Serializable;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;


@Table(name = "SA_USER")
public class User extends Person implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column(name = "USERKEY", length = 32, unique = true, nullable = false)
	private String userKey; // 用户编码
	@Column(name = "USERNAME", length = 50, nullable = false)
	private String userName;// 用户姓名
	@Column(name = "PWD", length = 50)
	private String pwd;// 用户密码

	@Column(name = "LOCALE", length = 10)
	private String locale;

	@Column(name = "OWNERKEY", length = 32)
	private String ownerKey;// 所属部门

	@Column(name = "ORGKEY", length = 32, nullable = false)
	private String orgKey;// 默认所属管理组织
 
	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getOwnerKey() {
		return ownerKey;
	}

	public void setOwnerKey(String ownerKey) {
		this.ownerKey = ownerKey;
	}

	public String getOrgKey() {
		return orgKey;
	}

	public void setOrgKey(String orgKey) {
		this.orgKey = orgKey;
	}

}