package com.yc.model;

import com.yc.entity.user.Department;
import com.yc.entity.user.MemberLevel;
import com.yc.entity.user.Sex;

public class MemberRecord {
	private Integer recordID;
	private String loginName;
	private String userName;
	private Sex sex;
	private MemberLevel level;
	private String phone;
	private String cateDate;
	private String endDate;
	private Department cateDepartment;
	private Department renewDepartment;
	private Boolean isSettle;

	public String getCateDate() {
		return cateDate;
	}

	public void setCateDate(String cateDate) {
		this.cateDate = cateDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getRecordID() {
		return recordID;
	}

	public void setRecordID(Integer recordID) {
		this.recordID = recordID;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public MemberLevel getLevel() {
		return level;
	}

	public void setLevel(MemberLevel level) {
		this.level = level;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Department getCateDepartment() {
		return cateDepartment;
	}

	public void setCateDepartment(Department cateDepartment) {
		this.cateDepartment = cateDepartment;
	}

	public Department getRenewDepartment() {
		return renewDepartment;
	}

	public void setRenewDepartment(Department renewDepartment) {
		this.renewDepartment = renewDepartment;
	}

	public Boolean getIsSettle() {
		return isSettle;
	}

	public void setIsSettle(Boolean isSettle) {
		this.isSettle = isSettle;
	}

}
