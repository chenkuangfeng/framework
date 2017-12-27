package com.ubsoft.framework.metadata.model.form.fdm;

import com.ubsoft.framework.core.dal.entity.BioMeta;
import com.ubsoft.framework.metadata.model.form.FormListenerMeta;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "fdm")
public class FdmMeta implements Serializable{
	private MasterMeta master;
	@XmlElementWrapper(name = "details")
	@XmlElements({@XmlElement(name = "detail", type = DetailMeta.class)})
	private List<DetailMeta> details;
    @XmlElementWrapper(name = "listeners")
    @XmlElement(name = "listener")
	private List<FormListenerMeta> listeners;


	public MasterMeta getMaster() {
		return master;
	}

	public void setMaster(MasterMeta master) {
		this.master = master;
	}

	public void setDetails(List<DetailMeta> details) {
		this.details = details;
	}

	public List<DetailMeta> getDetails() {
		return details;
	}
	
	public DetailMeta getDetailMeta(String key){
		for(DetailMeta dm:details){
			if(dm.getId().equals(key)){
				return dm;
			}
		}
		return null;
	}



	public List<FormListenerMeta> getListeners(){
		return listeners;
	}

	public void setListeners(List<FormListenerMeta> listeners){
		this.listeners = listeners;
	}
}
