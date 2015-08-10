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
@DiscriminatorValue("membersFamily")
// 会员家人
public class MembersFamily {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer membersFamilyID;

	@Column(length = 32)
	private String membersName;

	@Column
	@Enumerated(EnumType.STRING)
	private ConstitutionType bodyConstitution;

	@ManyToOne
	@JoinColumn(name = "membersUser_id")
	private MembersUser membersUser;

	public Integer getMembersFamilyID() {
		return membersFamilyID;
	}

	public void setMembersFamilyID(Integer membersFamilyID) {
		this.membersFamilyID = membersFamilyID;
	}

	public String getMembersName() {
		return membersName;
	}

	public void setMembersName(String membersName) {
		this.membersName = membersName;
	}

	public ConstitutionType getBodyConstitution() {
		return bodyConstitution;
	}

	public void setBodyConstitution(ConstitutionType bodyConstitution) {
		this.bodyConstitution = bodyConstitution;
	}

	public MembersUser getMembersUser() {
		return membersUser;
	}

	public void setMembersUser(MembersUser membersUser) {
		this.membersUser = membersUser;
	}

}
