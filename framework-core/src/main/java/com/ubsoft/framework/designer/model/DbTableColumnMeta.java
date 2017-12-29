package com.ubsoft.framework.designer.model;

import java.io.Serializable;

public class DbTableColumnMeta implements Serializable {

	/** 列名**/
	private String columnKey;
	/**备注**/
	private String columnName;
	/** 类型**/
	private int dataType;
	
	/** 长度**/
	private int length;
	
	/**是否为空**/	
	private int nullable;
	
	/**小数位数**/
	private int digits;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnKey() {
		return columnKey;
	}

	public void setColumnKey(String columnKey) {
		this.columnKey = columnKey;
	}

	

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getNullable() {
		return nullable;
	}

	public void setNullable(int nullable) {
		this.nullable = nullable;
	}

	public int getDigits() {
		return digits;
	}

	public void setDigits(int digits) {
		this.digits = digits;
	}

	

	
	 
	
	
	

}
