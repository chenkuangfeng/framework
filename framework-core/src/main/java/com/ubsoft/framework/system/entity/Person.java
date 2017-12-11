package com.ubsoft.framework.system.entity;

import java.io.Serializable;
import java.util.Date;

import com.ubsoft.framework.core.dal.annotation.Column;
import com.ubsoft.framework.core.dal.entity.BaseEntity;


public abstract class Person extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column(name = "SEX", length = 4)
	private String sex;// 性别

	@Column(name = "TEL", length = 25)
	private String tel;// 电话
	@Column(name = "PHONE", length = 25)
	private String phone;// 手机
	@Column(name = "FAX", length = 25)
	private String fax;// 传真
	@Column(name = "IDTYPE", length = 10)
	private String idType;// 证件类型
	@Column(name = "IDNUM", length = 32)
	private String idNum;// 证件编号

	@Column(name = "COUNTRY", length = 30)
	private String country;// 国家
	@Column(name = "PROVINCE", length = 30)
	private String province;// 省份
	@Column(name = "CITY", length = 30)
	private String city;// 城市

	@Column(name = "DISTRICT", length = 30)
	private String district;// 区县
	@Column(name = "ADDRESS", length = 100)
	private String address;// 地址
	@Column(name = "EMAIL", length = 80)
	private String email;// 邮件

	@Column(name = "POSITION", length = 30)
	private String position;// 岗位

	@Column(name = "BIRTHDAY")
	private Date birthday;

	
	@Column(name = "PHOTO", dataType = "BLOB", nullable = true)
	private byte[] photo;

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}