package com.ubsoft.framework.core.dal.session;

import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.dal.model.PageResult;

import java.io.Serializable;
import java.util.List;

/**
 * 数据库dao层操作类，原则上支持所有数据库和hive、 hbase、
 * 
 * @author chenkf
 * 
 */
public interface IDataSession {
	// 获取BIO

	Bio getBio(String bioName, Serializable id);

	Bio getBio(String bioName, String property, Object value);
	
	Bio getBio(String bioName, String [] properties, Object [] value);


	List<Bio> getBios(String bioName);
	
	List<Bio> getBios(String bioName, String property, Object value);
	List<Bio> getBios(String bioName, String property, Object value,String orderBy);

	
	List<Bio> getBios(String bioName, String [] properties, Object [] value);

	List<Bio> getBios(String bioName, String [] properties, Object [] value,String orderBy);

	// 保存Bio
	void saveBio(Bio bio);

	void saveBio(List<Bio> bios);

	void batchSaveBio(List<Bio> bios);

	// 删除bio
	void deleteBio(Bio bio);
	void deleteBio(List<Bio> bios);
	void deleteBio(String bioName, Serializable id);
	void deleteBio(String bioName, Serializable [] ids);
	void deleteBio(String bioName, String  property, Object  value);
	void deleteBio(String bioName, String [] properties, Object [] value);

	// Bio查询
	List<Bio> select(String sql, Object[] params,String bioName);

	List<Bio> select(String sql, Object[] params, int limit,String bioName);

	PageResult<Bio> select(String sql, int pageSize, int pageNumber, Object[] params,String bioName);
	
	//普通查询
	List<Bio> query(String sql, Object[] params);	
	List<Bio> query(String sql, Object[] params, int limit);

	PageResult<Bio> query(String sql, int pageSize, int pageNumber, Object[] params);

	// 获取bean
	<T extends Serializable> T get(Class<T> clazz, Serializable id);

	<T extends Serializable> T get(Class<T> clazz, String property, Object value);
	
	<T extends Serializable> T get(Class<T> clazz, String [] properties, Object [] value);


	<T extends Serializable> List<T> gets(Class<T> clazz);
	<T extends Serializable> List<T> gets(Class<T> clazz,String orderBy);


	<T extends Serializable> List<T> gets(Class<T> clazz, String  property, Object value);
	<T extends Serializable> List<T> gets(Class<T> clazz, String  property, Object value,String orderBy);


	<T extends Serializable> List<T> gets(Class<T> clazz, String [] properties, Object [] value);

	<T extends Serializable> List<T> gets(Class<T> clazz, String [] properties, Object [] value, String orderBy);

	// 保存实体bean
	<T extends Serializable> void save(T entity);

	<T extends Serializable> void save(List<T> entities);

	// 删除
	<T extends Serializable> void delete(T entity);

	<T extends Serializable> void delete(List<T> entities);

	// 原生sql查询
	<T extends Serializable> List<T> select(String sql, Object[] params, Class<T> clazz);

	<T extends Serializable> PageResult<T> select(String sql, int pageSize, int pageNumber, Object[] params, Class<T> clazz);

	// 执行sql
	int executeUpdate(String sql, Object[] params);

	// 批量执行
	int[] batchUpdate(String sql, List<Object[]> args);

	// 调用存储过程
	Object[] call(String sql, Object[] inParams, Object[] outParams);

	void call(String sql, Object[] inParams);

	// 获取单个值
	Object getUniqueResult(String sql, Object[] params);

	// 同步到数据库
	void flush();

}
