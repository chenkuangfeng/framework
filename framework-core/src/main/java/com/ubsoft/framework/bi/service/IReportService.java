package com.ubsoft.framework.bi.service;

import java.util.List;
import java.util.Map;

import com.ubsoft.framework.bi.entity.Report;
import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.dal.model.PageResult;
import com.ubsoft.framework.core.service.IBaseService;


public interface IReportService extends IBaseService<Report> {
	/**
	 * 初始化报表配置
	 */
	 void initReport();
	 /**
	  * 查询列表数据
	  * @param mapParams
	  * @return
	  */
	List<Bio> query(String sql, Map<String, Object> mapParams);
	/**
	 * 分页查询数据
	 * @param mapParams
	 * @return
	 */
	PageResult<Bio> queryPage(String sql, int pageSize, int pageNumber, Map<String, Object> mapParams);
	
	/**
	 * 表单类型报表数据加载
	 * @param mapParams
	 * @return
	 */
	Map<String,Object> loadRpt(Report report, Map<String, Object> mapParams);

	/**
	 * 返回游标类型的报表查询,主要考虑大数据量,通过游标方式获取数据
	 * @param sql
	 * @param mapParams
	 * @return
	 */
	//public ScrollableResults getScrollableResults(String sql, Map<String, Object> mapParams);
	
	
}
