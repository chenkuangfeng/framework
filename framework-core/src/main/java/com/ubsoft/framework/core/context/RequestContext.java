package com.ubsoft.framework.core.context;

import java.util.Map;


public class RequestContext {

	private static ThreadLocal<Map<String,String>> requestContext = new ThreadLocal<Map<String,String>>();
	public static String P_REMOTESERVER="remoteServer";
	public static Map<String,String> getRequest() {
		return requestContext.get();
	}
	public static void setRequest(Map<String,String> request) {
		requestContext.remove();
		requestContext.set(request); 

	}	
	
	public static String getRequestItem(String key){
		return requestContext.get().get(key);
	}
	
	public static void remove() {
		requestContext.remove();
	
	}

}
