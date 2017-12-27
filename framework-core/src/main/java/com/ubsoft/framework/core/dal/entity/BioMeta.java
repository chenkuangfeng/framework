package com.ubsoft.framework.core.dal.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.annotation.Transient;

@Table(name = "META_BIO")
public class BioMeta extends BaseEntity implements Serializable {
	/**
	 * bio名称
	 */
	private String bioKey;
	/**
	 * bio名称
	 */
	private String bioName;
	
	
	/**
	 * 表名
	 **/
	private String tableKey;
	/**
	 * 备注
	 **/
	private String remarks;
	/**
	 * 是否全局缓存0否,1是
	 */
	private String cacheable;
	/**
	 * 是否审计
	 **/
	private String auditable;
	
	
	/**
	 * 主键属性名名
	 */
	@Transient
	private BioPropertyMeta primaryProperty;
	
	
	/**
	 * 版本属性
	 */
	@Transient
	private BioPropertyMeta versionProperty;
	
	/**
	 * 数据列字典信息,Key不区分大小写,Bio属性名和数据库列名可不一样,列名和属性名都存一份。
	 * 
	 **/
	@Transient
	private Map<String, BioPropertyMeta> propertyMap = new HashMap<String, BioPropertyMeta>();

	/**
	 * 列集合
	 */
	@Transient
	private Set<BioPropertyMeta> propertySet = new HashSet<BioPropertyMeta>();

	public String getBioKey() {
		return bioKey;
	}

	public void setBioKey(String bioKey) {
		this.bioKey = bioKey;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getBioName() {
		return bioName;
	}

	public void setBioName(String bioName) {
		this.bioName = bioName;
	}

	public String getTableKey() {
		return tableKey;
	}

	public void setTableKey(String tableKey) {
		this.tableKey = tableKey;
	}



	
	
	

	public BioPropertyMeta getPrimaryProperty() {
		return primaryProperty;
	}

	public void setPrimaryProperty(BioPropertyMeta primaryProperty) {
		this.primaryProperty = primaryProperty;
	}

	public BioPropertyMeta getVersionProperty() {
		return versionProperty;
	}

	public void setVersionProperty(BioPropertyMeta versionProperty) {
		this.versionProperty = versionProperty;
	}

	
	

	public String getCacheable() {
		return cacheable;
	}

	public void setCacheable(String cacheable) {
		this.cacheable = cacheable;
	}

	public String getAuditable() {
		return auditable;
	}

	public void setAuditable(String auditable) {
		this.auditable = auditable;
	}

	public Map<String, BioPropertyMeta> getPropertyMap() {
		return propertyMap;
	}

	public void setPropertyMap(Map<String, BioPropertyMeta> propertyMap) {
		this.propertyMap = propertyMap;
	}

	public Set<BioPropertyMeta> getPropertySet() {
		return propertySet;
	}

	public void setPropertySet(Set<BioPropertyMeta> propertySet) {
		this.propertySet = propertySet;
	}

	/**
	 * 是否有指定的属性
	 * 
	 * @param key
	 * @return
	 */
	public boolean hasProperty(String key) {
		key = key.toUpperCase();
		return propertyMap.containsKey(key);
	}
	
	public BioPropertyMeta getProperty(String key){
		key = key.toUpperCase();
		return propertyMap.get(key);
	}

}