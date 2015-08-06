package com.yc.service;

import java.util.List;
import java.util.Map;

import com.yc.entity.user.Personnel;

public interface IPersonnelService  extends IGenericService<Personnel>{

	/***
	 * 通过登录名查找员工
	 * @param name
	 * @return
	 */
	Personnel getPersonnle(String name);

	/***
	 * 通过登录名、昵称查找员工
	 * @param loginName 登录名 like
	 * @param userName 昵称 like
	 * @return
	 */
	List<Personnel> getUsersByParameters(String loginName, String userName);

	/***
	 * 通过部门ID、昵称、职位ID查询员工
	 * @param map departmentID 部门ID、userName 昵称 like、positionid 职位ID
	 * @return
	 */
	List<Personnel> getAllByParametersForManage(Map<String, Object> map);

	/***
	 * 通过部门职位第三方表ID查询员工
	 * @param depAndPosID 第三方表ID
	 * @return
	 */
	List<Personnel> getAllByDepAndPos(Integer depAndPosID);

}
