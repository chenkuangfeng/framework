package com.ubsoft.framework.core.support.util;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 和反射相关的工具函数
 * 
 * @author chenkf
 * @version
 */
public class ReflectUtil {
	public static Object duplicateObject(Object src) {
		return duplicateObject(src, null);
	}

	public static Object duplicateObject(Object src, final ClassLoader cl) {
		// 使用序列化机制完全复制一个对象
		Object dest;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(src);
			byte[] res = bos.toByteArray();
			bos.close();
			oos.close();

			ByteArrayInputStream bis = new ByteArrayInputStream(res);
			ObjectInputStream ois = null;
			if (null == cl) {
				ois = new ObjectInputStream(bis);
			} else {
				ois = new ObjectInputStream(bis) {
					@Override
					protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
						return cl.loadClass(desc.getName());
					}
				};
			}
			dest = ois.readObject();
			bis.close();
			ois.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		return dest;
	}

	@SuppressWarnings("unchecked")
	public static Field getField(Class clazz, String name) throws NoSuchFieldException {
		Field field = _getField(clazz, name);
		return field;
	}

	@SuppressWarnings("unchecked")
	private static final Field _getField(final Class clazz, final String name) throws NoSuchFieldException {
		Class cls = clazz;
		try {
			Field field = cls.getDeclaredField(name);
			return field;
		} catch (NoSuchFieldException e) {
			cls = cls.getSuperclass();
			if (null == cls) {
				throw e;
			}
			return _getField(cls, name);
		}
	}

	public static final void setFieldIntValue(Object obj, String name, int intValue) {
		try {
			Field field = getField(obj.getClass(), name);
			field.setAccessible(true);
			field.setInt(obj, intValue);
		} catch (NoSuchFieldException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static final void setFieldValue(Object obj, String name, Object value) {
		try {
			Field field = getField(obj.getClass(), name);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (NoSuchFieldException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static final Object getFieldValue(Object obj, String name) {
		try {
			Field field = getField(obj.getClass(), name);
			field.setAccessible(true);
			return field.get(obj);
		} catch (NoSuchFieldException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}
	}

	private static Class[] _autoArgTypes(Object[] args) {
		// We need to get the argument type list so that we can differentiate
		// override methods
		Class[] _argTypes = new Class[args.length];
		for (int i = 0; i < args.length; i++) {
			if (args[i] != null) {
				// Check the primitive types first
				if (args[i] instanceof Boolean) {
					_argTypes[i] = Boolean.TYPE;
				} else if (args[i] instanceof Character) {
					_argTypes[i] = Character.TYPE;
				} else if (args[i] instanceof Byte) {
					_argTypes[i] = Byte.TYPE;
				} else if (args[i] instanceof Short) {
					_argTypes[i] = Short.TYPE;
				} else if (args[i] instanceof Integer) {
					_argTypes[i] = Integer.TYPE;
				} else if (args[i] instanceof Long) {
					_argTypes[i] = Long.TYPE;
				} else if (args[i] instanceof Float) {
					_argTypes[i] = Float.TYPE;
				} else if (args[i] instanceof Double) {
					_argTypes[i] = Double.TYPE;
				} else if (args[i] instanceof Void) {
					_argTypes[i] = Void.TYPE;
				}else {
					String className=args[i].getClass().getSimpleName();
					if(className.equals("ArrayList")){
						_argTypes[i]=List.class;
					}else if(className.equals("HashMap")){
						_argTypes[i]=Map.class;
					}else{
						_argTypes[i] = args[i].getClass();
					}
				}
			} else {
				_argTypes[i] = Object.class;
			}
		}
		return _argTypes;
	}

	public static Method getMethod(Class clazz, String methodName, Class[] argTypes) throws NoSuchMethodException {
		Method mthd;
		Class[] _argTypes = argTypes;
		if (null != _argTypes) {
			mthd = _getMethod(clazz, methodName, _argTypes, null);
		} else {
			mthd = _getMethod(clazz, methodName, null, null);
		}
		return mthd;
	}

	@SuppressWarnings("unchecked")
	public static Method getMethod(Class clazz, String methodName, Object[] args) throws NoSuchMethodException {
		Method mthd;
		if (args != null && args.length > 0) {
			Class[] argTypes = _autoArgTypes(args);
			mthd = _getMethod(clazz, methodName, argTypes, null);
		} else {
			mthd = _getMethod(clazz, methodName, null, null);
		}
		return mthd;
	}

	@SuppressWarnings("unchecked")
	private static final Method _getMethod(final Class clazz, final String name, final Class[] argTypes, NoSuchMethodException firstNsme) throws NoSuchMethodException {
		Class cls = clazz;
		try {

			return cls.getDeclaredMethod(name, argTypes);
		} catch (NoSuchMethodException e) {
			if (null == firstNsme) {
				firstNsme = e; // 记住整个方法查找链中第一个 NoSuchMethodException,
				// 以便此后可以抛出这个 NoSuchMethodException
			}
			cls = cls.getSuperclass();
			if (null == cls) {
				throw firstNsme; // 这里抛出的是整个查找链中第一个 NoSuchMethodException,
				// 便于对错误进行准确定位
			}
			return _getMethod(cls, name, argTypes, firstNsme);
		}
	}

	public static PropertyDescriptor getPropertyDescriptor(Class clazz, String propertyName) {
		StringBuffer sb = new StringBuffer();// 构建一个可变字符串用来构建方法名称
		Method setMethod = null;
		Method getMethod = null;
		PropertyDescriptor pd = null;
		try {
			Field f = clazz.getDeclaredField(propertyName);// 根据字段名来获取字段
			if (f == null) {
				// 构建方法的后缀
				String methodEnd = propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
				sb.append("set" + methodEnd);// 构建set方法
				// 构建set 方法
				setMethod = clazz.getDeclaredMethod(sb.toString(), new Class[] { f.getType() });
				sb.delete(0, sb.length());// 清空整个可变字符串
				sb.append("get" + methodEnd);// 构建get方法
				// 构建get 方法
				getMethod = clazz.getDeclaredMethod(sb.toString(), new Class[] {});
				// 构建一个属性描述器 把对应属性 propertyName 的 get 和 set 方法保存到属性描述器中
				pd = new PropertyDescriptor(propertyName, getMethod, setMethod);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return pd;
	}

	public static void setProperty(Object obj, String propertyName, Object value) {
		Class clazz = obj.getClass();// 获取对象的类型
		PropertyDescriptor pd = getPropertyDescriptor(clazz, propertyName);// 获取
		// clazz
		// 类型中的
		// propertyName
		// 的属性描述器
		Method setMethod = pd.getWriteMethod();// 从属性描述器中获取 set 方法
		try {
			setMethod.invoke(obj, new Object[] { value });// 调用 set
			// 方法将传入的value值保存属性中去
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Object callMethod(Object obj, String methodName, Object[] args, Class[] argTypes) throws Throwable {
		try {
			Method mthd;
			if (null != argTypes) {
				mthd = getMethod(obj.getClass(), methodName, argTypes);
			} else {
				mthd = getMethod(obj.getClass(), methodName, args);
			}
			mthd.setAccessible(true);
			return mthd.invoke(obj, args);
		} catch (NoSuchMethodException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} catch (InvocationTargetException ex) {
			throw ex.getTargetException();
		}
	}

	public static Object callMethod(Object obj, String methodName, Object[] args) throws Throwable {
		return callMethod(obj, methodName, args, null);
	}

	public static Class classForName(String name) throws ClassNotFoundException {
		try {
			ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
			if (contextClassLoader != null) {
				return contextClassLoader.loadClass(name);
			} else {
				return Class.forName(name);
			}
		} catch (Exception e) {
			return Class.forName(name);
		}
	}

}
