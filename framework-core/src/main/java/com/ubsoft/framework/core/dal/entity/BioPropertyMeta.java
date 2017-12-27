package com.ubsoft.framework.core.dal.entity;
import java.io.Serializable;

import com.ubsoft.framework.core.dal.annotation.Table;
@Table(name = "META_BIO_PROPERTY")
public class BioPropertyMeta extends BaseEntity implements Serializable { 
     /*
      * bioMetaKey
      */
    private String bioKey;
    /*
     * bioMetaId
     * 
     */
   private String bioId;
    /**
     * 属性Key
     **/
    private String propertyKey;
    /**
     * 属性名称
     */
    private String propertyName;
    /**
     * 列
     **/
    private String columnKey;
    
    /**
     * 是否是版本
     **/
    private int versionKey;

    /**
     * 允许空
     **/
    private int nullable;
    
    /**
     * 是否主键1:是,0:否
     **/
    private int primaryKey;

    /**
     * 长度
     **/
    private int length;
    /**
     * 小数位数
     **/
    private int digits;
    /**
     * 排序
     */
    private int seq;

    /**
     * 默认值
     **/
    private String defaultValue;
    /**
     * 数据类型
     **/
    private String dataType;
    /**
     * 序列Key,
     **/
    private String seqKey;
    
    /**
     * 备注
     **/
    private String remarks;

	public String getBioKey() {
		return bioKey;
	}

	public void setBioKey(String bioKey) {
		this.bioKey = bioKey;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getColumnKey() {
		return columnKey;
	}

	public void setColumnKey(String columnKey) {
		this.columnKey = columnKey;
	}

	

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getSeqKey() {
		return seqKey;
	}

	public void setSeqKey(String seqKey) {
		this.seqKey = seqKey;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getBioId() {
		return bioId;
	}

	public void setBioId(String bioId) {
		this.bioId = bioId;
	}

	public int getVersionKey() {
		return versionKey;
	}

	public void setVersionKey(int versionKey) {
		this.versionKey = versionKey;
	}

	

	public int getNullable() {
		return nullable;
	}

	public void setNullable(int nullable) {
		this.nullable = nullable;
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public int getDigits() {
		return digits;
	}

	public void setDigits(int digits) {
		this.digits = digits;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	

	
    
	
    
    

    
}