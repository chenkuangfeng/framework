package com.ubsoft.framework.metadata.model.form.fdm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (name="detail")
public class DetailMeta implements Serializable{
	@XmlAttribute
	private String id;
	@XmlAttribute
	private String bio;
	private String sql;
	@XmlAttribute
	private String fk;
	@XmlAttribute
	private String refKey;

	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getBio(){
		return bio;
	}

	public void setBio(String bio){
		this.bio = bio;
	}

	public String getSql(){
		return sql;
	}

	public void setSql(String sql){
		this.sql = sql;
	}

	public String getFk(){
		return fk;
	}

	public void setFk(String fk){
		this.fk = fk;
	}

	public String getRefKey(){
		return refKey;
	}

	public void setRefKey(String refKey){
		this.refKey = refKey;
	}
}
