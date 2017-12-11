package com.ubsoft.framework.system.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;

@Table(name="SA_SESSION") 
public class Session extends BaseEntity implements Serializable {

	static final long serialVersionUID = 1598854421569208327L;
	@Column(name="SESSIONID",length=100,nullable=false) 
	private String sessionId;		
	@Column(name="USERKEY",length=32,nullable=false) 
	private String userKey;		
	@Column(name="USERNAME",length=100,nullable=false) 
	private String userName;
	@Column(name="STARTTIMESTAMP")
	private Date startTimestamp;	
	@Column(name="LASTACCESSTIME")
	private Date lastAccessTime;
	@Column(name="TIMEOUT")
	private Long timeout;
	@Column(name="CLIENTHOST",length=50,nullable=false) 
	private String clienthost;
	@Column(name="SERVERHOST",length=50,nullable=false) 
	private String serverHost;
	@Column(name="ORGID",length=32) 
	private String orgId;
	@Column(name="ORGKEY",length=32) 
	private String orgKey;
	@Column(name="ORGNAME",length=100) 
	private String orgName;
	
	
	private Map permissions=new HashMap();
	
	private Map roles=new HashMap(); 
	
	private Map attributes=new HashMap();	
	
	
	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
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
	
	
	public Date getLastAccessTime() {
		return lastAccessTime;
	}
	public void setLastAccessTime(Date lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}
	
	public Date getStartTimestamp() {
		return startTimestamp;
	}
	public void setStartTimestamp(Date startTimestamp) {
		this.startTimestamp = startTimestamp;
	}
	public long getTimeout() {
		return timeout;
	}
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	public String getClienthost() {
		return clienthost;
	}
	public void setClienthost(String clienthost) {
		this.clienthost = clienthost;
	}
	public String getServerHost() {
		return serverHost;
	}
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
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
	public Map getPermissions() {
		return permissions;
	}
	public void setPermissions(Map permissions) {
		this.permissions = permissions;
	}
	
	public Map getRoles() {
		return roles;
	}
	public void setRoles(Map roles) {
		this.roles = roles;
	}
	public Map getAttributes() {
		return attributes;
	}
	public void setAttributes(Map attributes) {
		this.attributes = attributes;
	}
	
	public boolean isPermitted(String permission){	
		if(userKey.equals("admin")){
			return true;
		}
		boolean permtied=permissions.containsKey(permission)||permissions.containsValue(permission);
		return permtied;
	}	
	public boolean hasRole(String role){
		return roles.containsKey("role");
	}
	public String getAttribute(String key){
		return attributes.get(key)==null?null:attributes.get(key).toString();
	}
	

	
}
