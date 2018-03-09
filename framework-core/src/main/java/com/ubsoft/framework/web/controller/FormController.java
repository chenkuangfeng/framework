package com.ubsoft.framework.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ContextLoader;

import com.ubsoft.framework.core.conf.AppConfig;
import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.dal.model.BioSet;
import com.ubsoft.framework.core.dal.model.ConditionLeafNode;
import com.ubsoft.framework.core.dal.model.ConditionTree;
import com.ubsoft.framework.core.dal.model.PageResult;
import com.ubsoft.framework.core.dal.model.QueryModel;
import com.ubsoft.framework.core.dal.util.DynamicDataSource;
import com.ubsoft.framework.core.dal.util.SQLUtil;
import com.ubsoft.framework.core.exception.ComException;
import com.ubsoft.framework.core.service.IFormService;
import com.ubsoft.framework.core.support.file.ExcelColumn;
import com.ubsoft.framework.core.support.file.ExcelUtil;
import com.ubsoft.framework.core.support.json.JsonHelper;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.metadata.cache.MemoryFdmMeta;
import com.ubsoft.framework.metadata.model.form.fdm.DetailMeta;
import com.ubsoft.framework.metadata.model.form.fdm.FdmMeta;
import com.ubsoft.framework.system.cache.MemoryLookup;
import com.ubsoft.framework.system.entity.LookupDetail;
import com.ubsoft.framework.system.entity.UISetting;
import com.ubsoft.framework.system.model.Subject;
import com.ubsoft.framework.system.service.ILookupDetailService;
import com.ubsoft.framework.system.service.IOrgService;
import com.ubsoft.framework.system.service.IPermService;

@RequestMapping("/")
@RestController
public class FormController extends BaseController {
	@Autowired
	IFormService formService;
	@Autowired
	ILookupDetailService lookupService;
	@Autowired
	IPermService permService;
	@Autowired
	IOrgService orgService;
	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * 界面通用查询Controller
	 * 
	 * @return
	 */
	@RequestMapping("/form/query.ctrl")
	public String query() {
		String qmString = request.getParameter("queryModel");
		String unitName = getUnitName();
		if (unitName != null) {
			DynamicDataSource.setDataSource(unitName);
		}
		Map<String, Object> res = new HashMap<String, Object>();
		try {
			Map mapping = new HashMap();
			mapping.put("nodes", ConditionLeafNode.class);
			QueryModel qm = (QueryModel) JsonHelper.json2Bean(qmString, QueryModel.class, mapping);
			if (qm.isPage()) {
				PageResult<Bio> result = formService.queryPage(qm);
				res.put("status", "S");
				res.put("ret", result);
			} else {
				List<Bio> set = formService.query(qm);
				res.put("status", "S");
				res.put("ret", set);
			}
			return JsonHelper.bean2Json(res);
		} catch (Exception ex) {
			ex.printStackTrace();
			return responseError(ex, res);
		} finally {
			if (unitName != null) {
				DynamicDataSource.removeDataSource();
			}
		}

	}

	/**
	 * 通用界面加载方法，针对单条数据单表或者主从表。
	 * 
	 * @return
	 */
	@RequestMapping("/form/load.ctrl")
	public String load() {
		String unitName = getUnitName();
		if (unitName != null) {
			DynamicDataSource.setDataSource(unitName);
		}
		Map<String, Object> res = new HashMap<String, Object>();
		String fdmKey = request.getParameter("fdm");
		String id = request.getParameter("id");
		try {
			FdmMeta fdmMeta = MemoryFdmMeta.getInstance().get(fdmKey);
			BioSet result = formService.load(fdmKey, id);
			res.put("status", "S");
			res.put("ret", result);
			String json = JsonHelper.bean2Json(res);
			return json;
		} catch (Exception ex) {
			return responseError(ex, res);
		} finally {
			if (unitName != null) {
				DynamicDataSource.removeDataSource();
			}
		}

	}

	/**
	 * 通用界面保存方法，针对单表和主从表
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/form/save.ctrl")
	public String saveForm() {
		String unitName = getUnitName();
		if (unitName != null) {
			DynamicDataSource.setDataSource(unitName);
		}
		Map<String, Object> res = new HashMap<String, Object>();
		String fdmKey = request.getParameter("fdm");
		String requestData = request.getParameter("data");
		FdmMeta fdmMeta = MemoryFdmMeta.getInstance().get(fdmKey);
		try {
			Map jsonConfig = new HashMap();
			jsonConfig.put("master", Bio.class);
			if (fdmMeta.getDetails() != null) {
				for (DetailMeta meta : fdmMeta.getDetails()) {
					String detailBioKey = meta.getId() == null ? meta.getBio() : meta.getId();
					jsonConfig.put(detailBioKey, Bio.class);
				}
			}
			BioSet bioSet = (BioSet) JsonHelper.json2Bean(requestData, BioSet.class, jsonConfig);
			formService.saveForm(fdmKey, bioSet);
			// 返回id,前台重新加载,后期优化
			res.put("ret", bioSet);
			res.put("id", bioSet.getMaster().get("id"));
			res.put("status", "S");
			return JsonHelper.bean2Json(res);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			return responseError(ex, res);
		} finally {
			if (unitName != null) {
				DynamicDataSource.removeDataSource();
			}
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/form/saveList.ctrl")
	public String saveList() {
		String unitName = getUnitName();
		if (unitName != null) {
			DynamicDataSource.setDataSource(unitName);
		}
		Map<String, Object> res = new HashMap<String, Object>();
		String fdmKey = request.getParameter("fdm");
		String requestData = request.getParameter("data");
		try {
			List<Bio> listBio = JsonHelper.json2List(requestData, Bio.class);
			// BioSet bioSet = (BioSet) JsonHelper.json2Bean(requestData,
			// BioSet.class, jsonConfig);
			formService.saveForm(fdmKey, listBio);
			res.put("ret", listBio);
			res.put("status", "S");
			return JsonHelper.bean2Json(res);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			return responseError(ex, res);
		} finally {
			if (unitName != null) {
				DynamicDataSource.removeDataSource();
			}
		}

	}

	@RequestMapping("/form/delete.ctrl")
	public String deleteForm() {
		String unitName = getUnitName();
		if (StringUtil.isNotEmpty(unitName)) {
			DynamicDataSource.setDataSource(unitName);
		}
		Map<String, Object> res = new HashMap<String, Object>();
		String fdmId = request.getParameter("fdm");
		String ids = request.getParameter("ids");

		Serializable[] delIds = (Serializable[]) JsonHelper.json2Array(ids, Serializable.class);
		try {
			FdmMeta fdmMeta = MemoryFdmMeta.getInstance().get(fdmId);
			if (fdmMeta == null) {
				throw new ComException(ComException.MIN_ERROR_CODE_FDM, "不存在FDM：" + fdmId);
			}
			formService.deleteForm(fdmId, delIds);
			res.put("status", "S");
			return JsonHelper.bean2Json(res);
		} catch (Exception ex) {
			return responseError(ex, res);
		} finally {
			if (StringUtil.isNotEmpty(unitName)) {
				DynamicDataSource.removeDataSource();
			}
		}

	}

	@RequestMapping("/form/deleteDetail.ctrl")
	public String deleteFormDetail() {
		String unitName = getUnitName();
		if (StringUtil.isNotEmpty(unitName)) {
			DynamicDataSource.setDataSource(unitName);
		}
		Map<String, Object> res = new HashMap<String, Object>();
		String fdmId = request.getParameter("fdm");
		String ids = request.getParameter("ids");
		// 删除明细表
		String detailKey = request.getParameter("detail");
		Serializable[] delIds = (Serializable[]) JsonHelper.json2Array(ids, Serializable.class);
		try {
			FdmMeta fdmMeta = MemoryFdmMeta.getInstance().get(fdmId);
			if (fdmMeta == null) {
				throw new ComException(ComException.MIN_ERROR_CODE_FDM, "不存在FDM：" + fdmId);
			}
			DetailMeta detailMeta = fdmMeta.getDetailMeta(detailKey);
			formService.deleteBio(detailMeta.getBio(), delIds);

			res.put("status", "S");

			return JsonHelper.bean2Json(res);
		} catch (Exception ex) {
			return responseError(ex, res);
		} finally {
			if (StringUtil.isNotEmpty(unitName)) {
				DynamicDataSource.removeDataSource();
			}
		}

	}

	/**
	 * 下拉框数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/form/lookup.ctrl")
	public String lookup() {
		Map<String, Object> res = new HashMap<String, Object>();
		String lkkey = request.getParameter("key");
		String unitName = getUnitName();
		if (StringUtil.isNotEmpty(unitName)) {
			DynamicDataSource.setDataSource(unitName);
		}
		try {
			List<LookupDetail> listResult = null;
			Object obj = MemoryLookup.getInstance().get(lkkey);
			if (obj != null) {
				listResult = (List<LookupDetail>) obj;
			} else {
				listResult = lookupService.gets("lkKey", lkkey, "seq");
				LookupDetail lkd = new LookupDetail();
				lkd.setLkdKey("");
				lkd.setLkdName("----");
				listResult.add(0, lkd);

				MemoryLookup.getInstance().put(lkkey, listResult);
			}
			// 插入空行

			res.put("status", "S");
			res.put("ret", listResult);
			String result = JsonHelper.bean2Json(res);
			return result;
		} catch (Throwable e) {
			return responseError(e, res);
		} finally {
			if (StringUtil.isNotEmpty(unitName)) {
				DynamicDataSource.removeDataSource();
			}
		}

	}

	/**
	 * 保存用户界面方案
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/form/saveUISetting.ctrl")
	public String saveUISetting() {
		String unitName = getUnitName();
		if (StringUtil.isNotEmpty(unitName)) {
			DynamicDataSource.setDataSource(unitName);
		}
		Map<String, Object> res = new HashMap<String, Object>();
		String uisSetting = request.getParameter("us");

		try {
			List<UISetting> uiSettingList = JsonHelper.json2List(uisSetting, UISetting.class);
			for (UISetting setting : uiSettingList) {
				if (setting.getUserKey() == null) {
					setting.setUserKey(Subject.getSubject().getUserKey());
				}
			}
			List<UISetting> result = formService.saveUISetting(uiSettingList);
			res.put("status", "S");
			res.put("ret", result);
			return JsonHelper.bean2Json(res);
		} catch (Exception ex) {

			return responseError(ex, res);
		} finally {
			if (StringUtil.isNotEmpty(unitName)) {
				DynamicDataSource.removeDataSource();
			}
		}

	}

	private String getMasterSql(FdmMeta fdmMeta, String condition, String orderBy) {
		// 查询的sql
		String sql = fdmMeta.getMaster().getSql();

		if (StringUtil.isNotEmpty(sql)) {
			if (StringUtil.isNotEmpty(condition)) {
				if (sql.toLowerCase().indexOf(" where") != -1) {
					sql += " and (" + condition + ")";
				} else {
					sql += " where " + condition;
				}
			}

			sql = SQLUtil.getDimensionSql(sql);// 加入数据纬度权限

			if (StringUtil.isNotEmpty(orderBy)) {
				sql += " order by " + orderBy;
			}
			// sql = this.getSqlDimension(sql);// 加入数据维度

		}
		return sql;
	}

	/**
	 * 界面通用查询Controller
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/form/exportExcel.ctrl")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String qmString = URLDecoder.decode(request.getParameter("queryModel"), "UTF-8");
		String columnKeysStr = URLDecoder.decode(request.getParameter("columns"), "UTF-8");// 列key
		String unitName = getUnitName();
		if (StringUtil.isNotEmpty(unitName)) {
			DynamicDataSource.setDataSource(unitName);
		}
		final SXSSFWorkbook workbook = new SXSSFWorkbook();
		final Sheet sheet = workbook.createSheet("sheet1");
		final List<ExcelColumn> columns = JsonHelper.json2List(columnKeysStr, ExcelColumn.class);

		OutputStream out = null;
		try {
			out = response.getOutputStream();
			Map mapping = new HashMap();
			mapping.put("nodes", ConditionLeafNode.class);
			QueryModel qm = (QueryModel) JsonHelper.json2Bean(qmString, QueryModel.class, mapping);
			String fdmId = qm.getFdmId();
			FdmMeta fdmMeta = MemoryFdmMeta.getInstance().get(fdmId);
			ConditionTree ctree = qm.getConditionTree();// 条件树
			String condition = ctree.transferCondition();// 条件字符串
			Object[] params = ctree.transferParameter();// 查询参数
			String orderBy = qm.getOrderBy();// 默认排序
			String sql = this.getMasterSql(fdmMeta, condition, orderBy);
			String queryColumn = "";
			for (ExcelColumn col : columns) {
				// 导出下拉中文处理
				queryColumn += col.getDisplayField() == null ? col.getField() : col.getDisplayField();
				queryColumn += ",";
			}
			queryColumn = queryColumn.substring(0, queryColumn.length() - 1);

			sql = "select " + queryColumn + " from (" + sql + ") T";
			for (int i = 0; i < columns.size(); i++) {
				ExcelColumn col = columns.get(i);
				//
				if (col.getWidth() > 0) {
					sheet.setColumnWidth(i, col.getWidth() * 256 / 7);
				} else {
					sheet.autoSizeColumn(i, true);
				}

			}
			ExcelUtil.createHeader(workbook, sheet, columns);
			jdbcTemplate.query(sql, params, new RowMapper() {
				public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
					ExcelUtil.createRow(workbook, sheet, columns, rs, rowNumber);
					return null;
				}
			});
			// 设置响应头和下载保存的文件名
			response.setHeader("content-disposition", "attachment;filename=export.xlsx");
			// 定义输出类型
			response.setContentType("APPLICATION/msexcel");
			workbook.write(out);
			// out.close();
			// 此处用openSession

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {

			if (unitName != null) {
				DynamicDataSource.removeDataSource();
			}
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@RequestMapping("/clear.ctrl")
	public String refreshCache(HttpServletResponse response) throws IOException {
		// 设置缓存配置文件路径
		String cachePath = request.getSession().getServletContext().getRealPath("/WEB-INF/conf/ehcache.xml");
		String configPath = request.getSession().getServletContext().getRealPath("/WEB-INF/conf/config.xml");
		// 加载系统全局配置信息
		AppConfig.initConfig(configPath);
		AppConfig.sprintContext = ContextLoader.getCurrentWebApplicationContext();
		// 加载系统服务器端缓存
		AppConfig.initCache(cachePath);
		// 加载fdm
		AppConfig.initMetadata();
		AppConfig.initEsb("");
		MemoryLookup.getInstance().clear();

		return "ok";
	}

}
