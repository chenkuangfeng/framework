package com.ubsoft.framework.system.entity;

import java.io.Serializable;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;
@Table(name="SA_APP_LOG") 
public class AppLog extends  BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(length=100,name="LOGKEY") 
	private String logKey;
	@Column(length=255,name="LOGMODULE") 
	private String logModule;
	@Column(length=20,name="LOGLEVEL") 
	private String logLevel;	

	@Column(name="LOGMSG") 
	private String logMsg;
	@Column(length=20,name="LOGSERVER") 
	private String logServer;
	public String getLogKey() {
		return logKey;
	}
	public void setLogKey(String logKey) {
		this.logKey = logKey;
	}
	public String getLogModule() {
		return logModule;
	}
	public void setLogModule(String logModule) {
		this.logModule = logModule;
	}
	public String getLogLevel() {
		return logLevel;
	}
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
	public String getLogMsg() {
		return logMsg;
	}
	public void setLogMsg(String logMsg) {
		this.logMsg = logMsg;
	}
	public String getLogServer() {
		return logServer;
	}
	public void setLogServer(String logServer) {
		this.logServer = logServer;
	}	
	
	
	
	
	
	
	
	
	
   

}