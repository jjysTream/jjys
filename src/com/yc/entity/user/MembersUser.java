package com.yc.entity.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.yc.entity.Collection;

@Entity
@DiscriminatorValue("membersUser")
// 会员用户
public class MembersUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer membersUserID;

	@Column(length = 32, unique = true, updatable = false)
	private String loginName;

	@Column(length = 32)
	private String password;

	@Column(length = 32)
	private String userName;

	@Column
	private String email;

	@Column
	private String validateCode;// 邮箱激活码

	@Column
	private Boolean status;

	@Column
	private String emailBindTime;// 邮箱绑定时间

	@Column
	@Enumerated(EnumType.STRING)
	private Sex sex;

	@Column
	private String birthday;

	@Column
	private String photoPath;

	@Column
	private String city;
	
	@Column
	private String phone;

	@Column
	@Enumerated(EnumType.STRING)
	private ConstitutionType bodyConstitution;// 体质

	@OneToMany(mappedBy = "membersUser")
	private List<MembersFamily> families;
	
	@OneToMany(mappedBy = "user")
	private List<Collection> collections ;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<Collection> getCollections() {
		return collections;
	}

	public void setCollections(List<Collection> collections) {
		this.collections = collections;
	}

	public Integer getMembersUserID() {
		return membersUserID;
	}

	public void setMembersUserID(Integer membersUserID) {
		this.membersUserID = membersUserID;
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

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getEmailBindTime() {
		return emailBindTime;
	}

	public void setEmailBindTime(String emailBindTime) {
		this.emailBindTime = emailBindTime;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public ConstitutionType getBodyConstitution() {
		return bodyConstitution;
	}

	public void setBodyConstitution(ConstitutionType bodyConstitution) {
		this.bodyConstitution = bodyConstitution;
	}

	public List<MembersFamily> getFamilies() {
		return families;
	}

	public void setFamilies(List<MembersFamily> families) {
		this.families = families;
	}
}
