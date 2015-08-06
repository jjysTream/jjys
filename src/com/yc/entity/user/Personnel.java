package com.yc.entity.user;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("personnel")//员工
public class Personnel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer personnelID;

	@Column(length = 32, unique = true, updatable = false)
	private String loginName;

	@Column(length = 32)
	private String password;

	@Column(length = 32)
	private String userName;

	@Column
	private String email;
	
	@Column
	@Enumerated(EnumType.STRING)
	private Sex sex;

	@Column
	private String phone;
	
	@ManyToOne
	@JoinColumn(name = "department_id")
	private Department department;//部门
	
	@Column
	private Boolean forbidden = true;//是否禁用
	
	public void setForbidden(Boolean forbidden) {
		this.forbidden = forbidden;
	}

	public Boolean getForbidden() {
		return forbidden;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Integer getPersonnelID() {
		return personnelID;
	}

	public void setPersonnelID(Integer personnelID) {
		this.personnelID = personnelID;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
