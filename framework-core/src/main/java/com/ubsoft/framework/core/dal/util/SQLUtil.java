package com.ubsoft.framework.core.dal.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.support.util.FreeMarkerUtil;
import com.ubsoft.framework.system.cache.MemoryDimension;
import com.ubsoft.framework.system.model.Subject;

import freemarker.template.TemplateException;

public class SQLUtil {
	public static final String DBMS_DB2 = "DB2";
	public static final String DMBS_INTERBASE = "INTERBASE";
	public static final String DBMS_INFORMIX = "INFORMIX";
	public static final String DBMS_SYBASE = "SYBASE";
	public static final String DBMS_MSSQL = "MSSQL";
	public static final String DBMS_MYSQL = "MYSQL";
	public static final String DBMS_ORACLE = "ORACLE";
	public static final String DBMS_ACCESS = "ACCESS";
	public static final String DBMS_OTHERS = "OTHERS";

	/**
	 * 根据数据库类型转换相应的sql
	 * 
	 * @param sql
	 * @param dbType
	 * @return
	 */
	public static String parseSql(String sql, String dbType) {

		return sql;
	}

	public static String getLimitSql(String sql, int limit, String dbType) {
		StringBuffer sb = new StringBuffer();
		if (dbType.equals("oracle")) {
			sb.append("SELECT T.*, ROWNUM RN ");
			sb.append("FROM (").append(sql).append(") T ");
			sb.append("WHERE ROWNUM <=").append(limit);
		}
		if (dbType.equals("mysql")) {
			sb.append(sql).append(" limit ").append(limit);
			// mysql 越望后分页速度越慢
			// SELECT * FROM product a JOIN (select id from product limit
			// 866613, 20) b ON a.ID = b.id

		}
		return sb.toString();
	}

	public static String getPageSql(String sql, int pageSize, int pageNo, String dbType) {
		int startNum = pageSize * (pageNo - 1); // 0 10
		int endNum = startNum + pageNo; // 10 20
		StringBuffer sb = new StringBuffer();
		if (dbType.equals("oracle")) {
			sb.append("SELECT * FROM ");
			sb.append("(");
			sb.append("SELECT T.*, ROWNUM RN ");
			sb.append("FROM (").append(sql).append(") T");
			sb.append("WHERE ROWNUM <=").append(endNum);
			sb.append(")");
			sb.append("WHERE RN >").append(startNum);
		}
		if (dbType.equals("mysql")) {
			sb.append(sql).append(" limit ").append(startNum + 1).append(",").append(pageSize);
			// mysql 越望后分页速度越慢
			// SELECT * FROM product a JOIN (select id from product limit
			// 866613, 20) b ON a.ID = b.id

		}
		return sb.toString();
	}

	/**
	 * 分页转换
	 * 
	 * @param sql
	 * @param dbType
	 * @param start
	 * @param limit
	 * @return
	 */
	public static String parseSql(String sql, String dbType, int start, int limit) {
		return sql;
	}

	public static String freeMarkerSql(String sql, Map<String, Object> mapParams) {
		FreeMarkerUtil futl = FreeMarkerUtil.getInstance();
		int i = 0;
		Map root = new HashMap();
		for (Map.Entry entry : mapParams.entrySet()) {
			String name = entry.getKey().toString();
			Object value = entry.getValue();
			root.put(name, value);
		}
		if (Subject.getSubject() != null) {
			// 设置当前用户参数
			root.put("userKey", Subject.getSubject().getUserKey());
			// 设置当前组织
			root.put("orgKey", Subject.getSubject().getSession().getOrgKey());
		}
		try {
			sql = futl.parseString(sql, root);
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sql;
	}

	public static String getDimensionSql(String sl) {
		String sql = sl;

		String regex = "\\{([^\\{\\}]+)\\}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(sql);
		while (m.find()) {
			String tbs = m.group(1);
			String[] tb = tbs.split(":");
			String tableName = tb[0];
			String as = tb[1];
			List<Bio> dims = MemoryDimension.getInstance().get(tableName.toUpperCase().trim());

			if (dims != null && dims.size() > 0) {
				String rp = sql.indexOf("where") != -1 ? " and " : " where ";
				for (Bio dim : dims) {
					// 自动加上权限纬度
					rp += " (EXISTS(SELECT '1' FROM SA_ROLE_DIMENSION RD,SA_USER_ROLE UR WHERE RD.ROLEKEY=UR.ROLEKEY";
					rp += " AND UR.USERKEY='" + Subject.getSubject().getUserKey() + "'";
					rp += " AND  RD.DIMVALUE=" + as + "." + dim.getString("VALUEFIELD");
					rp += " AND  RD.DIMKEY='" + dim.getString("DIMKEY") + "'";
					rp += ")";
					rp += " OR EXISTS (SELECT '1' FROM SA_USER_DIMENSION UD WHERE UD.DIMVALUE=" + as + "." + dim.getString("VALUEFIELD");
					rp += " AND UD.USERKEY='" + Subject.getSubject().getUserKey() + "'";
					rp += " AND  UD.DIMVALUE=" + as + "." + dim.getString("VALUEFIELD");
					rp += " AND  UD.dimKey='" + dim.getString("DIMKEY") + "'";
					rp += ")) AND ";

				}
				rp += " 1=1";
				if (sql.indexOf("$[" + tableName + "]") != -1) {
					// 只有管理员不受权限限制
					if (!Subject.getSubject().getUserKey().equals("admin")) {
						sql = sql.replace("$[" + tableName + "]", rp);
					} else {
						sql = sql.replace("$[" + tableName + "]", "");
					}
				} else {
					// 只有管理员不受权限限制
					if (!Subject.getSubject().getUserKey().equals("admin")) {
						sql += rp;
					}
				}

			}
			sql = sql.replace("{" + tbs + "}", tableName + " " + as);
		}

		// sql = sql.replaceAll(":", " ");
		// sql = sql.replaceAll("\\{", "").replaceAll("\\}", "");
		// System.out.println(sql);
		return sql;
	}

}
