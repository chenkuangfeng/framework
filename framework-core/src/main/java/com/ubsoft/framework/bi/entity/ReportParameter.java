package com.ubsoft.framework.bi.entity;

import java.io.Serializable;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;


@Table(name = "BI_REPORT_PARAMETER")
public class ReportParameter extends BaseEntity implements Serializable {	
	private static final long serialVersionUID = 1L;
	@Column(name="REPORTID",length = 32, nullable = false)
	private String reportId;
	@Column(name="PARAMKEY",length = 50, nullable = false)
	private String paramKey;
	@Column(name="PARAMNAME",length = 100, nullable = false)
	private String paramName;
	@Column(name="DATATYPE",length = 20)
	private String dataType;
	
	@Column(name="SEQ")
	private Integer seq;
	
	@Column(name="WIDTH")
	private Integer width;//宽度
	@Column(name="REQUIRED",length = 2)
	private String required;
	@Column(name="MUL",length = 2)
	private String mul;//多选
	@Column(name="SEARCHCODE",length = 255)
	private String searchCode;//弹出框编码
	@Column(name="CODE",length = 60)
	private String code;	//下拉框编码
	@Column(name="DEFAULTVALUE",length = 255)
	private String defaultValue;
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getParamKey() {
		return paramKey;
	}
	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	
	
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
	}
	public String getMul() {
		return mul;
	}
	public void setMul(String mul) {
		this.mul = mul;
	}
	
	public String getSearchCode() {
		return searchCode;
	}
	public void setSearchCode(String searchCode) {
		this.searchCode = searchCode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	
}
