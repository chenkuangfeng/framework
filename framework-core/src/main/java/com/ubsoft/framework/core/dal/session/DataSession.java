package com.ubsoft.framework.core.dal.session;

import com.ubsoft.framework.core.cache.MemoryBioMeta;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BioMeta;
import com.ubsoft.framework.core.dal.entity.BioPropertyMeta;
import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.dal.model.PageResult;
import com.ubsoft.framework.core.dal.util.BioRowMapper;
import com.ubsoft.framework.core.dal.util.SQLUtil;
import com.ubsoft.framework.core.exception.DataAccessException;
import com.ubsoft.framework.core.support.util.BeanUtil;
import com.ubsoft.framework.core.support.util.StringUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Repository
public class DataSession implements IDataSession {

	private final static String versionKey = "Version";
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Bio getBio(String bioName, Serializable id) {
		BioMeta meta = MemoryBioMeta.getInstance().get("bioName");
		if (meta != null) {
			Bio bio = this.getBio(bioName, meta.getPrimaryKey() + "=?", id);
			return bio;
		} else {
			throw new DataAccessException(DataAccessException.MIN_ERROR_CODE_DAL, "BioMeta:" + bioName + "不存在.");
		}
	}

	@Override
	public Bio getBio(String bioName, String property, Object... value) {
		List<Bio> set = null;
		if (value.length == 1) {
			set = this.getBios(bioName, property + "=?", value);
		}
		if (value.length > 1) {
			set = this.getBios(bioName, property, value);
		}
		if (set != null && set.size() == 1) {
			Bio bio = set.get(0);
			return bio;
		} else if (set.size() > 1) {
			throw new DataAccessException(DataAccessException.MIN_ERROR_CODE_DAL, "record is not only。");
		} else {
			return null;
		}
	}

	@Override
	public List<Bio> getBios(String bioName) {
		return this.getBios(bioName, null, null, new Object[] {});
	}

	@Override
	public List<Bio> getBios(String bioName, String condition, Object... value) {
		return this.getBios(bioName, condition, null, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bio> getBios(String bioName, String condition, String orderBy, Object... value) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT T.* FROM ").append(bioName).append(" T");
		if (StringUtil.isNotEmpty(condition)) {
			sql.append(" WHERE ").append(condition);
		}
		if (StringUtil.isNotEmpty(orderBy)) {
			sql.append(" ORDER BY  ").append(orderBy);
		}
		List<Bio> result = jdbcTemplate.query(sql.toString(), value, new BioRowMapper(bioName));		
		return result;
	}

	@Override
	public void saveBio(Bio bio) {
		if (bio.getObject(bio.getPrimaryKey()) != null) {
			update(bio);
		} else {
			insert(bio);
		}

	}

	public void batchSaveBio(List<Bio> set) {
		StringBuffer batchSql = new StringBuffer();
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (Bio bio : set) {
			if (bio.getObject(bio.getPrimaryKey()) != null) {
				Map<String, Object> updateArgs = this.getUpdateArgs(bio);
				String sql = updateArgs.get("sql").toString();
				Object[] args = (Object[]) updateArgs.get("args");
				batchSql.append(sql).append(",");
				batchArgs.add(args);
			} else {
				Map<String, Object> insertArgs = this.getInsertArgs(bio);
				String sql = insertArgs.get("sql").toString();
				Object[] args = (Object[]) insertArgs.get("args");
				batchSql.append(",").append(sql);
				batchArgs.add(args);
			}
		}
		String sql = batchSql.toString();
		sql = sql.replaceFirst(",", "");
		jdbcTemplate.batchUpdate(sql, batchArgs);
	}

	@Override
	public void saveBio(List<Bio> set) {
		for (Bio bio : set) {
			saveBio(bio);
		}

	}

	private void update(Bio bio) {
		Map<String, Object> updateArgs = this.getUpdateArgs(bio);
		String sql = updateArgs.get("sql") + "";
		Object[] args = (Object[]) updateArgs.get("args");
		int count = this.jdbcTemplate.update(sql, args);
		if (count < 1) {
			throw new DataAccessException(DataAccessException.MIN_ERROR_CODE_DAL, "记录已经被其他人修改.");
		}

	}

	private void insert(Bio bio) {
		Map<String, Object> insertArgs = this.getInsertArgs(bio);
		final String sql = insertArgs.get("sql") + "";
		Object[] args = (Object[]) insertArgs.get("args");
		this.jdbcTemplate.update(sql, args);
//		 //如果自增长列,考虑下代码面获取主键
//		 KeyHolder keyHolder = new GeneratedKeyHolder();
//		 jdbcTemplate.update(new PreparedStatementCreator() {
//		 public PreparedStatement createPreparedStatement(Connection conn)
//		 throws SQLException {
//		 PreparedStatement ps = conn.prepareStatement(sql, new String[] {
//		 "SITE_ID", "NAME" });
//		 ps.setObject(parameterIndex, x)
//		 return ps;
//		 }
//		 }, keyHolder);
//		 bio.setInt(bio.getPrimaryKey(), keyHolder.getKey().intValue());

	}

	private <T extends Serializable> Map<String, Object> getInsertArgs(Bio bio) {
		BioMeta meta = bio.getMeta();
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("INSERT INTO ");
		List<Object> params = new ArrayList<Object>();
		if (meta.hasProperty(versionKey)) {
			bio.setInt(versionKey, 0);
		}
		sql.append(bio.getMeta().getTableName());
		sql.append("(");
		StringBuilder args = new StringBuilder();
		args.append("(");
		for (Map.Entry<String, BioPropertyMeta> entry : meta.getProperties().entrySet()) {
			BioPropertyMeta property = entry.getValue();
			String propertyName = entry.getKey().toString();
			Object value = bio.getObject(propertyName);
			if (value == null) {
//				if (!property.isNullable()) {
//					if (value == null) {
//						throw new DataAccessException(101, meta.getName() + "的属性:" + property.getName() + "不能为空.");
//					}
//				} else if (property.getDefaultValue() != null) {
//					bio.setObject(propertyName, property.getDefaultValue());
//				} else {// 不插入
//					continue;
//				}
				continue;

			}
			sql.append(property.getColumn());
			args.append("?");
			params.add(value);
			sql.append(",");
			args.append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		args.deleteCharAt(args.length() - 1);
		args.append(")");
		sql.append(")");
		sql.append(" values ");
		sql.append(args);
		result.put("sql", sql);
		result.put("args", params.toArray());
		return result;

	}

	private <T extends Serializable> Map<String, Object> getUpdateArgs(Bio bio) {
		BioMeta meta = bio.getMeta();
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("UPDATE ");
		List<Object> params = new ArrayList<Object>();
		sql.append(" SET ");
		// 获取属性信息
		Object primaryValue = null;
		String primaryKey = null;
		for (Map.Entry<String, BioPropertyMeta> entry : meta.getProperties().entrySet()) {
			BioPropertyMeta property = entry.getValue();
			String propertyName = entry.getKey().toString();
			Object value = bio.getObject(propertyName);
			// 主键不更新
			if (property.equals(meta.getPrimaryKey())) {
				primaryValue = bio.getObject(propertyName);
				primaryKey = property.getColumn();
				continue;
			}
			sql.append(primaryKey);
			sql.append(" = ?");
			sql.append(",");
			params.add(value);
		}
		// 乐观锁用固定字段Version,如果更新行为0
		if (meta.hasProperty(versionKey)) {
			sql.append(versionKey).append("=").append(versionKey).append("+1");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" WHERE ");
		sql.append(primaryKey);
		sql.append(" = ?");
		if (meta.hasProperty(versionKey)) {
			sql.append(" AND ").append(versionKey);
			sql.append("=?");
			params.add(bio.getInt(versionKey));
		}
		if (primaryValue != null) {
			params.add(primaryValue);
		} else {
			throw new DataAccessException(101, meta.getName() + "缺少主键配置.");

		}
		result.put("sql", sql);
		result.put("args", params.toArray());
		return result;

	}

	public void deleteBio(Bio bio) {
		BioMeta meta = bio.getMeta();
		StringBuffer sql = new StringBuffer();
		int count = 0;
		sql.append("DELETE FROM ").append(meta.getTableName()).append(" WHERE ");
		sql.append(meta.getPrimaryKey()).append("=?");
		if (meta.hasProperty(versionKey)) {
			sql.append(" AND ").append(versionKey).append("=?");
			count = jdbcTemplate.update(sql.toString(), new Object[] { bio.getObject(meta.getPrimaryKey()), bio.getInt("Version") });
		} else {
			count = jdbcTemplate.update(sql.toString(), new Object[] { bio.getObject(meta.getPrimaryKey()) });
		}
		if (count < 1) {
			throw new DataAccessException(DataAccessException.MIN_ERROR_CODE_DAL, "记录已经被修改.");
		}

	}

	public void deleteBio(List<Bio> set) {
		for (Bio bio : set) {
			deleteBio(bio);
		}
	}

	public void deleteBio(String bioName, Serializable id) {
		StringBuffer sql = new StringBuffer();
		BioMeta meta = MemoryBioMeta.getInstance().get(bioName);
		sql.append("DELETE FROM ").append(meta.getTableName()).append(" WHERE ");
		sql.append(meta.getPrimaryKey()).append("=?");
		jdbcTemplate.update(sql.toString(), new Object[] { id });
	}

	public void deleteBio(String bioName, Serializable [] ids) {
		StringBuffer sql = new StringBuffer();
		BioMeta meta = MemoryBioMeta.getInstance().get(bioName);
		for(Serializable id:ids) {
			sql.append("DELETE FROM ").append(meta.getTableName()).append(" WHERE ");
			sql.append(meta.getPrimaryKey()).append("=?");
			jdbcTemplate.update(sql.toString(), new Object[] { id });

		}
	}

	public void deleteBio(String bioName, String property, Object... value) {
		StringBuffer sql = new StringBuffer();
		BioMeta meta = MemoryBioMeta.getInstance().get(bioName);
		sql.append("DELETE FROM ").append(meta.getTableName()).append(" WHERE ");
		sql.append(property);
		if (value.length == 1) {
			sql.append(property).append("=?");
		}
		jdbcTemplate.update(sql.toString(), new Object[] { value });
	}

	private String getDbType() {
		// 暂时默认oracel
		return "oracle";
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bio> queryBio(String sql, Object[] params) {
		sql = SQLUtil.parseSql(sql, this.getDbType());
		List<Bio> result = jdbcTemplate.query(sql, params, new BioRowMapper());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bio> queryBio(String sql, Object[] params, int limit) {
		String dbType = this.getDbType();
		sql = SQLUtil.parseSql(sql, dbType);
		sql = SQLUtil.getLimitSql(sql, limit, dbType);
		return jdbcTemplate.query(sql, params, new BioRowMapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageResult<Bio> queryBio(String sql, int pageSize, int pageNumber, Object[] params) {
		PageResult<Bio> pager = new PageResult<Bio>();
		String dbType = this.getDbType();
		sql = SQLUtil.parseSql(sql, dbType);
		String totalSql = "select count(1) from (" + sql + ") t ";
		int total = Integer.parseInt(getUniqueResult(totalSql, params) + "");
		sql = SQLUtil.getPageSql(sql, pageSize, pageNumber, dbType);
		List<Bio> result = jdbcTemplate.query(sql, params, new BioRowMapper());
		pager.setRows(result);
		pager.setTotal(total);
		return pager;

	}

	@Override
	public <T extends Serializable> T get(Class<T> clazz, Serializable id) {
		Table ta = clazz.getAnnotation(Table.class);
		String primaryKey = ta.primarykey();
		return get(clazz, primaryKey, id);
	}

	@Override
	public <T extends Serializable> T get(Class<T> clazz, String property, Object... value) {
		List<T> set = null;
		if (value.length == 1) {
			set = this.gets(clazz, property + "=?", value);
		}
		if (value.length > 1) {
			set = this.gets(clazz, property, value);
		}
		if (set != null && set.size() == 1) {
			return set.get(0);
		} else if (set.size() > 1) {
			throw new DataAccessException(DataAccessException.MIN_ERROR_CODE_DAL, "record is not only。");
		} else {
			return null;
		}
	}

	@Override
	public <T extends Serializable> List<T> gets(Class<T> clazz) {
		return this.gets(clazz, null, new Object[] {});
	}

	@Override
	public <T extends Serializable> List<T> gets(Class<T> clazz, String condition, Object... value) {
		return this.gets(clazz, condition, null, value);
	}

	@Override
	public <T extends Serializable> List<T> gets(Class<T> clazz, String condition, String orderBy, Object... value) {
		Table ta = clazz.getAnnotation(Table.class);
		String tableName = ta.name();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT T.* FROM ").append(tableName).append(" T");
		if (StringUtil.isNotEmpty(condition)) {
			sql.append(" WHERE ").append(condition);
		}
		if (StringUtil.isNotEmpty(orderBy)) {
			sql.append(" ORDER BY  ").append(orderBy);
		}
		List<T> result = jdbcTemplate.query(sql.toString(), value, new BeanPropertyRowMapper<T>(clazz));

		return result;
	}

	private <T extends Serializable> Map<String, Object> getInsertArgs(T entity, Table ta) {
		String tableName = ta.name();
		String primaryKey = ta.primarykey();
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("INSERT INTO ");
		List<Object> params = new ArrayList<Object>();
		sql.append(tableName);

		// Field[] fields = clazz.getDeclaredFields();
		// for (Field field : fields) {
		// boolean fieldHasAnno = field.isAnnotationPresent(Column.class);
		// if (fieldHasAnno) {
		// Column column = field.getAnnotation(Column.class);
		// // 输出注解属性
		// String name = column.name();
		//
		// }else{
		// field.get
		// }
		//
		// }
		PropertyDescriptor[] pds = BeanUtil.getPropertyDescriptors(entity.getClass());
		sql.append("(");
		StringBuilder args = new StringBuilder();
		args.append("(");
		for (PropertyDescriptor pd : pds) {
			if (pd.getName().equals("class"))
				continue;
			Object value = getReadMethodValue(pd.getReadMethod(), entity);
			if (value == null) {
				continue;
			}
			if(value instanceof List||value instanceof Map || value instanceof Array){
				continue;
			}
			sql.append(pd.getName());
			args.append("?");
			params.add(value);
			sql.append(",");
			args.append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		args.deleteCharAt(args.length() - 1);
		args.append(")");
		sql.append(")");
		sql.append(" values ");
		sql.append(args);
		result.put("sql", sql);
		result.put("args", params.toArray());
		return result;

	}

	private <T extends Serializable> Map<String, Object> getUpdateArgs(T entity, Table ta) {
		String tableName = ta.name();
		String primaryKey = ta.primarykey();
		String versionKey = ta.versionkey();
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("UPDATE ");
		sql.append(tableName);
		List<Object> params = new ArrayList<Object>();
		sql.append(" SET ");
		// 获取属性信息
		Object primaryValue = null;
		Object versionValue = null;
		PropertyDescriptor[] pds = BeanUtil.getPropertyDescriptors(entity.getClass());
		sql.append("(");
		StringBuilder args = new StringBuilder();
		args.append("(");
		for (PropertyDescriptor pd : pds) {
			if (pd.getName().equals("class"))
				continue;
			Object value = getReadMethodValue(pd.getReadMethod(), entity);
			if (pd.getName().equals(primaryKey)) {
				primaryValue = value;
				continue;
			}
			if (pd.getName().equals(versionKey)) {
				versionValue = value;
				continue;
			}
			//bean中只能是基础类型
			if(value instanceof List||value instanceof Map || value instanceof Array){
				continue;
			}
			sql.append(pd.getName());

			sql.append(primaryKey);
			sql.append(" = ?");
			sql.append(",");
			params.add(value);
		}
		// 乐观锁用字段Version,如果更新行为0
		if (versionKey != null) {
			sql.append(versionKey).append("=").append(versionKey).append("+1");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" WHERE ");
		sql.append(primaryKey);
		sql.append(" = ?");
		if (primaryValue != null) {
			params.add(primaryValue);
		} else {
			throw new DataAccessException(101, entity.getClass().getName() + "缺少主键配置.");
		}

		if (versionKey != null) {
			sql.append(" AND ").append(versionKey);
			sql.append("=?");
			params.add(versionValue);
		}
		result.put("sql", sql);
		result.put("args", params.toArray());
		return result;

	}

	@Override
	public <T extends Serializable> void save(T entity) {
		Table ta = entity.getClass().getAnnotation(Table.class);

		String primaryKey = ta.primarykey();
		String primaryValue = null;
		try {
			primaryValue = BeanUtils.getProperty(entity, primaryKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (primaryValue == null) {
			Map<String, Object> insertArgs = this.getInsertArgs(entity, ta);
			String sql = insertArgs.get("sql").toString();
			Object[] args = (Object[]) insertArgs.get("args");
			this.jdbcTemplate.update(sql, args);
		} else {
			Map<String, Object> insertArgs = this.getUpdateArgs(entity, ta);
			String sql = insertArgs.get("sql").toString();
			Object[] args = (Object[]) insertArgs.get("args");
			int count = this.jdbcTemplate.update(sql, args);
			if (count < 1) {
				throw new DataAccessException(DataAccessException.MIN_ERROR_CODE_DAL, "记录已经被修改.");
			}
		}

	}

	@Override
	public <T extends Serializable> void save(List<T> entities) {
		for (T entity : entities) {
			save(entity);
		}
	}

	@Override
	public <T extends Serializable> void delete(T entity) {
		Table ta = entity.getClass().getAnnotation(Table.class);
		String tableName = ta.name();
		String primaryKey = ta.primarykey();
		String versionKey = ta.versionkey();
		StringBuffer sql = new StringBuffer();
		int count = 0;
		sql.append("DELETE FROM ").append(tableName).append(" WHERE ");
		sql.append(primaryKey).append("=?");
		Object primaryValue = BeanUtil.getProperty(entity, primaryKey);
		if (versionKey != null) {
			sql.append(" AND ").append(versionKey).append("=?");
			Object versionValue = BeanUtil.getProperty(entity, versionKey);
			count = jdbcTemplate.update(sql.toString(), new Object[] { primaryValue, versionValue });
		} else {
			count = jdbcTemplate.update(sql.toString(), new Object[] { primaryValue });
		}
		if (count < 1) {
			throw new DataAccessException(DataAccessException.MIN_ERROR_CODE_DAL, "记录已经被修改.");
		}
	}

	@Override
	public <T extends Serializable> void delete(List<T> entities) {
		for (T entity : entities) {
			delete(entity);
		}

	}

	@Override
	public <T extends Serializable> List<T> query(String sql, Object[] params, Class<T> clazz) {

		sql = SQLUtil.parseSql(sql, this.getDbType());
		List<T> result = jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<T>(clazz));
		return result;
	}

	@Override
	public <T extends Serializable> PageResult<T> query(String sql, int pageSize, int pageNumber, Object[] params, Class<T> clazz) {
		PageResult<T> result = new PageResult<T>();
		String dbType = this.getDbType();
		sql = SQLUtil.parseSql(sql, dbType);
		String totalSql = "select count(1) from (" + sql + ") t ";
		int total = Integer.parseInt(getUniqueResult(totalSql, params) + "");
		sql = SQLUtil.getPageSql(sql, pageSize, pageNumber, dbType);
		List<T> rows = jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<T>(clazz));
		result.setRows(rows);
		result.setTotal(total);
		return result;
	}

	@Override
	public int executeUpdate(String sql, Object[] params) {
		return this.jdbcTemplate.update(sql, params);
	}

	@Override
	public int[] batchUpdate(String sql, List<Object[]> args) {
		return jdbcTemplate.batchUpdate(sql, args);
	}

	@Override
	public Object[] call(String sql, final Object[] inParams, final Object[] outParams) {
		return jdbcTemplate.execute(sql, new CallableStatementCallback<Object[]>() {
			public Object[] doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				Object[] result = new Object[outParams.length];
				try {
					int index = 1;
					if (inParams != null) {
						for (int i = 0; i < inParams.length; i++) {
							cs.setObject(index, inParams[i]);
							index++;
						}
					}
					int outIndex = index;
					if (outParams != null) {
						for (int i = 0; i < outParams.length; i++) {
							if (outParams[i] instanceof String) {
								cs.registerOutParameter(outIndex, Types.VARCHAR);
							} else if (outParams[i] instanceof Number) {
								cs.registerOutParameter(outIndex, Types.NUMERIC);
							} else if (outParams[i] instanceof Date) {
								cs.registerOutParameter(outIndex, Types.DATE);
							} else {
								throw new DataAccessException(100, "不支持存储过程out参数类型:" + outParams[i].getClass().getSimpleName());
							}
							outIndex++;
						}
					}
					cs.execute();
					for (int i = 0; i < outParams.length; i++) {

						result[i] = cs.getObject(index);
						index++;
					}

				} finally {
					if (cs != null) {
						cs.close();
						cs = null;

					}
				}
				return result;
			}
		});

	}

	@Override
	public void call(String sql, Object[] inParams) {
		jdbcTemplate.update(sql, inParams);
	}

	@Override
	public Object getUniqueResult(String sql, Object[] params) {
		return jdbcTemplate.queryForObject(sql, params, Object.class);
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub

	}

	private static Object getReadMethodValue(Method readMethod, Object entity) {
		if (readMethod == null) {
			return null;
		}
		try {
			if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
				readMethod.setAccessible(true);
			}
			return readMethod.invoke(entity);
		} catch (Exception e) {
			return null;
		}
	}

}
