package com.yc.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yc.dao.orm.commons.GenericDao;
import com.yc.entity.RechargeRecord;
import com.yc.entity.user.MemberLevel;
import com.yc.entity.user.Sex;
import com.yc.model.MemberRecord;
import com.yc.service.IRechargeRecordService;

@Component
public class RechargeRecordService extends GenericService<RechargeRecord> implements IRechargeRecordService{

	@Autowired
	GenericDao<RechargeRecord> collectionDao;
	
	@Override
	GenericDao<RechargeRecord> getDao() {
		return collectionDao;
	}

	@Override
	public List<RechargeRecord> getRechargeRecordByUser(Integer membersUserID) {
		StringBuffer hql = new StringBuffer(" from RechargeRecord where membersUser.membersUserID = "+membersUserID+" order by recordID desc" );
		return collectionDao.find(hql.toString(), null, null);
	}
	
}
