package com.ubsoft.framework.esb.entity;

import java.sql.Timestamp;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;


@Table(name = "ESB_LOG")
public class EsbLog extends BaseEntity {
	@Column(name="MESSAGEID",length = 50, nullable = false)
	private String messageId;
	@Column(name="EPKEY",length = 32, nullable = false)
	private String epKey;
	@Column(name="ROUTEKEY",length = 32, nullable = false)
	private String routeKey;
	@Column(name="LOGLEVEL",length = 10)
	private String logLevel;
		
	@Column(name="MSGIN")
	private String msgIn;
	
	@Column(name="MSGOUT")
	private String msgOut;
	@Column(name="DELAY")
	private int delay;

	public EsbLog() {

	}
	
	public EsbLog(String id, String createdBy, Timestamp createdDate,
			int delay, String epKey, String logLevel, String messageId,
			String routeKey) {
		this.id = id;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.delay = delay;
		this.epKey = epKey;
		this.logLevel = logLevel;
		this.messageId = messageId;
		this.routeKey = routeKey;

	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getEpKey() {
		return epKey;
	}

	public void setEpKey(String epKey) {
		this.epKey = epKey;
	}

	public String getRouteKey() {
		return routeKey;
	}

	public void setRouteKey(String routeKey) {
		this.routeKey = routeKey;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public String getMsgIn() {
		return msgIn;
	}

	public void setMsgIn(String msgIn) {
		this.msgIn = msgIn;
	}

	public String getMsgOut() {
		return msgOut;
	}

	public void setMsgOut(String msgOut) {
		this.msgOut = msgOut;
	}

}
