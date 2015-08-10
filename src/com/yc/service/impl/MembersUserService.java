package com.yc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yc.dao.orm.commons.GenericDao;
import com.yc.entity.user.MembersUser;
import com.yc.service.IMembersUserService;

@Component
public class MembersUserService extends GenericService<MembersUser> implements IMembersUserService{

	@Autowired
	GenericDao<MembersUser> userDao;
	
	@Override
	GenericDao<MembersUser> getDao() {
		return userDao;
	}

	@Override
	public MembersUser getUser(String mobile) {
		return userDao.getFirstRecord("loginName", mobile);
	}

	@Override
	public MembersUser getUserByEmail(String email) {
		return userDao.getFirstRecord("email", email);
	}
}
