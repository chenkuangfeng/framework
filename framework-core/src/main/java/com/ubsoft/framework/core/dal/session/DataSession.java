package com.ubsoft.framework.core.dal.session;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ubsoft.framework.core.cache.MemoryBioMeta;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;
import com.ubsoft.framework.core.dal.entity.BioMeta;
import com.ubsoft.framework.core.dal.entity.BioPropertyMeta;
import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.dal.model.PageResult;
import com.ubsoft.framework.core.dal.util.BeanRowMapper;
import com.ubsoft.framework.core.dal.util.BioRowMapper;
import com.ubsoft.framework.core.dal.util.SQLUtil;
import com.ubsoft.framework.core.dal.util.TypeUtil;
import com.ubsoft.framework.core.exception.DataAccessException;
import com.ubsoft.framework.core.support.util.BeanUtil;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.system.model.Subject;

@Repository
public class DataSession implements IDataSession {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	 @Autowired
	 NamedParameterJdbcTemplate namedJdbcTemplate;

	@Override
	public Bio getBio(String bioName, Serializable id) {
		BioMeta meta = MemoryBioMeta.getInstance().get(bioName);
		return this.getBio(bioName, meta.getPrimaryProperty().getColumnKey(), id);
	}

	@Override
	public Bio getBio(String bioName, String property, Object value) {

		return this.getBio(bioName, new String[] { property }, new Object[] { value });
	}

	public Bio getBio(String bioName, String[] properties, Object[] value) {
		List<Bio> bios = this.getBios(bioName, properties, value);
		if (bios.size() == 1)
			return bios.get(0);
		if (bios.size() > 1) {
			throw new DataAccessException(DataAccessException.MIN_ERROR_CODE_DAL, "记录不唯一.");
		}
		return null;
	}

	@Override
	public List<Bio> getBios(String bioName) {
		return this.getBios(bioName, new String[] {}, new Object[] {});
	}

	@Override
	public List<Bio> getBios(String bioName, String property, Object value) {
		return this.getBios(bioName, new String[] { property }, new Object[] { value });
	}

	public List<Bio> getBios(String bioName, String property, Object value, String orderBy) {
		return this.getBios(bioName, new String[] { property }, new Object[] { value }, orderBy);
	}

	public List<Bio> getBios(String bioName, String[] properties, Object[] parmas) {
		return this.getBios(bioName, properties, parmas, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bio> getBios(String bioName, String[] properties, Object[] value, String orderBy) {
		BioMeta meta = MemoryBioMeta.getInstance().get(bioName);
		if (meta == null) {
			throw new DataAccessException(DataAccessException.MIN_ERROR_CODE_DAL, "BioMeta:" + bioName + "不存在.");
		}
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT T.* FROM ").append(meta.getTableKey()).append(" T");
		if (properties != null && properties.length > 0) {
			sql.append(" WHERE ");
			int count = properties.length;
			int i = 1;
			for (String property : properties) {
				String columnKey = meta.getProperty(property).getColumnKey();
				sql.append(columnKey).append("=?");
				if (i != count) {
					sql.append(" AND ");
				}
				i++;
			}
		}

		if (StringUtil.isNotEmpty(orderBy)) {
			sql.append(" ORDER BY  ").append(orderBy);
		}
		List<Bio> result = this.select(sql.toString(), value, bioName);
		return result;
	}

	@Override
	public void saveBio(Bio bio) {
		if (bio.getStatus().equals(Bio.UPDATE)) {
			update(bio);
			bio.setStatus(Bio.UPDATE);
		} else if (bio.getStatus().equals(Bio.NEW)) {
			insert(bio);
			bio.setStatus(Bio.UPDATE);
		}

	}

	public void batchSaveBio(List<Bio> set) {
		StringBuffer batchSql = new StringBuffer();
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (Bio bio : set) {
			if (bio.getStatus().equals(Bio.UPDATE)) {
				Map<String, Object> updateArgs = this.getUpdateArgs(bio);
				String sql = updateArgs.get("sql").toString();
				Object[] args = (Object[]) updateArgs.get("args");
				batchSql.append(sql).append(",");
				batchArgs.add(args);
			} else if (bio.getStatus().equals(Bio.NEW)) {
				Map<String, Object> insertArgs = this.getInsertArgs(bio);
				String sql = insertArgs.get("sql").toString();
				Object[] args = (Object[]) insertArgs.get("args");
				batchSql.append(",").append(sql);
				batchArgs.add(args);
			}
			// bio.setStatus(Bio.NONE);
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
		String sql = updateArgs.get("sql").toString();
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
		// bio.setStatus(Bio.NONE);
		// //如果自增长列,考虑下代码面获取主键
		// KeyHolder keyHolder = new GeneratedKeyHolder();
		// jdbcTemplate.update(new PreparedStatementCreator() {
		// public PreparedStatement createPreparedStatement(Connection conn)
		// throws SQLException {
		// PreparedStatement ps = conn.prepareStatement(sql, new String[] {
		// "SITE_ID", "NAME" });
		// ps.setObject(parameterIndex, x)
		// return ps;
		// }
		// }, keyHolder);
		// bio.setInt(bio.getPrimaryKey(), keyHolder.getKey().intValue());

	}

	private void setDefaultValue(BioPropertyMeta property, Bio bio) {
		String propertyKey = property.getPropertyKey();
		// String columnKey = property.getColumnKey();
		if (StringUtil.isTrue(property.getPrimaryKey())) {
			if (property.getDataType().equals(TypeUtil.STRING)) {
				if (StringUtil.isEmpty(bio.getString(propertyKey))) {
					bio.setString(propertyKey, UUID.randomUUID().toString().replace("-", ""));
				}
			}
		}
		if (bio.getStatus().equals("NEW")) {
			if (property.getPropertyKey().toUpperCase().equals("CREATEDBY")) {
				bio.setString("CREATEDBY", Subject.getSubject().getUserKey());
			}
			if (property.getPropertyKey().toUpperCase().equals("CREATEDDATE")) {
				bio.setDate("CREATEDDATE", new Date(System.currentTimeMillis()));
			}
		} else {
			if (property.getPropertyKey().toUpperCase().equals("UPDATEDBY")) {
				bio.setString("UPDATEDBY", Subject.getSubject().getUserKey());
			}
			if (property.getPropertyKey().toUpperCase().equals("UPDATEDDATE")) {
				bio.setDate("UPDATEDDATE", new Date(System.currentTimeMillis()));
			}

		}
	}

	private <T extends Serializable> Map<String, Object> getInsertArgs(Bio bio) {
		BioMeta meta = bio.getMeta();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("INSERT INTO ");
		sql.append(bio.getMeta().getTableKey());
		sql.append("(");
		StringBuilder args = new StringBuilder();
		args.append("(");
		for (BioPropertyMeta property : meta.getPropertySet()) {
			String propertyKey = property.getPropertyKey();
			String columnKey = property.getColumnKey();
			setDefaultValue(property, bio);
			Object value = bio.getObject(propertyKey);
			value=TypeUtil.convert(property.getDataType(), value);
			if (value == null) {
				continue;
			}
			sql.append(columnKey);
			args.append("?");
			// 乐观锁字段默认是0
			if (StringUtil.isTrue(property.getVersionKey()) && property.getDataType().toLowerCase().equals(TypeUtil.INTEGER)) {
				params.add(0);
			} else {
				params.add(value);
			}
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
		sql.append(meta.getTableKey());
		List<Object> params = new ArrayList<Object>();
		String versionKey = null;
		String versionType = null;
		sql.append(" SET ");
		// 获取属性信息
		Object primaryValue = null;
		String primaryKey = null;
		for (BioPropertyMeta property : meta.getPropertySet()) {
			String propertyKey = property.getPropertyKey();
			String columnKey = property.getColumnKey();
			Object value = bio.getObject(propertyKey);			
			value=TypeUtil.convert(property.getDataType(), value);
			setDefaultValue(property, bio);
			// 主键不更新
			if (StringUtil.isTrue(property.getPrimaryKey())) {
				primaryValue = value;
				primaryKey = columnKey;
				continue;
			}
			if (StringUtil.isTrue(property.getVersionKey())) {
				versionKey = property.getColumnKey();
				versionType = property.getDataType();
			}
			sql.append(property.getColumnKey());
			sql.append(" = ?");
			sql.append(",");
			params.add(value);
		}
		// 乐观锁用
		if (versionKey != null) {
			if (versionType.toLowerCase().equals(TypeUtil.INTEGER)) {
				sql.append(versionKey).append("=").append(versionKey).append("+1");
			}
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" WHERE ");
		sql.append(primaryKey);
		sql.append(" = ?");
		if (primaryValue != null) {
			params.add(primaryValue);
		} else {
			throw new DataAccessException(101, meta.getBioKey() + "主键不能为空.");
		}
		if (versionKey != null) {
			if (versionType.toLowerCase().equals(TypeUtil.INTEGER)) {
				sql.append(" AND ").append(versionKey);
				sql.append("=?");
				params.add(bio.getInt(versionKey));
			}
		}
		result.put("sql", sql);
		result.put("args", params.toArray());
		return result;

	}

	public void deleteBio(Bio bio) {
		BioMeta meta = bio.getMeta();
		StringBuffer sql = new StringBuffer();
		String primaryKey = meta.getPrimaryProperty().getColumnKey();
		String primaryProperty = meta.getPrimaryProperty().getPropertyKey();
		String versionKey = meta.getVersionProperty().getColumnKey();
		String versionProperty = meta.getVersionProperty().getPropertyKey();
		int count = 0;
		sql.append("DELETE FROM ").append(meta.getTableKey()).append(" WHERE ");
		sql.append(primaryKey).append("=?");
		if (versionKey != null) {
			sql.append(" AND ").append(versionKey).append("=?");
			count = jdbcTemplate.update(sql.toString(), new Object[] { bio.getObject(primaryProperty), bio.getObject(versionProperty) });

		} else {
			count = jdbcTemplate.update(sql.toString(), new Object[] { bio.getObject(versionProperty) });
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
		if (meta == null) {
			throw new DataAccessException(DataAccessException.MIN_ERROR_CODE_DAL, "BioMeta:" + bioName + "不存在");
		}
		sql.append("DELETE FROM ").append(meta.getTableKey()).append(" WHERE ");
		sql.append(meta.getPrimaryProperty().getColumnKey()).append("=?");
		jdbcTemplate.update(sql.toString(), new Object[] { id });
	}

	public void deleteBio(String bioName, Serializable[] ids) {
		StringBuffer sql = new StringBuffer();
		BioMeta meta = MemoryBioMeta.getInstance().get(bioName);
		if (meta == null) {
			throw new DataAccessException(DataAccessException.MIN_ERROR_CODE_DAL, "BioMeta:" + bioName + "不存在");
		}
		for (Serializable id : ids) {
			sql.append("DELETE FROM ").append(meta.getTableKey()).append(" WHERE ");
			sql.append(meta.getPrimaryProperty().getColumnKey()).append("=?");
			jdbcTemplate.update(sql.toString(), new Object[] { id });
		}
	}

	public void deleteBio(String bioName, String property, Object value) {

		this.deleteBio(bioName, new String[] { property }, new Object[] { value });
	}

	public void deleteBio(String bioName, String[] properties, Object[] value) {
		StringBuffer sql = new StringBuffer();
		BioMeta meta = MemoryBioMeta.getInstance().get(bioName);
		if (meta == null) {
			throw new DataAccessException(DataAccessException.MIN_ERROR_CODE_DAL, "BioMeta:" + bioName + "不存在");

		}
		sql.append("DELETE FROM ").append(meta.getTableKey());//.append(" WHERE ");
		if (properties != null && properties.length > 0) {
			sql.append(" WHERE ");
			int count = properties.length;
			int i = 1;
			for (String property : properties) {
				String columnKey = meta.getProperty(property).getColumnKey();
				sql.append(columnKey).append("=?");
				if (i != count) {
					sql.append(" AND ");
				}
				i++;
			}
		}
		jdbcTemplate.update(sql.toString(), value );
	}

	private String getDbType() {
		// 暂时默认oracel
		return "oracle";
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bio> select(String sql, Object[] params, String bioName) {
		sql = SQLUtil.parseSql(sql, this.getDbType());
		List<Bio> result = jdbcTemplate.query(sql, params, new BioRowMapper(bioName));
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bio> select(String sql, Object[] params, int limit, String bioName) {
		String dbType = this.getDbType();
		sql = SQLUtil.parseSql(sql, dbType);
		sql = SQLUtil.getLimitSql(sql, limit, dbType);
		return jdbcTemplate.query(sql, params, new BioRowMapper(bioName));
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageResult<Bio> select(String sql, int pageSize, int pageNumber, Object[] params, String bioName) {
		PageResult<Bio> pager = new PageResult<Bio>();
		String dbType = this.getDbType();
		sql = SQLUtil.parseSql(sql, dbType);
		String totalSql = "select count(1) from (" + sql + ") t ";
		int total = Integer.parseInt(getUniqueResult(totalSql, params) + "");
		sql = SQLUtil.getPageSql(sql, pageSize, pageNumber, dbType);
		List<Bio> result = jdbcTemplate.query(sql, params, new BioRowMapper(bioName));
		pager.setRows(result);
		pager.setTotal(total);
		return pager;

	}

	@Override
	public List<Bio> query(String sql, Object[] params) {
		String bioName = null;
		return this.select(sql, params, bioName);
	}

	@Override
	public List<Bio> query(String sql, Object[] params, int limit) {
		String bioName = null;
		return this.select(sql, params, limit, bioName);
	}

	

	@Override
	public PageResult<Bio> query(String sql, int pageSize, int pageNumber, Object[] params) {
		String bioName = null;
		return this.select(sql, pageSize, pageNumber, params, bioName);
	}
	@Override
	public <T extends Serializable> T get(Class<T> clazz, Serializable id) {
		Table ta = clazz.getAnnotation(Table.class);
		String primaryKey = ta.primarykey();
		return get(clazz, primaryKey, id);
	}

	@Override
	public <T extends Serializable> T get(Class<T> clazz, String field, Object value) {
		return this.get(clazz, new String[] { field }, new Object[] { value });
	}

	public <T extends Serializable> T get(Class<T> clazz, String[] fields, Object[] value) {
		List<T> entities = this.gets(clazz, fields, value);
		if (entities.size() == 1)
			return entities.get(0);
		if (entities.size() > 1) {
			throw new DataAccessException(DataAccessException.MIN_ERROR_CODE_DAL, "记录不唯一.");
		}
		return null;
	}

	@Override
	public <T extends Serializable> List<T> gets(Class<T> clazz) {
		return this.gets(clazz, new String[] {}, new Object[] {});
	}

	public <T extends Serializable> List<T> gets(Class<T> clazz, String orderBy) {
		return this.gets(clazz, new String[] {}, new Object[] {}, orderBy);
	}

	@Override
	public <T extends Serializable> List<T> gets(Class<T> clazz, String field, Object value) {
		return this.gets(clazz, new String[] { field }, new Object[] { value });
	}

	public <T extends Serializable> List<T> gets(Class<T> clazz, String field, Object value, String orderBy) {
		return this.gets(clazz, new String[] { field }, new Object[] { value }, orderBy);
	}

	public <T extends Serializable> List<T> gets(Class<T> clazz, String[] fields, Object[] value) {
		return this.gets(clazz, fields, value, null);
	}

	@Override
	public <T extends Serializable> List<T> gets(Class<T> clazz, String[] fields, Object[] params, String orderBy) {
		Table ta = clazz.getAnnotation(Table.class);
		String tableName = ta.name();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT T.* FROM ").append(tableName).append(" T");
		if (fields != null && fields.length > 0) {
			sql.append(" WHERE ");
			int count = fields.length;
			int i = 1;
			for (String field : fields) {
				String columnKey = BeanUtil.getPropertyColumn(clazz, field);
				sql.append(columnKey).append("=?");
				if (i != count) {
					sql.append(" AND ");
				}
				i++;
			}
		}
		if (StringUtil.isNotEmpty(orderBy)) {
			orderBy = BeanUtil.getPropertyColumn(clazz, orderBy);
			sql.append(" ORDER BY  ").append(orderBy);
		}

		List<T> result = this.select(sql.toString(), params, clazz);

		return result;
	}

	private <T extends Serializable> Map<String, Object> getInsertArgs(T entity, Table ta) {
		String tableName = ta.name();
		String primaryKey = ta.primarykey();
		String versionKey = ta.versionkey().toUpperCase();
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("INSERT INTO ");
		List<Object> params = new ArrayList<Object>();
		sql.append(tableName);
		PropertyDescriptor[] pds = BeanUtil.getPropertyDescriptors(entity.getClass());
		sql.append("(");
		StringBuilder args = new StringBuilder();
		args.append("(");
		for (PropertyDescriptor pd : pds) {
			if (pd.getName().equals("class"))
				continue;
			String columnName = BeanUtil.getPropertyColumn(entity.getClass(), pd.getName());
			if (columnName == null) {
				continue;
			}
			Object value = getReadMethodValue(pd.getReadMethod(), entity);
			//设置主键和版本
			if (versionKey != null && columnName.equals(versionKey)) {
				value = 0;
			} else if (primaryKey.toUpperCase().equals(columnName.toUpperCase())) {
				value = UUID.randomUUID().toString().replace("-", "");
			}
			if (value == null)
				continue;

			sql.append(columnName);
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
		String primaryKey = ta.primarykey().toUpperCase();
		String versionKey = ta.versionkey().toUpperCase();
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("UPDATE ");
		sql.append(tableName);
		List<Object> params = new ArrayList<Object>();
		sql.append(" SET ");
		// 获取属性信息
		Object primaryValue = null;
		Object versionValue = null;
		PropertyDescriptor[] pds = BeanUtil.getPropertyDescriptors(entity.getClass());
		// sql.append("(");
		StringBuilder args = new StringBuilder();
		// args.append("(");
		for (PropertyDescriptor pd : pds) {
			// String typeName = pd.getPropertyType().getName();
			if (pd.getName().equals("class"))
				continue;
			String columnName = BeanUtil.getPropertyColumn(entity.getClass(), pd.getName());
			if (columnName == null) {
				continue;
			}
			Object value = getReadMethodValue(pd.getReadMethod(), entity);

			if (columnName.equals(primaryKey)) {
				primaryValue = value;
				continue;
			}
			if (columnName.equals(versionKey)) {
				versionValue = value;
				continue;
			}
			sql.append(columnName);
			sql.append(" = ?");
			sql.append(",");
			params.add(value);
		}
		sql.deleteCharAt(sql.length() - 1);
		// 乐观锁用字段Version,如果更新行为0
		if (versionKey != null) {
			sql.append(", ");
			sql.append(versionKey).append("=").append(versionKey).append("+1");
		}

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

	private <T extends Serializable> void setDefaultPropertyValue(T entity) {
		if (entity instanceof BaseEntity) {
			BaseEntity baseEntity = (BaseEntity) entity;
			Subject subject = Subject.getSubject();
			String userKey = null;
			if (subject != null) {
				userKey = subject.getUserKey();
			}
			if (!StringUtil.isEmpty(baseEntity.getId())) {
				// 设置修改时间
				Timestamp updateTmp = new Timestamp(System.currentTimeMillis());
				baseEntity.setUpdatedDate(updateTmp);
				if (userKey != null) {
					baseEntity.setUpdatedBy(userKey);
				}

			} else {
				if (baseEntity.getCreatedDate() == null) {
					Timestamp createTmp = new Timestamp(System.currentTimeMillis());
					baseEntity.setCreatedDate(createTmp);
				}
				if (userKey != null) {
					baseEntity.setCreatedBy(userKey);
				}

			}
		}
	}

	@Override
	public <T extends Serializable> void save(T entity) {
		Table ta = entity.getClass().getAnnotation(Table.class);
		String primaryKey = ta.primarykey();
		Object primaryValue = BeanUtil.getProperty(entity, primaryKey);
		if (primaryValue == null) {
			setDefaultPropertyValue(entity);
			Map<String, Object> insertArgs = this.getInsertArgs(entity, ta);
			String sql = insertArgs.get("sql").toString();
			Object[] args = (Object[]) insertArgs.get("args");
			this.jdbcTemplate.update(sql, args);
		} else {
			setDefaultPropertyValue(entity);
			Map<String, Object> updateArgs = this.getUpdateArgs(entity, ta);
			String sql = updateArgs.get("sql").toString();
			Object[] args = (Object[]) updateArgs.get("args");
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
	public <T extends Serializable> List<T> select(String sql, Object[] params, Class<T> clazz) {
		sql = SQLUtil.parseSql(sql, this.getDbType());
		List<T> result = jdbcTemplate.query(sql, params, new BeanRowMapper<T>(clazz));
		return result;
	}

	@Override
	public <T extends Serializable> PageResult<T> select(String sql, int pageSize, int pageNumber, Object[] params, Class<T> clazz) {
		PageResult<T> result = new PageResult<T>();
		String dbType = this.getDbType();
		sql = SQLUtil.parseSql(sql, dbType);
		String totalSql = "select count(1) from (" + sql + ") t ";
		int total = Integer.parseInt(getUniqueResult(totalSql, params) + "");
		sql = SQLUtil.getPageSql(sql, pageSize, pageNumber, dbType);
		List<T> rows = jdbcTemplate.query(sql, params, new BeanRowMapper<T>(clazz));
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
