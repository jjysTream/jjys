package com.yc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yc.dao.orm.commons.GenericDao;
import com.yc.entity.user.MembersFamily;
import com.yc.service.IMembersFamilyService;
@Component
public class MembersFamilyService extends GenericService<MembersFamily> implements IMembersFamilyService{

	@Autowired
	GenericDao<MembersFamily> membersFamilyDao;
	
	@Override
	GenericDao<MembersFamily> getDao() {
		return membersFamilyDao;
	}

	@Override
	public List<MembersFamily> getFamiliesByLoginName(String loginName) {
		return membersFamilyDao.getBy("membersUser.loginName", loginName);
	}
}
