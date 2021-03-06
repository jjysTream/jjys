package com.yc.controller.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yc.entity.user.Department;
import com.yc.entity.user.Personnel;
import com.yc.entity.user.Sex;
import com.yc.service.IDepartmentService;
import com.yc.service.IPersonnelService;

@Controller
@RequestMapping("/personnel")
public class PersonnelController {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(PersonnelController.class);

    @Autowired
    IPersonnelService personnelService;
   
    @Autowired
    IDepartmentService departmentService;
    
    List<Department> departments = null;
    
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("loginName");
        String pwd = request.getParameter("password");
        HttpSession session = request.getSession();
        session.removeAttribute("message");
        Personnel personnel = personnelService.getPersonnle(name);
        if (personnel == null) {
            request.getSession().setAttribute("message", "没有该用户！");
            return "redirect:/login";
        } else {
        	if (personnel.getForbidden() == false) {
        		if(personnel.getPassword().equals(pwd.trim())){    		 
        		 	session.setAttribute("loginPersonnle", personnel);
        		 	return "redirect:/management/index";
        		}   else {
                    request.getSession().setAttribute("message", "用户名或密码错误，请重新输入您的登陆信息！");
                    return "redirect:/login";
                }    		 
            }else {
       		 	request.getSession().setAttribute("message", "您已经被禁止了！");
                return "redirect:/login";
        	}
        }
    }

	@RequestMapping(value = "addPersonnel", method = RequestMethod.GET)
	public ModelAndView addPersonnel(Integer id,String mathed,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ModelMap mode = new ModelMap();
		Personnel personnels = (Personnel)request.getSession().getAttribute("loginPersonnle");
		Department department = personnels.getDepartment();
		Set<Department> departmentList = null;
		if(department != null){
			departmentList = department.getChildren();
			if(departmentList == null){
				departmentList = new HashSet<Department>();
			}
			departmentList.add(department);
		}
 		if (mathed.equals("add")) {		
			if (id != null) {
				mode.put("id", id);
				mode.put("mathed", "add");
				mode.put("page", "personnel");	
				mode.put("departmentlist", departmentList);
				return new ModelAndView("personnel/addPersonnel",mode);
			}else{
				mode.put("mathed", "add");
				mode.put("page", "personnel");	
				mode.put("departmentlist", departmentList);
				return new ModelAndView("personnel/addPersonnel",mode);
			}
		}else{
			Personnel personnel = personnelService.findById(id);
			mode.put("personnel", personnel);
			mode.put("mathed", "update");
			mode.put("page", "personnel");
			mode.put("departmentlist", departmentList);
			return  new ModelAndView("personnel/addPersonnel",mode);
		}
	}

	@RequestMapping(value = "addPersonnelList", method = RequestMethod.POST)
	public String addPersonnelList(Integer id,String loginName, String password, String sexs, String userName,Integer position_id, 
			Integer department_id,String phone, String email, String mathed,String page,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (mathed.equals("add")) {
			if (page.equals("personnel")) {
				Department department = departmentService.findById(department_id);
				if(department != null){
					Personnel personnel = new Personnel();
					personnel.setLoginName(loginName);
					personnel.setPassword(password);
					personnel.setUserName(userName);
					personnel.setSex(Sex.valueOf(sexs));
					personnel.setPhone(phone);
					personnel.setEmail(email);
					personnel.setIsView(false);
					personnel.setForbidden(false);
					personnel.setDepartment(department);
					personnel =personnelService.save(personnel);
					List<Personnel> list = department.getPersonnels();
					if(list == null){
						list = new ArrayList<Personnel>();
					}
					System.out.println("list========="+list.size());
					list.add(personnel);
					System.out.println("list========="+list.size());
					department.setPersonnels(list);
					departmentService.update(department);
				}
			}
		}else{
			Department department = departmentService.findById(department_id);
			Personnel personnel = personnelService.findById(id);
			personnel.setPhone(phone);
			personnel.setEmail(email);
			personnel.setDepartment(department);
			personnel = personnelService.update(personnel);	
			List<Personnel> list = department.getPersonnels();
			if(list == null){
				list = new ArrayList<Personnel>();
			}
			list.add(personnel);
			department.setPersonnels(list);
			departmentService.update(department);
		}
		return "redirect:/personnel/personnel";
	}

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().removeAttribute("loginPersonnle");
        return "redirect:/login";
    }
  
    @RequestMapping(value = "toAddPersonnle", method = RequestMethod.GET)
    public ModelAndView toAddPersonnle(Integer id, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.getSession().getAttribute("loginPersonnle");
    	ModelMap mode = new ModelMap();
    	mode.put("personel", request.getSession().getAttribute("loginPersonnle"));
    	return new ModelAndView("personnel/addPersonnle",mode);
    }
    @RequestMapping(value = "personnel", method = RequestMethod.GET)
	public ModelAndView getAllPersonnel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ModelMap mode = new ModelMap();
		departments = new ArrayList<Department>();
		departments.clear();
		List<Personnel> personnelList = new ArrayList<Personnel>();
		Personnel personnel = (Personnel)request.getSession().getAttribute("loginPersonnle");
		Department department = personnel.getDepartment();
		Set<Department> departmentList = null;
		if(department != null){
			getDepartmentList(department);
			departmentList = department.getChildren();
			if(departmentList == null){
				departmentList = new HashSet<Department>();
			}
			departmentList.add(department);
		}
		if(departments != null && departments.size()>0){
			for (Department depar : departments) {
				List<Personnel> pessonnels = personnelService.getAllByDepartmentID(depar.getDepartmentID());
				personnelList.addAll(pessonnels);
			}
		}
		mode.put("departmentlist", departmentList);
		mode.put("personnellist", personnelList);
		return new ModelAndView("personnel/personnel", mode);
	}

	private void getDepartmentList(Department department) {
		if (!departments.contains(department)) {
			departments.add(department);
		}
		List<Department> departmentList = departmentService.getDepartmentByParentID(department.getDepartmentID());
		if (departmentList != null && departmentList.size()>0) {
			for (Department dep : departmentList) {
				System.out.println("jiangbo dep =========="+dep.getDepartmentName());
				if(dep != null ){
					System.out.println("jiangbo dep 1=========="+dep.getDepartmentName());
					getDepartmentList(dep);
				}
				if(!departments.contains(dep)){
					departments.add(dep);
				}
			}
		}
	}

	@RequestMapping(value = "updateUser", method = RequestMethod.GET)
	public ModelAndView updateUser(Integer id, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Personnel personnel = personnelService.findById(id);
		ModelMap mode = new ModelMap();
		mode.put("personnel", personnel);
		return new ModelAndView("personnel/updateUser", mode);
	}

	@RequestMapping(value = "updateUser", method = RequestMethod.POST)
	public String updateUsers(Personnel personnel,Integer department_id, String sexs, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Personnel user = personnelService.getPersonnle(personnel.getLoginName());
		if (user != null) {
			Department department = departmentService.findById(department_id);
			Sex sex = Sex.valueOf(sexs);
			personnel.setForbidden(user.getForbidden());
			personnel.setDepartment(department);
			personnel.setSex(sex);
			BeanUtils.copyProperties(personnel, user);
			user.setDepartment(department);
			user.setSex(sex);
			personnelService.update(user);
		}
		return "redirect:/personnel/personnel";
	}

	@RequestMapping(value = "deleteUser", method = RequestMethod.GET)
	public String deleteUser(Integer id, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		personnelService.delete(id);
		return "redirect:/personnel/personnel";
	}
	
	@RequestMapping(value = "forbiddenPersonnel", method = RequestMethod.GET)
	public String forbiddenPersonnel(Integer id,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Personnel personnel = personnelService.findById(id);
		if( personnel.getForbidden() != null )
		{
			personnel.setForbidden(!personnel.getForbidden());;
			personnelService.update(personnel);
		}
		return "redirect:/personnel/personnel";
	}
	
}
