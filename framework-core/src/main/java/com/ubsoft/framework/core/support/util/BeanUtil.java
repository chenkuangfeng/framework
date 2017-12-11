package com.ubsoft.framework.core.support.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.dal.util.DataType;
import com.ubsoft.framework.core.exception.ComException;

public class BeanUtil {
	// 缓存,提高反射效率
	private static Map<Object, PropertyDescriptor[]> descriptorsCache = new HashMap<Object, PropertyDescriptor[]>();

	public static <T> T bio2Bean(Class<T> type, Bio bio) {
		try {
			PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(type);
			T obj = type.newInstance(); // 创建 JavaBean 对象
			// 给 JavaBean 对象的属性赋值
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName();
				String propertyType = descriptor.getPropertyType().getSimpleName();
				if (bio.containsKey(propertyName)) {
					Object value = bio.getObject(propertyName);
					value = DataType.convert(propertyType, value);
					Object[] args = new Object[1];
					args[0] = value;
					descriptor.getWriteMethod().invoke(obj, args);
				}
			}
			return obj;
		} catch (Throwable e) {
			throw new ComException(ComException.MIN_ERROR_CODE_Entity, e);
		}
	}

	public static <T> T bio2Bean(T obj, Bio bio) {
		try {
			Class type = obj.getClass();
			PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(type);

			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName();
				String propertyType = descriptor.getPropertyType().getSimpleName();
				if (bio.containsKey(propertyName.toUpperCase())) {
					Object value = bio.getObject(propertyName);
					value = DataType.convert(propertyType, value);
					Object[] args = new Object[1];
					args[0] = value;
					descriptor.getWriteMethod().invoke(obj, args);
				}
			}
			return obj;
		} catch (Throwable e) {
			throw new ComException(ComException.MIN_ERROR_CODE_Entity, e);
		}
	}

	public static <T> Bio bean2Bio(T bean) {
		try {
			Class type = bean.getClass();
			Bio bio = new Bio();
			PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(type);
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName();
				if (!propertyName.equals("class")) {
					Method readMethod = descriptor.getReadMethod();
					Object result = readMethod.invoke(bean, new Object[0]);
					// 转换成大写
					bio.setObject(propertyName, result);
				}
			}
			return bio;
		} catch (Throwable e) {
			throw new ComException(ComException.MIN_ERROR_CODE_Entity, e);
		}
	}

	public static <T> Bio bean2Bio(T bean, Bio bio) {
		try {
			Class type = bean.getClass();
			PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(type);
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName();
				if (!propertyName.equals("class")) {
					Method readMethod = descriptor.getReadMethod();
					Object result = readMethod.invoke(bean, new Object[0]);
					bio.setObject(propertyName, result);
				}
			}
			return bio;
		} catch (Throwable e) {
			throw new ComException(ComException.MIN_ERROR_CODE_Entity, e);
		}
	}

	public static PropertyDescriptor[] getPropertyDescriptors(Class type) {
		try {
			BeanInfo beanInfo = null;
			PropertyDescriptor[] propertyDescriptors = null;

			beanInfo = Introspector.getBeanInfo(type); // 获取类属性
			propertyDescriptors = beanInfo.getPropertyDescriptors();

			return propertyDescriptors;
		} catch (Exception ex) {
			throw new ComException(ComException.MIN_ERROR_CODE_Entity, ex);
		}
	}

	public static Object getProperty(Object bean, String property) {
		boolean hasProperty = false;
		try {
			PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(bean.getClass());
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName();
				if (propertyName.equals(property)) {
					hasProperty = true;
					Method readMethod = descriptor.getReadMethod();
					return readMethod.invoke(bean, new Object[0]);

				}

			}
		} catch (Exception ex) {
			throw new ComException(ComException.MIN_ERROR_CODE_Entity, ex);
		}
		if (hasProperty) {
			throw new ComException(ComException.MIN_ERROR_CODE_Entity, bean.getClass().getName() + "不存在属性:" + property);
		}
		return null;

	}

	public static <T> Map bean2Map(T bean) {
		try {
			Class type = bean.getClass();
			Map bio = new HashMap();
			PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(type);
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName();
				if (!propertyName.equals("class")) {
					Method readMethod = descriptor.getReadMethod();
					Object result = readMethod.invoke(bean, new Object[0]);
					// 转换成大写
					bio.put(propertyName, result);
				}
			}
			return bio;
		} catch (Throwable e) {
			throw new ComException(ComException.MIN_ERROR_CODE_Entity, e);
		}
	}
}
