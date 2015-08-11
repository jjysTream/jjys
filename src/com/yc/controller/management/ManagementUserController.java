package com.yc.controller.management;

import java.io.IOException;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yc.entity.RechargeRecord;
import com.yc.entity.user.Department;
import com.yc.entity.user.MembersUser;
import com.yc.entity.user.Personnel;
import com.yc.service.IMembersUserService;
import com.yc.service.IPersonnelService;
import com.yc.service.IRechargeRecordService;

@Controller
@RequestMapping("/management")
public class ManagementUserController {
	
	@Autowired
	IMembersUserService userService;
	
	@Autowired
	IRechargeRecordService rechargeRecordService;
	
	@Autowired
	IPersonnelService personnelService;
	
	List<Department> departments = null;
	
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return new ModelAndView("management/index");
	}
	
	@RequestMapping(value = "userList", method = RequestMethod.GET)
	public ModelAndView userList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Personnel personnel = (Personnel)request.getSession().getAttribute("loginPersonnle");
		ModelMap mode = new ModelMap();
		if(personnel != null){
			Department department = personnel.getDepartment();
			departments = new ArrayList<Department>();
			departments.clear();
			getDepartmentList(department);
			List<MembersUser> personnelList = new ArrayList<MembersUser>();
			if (departments != null && departments.size()>0) {
				for (Department depar : departments) {
					if (depar != null && depar.getLevel()==4) {
						List<MembersUser> userList = userService.getAllByDepartments(depar.getDepartmentID());
						if(userList != null){
							personnelList.addAll(userList);
						}
					}
				}
			}
			mode.put("list", personnelList);
		}
		return new ModelAndView("management/userList", mode);
	}

	private void getDepartmentList(Department department) {
		Set<Department> departmentList =  department.getChildren();
		if (departmentList != null && departmentList.size()>0) {
			Iterator<Department> iterator = departmentList.iterator();
			while (iterator.hasNext()) {
				Department dep = iterator.next();
				if(dep != null && dep.getChildren() != null){
					getDepartmentList(dep);
				}
				if(dep.getLevel() == 4 && !departments.contains(dep)){
					departments.add(dep);
				}
			}
		}
		if (department.getLevel() == 4 && !departments.contains(department)) {
			departments.add(department);
		}
	}
	
	@RequestMapping(value = "updateUser", method = RequestMethod.GET)
	public ModelAndView updateUser(Integer id, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MembersUser user = userService.findById(id);
		ModelMap mode = new ModelMap();
		mode.put("user", user);
		return new ModelAndView("management/updateUser", mode);
	}

	@RequestMapping(value = "updateUser", method = RequestMethod.POST)
	public String updateUsers(MembersUser memUser, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MembersUser user = userService.getUser(memUser.getLoginName());
		if (user != null) {
			user.setPassword(KL(MD5(memUser.getPassword())));
			BeanUtils.copyProperties(memUser, user);
			userService.update(user);
		}
		return "redirect:/management/userList";
	}

	@RequestMapping(value = "deleteUser", method = RequestMethod.GET)
	public String deleteUser(Integer id, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		userService.delete(id);
		return "redirect:/management/userList";
	}

	@RequestMapping(value = "regist", method = RequestMethod.GET)
	public ModelAndView register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return new ModelAndView("management/register");
	}

	@RequestMapping(value = "regist", method = RequestMethod.POST)
	public String registing(String page,MembersUser user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		user.setPassword(KL(MD5(user.getPassword())));
		Personnel personnel = (Personnel)request.getSession().getAttribute("loginPersonnle");
		user = userService.save(user);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		RechargeRecord rechargeRecord = new RechargeRecord();
		rechargeRecord.setCateDepartment(personnel.getDepartment());
		rechargeRecord.setRenewDepartment(personnel.getDepartment());
		rechargeRecord.setCreateDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
		cal.add(Calendar.YEAR,1);
		rechargeRecord.setEndDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
		rechargeRecord.setMembersUser(user);
		rechargeRecord = rechargeRecordService.save(rechargeRecord);
		List<RechargeRecord> list = user.getRechargeRecords();
		if(list == null){
			list = new ArrayList<RechargeRecord>();
		}
		list.add(rechargeRecord);
		user.setRechargeRecords(list);
		userService.update(user);
		return "redirect:/management/userList";
	}
	
	@RequestMapping(value = "rechargeForUser", method = RequestMethod.GET)
	public ModelAndView rechargeForUser(Integer id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<RechargeRecord> reccharges = rechargeRecordService.getRechargeRecordByUser(id);
		ModelMap mode = new ModelMap();
		mode.put("list", reccharges);
		return new ModelAndView("management/userRecharge",mode);
	}
	
	@RequestMapping(value = "xufei", method = RequestMethod.GET)
	public ModelAndView xufei(Integer id, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
		MembersUser user = userService.findById(id);
		ModelMap mode = new ModelMap();
		if(user != null){
			List<RechargeRecord> reccharges = rechargeRecordService.getRechargeRecordByUser(user.getMembersUserID());
			Personnel personnel = (Personnel)request.getSession().getAttribute("loginPersonnle");
			if(reccharges != null){
				if(reccharges.get(0) != null){
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					RechargeRecord rechargeRecord = new RechargeRecord();
					rechargeRecord.setCateDepartment(reccharges.get(0).getCateDepartment());
					rechargeRecord.setRenewDepartment(personnel.getDepartment());
					Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(reccharges.get(0).getEndDate());
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(date1);
					if(cal.getTimeInMillis() -cal1.getTimeInMillis()>=0){
						rechargeRecord.setCreateDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
						cal.add(Calendar.YEAR,1);
						rechargeRecord.setEndDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
					}else{
						long time = cal1.getTimeInMillis() -cal.getTimeInMillis();
						rechargeRecord.setCreateDate(new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime()));
						cal1.add(Calendar.YEAR,1);
						time = cal1.getTimeInMillis()+ time ;
						cal1.setTimeInMillis(time);
						rechargeRecord.setEndDate(new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime()));
					}
					rechargeRecord.setMembersUser(user);
					rechargeRecord = rechargeRecordService.save(rechargeRecord);
					List<RechargeRecord> list = user.getRechargeRecords();
					if(list == null){
						list = new ArrayList<RechargeRecord>();
					}
					list.add(rechargeRecord);
					user.setRechargeRecords(list);
					userService.update(user);
					mode.put("massage", "续费成功");
				}
			}else{
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				RechargeRecord rechargeRecord = new RechargeRecord();
				rechargeRecord.setCateDepartment(personnel.getDepartment());
				rechargeRecord.setRenewDepartment(personnel.getDepartment());
				rechargeRecord.setCreateDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
				cal.add(Calendar.YEAR,1);
				rechargeRecord.setEndDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
				rechargeRecord.setMembersUser(user);
				rechargeRecord = rechargeRecordService.save(rechargeRecord);
				List<RechargeRecord> list = user.getRechargeRecords();
				if(list == null){
					list = new ArrayList<RechargeRecord>();
				}
				list.add(rechargeRecord);
				user.setRechargeRecords(list);
				userService.update(user);
			}
		}else{
			mode.put("massage", "没查找到该用户");
		}
		List<RechargeRecord> reccharges = rechargeRecordService.getRechargeRecordByUser(id);
		mode.put("list", reccharges);
		return new ModelAndView("management/userRecharge",mode);
	}
	
	@RequestMapping(value = "deletexufei", method = RequestMethod.GET)
	public String deletexufei(Integer id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		RechargeRecord rechargeRecord = rechargeRecordService.findById(id);
		MembersUser user = rechargeRecord.getMembersUser();
		rechargeRecordService.delete(id);
		return  "redirect:/management/rechargeForUser?id="+user.getMembersUserID();
	}
	
	@RequestMapping(value = "searchUser", method = RequestMethod.POST)
	public ModelAndView searchUser(String seachName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MembersUser membersUser = userService.getUser(seachName);
		ModelMap mode = new ModelMap();
		List<MembersUser> list = new ArrayList<MembersUser>();
		list.add(membersUser);
		mode.put("list", list);
		return new ModelAndView("management/userList", mode);
	}
	
	// MD5加码。32位
	public static String MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];

		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}

	// 可逆的加密算法
	public static String KL(String inStr) {
		// String s = new String(inStr);
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;
	}

	// 加密后解密
	public static String JM(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String k = new String(a);
		return k;
	}
}
