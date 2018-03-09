package com.ubsoft.framework.core.dal.util;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONNull;

import com.ubsoft.framework.core.exception.DataAccessException;
import com.ubsoft.framework.core.support.json.JsonHelper;
import com.ubsoft.framework.core.support.util.DateUtil;
import com.ubsoft.framework.core.support.util.StringUtil;

public class TypeUtil {
    
	public static String STRING = String.class.getSimpleName();
	public static String STRING_ARRAY = String[].class.getSimpleName();
	public static String INTEGER = Integer.class.getSimpleName();
	public static String INT = "int";
	public static String INTEGER_ARRAY = Integer[].class.getSimpleName();
	public static String INT_ARRAY ="int[]";

	public static String DATE = Date.class.getSimpleName();
	public static String TIMESTAMP = Timestamp.class.getSimpleName();
	public static String TIME = Time.class.getSimpleName();
	public static String DOUBLE = Double.class.getSimpleName();
	public static String SHORT = Short.class.getSimpleName();
	public static String BIGDECIMAL = BigDecimal.class.getSimpleName();
	public static String FLOAT = Float.class.getSimpleName();
	public static String LONG = Long.class.getSimpleName();

	public static String BOOLEAN = Boolean.class.getSimpleName();
	public static String BYTE = Byte.class.getSimpleName();
	public static String BYTE_ARRAY = Byte[].class.getSimpleName();

	//java.sql.types影射到java类型
	public static Map<Integer,String> SQL_TYPE_MAPPING= new HashMap<Integer,String>();
	static{
		SQL_TYPE_MAPPING.put(Types.INTEGER, INTEGER);
		SQL_TYPE_MAPPING.put(Types.BOOLEAN, INTEGER);
		SQL_TYPE_MAPPING.put(Types.NUMERIC, INTEGER);
		
		SQL_TYPE_MAPPING.put(Types.BIT, INTEGER);
		SQL_TYPE_MAPPING.put(Types.CHAR, STRING);
		SQL_TYPE_MAPPING.put(Types.NCHAR, STRING);
		SQL_TYPE_MAPPING.put(Types.VARCHAR, STRING);
		SQL_TYPE_MAPPING.put(Types.NVARCHAR, STRING);
		SQL_TYPE_MAPPING.put(Types.LONGNVARCHAR, STRING);
		SQL_TYPE_MAPPING.put(Types.LONGVARCHAR, STRING);
		

		SQL_TYPE_MAPPING.put(Types.BIGINT, LONG);
		SQL_TYPE_MAPPING.put(Types.DOUBLE, DOUBLE);
		SQL_TYPE_MAPPING.put(Types.DECIMAL, BIGDECIMAL);
		SQL_TYPE_MAPPING.put(Types.FLOAT, FLOAT);
		SQL_TYPE_MAPPING.put(Types.TINYINT, SHORT);



		SQL_TYPE_MAPPING.put(Types.DATE, DATE);
		SQL_TYPE_MAPPING.put(Types.TIME, TIME);
		SQL_TYPE_MAPPING.put(Types.TIMESTAMP, TIMESTAMP);

		SQL_TYPE_MAPPING.put(Types.BLOB, BYTE_ARRAY);
		SQL_TYPE_MAPPING.put(Types.CLOB, STRING);
		SQL_TYPE_MAPPING.put(Types.NCLOB, STRING);


	}
	/**
	 * 转换类型
	 * 
	 * @param targetType
	 * @param value
	 * @return
	 */
	public static Object convert(String targetType, Object value) {
		//targetType = targetType.toLowerCase();
		if (StringUtil.isEmpty(value) || value instanceof JSONNull) {
			return null;
		}
		if (targetType.equals(STRING)) {
			if (value instanceof String) {
				return (String) value;
			}
			return value.toString();
		} else if (targetType.equals(INTEGER)||targetType.equals(INT)) {
			if (value instanceof Integer) {
				return (Integer) value;
			}
			return Integer.parseInt(value.toString());

		} else if (targetType.equals(DOUBLE)||targetType.equals("double")) {

			if (value instanceof Double) {
				return (Double) value;
			} else {
				return Double.parseDouble(value.toString());
			}

		} else if (targetType.equals(LONG)||targetType.equals("long")) {

			if (value instanceof Long) {
				return (Long) value;
			} else {
				return Long.parseLong(value.toString());
			}

		} else if (targetType.equals(BIGDECIMAL)) {

			if (value instanceof BigDecimal) {
				return (BigDecimal) value;
			} else {
				return new BigDecimal(value.toString());
			}

		} else if (targetType.equals(FLOAT)||targetType.equals("float")) {
			if (value instanceof Float) {
				return (Float) value;
			} else {
				return Float.parseFloat(value.toString());
			}

		} else if (targetType.equals(SHORT)) {
			if (value instanceof Short) {
				return (Short) value;
			} else {
				return Short.parseShort(value.toString());
			}

		} else if (targetType.equals(DATE)) {
			if (value instanceof Date) {
				return (Date) value;
			} else if (value instanceof Timestamp) {
				return new Date(((Timestamp) value).getTime());
			} else if (value instanceof Time) {
				return new Date(((Time) value).getTime());
			} else if (value instanceof java.sql.Date) {
				return new Date(((java.sql.Date) value).getTime());
			} else if (value instanceof String) {
				Date tempDate = DateUtil.string2Date(value.toString(), "yyyy-MM-dd HH:mm:ss");
				if (tempDate == null)
					tempDate = DateUtil.string2Date(value.toString(), "yyyy-MM-dd");
				return tempDate;
			} else {
				throw new DataAccessException(5, "数据类型错误，无法转换成日期。");
			}
		} else if (targetType.equals(TIMESTAMP)) {
			if (value instanceof Timestamp) {
				return (Timestamp) value;
			} else if (value instanceof Date) {
				return new Timestamp(((Date) value).getTime());
			} else if (value instanceof java.sql.Date) {
				return new Timestamp(((java.sql.Date) value).getTime());
			} else if (value instanceof String) {
				String strValue = value.toString();
				if (strValue.length() <= 10) {
					strValue += " 00:00:00";
				}
				return Timestamp.valueOf(strValue);
			} else {
				throw new DataAccessException(5, "数据类型错误，无法转换成时间。");
			}
		} else if (targetType.equals(TIME)) {

			if (value instanceof Time) {
				return (Time) value;
			} else if (value instanceof Timestamp) {

				return new Time(((Timestamp) value).getTime());
			} else if (value instanceof java.sql.Date) {
				return new Time(((java.sql.Date) value).getTime());
			} else if (value instanceof String) {
				String strValue = value.toString();
				if (strValue.length() <= 10) {
					strValue += " 00:00:00";
				}
				return Time.valueOf(strValue);// new
												// Time(DateUtil.string2Date(value.toString(),
												// "yyyy-MM-dd HH:mm:ss").getTime());
			} else {
				throw new DataAccessException(5, "数据类型错误，无法转换成日期。");
			}
		} else if (targetType.equals(BYTE_ARRAY)) {
			return (byte[]) value;
		} else if (targetType.equals(BOOLEAN)) {
			if (value instanceof Boolean) {
				return value;
			}
			String boolString = value.toString().toLowerCase();
			if (boolString.equals("1")) {
				boolString = "true";
			}
			if (boolString.equals("0")) {
				boolString = "false";
			}
			Boolean bolValue = Boolean.parseBoolean(boolString);
			return bolValue;
		} else if (targetType.equals(STRING_ARRAY)) {
			if (value instanceof String[]) {
				return (String[]) value;
			} else {
				// 支持json的转换
				if (value instanceof String) {
					String[] p = (String[]) JsonHelper.json2Array(value.toString(), String.class);
					return p;
				} else {
					return null;
				}
			}

		} else {
			throw new DataAccessException(10, "不支持类型:" + targetType + "的转换");
		}

	}

	public static Object getResultSetValue(ResultSet rs, int index) throws SQLException {
		Object obj = rs.getObject(index);
		String className = null;
		if (obj != null) {
			className = obj.getClass().getName();
		}
		if (obj instanceof Blob) {
			Blob blob = (Blob) obj;
			obj = blob.getBytes(1, (int) blob.length());
		} else if (obj instanceof Clob) {
			Clob clob = (Clob) obj;
			obj = clob.getSubString(1, (int) clob.length());
		} else if ("oracle.sql.TIMESTAMP".equals(className) || "oracle.sql.TIMESTAMPTZ".equals(className)) {
			obj = rs.getTimestamp(index);
		} else if (className != null && className.startsWith("oracle.sql.DATE")) {
			String metaDataClassName = rs.getMetaData().getColumnClassName(index);
			if ("java.sql.Timestamp".equals(metaDataClassName) || "oracle.sql.TIMESTAMP".equals(metaDataClassName)) {
				obj = rs.getTimestamp(index);
			} else {
				obj = rs.getDate(index);
			}
		} else if (obj != null && obj instanceof java.sql.Date) {
			if ("java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))) {
				obj = rs.getTimestamp(index);
			}
		}
		return obj;
	}

	protected static Object getResultSetValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
		Class<?> targetType = pd.getPropertyType();
		Object value = getResultSetValue(rs, index, targetType.getSimpleName());
		return value;
	}

	protected static Object getResultSetValue(ResultSet rs, int index, String targetType) throws SQLException {
		Object value = null;
		//targetType = targetType.toLowerCase();
		if (targetType.equals(STRING)) {
			value = rs.getObject(index);
			if (value instanceof Clob) {
				Clob clob = (Clob) value;
				value = clob.getSubString(1, (int) clob.length());
			} else {
				value = rs.getString(index);
			}

		} else if (targetType.equals(BOOLEAN)) {
			value = rs.getBoolean(index);
		} else if (targetType.equals(BYTE)) {
			value = rs.getByte(index);
		} else if (targetType.equals(SHORT)) {
			value = rs.getShort(index);
		} else if (targetType.equals(INTEGER)||targetType.equals(INT)) {
			value = rs.getInt(index);
		} else if (targetType.equals(LONG)||targetType.equals("long")) {
			value = rs.getLong(index);
		} else if (targetType.equals(FLOAT)||targetType.equals("float")) {
			value = rs.getFloat(index);
		} else if (targetType.equals(DOUBLE)||targetType.equals("double")) {
			value = rs.getDouble(index);
		} else if (targetType.equals(BIGDECIMAL)) {
			return rs.getBigDecimal(index);
		} else if (targetType.equals(DATE)) {
			return rs.getTimestamp(index);
		} else if (targetType.equals(TIME)) {
			return rs.getTime(index);
		} else if (targetType.equals(TIMESTAMP)) {
			return rs.getTimestamp(index);
		} else if (targetType.equals(BYTE_ARRAY)) {
			value = rs.getObject(index);
			if (value instanceof Blob) {
				Blob blob = (Blob) value;
				value = blob.getBytes(1, (int) blob.length());
				return value;
			} else {
				return rs.getBytes(index);
			}
		} else {
			return rs.getObject(index);
		}
		return value;
	}

}
