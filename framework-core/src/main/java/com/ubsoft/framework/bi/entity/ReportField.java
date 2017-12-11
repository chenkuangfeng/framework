package com.ubsoft.framework.bi.entity;

import java.io.Serializable;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;


@Table(name = "BI_REPORT_FIELD")
public class ReportField extends BaseEntity implements Serializable {	
	private static final long serialVersionUID = 1L;
	@Column(name="REPORTID",length = 32,nullable = false)
	private String reportId;
	@Column(name="FIELDKEY",length = 50, nullable = false)
	private String fieldKey;//字段key
	@Column(name="FIELDNAME",length = 100, nullable = false) //字段名
	private String fieldName;	
	@Column(name="FORMULA",length = 50) //计算方式:汇总,计数,平均;
	private String formula;	
	@Column(name="DATATYPE",length = 20)
	private String dataType;//字段数据类型
	@Column(name="FIELDLENGTH",nullable = false)
	private Integer fieldLength;	//长度	
	@Column(name="SCALE")
	private Integer scale;	//精度
	@Column(name="SEQ")
	private Integer seq;	//排序
	@Column(name="FORMAT",length = 50)
	private String format;//格式化 
	@Column(name="ALIGN",length = 50)
	private String align; //对齐	
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getFieldKey() {
		return fieldKey;
	}
	public void setFieldKey(String fieldKey) {
		this.fieldKey = fieldKey;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}	
	public Integer getFieldLength() {
		return fieldLength;
	}
	public void setFieldLength(Integer fieldLength) {
		this.fieldLength = fieldLength;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public Integer getScale() {
		return scale;
	}
	public void setScale(Integer scale) {
		this.scale = scale;
	}
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	
	
	

}
