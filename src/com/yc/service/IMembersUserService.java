package com.yc.service;

import java.util.List;

import com.yc.entity.user.MembersUser;

public interface IMembersUserService extends IGenericService<MembersUser> {

	/**
	 * 登录名查找用户
	 * @param loginName
	 * @return
	 */
	public MembersUser getUser(String loginName);

	/**
	 * email查找用户
	 * @param email
	 * @return
	 */
	public MembersUser getUserByEmail(String email);

	/**
	 * 创建用户机构查询
	 * @param personnelID
	 * @return
	 */
	public List<MembersUser> getAllByDepartments(Integer departmentID);
}
