package com.ubsoft.framework.core.dal.util;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.StringUtils;

import com.ubsoft.framework.core.exception.ComException;
import com.ubsoft.framework.core.support.util.BeanUtil;

public class BeanRowMapper<T> implements RowMapper<T> {
	
	private Class<T> mappedClass;
	private Map<String, PropertyDescriptor> mappedFields;

	public BeanRowMapper(Class<T> clazz) {
		this.mappedClass = clazz;
		//初始化
		mappedFields=BeanUtil.getMapPropertyDescriptor(clazz);		

	}

	

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		T result = null;
		try {
			result = mappedClass.newInstance();
			int columnCount = rsmd.getColumnCount();
			for (int index = 1; index <= columnCount; index++) {
				String column = JdbcUtils.lookupColumnName(rsmd, index);
				PropertyDescriptor pd = this.mappedFields.get(column.toLowerCase());	
				if(pd==null){
					System.out.println(column.toLowerCase()+"字段在"+mappedClass.getName()+"中不存在");
					continue;
				}
//				if(mappedClass.getSimpleName().equals("Session")){
//				System.out.println(mappedClass+"-"+column.toLowerCase());
//				}
				if (pd.getWriteMethod() != null) {
					Object value = TypeUtil.getResultSetValue(rs, index, pd);
					Object[] args = new Object[1];
					args[0] = value;
					pd.getWriteMethod().invoke(result, args);
				}
			}
			return result;
		} catch (Exception ex) {
			throw new ComException(ComException.MIN_ERROR_CODE_DAL + 100, ex.getMessage());
		}
	}

}
