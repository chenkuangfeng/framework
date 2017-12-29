package com.ubsoft.framework.core.service;

import com.ubsoft.framework.core.dal.model.Bio;

import java.io.Serializable;
import java.util.List;

public interface IBaseService<T extends Serializable> {
	Bio getBio(String bioName, Serializable id);

	Bio getBio(String bioName, String property, Object value);

	List<Bio> getBios(String bioName, String property, Object value, String orderBy);

	Bio getBio(String bioName, String[] properties, Object[] value);

	List<Bio> getBios(String bioName);

	List<Bio> getBios(String bioName, String property, Object value);

	List<Bio> getBios(String bioName, String[] properties, Object[] value);

	List<Bio> getBios(String bioName, String[] properties, Object[] value, String orderBy);

	// 保存Bio
	void saveBio(Bio bio);

	void saveBio(List<Bio> set);

	void deleteBio(Bio bio);

	void deleteBio(List<Bio> bios);

	void deleteBio(String bioName, Serializable id);

	void deleteBio(String bioName, Serializable[] ids);

	void deleteBio(String bioName, String property, Object value);

	void deleteBio(String bioName, String[] properties, Object[] value);

	T get(Serializable id);

	T get(String property, Object value);

	T get(String[] properties, Object[] value);

	List<T> gets();

	List<T> gets(String property, Object value);

	List<T> gets(String property, Object value, String orderBy);

	List<T> gets(String[] properties, Object[] value);

	List<T> gets(String[] properties, Object[] value, String orderBy);

	// 保存实体bean
	void save(T entity);

	void save(List<T> entities);

	// 删除
	void delete(T entity);

	void delete(List<T> entities);

}
