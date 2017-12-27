package com.ubsoft.framework.metadata.model.form.fdm;

import com.ubsoft.framework.core.dal.entity.BioMeta;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (name="master")
public class MasterMeta implements Serializable{
	@XmlAttribute
	private String bio;
	private String sql;
	@XmlTransient
	private BioMeta bioMeta;
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}

	public BioMeta getBioMeta() {
		return bioMeta;
	}

	public void setBioMeta(BioMeta bioMeta) {
		this.bioMeta = bioMeta;
	}
}
