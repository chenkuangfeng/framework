package com.ubsoft.framework.designer.service.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ubsoft.framework.core.context.AppContext;
import com.ubsoft.framework.core.dal.entity.BioMeta;
import com.ubsoft.framework.core.dal.entity.BioPropertyMeta;
import com.ubsoft.framework.core.dal.session.IDataSession;
import com.ubsoft.framework.core.dal.util.TypeUtil;
import com.ubsoft.framework.core.exception.DataAccessException;
import com.ubsoft.framework.core.service.IBioMetaService;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.designer.model.DbTableColumnMeta;
import com.ubsoft.framework.designer.model.DbTableMeta;
import com.ubsoft.framework.designer.service.IDbTableMetaService;

@Service("dbTableMetaService")
public class DbTableMetaService implements IDbTableMetaService {

	@Autowired
	IDataSession dataSession;
	@Autowired
	IBioMetaService bioMetaService;

	@Override
	public DbTableMeta getTableMeta(String unitName, String catalog, String schema, String tableName) {
		List<DbTableMeta> listDbTable = this.getTableMetaList(unitName, catalog, schema, tableName);
		if (listDbTable.size() == 0) {
			throw new DataAccessException(DataAccessException.DAL_ERROR_NO_VALUE_FOUND, "表:" + tableName + "不存在");
		}
		return listDbTable.get(0);
	}

	@Override
	public List<DbTableMeta> getTableMeta(String unitName, String catalog, String schema) {
		List<DbTableMeta> listDbTable = this.getTableMetaList(unitName, catalog, schema, null);
		return listDbTable;
	}

	/**
	 * 如果有下划线,去掉下划线,首字母小写,第二个,第三个首字母....大写
	 * 如果没有下滑线,直接返回小写
	 * @param name
	 * @return
	 */
	protected String getPropertKey(String column) {
		if(column.toUpperCase().equals("CREATEDBY")){
			return "createdBy";
		}
		if(column.toUpperCase().equals("CREATEDDATE")){
			return "createdDate";
		}
		if(column.toUpperCase().equals("UPDATEDBY")){
			return "updatedBy";
		}
		if(column.toUpperCase().equals("UPDATEDDATE")){
			return "updatedDate";
		}
		if (column.indexOf("_") == -1)
			return column.toLowerCase();
		column = column.toLowerCase();
		String[] names = column.split("_");
		if (names.length == 1) {
			return column.toLowerCase();
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
 
	@Override
	public void updateBioMetaFromTable(String unitName, String catalog, String schema, String[] tables) {
		for (String table : tables) {
			DbTableMeta tableMeta = this.getTableMeta(unitName, catalog, schema, table);
			BioMeta bioMeta = dataSession.get(BioMeta.class, "tableKey", table);
			if (bioMeta == null) {
				bioMeta = new BioMeta();
			}
			if (bioMeta.getBioKey() == null) {

				bioMeta.setBioKey(table);
			}
			if (bioMeta.getBioName() == null) {
				bioMeta.setBioName(table);
			}
			dataSession.save(bioMeta);
			List<DbTableColumnMeta> cmList = tableMeta.getColumns();
			int i = 1;
			for (DbTableColumnMeta cm : cmList) {
				Object[] args = new Object[] { bioMeta.getBioKey(), cm.getColumnKey().toUpperCase() };
				BioPropertyMeta property = dataSession.get(BioPropertyMeta.class, new String[]{"bioKey","columnKey"}, args);
				if (property == null) {
					property = new BioPropertyMeta();
				}
				property.setSeq(i);
				i++;
				property.setBioId(bioMeta.getId());
				property.setBioKey(bioMeta.getBioKey());
				//if (property.getPropertyKey() == null) {
					String popertyKey=this.getPropertKey(cm.getColumnKey());
					property.setPropertyKey(popertyKey);
				//}
				// 有备注设置备注,没有的如果为空,设置列名大写
				if (cm.getColumnName() != null) {
					property.setPropertyName(cm.getColumnName());
				} else {
					if (property.getPropertyName() == null) {
						property.setPropertyName(cm.getColumnKey().toUpperCase());

					}
				}
				property.setColumnKey(cm.getColumnKey().toUpperCase());
				if (property.getDataType() == null) {
					// oracle number类型都是bigdecimal,如果没有小数, 转换成int
					String dataType = TypeUtil.SQL_TYPE_MAPPING.get(cm.getDataType());
					if (dataType.equals("bigdecimal")) {
						if (cm.getDigits() == 0) {
							dataType = "integer";
						}
					}
					property.setDataType(dataType);
				}
				property.setLength(cm.getLength());
				property.setDigits(cm.getDigits());
				property.setNullable(cm.getNullable()+"");
				if (cm.getColumnKey().toUpperCase().equals(tableMeta.getPrimaryKey().toUpperCase())) {
					property.setPrimaryKey("1");
				}
				dataSession.save(property);
			}
			bioMetaService.refreshBioMeta(bioMeta.getBioKey());
		}

	}

	@Override
	public void updateTableFromBioMeta(String unitName, String catalog, String schema, String[] bioKeys) {
		// TODO Auto-generated method stub

	}

	private List<DbTableMeta> getTableMetaList(String unitName, String catalog, String schema, String table) {
		DataSource ds = (DataSource) AppContext.getBean(unitName);
		List<DbTableMeta> tableMetas = new ArrayList<DbTableMeta>();
		Connection connection = null;
		try {
			connection = ds.getConnection();
			DatabaseMetaData dbMeta = connection.getMetaData();
			String userName = dbMeta.getUserName();
			if (StringUtil.isEmpty(schema)) {
				schema = userName.toUpperCase();
			}
			String tb = "%";
			if (StringUtil.isNotEmpty(table)) {
				tb = table;
			}
			ResultSet rsTable = dbMeta.getTables(catalog, schema, tb, new String[] { "TABLE" });
			while (rsTable.next()) {
				String tableName = rsTable.getString("TABLE_NAME");
				DbTableMeta tableMeta = new DbTableMeta();
				tableMeta.setTableKey(tableName);
				tableMeta.setTableName(rsTable.getString("REMARKS"));
				List<DbTableColumnMeta> tableColumns = new ArrayList<DbTableColumnMeta>();

				ResultSet rspk = dbMeta.getPrimaryKeys(null, schema, tb);
				String primaryKey = "";
				while (rspk.next()) {
					String columnName = rspk.getString("COLUMN_NAME");// 列名
					primaryKey += columnName + ",";
				}
				primaryKey = primaryKey.substring(0, primaryKey.length() - 1);
				tableMeta.setPrimaryKey(primaryKey);

				ResultSet rsColumn = dbMeta.getColumns(catalog, schema, tb, "%");
				tableMeta.setColumns(tableColumns);
				String dbType = dbMeta.getDatabaseProductName().toLowerCase();
				if (dbType.indexOf("oracle") != -1) {
					dbType = "oracle";
				}
				while (rsColumn.next()) {
					DbTableColumnMeta columnMeta = new DbTableColumnMeta();
					columnMeta.setColumnKey(rsColumn.getString("COLUMN_NAME"));

					String remarks = null;
					// oracle备注从表里面取
					if (dbType.equals("oracle")) {
						String sql = "select * from user_col_comments where table_name=? and column_name=?";
						PreparedStatement ps = connection.prepareStatement(sql);
						ps.setString(1, tableName);
						ps.setString(2, rsColumn.getString("COLUMN_NAME"));
						ResultSet rs = ps.executeQuery();
						while (rs.next()) {
							remarks = rs.getString("COMMENTS");
						}
						ps.close();

					} else {
						remarks = rsColumn.getString("REMARKS");
					}

					columnMeta.setColumnName(remarks);
					columnMeta.setLength(rsColumn.getInt("COLUMN_SIZE"));
					columnMeta.setDataType(rsColumn.getInt("DATA_TYPE"));
					columnMeta.setNullable(rsColumn.getInt("NULLABLE"));
					columnMeta.setDigits(rsColumn.getInt("DECIMAL_DIGITS"));
					// if(rsColumn.getString("COLUMN_NAME").toUpperCase().equals(tableMeta.getPrimaryKey())){
					// columnMeta.
					// }
					// int dataType = rsColumn.getInt("DATA_TYPE"); //
					// 对应的java.sql.Types类型
					// String dataTypeName = rsColumn.getString("TYPE_NAME");//
					// java.sql.Types类型
					// int columnSize = rsColumn.getInt("COLUMN_SIZE");// 列大小
					// int decimalDigits = rsColumn.getInt("DECIMAL_DIGITS");//
					// 小数位数
					// int numPrecRadix = rsColumn.getInt("NUM_PREC_RADIX");//
					// 基数（通常是10或2）
					// int nullAble = rsColumn.getInt("NULLABLE");// 是否允许为null
					// String remarks = rsColumn.getString("REMARKS");// 列描述
					// String columnDef = rsColumn.getString("COLUMN_DEF");//
					// 默认值
					// int sqlDataType = rsColumn.getInt("SQL_DATA_TYPE");//
					// sql数据类型
					// int sqlDatetimeSub = rsColumn.getInt("SQL_DATETIME_SUB");
					// // SQL日期时间分?
					// int charOctetLength =
					// rsColumn.getInt("CHAR_OCTET_LENGTH"); // char类型的列中的最大字节数
					// int ordinalPosition =
					// rsColumn.getInt("ORDINAL_POSITION"); // 表中列的索引（从1开始）
					tableColumns.add(columnMeta);
				}

				// 外键信息
				// ResultSet rsfk = dbMeta.getExportedKeys(catalog, schema,
				// tableName);
				// while (rsfk.next()) {
				// String pkTableCat = rsfk.getString("PKTABLE_CAT");//
				// 主键表的目录（可能为空）
				// String pkTableSchem = rsfk.getString("PKTABLE_SCHEM");//
				// 主键表的架构（可能为空）
				// String pkTableName = rsfk.getString("PKTABLE_NAME");// 主键表名
				// String pkColumnName = rsfk.getString("PKCOLUMN_NAME");// 主键列名
				// String fkTableCat = rsfk.getString("FKTABLE_CAT");//
				// 外键的表的目录（可能为空）出口（可能为null）
				// String fkTableSchem = rsfk.getString("FKTABLE_SCHEM");//
				// 外键表的架构（可能为空）出口（可能为空）
				// String fkTableName = rsfk.getString("FKTABLE_NAME");// 外键表名
				// String fkColumnName = rsfk.getString("FKCOLUMN_NAME"); //
				// 外键列名
				// }
				tableMetas.add(tableMeta);

			}
			connection.close();
			return tableMetas;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (connection != null && !connection.isClosed()) {
					try {
						connection.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (Exception ex) {

			}
		}

	}
}
