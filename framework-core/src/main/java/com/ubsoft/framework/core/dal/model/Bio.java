package com.ubsoft.framework.core.dal.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import com.ubsoft.framework.core.cache.MemoryBioMeta;
import com.ubsoft.framework.core.dal.entity.BioMeta;
import com.ubsoft.framework.core.exception.DataAccessException;
import com.ubsoft.framework.core.support.util.DateUtil;

public class Bio extends HashMap<String,Object> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//新建
	public static String NEW="NEW";
	//更新
	public static String UPDATE="UPDATE";
	//删除
	public static String DELETE="DELETE";
	//没有动作,但需要传到后台
	public static String NONE="NONE";

  
    public Bio(String name){
    	put("BIO_NAME",name);
        put("ROW_STATUS",NEW);

    }

    public Bio(){
    	put("ROW_STATUS",NEW);
    }

    public String getName(){
        return get("BIO_NAME")==null?null:get("BIO_NAME").toString();
    }

    public void setName(String name){
    	put("BIO_NAME",name);
    }

   
    /**
     * 设置BigDecimal值
     *
     * @param key
     * @param dec
     */
    public void setBigDecimal(String key, BigDecimal dec){
        put(key, dec);
    }

    /**
     * 以BigDecimal类型返回键值
     *
     * @param key 键名
     * @return BigDecimal 键值
     * @throws Exception
     */
    public BigDecimal getBigDecimal(String key){
        Object value = get(key);
        if (value == null)
            return null;
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else {
            return new BigDecimal(value + "");
        }
    }

    /**
     * 以Date类型返回键值
     *
     * @param key 键名
     * @return Date 键值
     */
    public Date getDate(String key){
        Object value = get(key);
        if (value == null)
            return null;
        if (value instanceof Date) {
            return (Date) value;
        } else if (value instanceof Timestamp) {
            return new Date(((Timestamp) value).getTime());
        } else if (value instanceof Time) {
            return new Date(((Time) value).getTime());
        } else if (value instanceof java.sql.Date) {
            return new Date(((java.sql.Date) value).getTime());
        } else if (value instanceof String) {
            return DateUtil.string2Date(value.toString(), "yyyy-MM-dd HH:mm:ss");

        } else {
            throw new DataAccessException(5, "数据类型错误，无法转换成日期。");
        }
    }

    public void setDate(String key, Date date){
        put(key, date);

    }

    public Time getTime(String key){
        Object value = get(key);
        if (value == null)
            return null;
        if (value instanceof Time) {
            return (Time) value;
        } else if (value instanceof Timestamp) {
            return new Time(((Timestamp) value).getTime());
        } else if (value instanceof Date) {
            return new Time(((Date) value).getTime());
        } else if (value instanceof java.sql.Date) {
            return new Time(((java.sql.Date) value).getTime());
        } else if (value instanceof String) {
            return Time.valueOf(value.toString());
        } else {
            throw new DataAccessException(5, "数据类型错误，无法转换成时间。");
        }
    }

    public void setTime(String key, Time time){
        put(key, time);

    }   

    /**
     * 以Integer类型返回键值
     *
     * @param key 键名
     * @return Integer 键值
     */
    public int getInt(String key){

        Object value = get(key);
        if (value != null) {
            if (value instanceof Integer) {
                return (Integer) value;
            } else {
                return Integer.parseInt(value.toString());
            }
        } else
            return 0;
    }

    public void setInt(String key, int value){

        put(key, value);
    }

    /**
     * 以Double类型返回键值
     *
     * @param key 键名
     * @return Integer 键值
     */
    public double getDouble(String key){
        Object value = get(key);
        if (value != null) {
            if (value instanceof Double) {
                return (Double) value;
            } else {
                return Double.parseDouble(value.toString());
            }

        } else
            return 0;
    }

    public void setDouble(String key, double value){
        put(key, value);
    }

    /**
     * 以short类型返回键值
     *
     * @param key 键名
     * @return Integer 键值
     */
    public short getShort(String key){
        Object value = get(key);
        if (value != null) {
            if (value instanceof Short) {
                return (Short) value;
            } else {
                return Short.parseShort(value.toString());
            }

        } else
            return 0;
    }

    public void setShort(String key, short value){
        put(key, value);
    }

    /**
     * 以short类型返回键值
     *
     * @param key 键名
     * @return Integer 键值
     */
    public byte[] getBinary(String key){

        Object value = get(key);
        if (value != null)
            return (byte[]) value;
        else
            return null;
    }

    public void setBinary(String key, byte[] value){

        put(key, value);
    }

    /**
     * 以Long类型返回键值
     *
     * @param key 键名
     * @return Long 键值
     */
    public long getLong(String key){

        Object value = get(key);
        if (value != null) {
            if (value instanceof Long) {
                return (Long) value;
            } else {
                return Long.parseLong(value.toString());
            }

        } else
            return 0;
    }

    public void setLong(String key, long value){

        put(key, value);
    }

    /**
     * 以String类型返回键值
     *
     * @param key 键名
     * @return String 键值
     */
    public String getString(String key){

        Object value = get(key);
        if (value == null) {
            return null;
        } else if (value.equals(" ")) {
            return value.toString();
        } else {
            return value.toString().trim();
        }
    }

    public void setString(String key, String value){

        put(key, value);
    }

    /**
     * 以Timestamp类型返回键值
     *
     * @param key 键名
     * @return Timestamp 键值
     */
    public Timestamp getTimestamp(String key){

        Object value = get(key);
        if (value == null)
            return null;
        if (value instanceof Timestamp) {
            return (Timestamp) value;
        } else if (value instanceof Date) {
            return new Timestamp(((Date) value).getTime());
        } else if (value instanceof java.sql.Date) {
            return new Timestamp(((java.sql.Date) value).getTime());
        } else if (value instanceof String) {
            return Timestamp.valueOf(value + "");
        } else {
            throw new DataAccessException(5, "数据类型错误，无法转换成时间。");
        }
    }

    public void setTimestamp(String key, Timestamp value){
        put(key, value);
    }

    /**
     * 以Boolean类型返回键值
     *
     * @param key 键名
     * @return Timestamp 键值
     * @throws Exception
     */
    public boolean getBoolean(String key){
        Object value = get(key);
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            if (value.toString().equals("1") || value.toString().toUpperCase().equals("TRUE"))
                return true;
            else if (value.toString().equals("0") || value.toString().toUpperCase().equals("FALSE"))
                return false;
            else
                return false;
        } else {
            return Boolean.parseBoolean(value.toString());
        }
    }

    public void setBoolean(String key, boolean value){

        put(key, value);
    }

    public void setObject(String key, Object value){

        put(key, value);
    }

    public Object getObject(String key){

        Object value = get(key);
        return value;
    }


    public BioMeta getMeta(){
        BioMeta meta = MemoryBioMeta.getInstance().get(this.getName());
        return meta;

    }

    
    
    @Override
    public Object put(String key, Object value) {
       key=key.toUpperCase();
       return super.put(key, value);
    }
    @Override
    public Object get(Object key) {
    	key=key.toString().toUpperCase();
    	return super.get(key);
    }
    @Override
    public boolean containsKey(Object key){
    	key=key.toString().toUpperCase();
    	return super.containsKey(key);
    }

	public String getStatus() {
        return get("ROW_STATUS")==null?null:get("ROW_STATUS").toString();
	}

	public void setStatus(String status) {
		put("ROW_STATUS",status);
	}

//    public boolean containsKey(String key){
//        BioMeta meta = MemoryBioMeta.getInstance().get(key);
//        if (meta != null) {
//            if (!meta.hasProperty(key)) {
//                throw new DataAccessException(100, name + "不存在属性:" + key);
//            } else {
//                return true;
//            }
//        } else {
//            return containsKey(key);
//        }
//    }


}