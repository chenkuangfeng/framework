package com.ubsoft.framework.rpc.model;


public class SessionContext{
	private static SessionContext context;
	private String unitName;	
	private String sessionId;
	private String userKey;
	private String userName;
	private String orgKey;
	private String orgName;
	
	
	public static SessionContext getContext() {
		return context;
	}

	public static void setContext(SessionContext context) {
		SessionContext.context = context;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

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

	public String getOrgKey() {
		return orgKey;
	}

	public void setOrgKey(String orgKey) {
		this.orgKey = orgKey;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	
	
}
