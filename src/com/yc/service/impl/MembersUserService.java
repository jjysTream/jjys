package com.yc.service.impl;

import java.util.List;

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
	public MembersUser getUser(String loginName) {
		return userDao.getFirstRecord("loginName", loginName);
	}

	@Override
	public MembersUser getUserByEmail(String email) {
		return userDao.getFirstRecord("email", email);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MembersUser> getAllByDepartments(Integer departmentID) {
		StringBuffer hql = new StringBuffer("SELECT DISTINCT mu.* FROM membersuser mu LEFT JOIN rechargerecord rg ON rg.membersUser_id = mu.membersUserID WHERE rg.creatDepartment_id = "+departmentID);
		return userDao.getEntityManager().createNativeQuery(hql.toString(), MembersUser.class).getResultList();
	}
}
