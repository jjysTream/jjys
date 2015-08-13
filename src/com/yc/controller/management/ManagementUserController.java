package com.yc.controller.management;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.yc.entity.RechargeRecord;
import com.yc.entity.user.Department;
import com.yc.entity.user.MemberLevel;
import com.yc.entity.user.MembersUser;
import com.yc.entity.user.Personnel;
import com.yc.entity.user.Sex;
import com.yc.model.MemberRecord;
import com.yc.service.IDepartmentService;
import com.yc.service.IMembersUserService;
import com.yc.service.IPersonnelService;
import com.yc.service.IRechargeRecordService;

@Controller
@RequestMapping("/management")
public class ManagementUserController {

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(ManagementUserController.class);

	@Autowired
	IMembersUserService userService;

	@Autowired
	IRechargeRecordService rechargeRecordService;

	@Autowired
	IPersonnelService personnelService;

	@Autowired
	IDepartmentService departmentService;

	@Autowired
	IRechargeRecordService recordService;

	List<Department> departments = null;

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return new ModelAndView("management/index");
	}

	@RequestMapping(value = "userList", method = RequestMethod.GET)
	public ModelAndView userList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Personnel personnel = (Personnel) request.getSession().getAttribute("loginPersonnle");
		ModelMap mode = new ModelMap();
		if (personnel != null) {
			Department department = personnel.getDepartment();
			departments = new ArrayList<Department>();
			departments.clear();
			getDepartmentList(department);
			List<MembersUser> personnelList = new ArrayList<MembersUser>();
			if (departments != null && departments.size() > 0) {
				for (Department depar : departments) {
					if (depar != null && depar.getLevel() == 4) {
						List<MembersUser> userList = userService.getAllByDepartments(depar.getDepartmentID());
						if (userList != null) {
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
		if (department.getLevel() == 4 && !departments.contains(department)) {
			departments.add(department);
		}
		List<Department> departmentList = departmentService.getDepartmentByParentID(department.getDepartmentID());
		if (departmentList != null && departmentList.size() > 0) {
			for (Department dep : departmentList) {
				if (dep != null && dep.getChildren() != null) {
					getDepartmentList(dep);
				}
				if (dep.getLevel() == 4 && !departments.contains(dep)) {
					departments.add(dep);
				}
			}
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
	public String registing(String page, MembersUser user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		user.setPassword(KL(MD5(user.getPassword())));
		Personnel personnel = (Personnel) request.getSession().getAttribute("loginPersonnle");
		user = userService.save(user);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		RechargeRecord rechargeRecord = new RechargeRecord();
		rechargeRecord.setCateDepartment(personnel.getDepartment());
		rechargeRecord.setRenewDepartment(personnel.getDepartment());
		rechargeRecord.setCreateDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
		cal.add(Calendar.YEAR, 1);
		rechargeRecord.setEndDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
		rechargeRecord.setMembersUser(user);
		rechargeRecord = rechargeRecordService.save(rechargeRecord);
		List<RechargeRecord> list = user.getRechargeRecords();
		if (list == null) {
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
		return new ModelAndView("management/userRecharge", mode);
	}

	@RequestMapping(value = "xufei", method = RequestMethod.GET)
	public ModelAndView xufei(Integer id, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
		MembersUser user = userService.findById(id);
		ModelMap mode = new ModelMap();
		if (user != null) {
			List<RechargeRecord> reccharges = rechargeRecordService.getRechargeRecordByUser(user.getMembersUserID());
			Personnel personnel = (Personnel) request.getSession().getAttribute("loginPersonnle");
			if (reccharges != null) {
				if (reccharges.get(0) != null) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					RechargeRecord rechargeRecord = new RechargeRecord();
					rechargeRecord.setCateDepartment(reccharges.get(0).getCateDepartment());
					rechargeRecord.setRenewDepartment(personnel.getDepartment());
					Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(reccharges.get(0).getEndDate());
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(date1);
					if (cal.getTimeInMillis() - cal1.getTimeInMillis() >= 0) {
						rechargeRecord.setCreateDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
						cal.add(Calendar.YEAR, 1);
						rechargeRecord.setEndDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
					} else {
						long time = cal1.getTimeInMillis() - cal.getTimeInMillis();
						rechargeRecord.setCreateDate(new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime()));
						cal1.add(Calendar.YEAR, 1);
						time = cal1.getTimeInMillis() + time;
						cal1.setTimeInMillis(time);
						rechargeRecord.setEndDate(new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime()));
					}
					rechargeRecord.setMembersUser(user);
					rechargeRecord = rechargeRecordService.save(rechargeRecord);
					List<RechargeRecord> list = user.getRechargeRecords();
					if (list == null) {
						list = new ArrayList<RechargeRecord>();
					}
					list.add(rechargeRecord);
					user.setRechargeRecords(list);
					userService.update(user);
					mode.put("massage", "续费成功");
				}
			} else {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				RechargeRecord rechargeRecord = new RechargeRecord();
				rechargeRecord.setCateDepartment(personnel.getDepartment());
				rechargeRecord.setRenewDepartment(personnel.getDepartment());
				rechargeRecord.setCreateDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
				cal.add(Calendar.YEAR, 1);
				rechargeRecord.setEndDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
				rechargeRecord.setMembersUser(user);
				rechargeRecord = rechargeRecordService.save(rechargeRecord);
				List<RechargeRecord> list = user.getRechargeRecords();
				if (list == null) {
					list = new ArrayList<RechargeRecord>();
				}
				list.add(rechargeRecord);
				user.setRechargeRecords(list);
				userService.update(user);
			}
		} else {
			mode.put("massage", "没查找到该用户");
		}
		List<RechargeRecord> reccharges = rechargeRecordService.getRechargeRecordByUser(id);
		mode.put("list", reccharges);
		return new ModelAndView("management/userRecharge", mode);
	}

	@RequestMapping(value = "deletexufei", method = RequestMethod.GET)
	public String deletexufei(Integer id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		RechargeRecord rechargeRecord = rechargeRecordService.findById(id);
		MembersUser user = rechargeRecord.getMembersUser();
		rechargeRecordService.delete(id);
		return "redirect:/management/rechargeForUser?id=" + user.getMembersUserID();
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

	@RequestMapping(value = "importUser", method = RequestMethod.GET)
	public ModelAndView importUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("management/importUser");
	}

	@RequestMapping(value = "userImport", method = RequestMethod.POST)
	public ModelAndView userImport(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
		parseFile(file);
		ModelMap map = new ModelMap();
		map.put("message", "上传成功");
		return new ModelAndView("management/importUser", map);
	}

	private void parseFile(MultipartFile multipartFile) throws Exception {
		String fileName = multipartFile.getOriginalFilename().toLowerCase();
		if (fileName.endsWith("xls")) {
			LOG.debug("==== deal the xls file ");
			HSSFWorkbook webwork = new HSSFWorkbook(multipartFile.getInputStream());
			dealWorkbook(webwork);
		} else if (fileName.endsWith("xlsx")) {
			LOG.debug("==== deal the xlsx file ");
			Workbook webwork = new XSSFWorkbook(multipartFile.getInputStream());
			dealWorkbook(webwork);
		}
	}

	private void dealWorkbook(Workbook webwork) {
		try {
			Sheet sheet = webwork.getSheetAt(0);
			int firstCellNum;
			String sex = "";
			for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				firstCellNum = row.getFirstCellNum();
				MembersUser patient = new MembersUser();
				patient.setLoginName(row.getCell(firstCellNum).getStringCellValue());
				patient.setUserName(row.getCell(firstCellNum + 1).getStringCellValue());
				patient.setPassword(row.getCell(firstCellNum + 2).getStringCellValue());
				sex = row.getCell(firstCellNum + 3).getStringCellValue();
				sex = sex.equals("女") ? "Female" : "Male";
				patient.setSex(Sex.valueOf(sex));
				patient.setPhone(row.getCell(firstCellNum + 4).getStringCellValue());
				userService.save(patient);
			}
			LOG.debug("last row = " + sheet.getLastRowNum());
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	@RequestMapping(value = "retrieval", method = RequestMethod.GET)
	public ModelAndView retrieval(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("management/retrieval");
	}

	@RequestMapping(value = "retrieval", method = RequestMethod.POST)
	public ModelAndView retrieval(String level, String paymentDateLeft, String paymentDateRight, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelMap mode = retvievalMath(level, paymentDateLeft, paymentDateRight, request);
		return new ModelAndView("management/retrieval", mode);
	}

	private ModelMap retvievalMath(String level, String paymentDateLeft, String paymentDateRight, HttpServletRequest request) throws ParseException {
		ModelMap mode = new ModelMap();
		mode.put("level", level);
		mode.put("paymentDateLeft", paymentDateLeft);
		mode.put("paymentDateRight", paymentDateRight);
		Map<String, Object> map = new HashMap<String, Object>();
		if (!level.equals("-1")) {
			map.put("level", level);
		} else {
			map.put("level", null);
		}
		if (!paymentDateLeft.equals("")) {
			map.put("paymentDateLeft", paymentDateLeft);
		} else {
			map.put("paymentDateLeft", null);
		}
		if (!paymentDateRight.equals("")) {
			map.put("paymentDateRight", paymentDateRight);
		} else {
			map.put("paymentDateRight", null);
		}
		Personnel personnel = (Personnel) request.getSession().getAttribute("loginPersonnle");
		if (personnel != null) {
			Department department = personnel.getDepartment();
			departments = new ArrayList<Department>();
			departments.clear();
			getDepartmentList(department);
			List<MemberRecord> personnelList = new ArrayList<MemberRecord>();
			if (departments != null && departments.size() > 0) {
				for (Department depar : departments) {
					if (depar != null && depar.getLevel() == 4) {
						List<MemberRecord> userList = departmentService.getAllByParam(map, depar.getDepartmentID());
						if (userList != null) {
							personnelList.addAll(userList);
						}
					}
				}
			}
			mode.put("list", personnelList);
		}
		return mode;
	}

	@RequestMapping(value = "isSettle", method = RequestMethod.GET)
	public ModelAndView isSettle(Integer recordID, String level, String paymentDateLeft, String paymentDateRight, HttpServletRequest request, HttpServletResponse response) throws Exception {
		RechargeRecord record = recordService.findById(recordID);
		record.setIsSettle(!record.getIsSettle());
		recordService.update(record);
		ModelMap mode = retvievalMath(level, paymentDateLeft, paymentDateRight, request);
		mode.put("message", "已结算");
		return new ModelAndView("management/retrieval", mode);
	}

	@RequestMapping(value = "seniorassociate", method = RequestMethod.GET)
	public ModelAndView seniorassociate(String level, String paymentDateLeft, String paymentDateRight, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelMap mode = new ModelMap();
		mode.put("level", level);
		mode.put("paymentDateLeft", paymentDateLeft);
		mode.put("paymentDateRight", paymentDateRight);
		Map<String, Object> map = new HashMap<String, Object>();
		if (!level.equals("-1")) {
			map.put("level", level);
		} else {
			map.put("level", null);
		}
		if (!paymentDateLeft.equals("")) {
			map.put("paymentDateLeft", paymentDateLeft);
		} else {
			map.put("paymentDateLeft", null);
		}
		if (!paymentDateRight.equals("")) {
			map.put("paymentDateRight", paymentDateRight);
		} else {
			map.put("paymentDateRight", null);
		}
		Personnel personnel = (Personnel) request.getSession().getAttribute("loginPersonnle");
		if (personnel != null) {
			Department department = personnel.getDepartment();
			departments = new ArrayList<Department>();
			departments.clear();
			getDepartmentList(department);
			List<MemberRecord> personnelList = new ArrayList<MemberRecord>();
			if (departments != null && departments.size() > 0) {
				for (Department depar : departments) {
					if (depar != null && depar.getLevel() == 4) {
						List<MemberRecord> userList = departmentService.getAllByDepart(map, depar.getDepartmentID());
						if (userList != null) {
							personnelList.addAll(userList);
						}
					}
				}
			}
			mode.put("list", personnelList);
		}
		return new ModelAndView("management/seniorassociate", mode);
	}

	@RequestMapping(value = "exports", method = RequestMethod.GET)
	public ModelAndView exports(String level, String paymentDateLeft, String paymentDateRight, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelMap mode = new ModelMap();
		mode.put("level", level);
		mode.put("paymentDateLeft", paymentDateLeft);
		mode.put("paymentDateRight", paymentDateRight);
		return new ModelAndView("management/exports", mode);
	}

	@RequestMapping(value = "export", method = RequestMethod.POST)
	public ModelAndView export(@RequestParam("file") MultipartFile file, String level, String paymentDateLeft, String paymentDateRight, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileName = file.getOriginalFilename();
		if (fileName.endsWith("xls") || fileName.endsWith("xlsx")) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (!level.equals("-1")) {
				map.put("level", level);
			} else {
				map.put("level", null);
			}
			if (!paymentDateLeft.equals("")) {
				map.put("paymentDateLeft", paymentDateLeft);
			} else {
				map.put("paymentDateLeft", null);
			}
			if (!paymentDateRight.equals("")) {
				map.put("paymentDateRight", paymentDateRight);
			} else {
				map.put("paymentDateRight", null);
			}
			Personnel personnel = (Personnel) request.getSession().getAttribute("loginPersonnle");
			if (personnel != null) {
				Department department = personnel.getDepartment();
				departments = new ArrayList<Department>();
				departments.clear();
				getDepartmentList(department);
				List<MemberRecord> personnelList = new ArrayList<MemberRecord>();
				if (departments != null && departments.size() > 0) {
					for (Department depar : departments) {
						if (depar != null && depar.getLevel() == 4) {
							List<MemberRecord> userList = departmentService.getAllByDepart(map, depar.getDepartmentID());
							if (userList != null) {
								personnelList.addAll(userList);
							}
						}
					}
				}
				
				export(personnelList,file);
			}
		}
		return null;
	}

	private void export(List<MemberRecord> personnelList,MultipartFile file) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook(file.getInputStream()); 
		HSSFSheet sheet = wb.createSheet("会员用户");
		HSSFRow row = sheet.createRow((int) 0);
		HSSFCellStyle style = wb.createCellStyle(); 
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("身份证");
		cell.setCellStyle(style);  
		cell = row.createCell((short) 1);  
        cell.setCellValue("用户名");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 2);  
        cell.setCellValue("等级");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 3);  
        cell.setCellValue("性别");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 4);  
        cell.setCellValue("电话");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 5);  
        cell.setCellValue("创建机构");  
        cell.setCellStyle(style); 
        for (int i = 0; i < personnelList.size(); i++) {
        	row = sheet.createRow((int) i + 1);
        	MemberRecord record = personnelList.get(i);
        	row.createCell((short) 0).setCellValue(record.getLoginName()); 
        	row.createCell((short) 1).setCellValue(record.getUserName()); 
        	row.createCell((short) 2).setCellValue(record.getLevel() ==MemberLevel.common?"":"G" ); 
        	row.createCell((short) 3).setCellValue(record.getSex() ==Sex.Male?"男":"女" ); 
        	row.createCell((short) 4).setCellValue(record.getPhone()); 
        	row.createCell((short) 5).setCellValue(record.getCateDepartment().getDepartmentName()); 
		}
        
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
