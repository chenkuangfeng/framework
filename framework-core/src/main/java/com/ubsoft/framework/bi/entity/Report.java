package com.ubsoft.framework.bi.entity;

import java.io.Serializable;
import java.util.List;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;


@Table(name = "BI_REPORT")
public class Report extends BaseEntity implements Serializable {	
	@Column(name="REPORTKEY",length = 32, unique = true, nullable = false)
	private String reportKey;
	@Column(name="REPORTNAME",length = 100, nullable = false)
	private String reportName;
	@Column(name="REPORTTYPE",length = 50, nullable = false)
	private String reportType;//报表类型	
	@Column(name="UNITNAME",length = 100)
	private String unitName;
	
	@Column(name="SQLVALUE",length = 100)
	private String sqlValue;
	@Column(name="MODULE",length = 50)
	private String module;//报表地址或者模板地址	

	@Column(name="CATALOG",length = 50)
	private String catalog;
	public String getReportKey() {
		return reportKey;
	}	
	//报表图例字段
	@Column(name="LEGENDFIELD",length = 100)
	private String legendField;//图例纬度
	@Column(name="XFIELD",length = 100)
	private String xField;//X坐标字段
	@Column(name="YFIELD",length = 100)
	private String yField;//Y坐标字段
	public void setReportKey(String reportKey) {
		this.reportKey = reportKey;
	}
	
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	
	
	
	
	public String getSqlValue() {
		return sqlValue;
	}
	public void setSqlValue(String sqlValue) {
		this.sqlValue = sqlValue;
	}



	
	private List<ReportField> rptFields;
	
	private List<ReportParameter> rptParameters;
	
	
	
	private List<ReportDataSet> rptDataSets;
	public List<ReportField> getRptFields() {
		return rptFields;
	}
	public void setRptFields(List<ReportField> rptFields) {
		this.rptFields = rptFields;
	}
	public List<ReportParameter> getRptParameters() {
		return rptParameters;
	}
	public void setRptParameters(List<ReportParameter> rptParameters) {
		this.rptParameters = rptParameters;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public List<ReportDataSet> getRptDataSets() {
		return rptDataSets;
	}
	public void setRptDataSets(List<ReportDataSet> rptDataSets) {
		this.rptDataSets = rptDataSets;
	}

	public String getLegendField() {
		return legendField;
	}

	public void setLegendField(String legendField) {
		this.legendField = legendField;
	}

	public String getxField() {
		return xField;
	}

	public void setxField(String xField) {
		this.xField = xField;
	}

	public String getyField() {
		return yField;
	}

	public void setyField(String yField) {
		this.yField = yField;
	}
	
	
	
	
}
