package com.ubsoft.framework.core.service.impl;

import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.dal.session.IDataSession;
import com.ubsoft.framework.core.service.IBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Service
@Transactional
public class BaseService<T extends Serializable> implements IBaseService<T> {

	protected Class<T> clazz;
	@Autowired
	protected IDataSession dataSession;

	@SuppressWarnings("unchecked")
	public BaseService() {
		// 当前对象的直接超类的 Type
		Type genericSuperclass = getClass().getGenericSuperclass();
		if (genericSuperclass instanceof ParameterizedType) {
			// 参数化类型
			ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
			// 返回表示此类型实际类型参数的 Type 对象的数组
			Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
			this.clazz = (Class<T>) actualTypeArguments[0];
		} else {
			this.clazz = (Class<T>) genericSuperclass;
		}

	}

	@Override
	public List<Bio> getBios(String bioName) {
		return dataSession.getBios(bioName);
	}

	@Override
	public Bio getBio(String bioName, Serializable id) {
		return dataSession.getBio(bioName, id);
	}

	@Override
	public Bio getBio(String bioName, String property, Object... value) {
		return dataSession.getBio(bioName, property, value);
	}

	@Override
	public List<Bio> getBios(String bioName, String property, Object... value) {
		return dataSession.getBios(bioName, property, value);
	}

	@Override
	public List<Bio> getBios(String bioName, String property, String orderBy, Object... value) {
		return dataSession.getBios(bioName, property, orderBy, value);
	}

	@Override
	public void saveBio(Bio bio) {

		dataSession.saveBio(bio);

	}

	@Override
	public void saveBio(List<Bio> set) {
		dataSession.saveBio(set);

	}

	@Override
	public void deleteBio(Bio bio) {
		dataSession.deleteBio(bio);

	}

	public void deleteBio(List<Bio> set) {
		for (Bio bio : set) {
			dataSession.deleteBio(bio);
		}

	}

	@Override
	public void deleteBio(String bioName, Serializable id) {
		dataSession.deleteBio(bioName, id);

	}

	@Override
	public void deleteBio(String bioName, Serializable [] ids) {
		dataSession.deleteBio(bioName, ids);

	}
	@Override
	public void deleteBio(String bioName, String property, Object... value) {
		dataSession.deleteBio(bioName, property, value);

	}

	@Override
	public T get(Serializable id) {
		return dataSession.get(clazz, id);
	}

	@Override
	public T get(String property, Object... value) {
		return dataSession.get(clazz, property, value);
	}

	@Override
	public List<T> gets() {
		return dataSession.gets(clazz);
	}

	@Override
	public List<T> gets(String property, Object... value) {
		return dataSession.gets(clazz, property, value);
	}

	public List<T> gets(String property, String orderBy,Object [] value) {
		return dataSession.gets(clazz, property, orderBy, value);
	}

	@Override
	public void save(T entity) {
		dataSession.save(entity);

	}

	@Override
	public void save(List<T> entities) {
		dataSession.save(entities);

	}

	@Override
	public void delete(T entity) {
		dataSession.delete(entity);

	}

	@Override
	public void delete(List<T> entities) {
		dataSession.delete(entities);

	}

	

}
