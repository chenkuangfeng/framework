package com.ubsoft.framework.system.entity;

import java.io.Serializable;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.annotation.Table;
import com.ubsoft.framework.core.dal.entity.BaseEntity;


@Table(name = "SC_REGION")
public class Region extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;	

	/**区域编码*/
	@Column(length = 32, unique = true, nullable = false, name = "REGIONKEY")
	private String regionKey;
	/**区域名称*/
	@Column(length = 100, nullable = false, name = "REGIONNAME")
	private String regionName;
	/**上级区域编码*/
	@Column(length = 100, nullable = false, name = "OWNERKEY")	
	private String ownerKey;		
	/**排序*/ 
	@Column( name = "SEQ")
	private Integer seq;
	@Column(length = 255, name = "UDF1")
	private String udf1;
	@Column(length = 255, name = "UDF2")
	private String udf2;
	@Column(length = 255, name = "UDF3")
	private String udf3;
	@Column(length = 255, name = "UDF4")
	private String udf4;
	@Column(length = 255, name = "UDF5")
	private String udf5;
	@Column(length = 255, name = "UDF6")
	private String udf6;
	public String getRegionKey() {
		return regionKey;
	}
	public void setRegionKey(String regionKey) {
		this.regionKey = regionKey;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getOwnerKey() {
		return ownerKey;
	}
	public void setOwnerKey(String ownerKey) {
		this.ownerKey = ownerKey;
	}
	public String getUdf1() {
		return udf1;
	}
	public void setUdf1(String udf1) {
		this.udf1 = udf1;
	}
	public String getUdf2() {
		return udf2;
	}
	public void setUdf2(String udf2) {
		this.udf2 = udf2;
	}
	public String getUdf3() {
		return udf3;
	}
	public void setUdf3(String udf3) {
		this.udf3 = udf3;
	}
	public String getUdf4() {
		return udf4;
	}
	public void setUdf4(String udf4) {
		this.udf4 = udf4;
	}
	public String getUdf5() {
		return udf5;
	}
	public void setUdf5(String udf5) {
		this.udf5 = udf5;
	}
	public String getUdf6() {
		return udf6;
	}
	public void setUdf6(String udf6) {
		this.udf6 = udf6;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}


	

}