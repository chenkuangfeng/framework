package com.ubsoft.framework.core.dal.entity;
import com.ubsoft.framework.core.dal.annotation.Table;
import java.io.Serializable;
@Table(name = "META_BIO_Property")
public class BioPropertyMeta implements Serializable {
    /**
     * 名称
     **/
    private String name;
    /**
     * 列
     **/
    private String column;

    /**
     * 允许空
     **/
    private boolean nullable;

    /**
     * 长度
     **/
    private int length;
    /**
     * 默认值
     **/
    private Object defaultValue;
    /**
     * 数据类型
     **/
    private String dataType;
    /**
     * 序列Key,
     **/
    private String seqKey;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getColumn(){
        return column;
    }

    public void setColumn(String column){
        this.column = column;
    }

    public boolean isNullable(){
        return nullable;
    }

    public void setNullable(boolean nullable){
        this.nullable = nullable;
    }



    public int getLength(){
        return length;
    }

    public void setLength(int length){
        this.length = length;
    }

    public Object getDefaultValue(){
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue){
        this.defaultValue = defaultValue;
    }

    public String getDataType(){
        return dataType;
    }

    public void setDataType(String dataType){
        this.dataType = dataType;
    }

    public String getSeqKey(){
        return seqKey;
    }

    public void setSeqKey(String seqKey){
        this.seqKey = seqKey;
    }
}