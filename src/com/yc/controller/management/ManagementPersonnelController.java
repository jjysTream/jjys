package com.yc.controller.management;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
public class ManagementPersonnelController {

	@Autowired
	IDepartmentService departmentService;

	@Autowired
	IPersonnelService personnelService;

	// 部门管理
	@RequestMapping(value = "department", method = RequestMethod.GET)
	public ModelAndView department(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Department> list = departmentService.getDepartmentByParent();
		ModelMap mode = new ModelMap();
		mode.put("treeList1", list);
		return new ModelAndView("management/department", mode);
	}

	@RequestMapping(value = "getDepartment", method = RequestMethod.GET)
	public ModelAndView getDepartment(Integer departmentId, String page, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Department department = departmentService.findById(departmentId);
		List<Department> list = departmentService.getDepartmentByParent();
		ModelMap mode = new ModelMap();
		mode.put("department", department);
		mode.put("treeList1", list);
		return new ModelAndView("management/department", mode);
	}

	@RequestMapping(value = "addOrUpdateDep", method = RequestMethod.POST)
	public String addOrUpdateDep(Integer departmentID, String departmentname, String describes, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Department department = departmentService.findById(departmentID);
		if(department.getLevel()<4){
			Department depart = addDepartment(department, departmentname, describes);
		}
			return "redirect:/management/getDepartment?departmentId=" + departmentID + "&page=department";
		
	}

	@RequestMapping(value = "updateDepartmen", method = RequestMethod.POST)
	public String updateDepartmen(Integer departmentId, String departmentname, String describes, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Department department = departmentService.findById(departmentId);
		Department depart = updateDepartment(department, departmentname, describes);
		return "redirect:/management/getDepartment?departmentId=" + depart.getDepartmentID() + "&page=department";
	}

	@RequestMapping(value = "deleteDepartmen", method = RequestMethod.POST)
	public String deleteDepartmen(Integer departmentId, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Department department = departmentService.findById(departmentId);
		getNode(department);
		return "redirect:/management/department";
	}

	private void getNode(Department department) {
		Set<Department> list = department.getChildren();
		if (list != null && list.size() > 0) {
			Iterator<Department> iter = list.iterator();
			while (iter.hasNext()) {
				Department dep = iter.next();
				if (dep.getChildren() != null && dep.getChildren().size() > 0) {
					getNode(dep);
				}
				List<Personnel> personnels = dep.getPersonnels();
				if (personnels != null && personnels.size() > 0) {
					for (int k = 0; k < personnels.size(); k++) {
						Personnel personnel = personnels.get(k);
						personnel.setDepartment(null);
						personnelService.update(personnel);
					}
				}
				departmentService.delete(dep.getDepartmentID());
			}
		}
				List<Personnel> personnels = department.getPersonnels();
				if (personnels != null && personnels.size() > 0) {
					for (int g = 0; g < personnels.size(); g++) {
						Personnel personnel = personnels.get(g);
						personnel.setDepartment(null);
						personnelService.update(personnel);
					}
				}
				departmentService.delete(department.getDepartmentID());
	}

	private Department updateDepartment(Department department, String departmentname, String describes) {
		department.setDepartmentName(departmentname);
		department.setDescribes(describes);
		department = departmentService.update(department);
		return department;
	}

	private Department addDepartment(Department parentDepartment, String departmentname, String describes) {
		Department department = new Department();
		department.setParentLevel(parentDepartment);
		department.setDepartmentName(departmentname);
		department.setDescribes(describes);
		department.setLevel(parentDepartment.getLevel()+1);
		department = departmentService.save(department);
		if (parentDepartment != null) {
			parentDepartment.getChildren().add(department);
			departmentService.update(parentDepartment);
		}
		return department;
	}

	@RequestMapping(value = "searchPersonnel", method = RequestMethod.GET)
	public ModelAndView searchPersonnel(String userName, String departmentName, String positions, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		if (departmentName.equals("")) {
			map.put("departmentName", null);
		} else {
			map.put("departmentName", Integer.parseInt(departmentName));
		}
		if (positions.equals("")) {
			map.put("positions", null);
		} else {
			map.put("positions", Integer.parseInt(positions));
		}
		if (userName.equals("")) {
			map.put("userName", null);
		} else {
			map.put("userName", userName);
		}
		List<Personnel> personnels = personnelService.getAllByParametersForManage(map);
		List<Department> list = departmentService.getDepartmentByParent();
		List<Department> departments = departmentService.getAll();
		ModelMap mode = new ModelMap();
		mode.put("treeList1", list);
		mode.put("departments", departments);
		mode.put("personnels", personnels);
		return new ModelAndView("management/posDivide", mode);
	}

	@RequestMapping(value = "forbiddenPersonnel", method = RequestMethod.GET)
	public String forbiddenPersonnel(Integer id, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Personnel personnel = personnelService.findById(id);
		personnel.setForbidden(!personnel.getForbidden());
		personnelService.update(personnel);
		return "redirect:/management/personnel";
	}
}
