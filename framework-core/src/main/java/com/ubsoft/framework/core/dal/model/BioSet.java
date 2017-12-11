package com.ubsoft.framework.core.dal.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BioSet   implements Serializable {
	

	private Bio master;
	private Map<String,List<Bio>> details;
	
	private Map<String,List<Bio>> others;

	public Bio getMaster() {
		return master;
	}

	public void setMaster(Bio master) {
		this.master = master;
	}

	public Map<String, List<Bio>> getDetails() {
		return details;
	}

	public void setDetails(Map<String, List<Bio>> details) {
		this.details = details;
	}

	public Map<String, List<Bio>> getOthers() {
		return others;
	}

	public void setOthers(Map<String, List<Bio>> others) {
		this.others = others;
	}
	
	public List<Bio> getDetail(String key){
		if(details!=null){
			return details.get(key);
		}
		return null;
	}
	
	public void setDetail(String key,List<Bio> bio){
		if(details==null){
			details= new HashMap<String,List<Bio>>();
		}
		details.put(key, bio);
	}

	
	
	
}