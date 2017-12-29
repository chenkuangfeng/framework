package com.ubsoft.framework.designer.model;

import java.io.Serializable;
import java.util.List;

public class DbTableMeta implements Serializable {
	/** 表名**/
	private String tableKey;
	/** 备注  **/
	private String tableName;
	/** 主键列  **/	 
	private String primaryKey;
	/** 列**/
	private List<DbTableColumnMeta> columns;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getTableKey() {
		return tableKey;
	}
	public void setTableKey(String tableKey) {
		this.tableKey = tableKey;
	}
	public List<DbTableColumnMeta> getColumns() {
		return columns;
	}
	public void setColumns(List<DbTableColumnMeta> columns) {
		this.columns = columns;
	}
	public String getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	
	
	
}
