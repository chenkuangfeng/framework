package com.ubsoft.framework.core.dal.util;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import net.sf.json.JSONNull;

import com.ubsoft.framework.core.exception.DataAccessException;
import com.ubsoft.framework.core.support.util.DateUtil;
import com.ubsoft.framework.core.support.util.StringUtil;

public class DataType {

	public static String STRING = "String";
	public static String STRINGARRAY = "String[]";
	public static String INTEGER = "Integer";
	public static String INTEGER_S = "int";

	public static String INTEGERARRAY = "Integer[]";
	public static String INTEGERARRAY_S = "int[]";

	public static String DATE = "Date";
	public static String TIMESTAMP = "Timestamp";
	public static String TIME = "Time";
	public static String DOUBLE = "Double";
	public static String DOUBLE_S = "double";

	public static String SHORT = "Short";
	public static String SHORT_S = "Short_S";

	public static String BIGDECIMAL = "BigDecimal";
	public static String FLOAT = "Float";
	public static String FLOAT_S = "float";

	public static String LONG = "Long";
	public static String LONG_S = "long";

	public static String BYTE_ARRAY="byte[]";

	public static Object convert(String type, Object value) {
		if (StringUtil.isEmpty(value)||value instanceof JSONNull) {
			return null;
		}
		if (type.equals(DataType.STRING)) {
			if (value instanceof String) {
				return (String) value;
			}
			return value.toString();
		} else if (type.equals(DataType.INTEGER)||type.equals(DataType.INTEGER_S)) {
			if (value instanceof Integer) {
				return (Integer) value;
			}
			return Integer.parseInt(value.toString());

		} else if (type.equals(DataType.DOUBLE)||type.equals(DataType.DOUBLE_S)) {

			if (value instanceof Double) {
				return (Double) value;
			} else {
				return Double.parseDouble(value.toString());
			}

		} else if (type.equals(DataType.LONG)||type.equals(DataType.LONG_S)) {

			if (value instanceof Long) {
				return (Long) value;
			} else {
				return Long.parseLong(value.toString());
			}

		} else if (type.equals(DataType.BIGDECIMAL)) {

			if (value instanceof BigDecimal) {
				return (BigDecimal) value;
			} else {
				return new BigDecimal(value.toString());
			}

		} else if (type.equals(DataType.FLOAT)||type.equals(DataType.FLOAT_S)) {
			if (value instanceof Float) {
				return (Float) value;
			} else {
				return Float.parseFloat(value.toString());
			}

		}else if (type.equals(DataType.SHORT)||type.equals(DataType.SHORT_S)) {
			if (value instanceof Short) {
				return (Short) value;
			} else {
				return Short.parseShort(value.toString());
			}

		} else if (type.equals(DataType.DATE)) {
			if (value instanceof Date) {
				return (Date) value;
			} else if (value instanceof Timestamp) {
				return new Date(((Timestamp) value).getTime());
			} else if (value instanceof Time) {
				return new Date(((Time) value).getTime());
			} else if (value instanceof java.sql.Date) {
				return new Date(((java.sql.Date) value).getTime());
			} else if (value instanceof String) {
				Date tempDate=DateUtil.string2Date(value.toString(), "yyyy-MM-dd HH:mm:ss");
				if(tempDate==null)
					tempDate=DateUtil.string2Date(value.toString(), "yyyy-MM-dd");
				return tempDate;
			} else {
				throw new DataAccessException(5, "数据类型错误，无法转换成日期。");
			}
		} else if (type.equals(DataType.TIMESTAMP)) {
			if (value instanceof Timestamp) {
				return (Timestamp) value;
			} else if (value instanceof Date) {
				return new Timestamp(((Date) value).getTime());
			} else if (value instanceof java.sql.Date) {
				return new Timestamp(((java.sql.Date) value).getTime());
			} else if (value instanceof String) {
				String strValue=value.toString();
				if(strValue.length()<=10){
					strValue+=" 00:00:00";
				}
				return Timestamp.valueOf(strValue);
			} else {
				throw new DataAccessException(5, "数据类型错误，无法转换成时间。");
			}
		} else if (type.equals(DataType.TIME)) {
			
			if (value instanceof Time) {
				return (Time) value;
			} else if (value instanceof Timestamp) {
				
				return new Time(((Timestamp) value).getTime());
			} else if (value instanceof java.sql.Date) {
				return new Time(((java.sql.Date) value).getTime());
			} else if (value instanceof String) {
				String strValue=value.toString();
				if(strValue.length()<=10){
					strValue+=" 00:00:00";
				}
				
				return Time.valueOf(strValue);//new Time(DateUtil.string2Date(value.toString(), "yyyy-MM-dd HH:mm:ss").getTime());
			} else {
				throw new DataAccessException(5, "数据类型错误，无法转换成日期。");
			}
		} else if (type.equals(DataType.BYTE_ARRAY)) {
			return (byte[]) value;
		}else if(type.equals("Boolean")||type.equals("boolean")){
			if(value instanceof Boolean){
				return value;
			}
			String boolString=value.toString().toLowerCase();
			if(boolString.equals("1")){
				boolString="true";
			}
			if(boolString.equals("0")){
				boolString="false";
			}
			Boolean bolValue=Boolean.parseBoolean(boolString);
			return bolValue;
		}else{
			throw new DataAccessException(10,"不支持类型:"+type+"的转换");
		}
		
	}

}
