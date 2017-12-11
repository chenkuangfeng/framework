package com.ubsoft.framework.esb.service;

import com.ubsoft.framework.esb.entity.Endpoint;
import com.ubsoft.framework.esb.model.Exchange;

public interface IEsbEngine  {

	public static String ROUTE_BEAN="BEAN"; //BEAN路由类别
	public static String ROUTE_MSG="MSG"; //消息转换类别，支持xml到bean bean到xml、json到bean到json
	public static String ROUTE_MQ_FROM="MQFROM";//消息队列路从路由类别
	public static String ROUTE_MQ_TO="MQTO";//消息队列路到路由类别
	public static String ROUTE_HTTP="HTTP";
	public static String ROUTE_FTP_FROM="FTPFROM";
	public static String ROUTE_FTP_TO="FTPTO";
	public static String BEAN_METHOD="process";//bean默认处理方法
		
	public void process(Endpoint ep, Exchange eg) throws Exception;

	void initEngine();
}
