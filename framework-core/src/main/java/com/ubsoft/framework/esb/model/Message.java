package com.ubsoft.framework.esb.model;

import java.util.HashMap;
import java.util.Map;

public class Message implements Cloneable{	
	private String messageId;
	private boolean fault;
	private Map<String,Object> headers;
	private Object body;
	
	@Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public Map<String, Object> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, Object> headers) {
		this.headers = headers;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	
	public Object getHeader(String key){
		if(headers==null){
			headers=new HashMap<String,Object>();
		}
		return headers.get(key);
	}
	public Object setHeader(String key,Object header){
		if(headers==null){
			headers=new HashMap<String,Object>();
		}
		return headers.put(key, header);
	}
	public void removeHeader(String key){
		if(headers!=null){
			 headers.remove(key);
		}
	}
	public boolean isFault() {
		return fault;
	}
	public void setFault(boolean fault) {
		this.fault = fault;
	}
	
	
}
