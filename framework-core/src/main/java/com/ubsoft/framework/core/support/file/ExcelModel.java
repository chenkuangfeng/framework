package com.ubsoft.framework.core.support.file;

import java.util.List;
import java.util.Map;

public class ExcelModel {
	protected String path;
    /**
     * 工作表名
     */
    protected String sheetName;
    
    protected List<Map> data;
    /**
     * 数据表的标题内容
     */
    protected List<ExcelColumn> columns;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public List<Map> getData() {
		return data;
	}
	public void setData(List<Map> data) {
		this.data = data;
	}
	public List<ExcelColumn> getColumns() {
		return columns;
	}
	public void setColumns(List<ExcelColumn> columns) {
		this.columns = columns;
	}
    
    
}
