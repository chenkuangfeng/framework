package com.ubsoft.framework.web.controller;

import com.ubsoft.framework.core.dal.model.ConditionLeafNode;
import com.ubsoft.framework.core.dal.model.ConditionTree;
import com.ubsoft.framework.core.dal.model.QueryModel;
import com.ubsoft.framework.core.dal.util.DynamicDataSource;
import com.ubsoft.framework.core.dal.util.SQLUtil;
import com.ubsoft.framework.core.support.file.ExcelColumn;
import com.ubsoft.framework.core.support.file.ExcelUtil;
import com.ubsoft.framework.core.support.json.JsonHelper;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.metadata.cache.MemoryFdmMeta;
import com.ubsoft.framework.metadata.model.form.fdm.FdmMeta;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/")
@Controller
public class FileController extends BaseController {

    @Autowired
    JdbcTemplate jdbcTemplate;
    /**
     * 界面通用查询Controller
     *
     * @return
     */
    @RequestMapping("/file/exportExcel.ctrl")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response){
        String qmString = request.getParameter("queryModel");
        String columnKeysStr = request.getParameter("columns");// 列key
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
                public Object mapRow(ResultSet rs, int rowNumber) throws SQLException{
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

    private String getMasterSql(FdmMeta fdmMeta, String condition, String orderBy){
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


}
