package com.ubsoft.framework.metadata.model.form.fdm;

import com.ubsoft.framework.core.dal.entity.BioMeta;
import com.ubsoft.framework.metadata.model.form.FormListenerMeta;

import javax.xml.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "detail")
public class DetailMeta implements Serializable {
	@XmlAttribute
	private String id;
	@XmlAttribute
	private String bio;
	private String sql;
	@XmlAttribute
	private String fk;
	@XmlAttribute
	private String mk;

	/**
	 * 从父表带到字表的字段映射集合
	 */
	@XmlElementWrapper(name = "keymap")
	@XmlElement(name = "key")
	private List<KeyMapMeta> keys;
	@XmlAttribute
	private String orderBy;

	@XmlTransient
	private BioMeta bioMeta;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getFk() {
		return fk;
	}

	public void setFk(String fk) {
		this.fk = fk;
	}

	public String getMk() {
		return mk;
	}

	public void setMk(String mk) {
		this.mk = mk;
	}

	public BioMeta getBioMeta() {
		return bioMeta;
	}

	public void setBioMeta(BioMeta bioMeta) {
		this.bioMeta = bioMeta;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public List<KeyMapMeta> getKeys() {
		return keys;
	}

	public void setKeys(List<KeyMapMeta> keys) {
		this.keys = keys;
	}

}
