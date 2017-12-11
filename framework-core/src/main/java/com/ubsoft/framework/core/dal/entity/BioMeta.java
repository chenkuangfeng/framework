package com.ubsoft.framework.core.dal.entity;

import com.ubsoft.framework.core.dal.annotation.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Table(name = "META_BIO")
public class BioMeta implements Serializable {
    /**
     * 名称
    */
    private String name;
    /**
     * 主键字段名
     */
    private String primaryKey;
    /**
     * 版本乐观锁字
     */
    private String versionKey = "Version";
    /**
     * 表名
     **/
    private String tableName;
    /**
     * 备注
     **/
    private String remarks;
    /**
     * 是否全局缓存
     */
    private Boolean cache;
    /**
     * 是否审计
     **/
    private Boolean audit;
    /**
     * 数据列信息
     **/
    private Map<String, BioPropertyMeta> properties = new HashMap<String, BioPropertyMeta>();
    /**
     * 数据列列表
     **/
    private List<BioPropertyMeta> propertyList = new ArrayList<BioPropertyMeta>();

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Boolean getCache(){
        return cache;
    }

    public void setCache(Boolean cache){
        this.cache = cache;
    }

    public Boolean getAudit(){
        return audit;
    }

    public void setAudit(Boolean audit){
        this.audit = audit;
    }

    public Map<String, BioPropertyMeta> getProperties(){
        return properties;
    }

    public void setProperties(Map<String, BioPropertyMeta> properties){
        this.properties = properties;
    }


    public String getRemarks(){
        return remarks;
    }

    public void setRemarks(String remarks){
        this.remarks = remarks;
    }

    public List<BioPropertyMeta> getPropertyList(){
        return propertyList;
    }

    public void setPropertyList(List<BioPropertyMeta> propertyList){
        this.propertyList = propertyList;
    }

    /**
     * 是否有指定的属性
     *
     * @param key
     * @return
     */
    public boolean hasProperty(String key){
        return properties.containsKey(key);
    }

    public String getTableName(){
        return tableName;
    }

    public void setTableName(String tableName){
        this.tableName = tableName;
    }

    public String getPrimaryKey(){
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey){
        this.primaryKey = primaryKey;
    }

    public String getVersionKey(){
        return versionKey;
    }

    public void setVersionKey(String versionKey){
        this.versionKey = versionKey;
    }


}