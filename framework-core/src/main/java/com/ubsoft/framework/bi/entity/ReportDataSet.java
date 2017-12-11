package com.ubsoft.framework.bi.entity;

import java.io.Serializable;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;

/**
 * 
* @ClassName: ReportDataSet
* @Description: 报表子数据集
* @author chenkf
* @date 2017-2-27 下午03:27:24
* @version V1.0
 */

@Table(name = "BI_REPORT_DATASET")
public class ReportDataSet extends BaseEntity implements Serializable {

	@Column(name="REPORTID",length = 32, nullable = false)
	private String reportId;
	
	@Column(name="DATASETKEY",length = 32, nullable = false)
	private String dataSetKey;
	
	@Column(name="DATASETNAME",length = 100, nullable = false)
	private String dataSetName;	
	@Column(name="SQLVALUE",length = 32, nullable = false)
	private String sqlValue;
	
	@Column(name="REFFIELDKEY",length = 100, nullable = false)
	private String refFieldKey;

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getDataSetKey() {
		return dataSetKey;
	}

	public void setDataSetKey(String dataSetKey) {
		this.dataSetKey = dataSetKey;
	}

	public String getDataSetName() {
		return dataSetName;
	}

	public void setDataSetName(String dataSetName) {
		this.dataSetName = dataSetName;
	}

	

	public String getRefFieldKey() {
		return refFieldKey;
	}

	public void setRefFieldKey(String refFieldKey) {
		this.refFieldKey = refFieldKey;
	}

	public String getSqlValue() {
		return sqlValue;
	}

	public void setSqlValue(String sqlValue) {
		this.sqlValue = sqlValue;
	}
	
	
	
	

}
