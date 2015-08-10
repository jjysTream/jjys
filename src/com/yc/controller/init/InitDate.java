package com.yc.controller.init;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Repository;

import com.yc.entity.user.Department;
import com.yc.entity.user.Personnel;
import com.yc.service.IDepartmentService;
import com.yc.service.IPersonnelService;

@SuppressWarnings("rawtypes")
@Repository
public class InitDate implements ApplicationListener{
	
	@Autowired
	IPersonnelService personnelService;
	
	@Autowired
	IDepartmentService departmentService;
	
	@Override
	public void onApplicationEvent(ApplicationEvent arg0) {
		initArticleCategory();
	}

	private void initArticleCategory() {
		Department department = departmentService.findById(1);
		if(department == null){
			department = new Department();
			department.setDepartmentName("俊健天源总公司");
			department.setDescribes("俊健天源是一家专注养生、保健的公司");
			department.setParentLevel(null);
			department = departmentService.save(department);
		}
		Personnel  nat = personnelService.findById(1);
		if (nat==null) {
			nat = new Personnel();
			nat.setUserName("管理员");
			nat.setLoginName("admin");
			nat.setPassword("000000");
			nat.setForbidden(false);
			nat.setDepartment(department); 
			nat = personnelService.save(nat);
			List<Personnel> list = new ArrayList<Personnel>();
			list.add(nat);
			department.setPersonnels(list);
			departmentService.update(department);
		}
	}

}
