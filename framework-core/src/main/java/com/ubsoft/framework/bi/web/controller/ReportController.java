package com.ubsoft.framework.bi.web.controller;


import com.ubsoft.framework.bi.cache.MemoryReport;
import com.ubsoft.framework.bi.entity.Report;
import com.ubsoft.framework.bi.service.IReportService;
import com.ubsoft.framework.bi.util.JDataSourceUtil;
import com.ubsoft.framework.bi.util.JasperUtil;
import com.ubsoft.framework.bi.web.view.JasperView;
import com.ubsoft.framework.bi.web.view.JxlsView;
import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.dal.model.PageResult;
import com.ubsoft.framework.core.dal.util.DataType;
import com.ubsoft.framework.core.dal.util.DynamicDataSource;
import com.ubsoft.framework.core.dal.util.SQLUtil;
import com.ubsoft.framework.core.exception.ComException;
import com.ubsoft.framework.core.exception.ExceptionHelper;
import com.ubsoft.framework.core.service.IFormService;
import com.ubsoft.framework.core.support.file.ExcelColumn;
import com.ubsoft.framework.core.support.file.ExcelUtil;
import com.ubsoft.framework.core.support.json.JsonHelper;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.system.entity.UISetting;
import com.ubsoft.framework.system.model.Subject;
import com.ubsoft.framework.web.controller.BaseController;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @ClassName: ReportController
 * @Description: 报表Http服务入口
 * @author chenkf
 * @date 2017-2-24 上午11:12:23
 * @version V1.0
 */
@RequestMapping("/report")
@Controller
public class ReportController extends BaseController {
	public static String REPORT_TYPE_FREEMARKER = "FREEMARKER";// freemarker模板报表
	public static String REPORT_TYPE_JSP = "JSP";// JSP模板报表
	public static String REPORT_TYPE_JASPER = "JASPER";// jasper报表
	public static String REPORT_TYPE_EXCEL = "EXCEL";// EXCEL模板报表
	public static String REPORT_TYPE_LIST = "TABLE";// 查询列表报表
	public static String DOCTYPE_PDF = "PDF";
	public static String DOCTYPE_HTML = "HTML";
	public static String DOCTYPE_XLS = "XLS";
	public static String DOCTYPE_XLSX = "XLSX";
	public static String DOCTYPE_XML = "XML";
	public static String DOCTYPE_RTF = "RFT";

	@Autowired
    IReportService rptService;

	@Autowired
    IFormService formService;
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    /**
	 *
	 * @Title: report
	 * @Description: 根据报表类型和模板生成报表数据界面； 主数据用d，参数用p传递模型
	 * @author chenkf
	 * @date 2017-2-24 上午11:14:15
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	@RequestMapping("/rpt.ctrl")
	public ModelAndView rpt(HttpServletRequest request, HttpServletResponse response) {
		String rptKey = request.getParameter("rptKey");
		String p = request.getParameter("p");// 查询参数
		Map mapParams = new HashMap();
		try {
			if (p != null) {
				mapParams = JsonHelper.transferMapParams(p);
			}
			Report rpt = MemoryReport.getInstance().get(rptKey);
			// 设置数据源
			if (rpt == null) {
				response.getWriter().write(rptKey + "报表不存在。");// 直接打印界面返回报错
			}
			String template = null;
			// ----------------常规List查询报表 转到界面但不执行查询----------------------//
			if (rpt.getReportType().equals("TABLE")) {
				template = rpt.getModule();
				if (template == null) {
					template = "/sys/report/template/TmpLst";
				}
				ModelAndView mav = new ModelAndView("view" + template);
				mav.addObject("p", rpt.getRptParameters());// 查询条件对象
				mav.addObject("fields", rpt.getRptFields());
				mav.addObject("rpt", rpt);
				// 加载报表界面方案
				List<UISetting> settings = formService.getUISetting(Subject.getSubject().getUserKey(), rpt.getReportKey());
				mav.addObject("uisetting", settings);
				return mav;

				// 饼图
			} else if (rpt.getReportType().equals("PIECHART") || rpt.getReportType().equals("PIEAREACHART") ||
					rpt.getReportType().equals("PIEDONUT")) {
				DynamicDataSource.setDataSource(rpt.getUnitName());
				template = rpt.getModule();
				String pieType= null;
				//集齐后需优化
				if (template == null) {
					template = "/sys/report/template/TmpPieChart";
					if (rpt.getReportType().equals("PIECHART"))
						template = "/sys/report/template/TmpPieChart";
					else if (rpt.getReportType().equals("PIEAREACHART")){
						template = "/sys/report/template/TmpPieChart";
						pieType ="PIEAREA";
						//template = "/sys/report/template/TmpPieAreaChart";
					}else if (rpt.getReportType().equals("PIEDONUT")){
						pieType ="PIEDONUT";
					}
				}
				ModelAndView mav = new ModelAndView("view" + template);
				mav.addObject("pieType", pieType);
				// mav.addObject("p", rpt.getRptParameters());// 查询条件对象
				List<Bio> result = rptService.query(rpt.getSqlValue(), mapParams);
				// 饼图的legend取xField的值 value取yField的值
				String legendField = rpt.getxField();
				String[] lengend = new String[result.size()];
				List series = new ArrayList();
				int i = 0;
				for (Bio bio : result) {
					Map serie = new HashMap();
					serie.put("value", bio.getDouble(rpt.getyField()));
					serie.put("name", bio.getString(rpt.getxField()));
					lengend[i] = bio.getString(legendField);
					series.add(serie);
					i++;
				}


				String legendData = JsonHelper.collection2json(lengend);
				mav.addObject("legendData", legendData);
				String serieData = JsonHelper.collection2json(JsonHelper.collection2json(series));
				mav.addObject("serieData", serieData);
				mav.addObject("rpt", rpt);
				return mav;
				// barchart
			} else if (rpt.getReportType().equals("BARCHART")||rpt.getReportType().equals("LINECHART")) {
				DynamicDataSource.setDataSource(rpt.getUnitName());
				template = rpt.getModule();
				if (template == null) {
					if(rpt.getReportType().equals("BARCHART")){
					template = "/sys/report/template/TmpBarChart";
					}else{
						template = "/sys/report/template/TmpLineChart";

					}
				}
				ModelAndView mav = new ModelAndView("view" + template);
				// mav.addObject("p", rpt.getRptParameters());// 查询条件对象
				List<Bio> result = rptService.query(rpt.getSqlValue(), mapParams);

				String axisType = null;

				String legendField = rpt.getLegendField();

				List series = new ArrayList();
				List axisData = new ArrayList();
				// 单个纬度对比,设置x,和Y
				if (legendField == null) {
					if (result.size() > 0) {
						Object xFieldValue = result.get(0).get(rpt.getxField());
						if (xFieldValue instanceof Number) {
							axisType = "Y";
						} else {
							axisType = "X";
						}
					}
					mav.addObject("axisType", axisType);
					Map serie = new HashMap();
					if (axisType.equals("X")) {
						serie.put("name", rpt.getyField());
					} else {
						serie.put("name", rpt.getxField());

					}

					List seriesData = new ArrayList();

					for (Bio bio : result) {
						if (axisType.equals("X")) {
							axisData.add(bio.getString(rpt.getxField()));
							seriesData.add(bio.getDouble(rpt.getyField()));
						} else {
							axisData.add(bio.getString(rpt.getyField()));
							seriesData.add(bio.getDouble(rpt.getxField()));
						}
					}
					serie.put("data", JsonHelper.collection2json(seriesData));
					series.add(serie);
					mav.addObject("axisData", JsonHelper.collection2json(axisData));

				} else {
					String[] lengendFields = legendField.split(",");
					String legendData = JsonHelper.collection2json(lengendFields);
					mav.addObject("legendData", legendData);
					if (rpt.getxField() != null) {
						axisType = "X";
					} else {
						axisType = "Y";
					}
					mav.addObject("axisType", axisType);

					for (Bio bio : result) {
						if (axisType.equals("X")) {
							axisData.add(bio.getString(rpt.getxField()));
						}else{
							axisData.add(bio.getString(rpt.getyField()));
						}
					}
					mav.addObject("axisData", JsonHelper.collection2json(axisData));

					for (String lengend : lengendFields) {
						Map serie = new HashMap();
						serie.put("name", lengend);
						List seriesData = new ArrayList();
						for (Bio bio : result) {
							seriesData.add(bio.getDouble(lengend));
						}
						serie.put("data", JsonHelper.collection2json(seriesData));
						series.add(serie);
					}

				}
				mav.addObject("series", series);

				mav.addObject("rpt", rpt);
				return mav;
				// freemarker报表，转到界面并执行查询----------------------//
			} else if (rpt.getReportType().equals(REPORT_TYPE_FREEMARKER)) {
				ModelAndView mav = new ModelAndView(rpt.getModule());
				DynamicDataSource.setDataSource(rpt.getUnitName());
				Map<String, Object> result = rptService.loadRpt(rpt, mapParams);
				Iterator iter = result.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					mav.addObject(entry.getKey() + "", entry.getValue());
				}
				mav.addObject("user", Subject.getSubject().getUserKey());
				// mav.addObject("user", Subject.getSubject().getUserKey());
				return mav;
				// ---------------- jsp页面报表----------------------//
			} else if (rpt.getReportType().equals(REPORT_TYPE_JSP)) {
				ModelAndView mav = new ModelAndView(rpt.getModule());
				DynamicDataSource.setDataSource(rpt.getUnitName());

				Map<String, Object> result = rptService.loadRpt(rpt, mapParams);
				Iterator iter = result.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					mav.addObject(entry.getKey() + "", entry.getValue());
				}
				return mav;
				// ----------------jasper报表----------------------//
			} else if (rpt.getReportType().equals(REPORT_TYPE_JASPER)) {
				DynamicDataSource.setDataSource(rpt.getUnitName());

				String showType = request.getParameter("showType");
				if (StringUtil.isEmpty(showType)) {
					showType = DOCTYPE_HTML;
				}
				Map model = new HashMap();
				// 界面显示类型，支持html和pdf
				model.put("type", showType);
				Map jasperParams = new HashMap();
				String reportFile = request.getSession().getServletContext().getRealPath(rpt.getModule());
				model.put("reportFile", reportFile);
				if (showType.equals(DOCTYPE_HTML)) {
					// 生成html图片路径
					String imgDir = request.getSession().getServletContext().getRealPath("/WEB-INF/view/template/image/");
					// 显示html图片路径
					String imgUrl = request.getContextPath() + "/WEB-INF/view/template/image/";
					model.put("imgDir", imgDir);
					model.put("imgUrl", imgUrl);
				}
				Map<String, Object> result = rptService.loadRpt(rpt, mapParams);
				Iterator iter = result.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					if (entry.getKey().equals("d")) {
						// jasreport的数据源
						model.put("d", entry.getValue());
					} else {
						// 子表作为jasper的参数传递
						jasperParams.put(entry.getKey(), entry.getValue());
					}
				}
				model.put("p", jasperParams);
				return new ModelAndView(new JasperView(), model);
				// ----------------Excel模板报表报表----------------------//
			} else if (rpt.getReportType().equals(REPORT_TYPE_EXCEL)) {
				DynamicDataSource.setDataSource(rpt.getUnitName());

				Map model = new HashMap();
				String reportFile = request.getSession().getServletContext().getRealPath(rpt.getModule());
				model.put("templateFileName", reportFile);
				Map<String, Object> result = rptService.loadRpt(rpt, mapParams);
				model.put("d", result);
				model.put("destFileName", rpt.getReportName() + ".xlsx");
				return new ModelAndView(new JxlsView(), model);
			} else {
				throw new ComException(99, "不支持该报表格式。");
			}

		} catch (Throwable e) {
			e.printStackTrace();
			ExceptionHelper.dealRuntimeException(e);
			return null;
		} finally {
			DynamicDataSource.removeDataSource();
		}

	}

	/**
	 *
	 * @Title: export
	 * @Description: 根据报表类型和要导出的类型导出报表数据； 目前只支持列表类型的报表导出到excel
	 * @author chenkf
	 * @param request
	 * @param response
	 */
	@RequestMapping("/export.ctrl")
	public void export(HttpServletRequest request, HttpServletResponse response) {
		try {
			String fileType = request.getParameter("type");
			String rptKey = request.getParameter("rptKey");
			String p = request.getParameter("p");
			// p=java.net.URLDecoder.decode(p,"utf-8");
			Report rpt = MemoryReport.getInstance().get(rptKey);
			Map<String, Object> mapParam = new HashMap<String, Object>();
			if (p != null) {
				JSONArray jsonArray = JSONArray.fromObject(p);
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject param = jsonArray.getJSONObject(i);
					String type = param.getString("type");
					String name = param.getString("name");
					Object value = param.get("value");
					mapParam.put(name, DataType.convert(type, value));
				}
			}
			if (rpt.getReportType().equals("TABLE")) {
				String columnKeysStr = request.getParameter("columns");// 列key
				// columnKeysStr=java.net.URLDecoder.decode(columnKeysStr,"utf-8");
				List<ExcelColumn> columns = JsonHelper.json2List(columnKeysStr, ExcelColumn.class);
				SXSSFWorkbook workbook = exportTableExcel(rpt, mapParam, columns);
				OutputStream out = response.getOutputStream();
				// 设置响应头和下载保存的文件名
				response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(rpt.getReportName(), "UTF-8") + ".xlsx");
				// 定义输出类型
				response.setContentType("APPLICATION/msexcel");
				workbook.write(out);
				out.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ComException(0, "生成EXCEL异常：" + ex.getMessage());
		}
	}

	private SXSSFWorkbook exportTableExcel(Report rpt, Map<String, Object> mapParam, final List<ExcelColumn> columns) {

		final SXSSFWorkbook workbook = new SXSSFWorkbook();
		try {
			String unitName = rpt.getUnitName();
			String sql = rpt.getSqlValue();
			// 设置数据源
			if (unitName != null) {
				DynamicDataSource.setDataSource(unitName);
			}
			if (rpt.getReportType().equals("TABLE")) {
				sql = SQLUtil.freeMarkerSql(sql, mapParam);
				// 添加数据纬度
				sql = SQLUtil.getDimensionSql(sql);
				sql = sql.replace("?", ":");
				// ScrollableResults之支持数组,需要指定固定列
				String queryColumn = "";
				for (ExcelColumn col : columns) {
					queryColumn += col.getField() + ",";
				}
				queryColumn = queryColumn.substring(0, queryColumn.length() - 1);
				sql = "select " + queryColumn + " from (" + sql + ") T";
				// 此处用openSession
//				session = sessionFactory.openSession();
//				Query query = session.createSQLQuery(sql);
				//setQueryParameter(query, mapParam);
				//srs = query.scroll();

				final Sheet sheet = workbook.createSheet(rpt.getReportName());
				// sheet.setDefaultColumnWidth((short) 15);
				for (int i = 0; i < columns.size(); i++) {
					ExcelColumn col = columns.get(i);
					// sheet.autoSizeColumn(i, true);
					if (col.getWidth() > 0) {
						sheet.setColumnWidth(i, col.getWidth() * 256 / 7);
					}

				}
				ExcelUtil.createHeader(workbook, sheet, columns);
                setQueryParameter(mapParam);//转换参数
                jdbcTemplate.query(sql, mapParam, new RowMapper() {
                    public Object mapRow(ResultSet rs, int rowNumber) throws SQLException{
                        ExcelUtil.createRow(workbook, sheet, columns, rs, rowNumber);
                        return null;
                    }
                });


			}
		} catch (Throwable e) {
			ExceptionHelper.dealRuntimeException(e);
		} finally {
			DynamicDataSource.removeDataSource();


		}
		return workbook;
	}

	@SuppressWarnings("rawtypes")
	private void setQueryParameter(Map<String, Object> mapParam) {
		for (Map.Entry entry : mapParam.entrySet()) {
			String name = entry.getKey().toString();
			Object value = entry.getValue();
			if (value instanceof Object[]) {
				Object [] arayValue=(Object[])value;
                //in 参数必须是list
                mapParam.put(name,  Arrays.asList(arayValue));
			} else {
				if (value instanceof Date) {
					// 用sql date解决日期不能走索引的问题
					value = new java.sql.Date(((Date) value).getTime());
					mapParam.put(name,value);
				}

			}
		}
	}

	/**
	 *
	 * @Title: print
	 * @Description: 后台直接打印报表，只支持jasperReport的打印
	 * @author chenkf
	 * @date 2017-2-28 下午04:24:44
	 * @param request
	 * @param response
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/print.ctrl")
	public @ResponseBody
	String print(HttpServletRequest request, HttpServletResponse response) {
		try {
			String rptKey = request.getParameter("rptKey");// 报表key
			String p = request.getParameter("p");// 参数
			String printName = request.getParameter("print");// 打印机
			Report rpt = MemoryReport.getInstance().get(rptKey);
			String reportFile = request.getSession().getServletContext().getRealPath(rpt.getModule());// 报表文件
			Map mapParams = JsonHelper.transferMapParams(p);

			DynamicDataSource.setDataSource(rpt.getUnitName());

			Map<String, Object> result = rptService.loadRpt(rpt, mapParams);
			JDataSourceUtil ds = null;// 报表数据源
			Map jasperParams = new HashMap();
			Iterator iter = result.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				if (entry.getKey().equals("d")) {
					ds = new JDataSourceUtil((Map) entry.getValue());
				} else {
					// 子表作为jasper的参数传递
					jasperParams.put(entry.getKey(), entry.getValue());
				}
			}
			JasperUtil.print(reportFile, printName, request, response, jasperParams, ds);
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("status", "S");
			res.put("ret", "已发送打印机。");
			// String result = JsonHelper.bean2Json(res);
			return JsonHelper.bean2Json(res);
		} catch (Exception e) {
			ComException ex = null;
			if (e instanceof ComException) {
				ex = (ComException) e;
			} else {
				ex = new ComException(-2, e.getMessage());
			}
			int code = ex.getCode();

			String message = ex.getMessage();
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("status", "E");
			res.put("errCode", code);
			res.put("errMsg", message);
			String result = JsonHelper.bean2Json(res);
			return result;
		} finally {
			DynamicDataSource.removeDataSource();
		}

	}

	/**
	 * 报表数据查询
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	@RequestMapping("/query.ctrl")
	public @ResponseBody
	String query(HttpServletRequest request, HttpServletResponse response) {
		String rptKey = request.getParameter("rptKey");
		String pageSize = request.getParameter("pageSize");
		String pageNo = request.getParameter("pageNo");
		/**
		 * 查询参数
		 */
		String p = request.getParameter("p");
		JSONArray jsonArray = JSONArray.fromObject(p);
		Map mapParam = new HashMap();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject param = jsonArray.getJSONObject(i);
			String type = param.getString("type");
			String name = param.getString("name");
			Object value = param.get("value");
			mapParam.put(name, DataType.convert(type, value));
		}
		Report rpt = MemoryReport.getInstance().get(rptKey);
		String unitName = rpt.getUnitName();
		// 设置数据源
		if (unitName != null) {
			DynamicDataSource.setDataSource(unitName);
		}
		Map<String, Object> res = new HashMap<String, Object>();
		try {
			if (pageSize != null) {
				PageResult<Bio> result = rptService.queryPage(rpt.getSqlValue(), Integer.parseInt(pageSize), Integer.parseInt(pageNo), mapParam);
				res.put("status", "S");
				res.put("ret", result);
			} else {
				List<Bio> result = rptService.query(rpt.getSqlValue(), mapParam);
				res.put("status", "S");
				res.put("ret", result);

			}
			String result = JsonHelper.bean2Json(res);
			// System.out.println(result);
			return result;
		} catch (Exception ex) {

			return responseError(ex, res);
		} finally {
			if (unitName != null) {
				DynamicDataSource.removeDataSource();
			}
		}
	}

}
