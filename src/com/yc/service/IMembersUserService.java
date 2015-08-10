package com.yc.service;

import com.yc.entity.user.MembersUser;

public interface IMembersUserService extends IGenericService<MembersUser> {

	public MembersUser getUser(String mobile);
	
	public MembersUser getUserByEmail(String email);
}
