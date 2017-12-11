package com.ubsoft.framework.esb.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;

/**
 * 接口入口，包含各种协议对外的服务，定时执行,MQ读取等；
 * 
 * @author Administrator
 * 
 */

@Table(name = "ESB_ENDPOINT")
public class Endpoint extends BaseEntity {
	@Column(name="EPKEY",length = 32, unique = true, nullable = false)
	private String epKey;
	@Column(name="EPNAME",length = 100, nullable = false)
	private String epName;
	@Column(name="EPTYPE",length = 100, nullable = false)
	private String epType;// ws:webservice,mq:mq,rs:rest,quartz:定时执行,rmi:rmi,EJB:ejb,socket:socket;
	@Column(name="STARTTIME")
	private Timestamp startTime;
	@Column(name="ENDTIME")
	private Timestamp endTime;	
	@Column(name="PARAMS",length = 50)
	private String params;
	
	
	private List<Route> routes = new ArrayList<Route>();

	public String getEpKey() {
		return epKey;
	}

	public void setEpKey(String epKey) {
		this.epKey = epKey;
	}
	public String getEpName() {
		return epName;
	}

	public void setEpName(String epName) {
		this.epName = epName;
	}

	public String getEpType() {
		return epType;
	}

	public void setEpType(String epType) {
		this.epType = epType;
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	

}
