package com.ubsoft.framework.web.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



public class WebContext {

	private static ThreadLocal<HttpServletRequest> requestContext = new ThreadLocal<HttpServletRequest>();
	private static ThreadLocal<HttpServletResponse> responseContext = new ThreadLocal<HttpServletResponse>();

	public static HttpServletRequest getRequest() {
		return requestContext.get();

	}
	public static void setRequest(HttpServletRequest request) {
		requestContext.remove();
		requestContext.set(request);

	}	
	
	public static HttpServletResponse getResponse() {
		return  responseContext.get();

	}
	public static void setResponse(HttpServletResponse response) {
		responseContext.remove();
		responseContext.set(response);

	}	
	public static HttpSession getSession(){
		HttpServletRequest request=  requestContext.get();
		return request.getSession();
	}
	public static void remove() {
		requestContext.remove();
		responseContext.remove();
	}
	
	 

}
