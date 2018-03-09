package com.ubsoft.framework.esb.entity;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;

/**
 * 接口路由，接口进入后调用路由，路由：包含bean，http,ftp,ws,http,ejb,rmi等。
 * @author chenkf
 *
 */

@Table(name="ESB_ROUTE")  
public class Route extends BaseEntity {
	@Column(name="EPID",length=32,nullable=false) 
	private String epId;
	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	
	@Column(name="ROUTEKEY",length=32,nullable=false) 
	private String routeKey;
	@Column(name="ROUTENAME",length=100,nullable=false) 
	private String routeName;
	@Column(name="ROUTETYPE",length=100,nullable=false) 
	private String routeType;//bean，http,ftp,ws,http,ejb,rmi
	@Column(name="ROUTEMODULE",length=255,nullable=false) 
	private String routeModule ;//路由调用的服务或者地址，serviceName，http地址，webservice地址等	
	@Column(name="SEQ")
	private Integer seq;//序号
	@Column(name="logable")
	private String logable ;
	@Column(name="PARAMS",length = 100)
	
	private String params;
	
	
	
	
	
	

	public String getRouteKey() {
		return routeKey;
	}
	
	public void setRouteKey(String routeKey) {
		this.routeKey = routeKey;
	}
	
	public String getRouteName() {
		return routeName;
	}
	
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	
	public String getRouteType() {
		return routeType;
	}
	
	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}
	
	public String getEpId() {
		return epId;
	}

	public void setEpId(String epId) {
		this.epId = epId;
	}

	public String getRouteModule() {
		return routeModule;
	}

	public void setRouteModule(String routeModule) {
		this.routeModule = routeModule;
	}

	
	public String getLogable() {
		return logable;
	}

	public void setLogable(String logable) {
		this.logable = logable;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	

	

	
	
	
}
