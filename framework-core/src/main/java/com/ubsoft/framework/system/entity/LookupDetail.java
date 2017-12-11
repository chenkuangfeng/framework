package com.ubsoft.framework.system.entity;

import java.io.Serializable;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;

@Table(name="SA_LOOKUP_DTL") 
public class LookupDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Column(name="LKID",length = 50,nullable = false)
	private String lkId;
	
	@Column(name="LKDKEY",length=32,nullable=false) 
	private String lkdKey;	
	@Column(name="LKKEY",length=32,nullable=false) 
	private String lkKey;	
	@Column(name="LKDNAME",length=50,nullable=false) 
	private String lkdName;		
	
	@Column(name="SEQ") 
	private Integer seq;
	public String getLkdKey() {
		return lkdKey;
	}
	public void setLkdKey(String lkdKey) {
		this.lkdKey = lkdKey;
	}
	
	public String getLkKey() {
		return lkKey;
	}
	public void setLkKey(String lkKey) {
		this.lkKey = lkKey;
	}
	
	public String getLkdName() {
		return lkdName;
	}
	public void setLkdName(String lkdName) {
		this.lkdName = lkdName;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	
	public String getLkId() {
		return lkId;
	}
	public void setLkId(String lkId) {
		this.lkId = lkId;
	}
	
	


}