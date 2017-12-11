package com.ubsoft.framework.bi.model;

import java.util.List;
import java.util.Map;

import com.ubsoft.framework.bi.entity.Report;
import com.ubsoft.framework.bi.entity.ReportField;
import com.ubsoft.framework.bi.entity.ReportParameter;


public class ReportModel {
	
	private List<ReportField> fields;
	private List<ReportParameter> params;
	private List<Map> data;
	private Report report;
	public List<ReportField> getFields() {
		return fields;
	}
	public void setFields(List<ReportField> fields) {
		this.fields = fields;
	}
	public List<ReportParameter> getParams() {
		return params;
	}
	public void setParams(List<ReportParameter> params) {
		this.params = params;
	}
	public Report getReport() {
		return report;
	}
	public void setReport(Report report) {
		this.report = report;
	}
	public List<Map> getData() {
		return data;
	}
	public void setData(List<Map> data) {
		this.data = data;
	}
	
	
	
	
}
