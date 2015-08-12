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
import com.yc.entity.user.Department;
import com.yc.entity.user.MemberLevel;
import com.yc.entity.user.Sex;
import com.yc.model.MemberRecord;
import com.yc.service.IDepartmentService;

@Component
public class DepartmentService extends GenericService<Department> implements IDepartmentService {

	@Autowired
	GenericDao<Department> departmentDao;
	
	@Override
	GenericDao<Department> getDao() {
		return departmentDao;
	}

	@Override
	public List<Department> getDepartmentByParent() {
		StringBuffer hql = new StringBuffer(" from Department depart where depart.parentLevel.departmentID is null");
		return departmentDao.find(hql.toString(), null, null);
	}

	@Override
	public boolean deleteForTree(Integer departmentID) {
		StringBuffer hql = new StringBuffer("DELETE FROM Department WHERE departmentID = "+departmentID);
		departmentDao.getEntityManager().getTransaction().begin();
		boolean isok = departmentDao.getEntityManager().createNativeQuery(hql.toString(), Department.class).executeUpdate()>0;
		departmentDao.getEntityManager().getTransaction().commit();
		departmentDao.getEntityManager().clear();
		return isok;
	}
	
	@Override
	public List<Department> getDepartmentByParentID(Integer departmentID) {
		StringBuffer hql = new StringBuffer(" from Department depart where depart.parentLevel.departmentID ="+departmentID);
		return departmentDao.find(hql.toString(), null, null);
	}
	
	@Override
	public List<MemberRecord> getAllByParam(Map<String, Object> map,Integer deparmentID) throws ParseException {
		StringBuffer hql = new StringBuffer("SELECT recordID,loginName,username,sex,mu.level,phone,creatDepartment_id,"
				+ " membersuserid,isSettle,createDate,endDate FROM rechargerecord rg LEFT JOIN membersuser mu ON mu.membersUserID = rg.membersUser_id "
				+ " WHERE rg.creatDepartment_id = "+deparmentID );
		if(map.get("level") != null){
			hql.append(" and mu.level = '"+map.get("level")+"'");
		}
		if(map.get("paymentDateLeft") != null && !map.get("paymentDateLeft").equals("") && map.get("paymentDateRight") != null && !map.get("paymentDateRight").equals("")){
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			cal.setTime(sdf.parse(map.get("paymentDateRight").toString()));
			Date d1 = cal.getTime();
			Date dd = sdf.parse(map.get("paymentDateLeft").toString());
			cal.setTime(new Date(dd.getTime()));
			Date d2 = cal.getTime();
			long daterange = d1.getTime() - d2.getTime();
			if (daterange > 0) {
				long time = 1000 * 3600 * 24;
				System.out.println("daterange / time======"+daterange / time);
				List<String> dates = CalendarDays(d2,Integer.parseInt(String.valueOf(daterange / time)));
				StringBuilder takeDates = new StringBuilder();
				for (String date : dates) {
					if (takeDates.length() > 0) {
						takeDates.append(",");
					}
					takeDates.append("'");
					takeDates.append(date);
					takeDates.append("'");
				}
				hql.append(" and rg.createDate IN (" + takeDates.toString() + ") and rg.endDate in (" + takeDates.toString() + ")");
			}
		}
		Query query = departmentDao.getEntityManager().createNativeQuery(hql.toString());
		@SuppressWarnings("rawtypes")
		List objecArraytList = query.getResultList();
		List<MemberRecord> pr = new ArrayList<MemberRecord>();
		MemberRecord mode = null;
		if (objecArraytList != null && objecArraytList.size() > 0) {
			for (int i = 0; i < objecArraytList.size(); i++) {
				mode = new MemberRecord();
				Object[] obj = (Object[]) objecArraytList.get(i);
				if (obj[i] != null) {
					mode.setRecordID(Integer.parseInt(obj[0].toString()));
					mode.setLoginName(obj[1].toString());
					mode.setUserName(obj[2].toString());
					mode.setSex(Sex.valueOf(obj[3].toString()));
					mode.setLevel(MemberLevel.valueOf(obj[4].toString()));
					mode.setPhone(obj[5].toString());
					if(obj[6] != null){
						Department catedepart = departmentDao.findById(Integer.parseInt(obj[6].toString()));
						mode.setCateDepartment(catedepart);
					}
					if(obj[7] != null){
						Department catedepart1 = departmentDao.findById(Integer.parseInt(obj[7].toString()));
						mode.setRenewDepartment(catedepart1);
					}
					mode.setIsSettle(Boolean.valueOf(obj[8].toString()));
					mode.setCateDate(obj[9].toString());
					mode.setEndDate(obj[10].toString());
					pr.add(mode);
				}
			}
		}
		System.out.println("pr============="+pr.size());
		return pr;
	}
	private List<String> CalendarDays(Date d1,int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<String> weekDays = new ArrayList<String>();
		for (int i = 0; i < day; i++) {
			weekDays.add(format.format(cal.getTime()));
			cal.add(Calendar.DATE, 1);
		}
		return weekDays;
	}
}
