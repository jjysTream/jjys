package com.yc.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yc.dao.orm.commons.GenericDao;
import com.yc.entity.user.Personnel;
import com.yc.service.IPersonnelService;

@Component
public class PersonnelService extends GenericService<Personnel> implements IPersonnelService {
	@Autowired
	GenericDao<Personnel> personnelDao;

	@Override
	GenericDao<Personnel> getDao() {
		return personnelDao;
	}

	@Override
	public Personnel getPersonnle(String name) {
		return personnelDao.getFirstRecord("loginName", name);
	}

	@Override
	public List<Personnel> getUsersByParameters(String loginName, String userName) {
		StringBuffer hql = new StringBuffer(" from Personnel u where (? is null or u.loginName like ?) and (? is null or u.userName like ?)");
		Object[] paramete =  new Object[4];
		paramete[0] = loginName ;
		paramete[1] = "%"+loginName +"%";
		paramete[2] = userName ;
		paramete[3] = "%"+userName +"%";
		return personnelDao.find(hql.toString(), paramete, -1,-1);
	}

	@Override
	public List<Personnel> getAllByParametersForManage(Map<String, Object> map) {
		StringBuffer hql = new StringBuffer(" from Personnel u where (? is null or u.departAndPositions.department.departmentID = ?) and (? is null or u.userName like ?) and (? is null or u.departAndPositions.positions.positionID = ?)");
		Object[] paramete =  new Object[6];
		paramete[0] = map.get("departmentID") ;
		paramete[1] = map.get("departmentID");
		paramete[2] = map.get("userName") ;
		paramete[3] = "%"+map.get("userName") +"%";
		paramete[4] = map.get("positionid") ;
		paramete[5] = map.get("positionid");

		List<Personnel> list = personnelDao.find(hql.toString(), paramete, -1,-1);
		
		if ( map.get("forbidden") != null)
		{
			List<Personnel> list2 = new LinkedList<Personnel>();
			for ( int i = 0; i < list.size(); i++ )
			{
				if ( map.get("forbidden").equals("no")) 
				{
					if ( list.get(i).getForbidden() == null )
					{									
						list2.add(list.get(i));
					}
				}
				
				else
				{
					if ( list.get(i).getForbidden() != null )
					{
						list2.add(list.get(i));
					}
				}
			}
			return list2;
		}
		return list;
	}

	@Override
	public List<Personnel> getAllByDepAndPos(Integer depAndPosID) {
		return personnelDao.getBy("departAndPositions.id", depAndPosID);
	}

}
