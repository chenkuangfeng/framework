package com.ubsoft.framework.system.entity;

import java.io.Serializable;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.entity.BaseEntity;

public abstract class Unit extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "LICTYPE", length = 2)
	private String licType;// 执照类型
	@Column(name = "LICCODE", length = 30)
	private String licCode;// 执照编号
	@Column(name = "TEL", length = 25)
	private String tel;// 电话
	@Column(name = "FAX", length = 25)
	private String fax;// 传真

	@Column(name = "EMAIL", length = 30)
	private String email;// 邮箱

	@Column(name = "POSTCODE", length = 30)
	private String postCode;// 邮编

	@Column(name = "PROVINCE", length = 30)
	private String province;// 省份

	@Column(name = "CITY", length = 30)
	private String city;// 城市

	@Column(name = "DISTRICT", length = 30)
	private String district;// 区县

	@Column(name = "AREA", length = 30)
	private String area;// 区域
	@Column(name = "ADDRESS", length = 100)
	private String address;// 地址
	@Column(name = "WEBSITE", length = 30)
	private String website;// 网站
	@Column(name = "CONTACTPERSON", length = 30)
	private String contactPerson;// 联系人 ;

	@Column(name = "GM_NAME", length = 30)
	private String GM_NAME;// 总经理 ;
	@Column(name = "GM_TEL", length = 25)
	private String GM_TEL;// 总经理电话
	@Column(name = "GM_EMAIL", length = 30)
	private String GM_EMAIL;// 总经理邮箱

	@Column(name = "AM_NAME", length = 30)
	private String AM_NAME;// 行政负责人 ;
	@Column(name = "AM_TEL", length = 25)
	private String AM_TEL;// 行政负责人电话
	@Column(name = "AM_EMAIL", length = 30)
	private String AM_EMAIL;// 行政负责人邮箱

	public String getLicType() {
		return licType;
	}

	public void setLicType(String licType) {
		this.licType = licType;
	}

	public String getLicCode() {
		return licCode;
	}

	public void setLicCode(String licCode) {
		this.licCode = licCode;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getGM_NAME() {
		return GM_NAME;
	}

	public void setGM_NAME(String gM_NAME) {
		GM_NAME = gM_NAME;
	}

	public String getGM_TEL() {
		return GM_TEL;
	}

	public void setGM_TEL(String gM_TEL) {
		GM_TEL = gM_TEL;
	}

	public String getGM_EMAIL() {
		return GM_EMAIL;
	}

	public void setGM_EMAIL(String gM_EMAIL) {
		GM_EMAIL = gM_EMAIL;
	}

	public String getAM_NAME() {
		return AM_NAME;
	}

	public void setAM_NAME(String aM_NAME) {
		AM_NAME = aM_NAME;
	}

	public String getAM_TEL() {
		return AM_TEL;
	}

	public void setAM_TEL(String aM_TEL) {
		AM_TEL = aM_TEL;
	}

	public String getAM_EMAIL() {
		return AM_EMAIL;
	}

	public void setAM_EMAIL(String aM_EMAIL) {
		AM_EMAIL = aM_EMAIL;
	}

}