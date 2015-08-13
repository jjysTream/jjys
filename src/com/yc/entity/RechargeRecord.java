package com.yc.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.yc.entity.user.Department;
import com.yc.entity.user.MembersUser;

@Entity
@DiscriminatorValue("rechargeRecord") 
public class RechargeRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer recordID;
	
	@Column
	private String createDate;
	
	@Column
	private String endDate;
	
	@OneToOne
	@JoinColumn(name = "creatDepartment_id")
	private Department cateDepartment;
	
	@OneToOne
	@JoinColumn(name = "renewDepartment_id")
	private Department renewDepartment;
	
	@ManyToOne
	@JoinColumn(name = "membersUser_id")
	private MembersUser membersUser;
	
	@Column
	private Boolean isSettle = false;

	public Boolean getIsSettle() {
		return isSettle;
	}

	public void setIsSettle(Boolean isSettle) {
		this.isSettle = isSettle;
	}

	public Integer getRecordID() {
		return recordID;
	}

	public void setRecordID(Integer recordID) {
		this.recordID = recordID;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

	public MembersUser getMembersUser() {
		return membersUser;
	}

	public void setMembersUser(MembersUser membersUser) {
		this.membersUser = membersUser;
	}
}
