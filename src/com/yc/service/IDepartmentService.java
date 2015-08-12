package com.yc.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.yc.entity.user.Department;
import com.yc.model.MemberRecord;

public interface IDepartmentService extends IGenericService<Department> {

	/***
	 * 查询所有父节点为Null的部门
	 * @return
	 */
	List<Department> getDepartmentByParent();

	/***
	 * 通过部门ID删除本部门
	 * @param departmentID 部门ID
	 * @return
	 */
	boolean  deleteForTree(Integer departmentID);

	/**
	 * 通过上级部门ID 查询部门
	 * @param departmentID 部门ID
	 * @return
	 */
	List<Department> getDepartmentByParentID(Integer departmentID);

	/**
	 * 通过map查找链表
	 * @param map level 等级 ，paymentDateLeft 起始日期，paymentDateRight 结束
	 * @param departmentID 部门ID
	 * @return
	 */
	List<MemberRecord> getAllByParam(Map<String, Object> map, Integer departmentID) throws ParseException;

}
