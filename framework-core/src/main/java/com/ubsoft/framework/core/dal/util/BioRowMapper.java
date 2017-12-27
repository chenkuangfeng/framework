package com.ubsoft.framework.core.dal.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import com.ubsoft.framework.core.cache.MemoryBioMeta;
import com.ubsoft.framework.core.dal.entity.BioMeta;
import com.ubsoft.framework.core.dal.entity.BioPropertyMeta;
import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.exception.ComException;

public class BioRowMapper implements RowMapper {

	private String bioName;

	public BioRowMapper() {
	}

	public BioRowMapper(String bioName) {
		this.bioName = bioName;
	}

	/**
	 * 如果指定BioName, 1.返回结果集中的字段名称取bioMeta的属性值, 2.
	 * 如果bioMeta里面不存在,如果列有下划线,去掉下划线并转换首字母小写,下划线后面的首字母大写, 3. 如果没有下划线,直接转换成小写
	 * 
	 * 如果不指定BioName 1.如果列有下划线,去掉下划线并转换首字母小写,下划线后面的首字母大写 2.如果没有下划线,直接转换成小写
	 */
	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Bio bio = null;
		BioMeta meta = null;
		if (bioName != null) {
			bio = new Bio(bioName);
			meta = MemoryBioMeta.getInstance().get(bioName);
		} else {
			bio = new Bio();
		}
		bio.setStatus(Bio.UPDATE);
		for (int index = 1; index <= columnCount; index++) {
			String column = JdbcUtils.lookupColumnName(rsmd, index);
			Object value = TypeUtil.getResultSetValue(rs, index);
			if (bioName != null) {
				if (meta != null) {
					BioPropertyMeta property = meta.getProperty(column);
					if (property != null) {
						bio.setObject(property.getPropertyKey(), value);
					} else {
						//column = //getPropertKey(column);
						bio.setObject(column, value);
					}
				} else {
					throw new ComException(ComException.MIN_ERROR_CODE_FDM, "BioMeta:" + bioName + "不存在 .");
				}
			} else {
				//column = getPropertKey(column);
				bio.setObject(column, value);
			}

		}
		return bio;
	}

	/**
	 * 如果有下划线,去掉下划线,首字母小写,第二个,第三个首字母....大写
	 * 如果没有下滑线,直接返回小写
	 * @param name
	 * @return
	 */
	protected String getPropertKey(String column) {
		if (column.indexOf("_") == -1)
			return column.toLowerCase();
		column = column.toLowerCase();
		String[] names = column.split("_");
		if (names.length == 1) {
			return column;
		} else {
			StringBuilder result = new StringBuilder();
			for (int i = 0; i < names.length; i++) {
				if (i == 0) {
					result.append(names[i]);
				} else {
					result.append(names[i].substring(0, 1).toUpperCase()).append(names[i].substring(1));
				}
			}
			return result.toString();
		}

	}

}
