package com.yc.service;

import java.util.List;

import com.yc.entity.user.Department;

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

}
