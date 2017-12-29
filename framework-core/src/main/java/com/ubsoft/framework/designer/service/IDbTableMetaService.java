package com.ubsoft.framework.designer.service;

import java.util.List;

import com.ubsoft.framework.designer.model.DbTableMeta;


public interface IDbTableMetaService{

	/**
	 * 从内存中获取单个表的结果信息
	 * @param tableName
	 * @return
	 */
	DbTableMeta getTableMeta(String unitName,String catalog,String schema,String tableName);	
	/**
	 * 获取所有表的结构
	 * @param unitName
	 * @param catalog
	 * @param schema
	 * @return
	 */
	List<DbTableMeta> getTableMeta(String unitName,String catalog,String schema);
	/**
	 * 根据表生成bioMeta
	 * @param tables
	 */
	void updateBioMetaFromTable(String unitName,String catalog,String schema,String [] tables);
	/**
	 * 根据bio生成表
	 * @param unitName
	 * @param catalog
	 * @param schema
	 * @param bioKeys
	 */
	void updateTableFromBioMeta(String unitName,String catalog,String schema,String [] bioKeys);


	
	
}
