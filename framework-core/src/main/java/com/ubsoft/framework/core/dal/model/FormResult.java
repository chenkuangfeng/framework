package com.ubsoft.framework.core.dal.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class FormResult   implements Serializable {

	private Bio master;
	private Map<String,List<Bio>> details;
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
	
	
	
	
	
	
}