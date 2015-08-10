package com.yc.service;

import java.util.List;

import com.yc.entity.user.MembersFamily;

public interface IMembersFamilyService extends IGenericService<MembersFamily>{

	/**
	 * 通过登录名获取其所有家庭成员
	 * @param loginName 登录名-身份证
	 * @return
	 */
	public List<MembersFamily> getFamiliesByLoginName(String loginName);
}
