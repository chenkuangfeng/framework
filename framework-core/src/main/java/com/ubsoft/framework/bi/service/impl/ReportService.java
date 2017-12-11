package com.ubsoft.framework.bi.service.impl;//package com.framework.bi.service.impl;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.framework.bi.cache.MemoryReport;
//import com.framework.bi.entity.Report;
//import com.framework.bi.entity.ReportDataSet;
//import com.framework.bi.entity.ReportField;
//import com.framework.bi.entity.ReportParameter;
//import com.framework.bi.service.IReportService;
//import com.framework.core.model.Bio;
//import com.framework.core.model.PageResult;
//import com.framework.core.persistence.session.impl.DataSession;
//import com.framework.core.persistence.support.BioResultTransformer;
//import com.framework.core.persistence.support.SQLUtil;
//import com.framework.core.service.impl.BaseService;
//
//@Service("rptService")
//@Transactional
//public class ReportService extends BaseService<Report> implements IReportService {
//
//	
//
//	@Override
//	public void initReport() {
//		List<ReportField> rptFields = null;// 报表字段
//		List<ReportParameter> rptParameters = null;// 报表参数
//		List<ReportDataSet> rptDataSets = null;// 报表子数据集
//		List<Report> rpts = this.gets("status=?", new Object[] { "1" });
//		for (Report rpt : rpts) {
//			rptFields = this.dataSession.gets(ReportField.class, "reportId=?", "seq", rpt.getId());
//			rptParameters = this.dataSession.gets(ReportParameter.class, "reportId=?", "seq", rpt.getId());
//			// rptDataSets = this.dataSession.gets(ReportDataSet.class,
//			// "reportId=?","seq" ,rpt.getId());
//			rpt.setRptFields(rptFields);
//			rpt.setRptParameters(rptParameters);
//			rpt.setRptDataSets(rptDataSets);
//			MemoryReport.getInstance().put(rpt);
//		}
//
//	}
//
//	@Override
//	public List<Bio> query(String sql, Map<String, Object> mapParams) {
//		sql = SQLUtil.freeMarkerSql(sql, mapParams);
//		// 添加数据纬度
//		sql = SQLUtil.getDimensionSql(sql);
//		sql = sql.replace("?", ":");
//		
//		setQueryParameter(query,mapParams);
//
//		return query.list();
//	}
//
//	@Override
//	public PageResult<Bio> queryPage(String sql, int pageSize, int pageNumber, Map<String, Object> mapParams) {
//		DataSession session = (DataSession) dataSession;
//		sql = SQLUtil.freeMarkerSql(sql, mapParams);
//		// 添加数据纬度
//		sql = SQLUtil.getDimensionSql(sql);
//		sql = sql.replace("?", ":");
//		Query query = session.getSession().createSQLQuery(sql).setResultTransformer(BioResultTransformer.INSTANCE);
//		PageResult<Bio> pager = new PageResult<Bio>();
//		setQueryParameter(query,mapParams);
//		// 设置也数据
//		query.setFirstResult((pageNumber - 1) * pageSize);
//		query.setMaxResults(pageSize);
//		pager.setRows(query.list());
//		// 查询并设置总行数用于分页
//		String totalSql = "select count(1) from (" + sql + ") t ";
//		Query totalQuery = session.getSession().createSQLQuery(totalSql);
//		setQueryParameter(totalQuery,mapParams);
//
//		pager.setTotal(Integer.parseInt(totalQuery.uniqueResult() + ""));
//
//		return pager;
//	}
//
//	/**
//	 * 动态参数通过freemark test改变.
//	 * 
//	 * @param sql
//	 * @param mapParams
//	 * @return
//	 */
//	
//	@Override
//	public Map<String, Object> loadRpt(Report rpt, Map<String, Object> mapParams) {
//		Map<String, Object> result = new HashMap<String, Object>();
//		List<Bio> mainData = null;
//		// 如果是数据维度查询
//		mainData = query(rpt.getSqlValue(), mapParams);
//		// 如果有子结果集，主数据只有一条数据
//		if (rpt.getRptDataSets() != null && rpt.getRptDataSets().size() > 0) {
//			Map<String, Object> record = mainData.get(0);
//			for (ReportDataSet rptDs : rpt.getRptDataSets()) {
//				Map tempParam = new HashMap();
//				String[] refFields = rptDs.getRefFieldKey().split(",");
//				for (String refField : refFields) {
//					tempParam.put(refField, record.get(refField));
//				}
//				List<Bio> rptData = query(rptDs.getSqlValue(), tempParam);
//				result.put(rptDs.getDataSetKey(), rptData);
//			}
//			// 主数据用d关键字
//			result.put("d", record);
//		} else {// 如果没有子结果集，data为list类型
//			result.put("d", mainData);
//		}
//		return result;
//	}
//
//	@SuppressWarnings("rawtypes")
//	public ScrollableResults getScrollableResults(String sql, Map<String,Object> mapParams) {
//		sql = SQLUtil.freeMarkerSql(sql, mapParams);
//		// 添加数据纬度
//		sql = SQLUtil.getDimensionSql(sql);
//		sql = sql.replace("?", ":");
//		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//		setQueryParameter(query,mapParams);
//		ScrollableResults srs = query.scroll();
//		return srs;
//	}
//	private void setQueryParameter(Query query,Map<String, Object> mapParam){
//		for (Map.Entry entry : mapParam.entrySet()) {
//			String name = entry.getKey().toString();
//			Object value = entry.getValue();
//			if (value instanceof Object[]) {
//				Object[] objs = (Object[]) value;
//				query.setParameterList(name, objs);
//			} else {
//				if(value instanceof Date){
//					//用sql date解决日期不能走索引的问题
//					value=new java.sql.Date(((Date)value).getTime());
//				}
//				query.setParameter(name, value);
//				//query.setp
//			}
//		}
//	}
//	
//
//}
