package com.yc.controller.management;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yc.entity.user.Department;
import com.yc.entity.user.Personnel;
import com.yc.service.IDepartmentService;
import com.yc.service.IPersonnelService;

@Controller
@RequestMapping("/management")
public class ManagementQuanXianController {

	@Autowired
	IDepartmentService departmentService;

	@Autowired
	IPersonnelService personnelService;
	
	@RequestMapping(value = "searchPersonnelResult", method = RequestMethod.POST)
	public ModelAndView searchPersonnelResult(String userName,Integer position_id,Integer department_id,
				String forbidden, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (department_id == 0 ) {
			map.put("departmentID", null);
		} else {
			map.put("departmentID", department_id);
		}
		
		if ( forbidden.equals("info") ) {
			map.put("forbidden", null);
		} else {
			map.put("forbidden", forbidden);
		}
		Personnel personnel = (Personnel)request.getSession().getAttribute("loginPersonnle");
		Department department  = personnel.getDepartment();
		Set<Department> departmentList = null;
		if(department != null){
			departmentList = department.getChildren();
			if(departmentList == null){
				departmentList = new HashSet<Department>();
			}
			departmentList.add(department);
		}
		ModelMap mode = new ModelMap();
		mode.put("departmentlist", departmentList);
		if(map.get("departmentID") == null && map.get("forbidden") == null){
			List<Personnel> personnelList = new ArrayList<Personnel>();
			for (Department departments : departmentList) {
					personnelList.addAll(departments.getPersonnels());
			}
			mode.put("personnellist", personnelList);
		}else{
			List<Personnel> list = personnelService.getAllByParametersForManage(map);	
			mode.put("personnellist", list);
		}
		return new ModelAndView("personnel/personnel",mode);
	}
}
