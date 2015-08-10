package com.yc.controller.web.controller;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yc.entity.Collection;
import com.yc.entity.user.ConstitutionType;
import com.yc.entity.user.MembersFamily;
import com.yc.entity.user.MembersUser;
import com.yc.entity.user.Sex;
import com.yc.model.MemberFamilyModel;
import com.yc.service.ICollectionService;
import com.yc.service.IMembersFamilyService;
import com.yc.service.IMembersUserService;

@Controller
@RequestMapping("/webUser")
public class UserController {


	private static final Logger LOG = Logger.getLogger(UserController.class);

	@Autowired
	IMembersUserService userService;
	
	@Autowired
	IMembersFamilyService memberService;

	@Autowired
	ICollectionService collectionService;
	
	@RequestMapping(value = "login", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> login(String page, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("loginName");
		String pwd = KL(JM(KL(MD5(request.getParameter("password")))));
		HttpSession session = request.getSession();
		MembersUser personnel = userService.getUser(name);
		ModelMap mode = new ModelMap();
		if (personnel == null) {
			request.getSession().setAttribute("message", "noUser");
			mode.put("message", "noUser");
			return mode;
		} else {
			if (personnel.getPassword().equals(pwd)) {
				session.setAttribute("loginUser", personnel);
				mode.put("message", "login");
				return mode;
			} else {
				request.getSession().setAttribute("message", "pwdIsWrong");
				mode.put("message", "pwdIsWrong");
				return mode;
			}
		}
	}
	
	//用户是否收藏了商品或者店铺
		@RequestMapping(value = "detail/isCollected", method = RequestMethod.POST)
		@ResponseBody
		public Map<String, Object> isCollected(String userName, Integer recommID, Integer shopID )
				throws ServletException, IOException {
			ModelMap mode = new ModelMap();
			MembersUser user = userService.getUser(userName);
			List<Collection> collections = collectionService.getAllByUser(user.getMembersUserID());
			int i;
			for ( i = 0; i < collections.size(); i++ ) {
				if ( recommID != 0 && collections.get(i).getRecommendation() != null ) {
					if ( collections.get(i).getRecommendation().getRecommendationID() == recommID ) {
						mode.put("isCollected", true);
						break;
					}
				} 
//				else if ( shopID != 0 && collections.get(i).getShop() != null ) {
//					if ( collections.get(i).getShop().getId() == shopID ) {
//						mode.put("isCollected", true);
//						break;
//					}
//				}
			}
			if ( i >= collections.size() ) {
				mode.put("isCollected", false);
			}
			return mode;
		}
			
	// 所有收藏
	@RequestMapping(value = "collection", method = RequestMethod.GET)
	public ModelAndView getAllCollection(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ModelMap mode = new ModelMap();
		MembersUser user = (MembersUser) request.getSession().getAttribute("loginUser");
		List<Collection> collections = collectionService.getAllByUser(user.getMembersUserID());
		mode.put("collections", collections);
		mode.put("user", user);
		return new ModelAndView("user/collection", mode);
	}

	// 删除收藏
	@RequestMapping(value = "deleteCollection", method = RequestMethod.GET)
	public String deleteCollection(Integer collectionID, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		collectionService.delete(collectionID);
		return "redirect:/user/collection";
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
	@RequestMapping(value = "addMember", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public void addMember(String loginName, String memberName, String memberConstitution,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		MembersUser user = userService.getUser(loginName);
		MembersFamily member = new MembersFamily();
		member.setMembersFamilyID(null);
		member.setMembersName(memberName);
		member.setBodyConstitution(saveTransferConsitution(memberConstitution));
		member.setMembersUser(user);
		memberService.save(member);
	}
	
	@RequestMapping(value = "getMembers", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> getMembers(String loginName,HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		List<MembersFamily> members = memberService.getFamiliesByLoginName(loginName);
		List<MemberFamilyModel> memberModels = new ArrayList<MemberFamilyModel>();
		MemberFamilyModel member = new MemberFamilyModel();
		for ( int i = 0; i < members.size(); i++ ) {
			ConstitutionType memberConstitution = members.get(i).getBodyConstitution();
			member.setMembersFamilyId(members.get(i).getMembersFamilyID());
			member.setMembersName(members.get(i).getMembersName());
			member.setConstitution(getTransferConsitution(memberConstitution));
			memberModels.add(member);
		}
		
		ModelMap mode = new ModelMap();
		mode.put("members", memberModels);
		return mode;
	}
	
	@RequestMapping(value = "deleteMember", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public void deleteMember(Integer memberId,HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		memberService.delete(memberId);
	}
	
	@RequestMapping(value = "saveUserInfo", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public void saveUserInfo(String loginName, String userName, String sex, 
			String constitution, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MembersUser user = userService.getUser(loginName);
		user.setUserName(userName);
		user.setBodyConstitution(saveTransferConsitution(constitution));
		if ( "女".equals(sex) ) {
			user.setSex(Sex.Female);
		} else {
			user.setSex(Sex.Male);
		}
		userService.update(user);
	}
	
	@RequestMapping(value = "getUser", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> getUser(String loginName,HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		MembersUser user = userService.getUser(loginName);
		ModelMap mode = new ModelMap();
		mode.put("user", user);
		return mode;
	}
	
	private String getTransferConsitution( ConstitutionType constitution ) {
		if ( ConstitutionType.FlatAndQuality.equals(constitution)) {
			return "平和质";
		} else if ( ConstitutionType.QiDeficiency.equals(constitution)) {
			return "气虚质";
		} else if ( ConstitutionType.YangXuzhi.equals(constitution)) {
			return "阳虚质";
		} else if ( ConstitutionType.YinDeficiency.equals(constitution)) {
			return "阴虚质";
		} else if ( ConstitutionType.PhlegmDampnessQuality.equals(constitution)) {
			return "痰湿质";
		} else if ( ConstitutionType.HotAndhumidQuality.equals(constitution)) {
			return "湿热质";
		} else if ( ConstitutionType.BloodStasis.equals(constitution)) {
			return "血瘀质";
		} else if ( ConstitutionType.QiStagnation.equals(constitution)) {
			return "气郁质";
		} else if ( ConstitutionType.IntrinsicQuality.equals(constitution)) {
			return "特禀质";
		}
		return null;
	}
	
	private ConstitutionType saveTransferConsitution( String constitution ) {
		if ( "平和质".equals(constitution)) {
			return ConstitutionType.FlatAndQuality;
		} else if ( "气虚质".equals(constitution)) {
			return ConstitutionType.QiDeficiency;
		} else if ( "阳虚质".equals(constitution)) {
			return ConstitutionType.YangXuzhi;
		} else if ( "阴虚质".equals(constitution)) {
			return ConstitutionType.YinDeficiency;
		} else if ( "痰湿质".equals(constitution)) {
			return ConstitutionType.PhlegmDampnessQuality;
		} else if ( "湿热质".equals(constitution)) {
			return ConstitutionType.HotAndhumidQuality;
		} else if ( "血瘀质".equals(constitution)) {
			return ConstitutionType.BloodStasis;
		} else if ( "气郁质".equals(constitution)) {
			return ConstitutionType.QiStagnation;
		} else if ( "特禀质".equals(constitution)) {
			return ConstitutionType.IntrinsicQuality;
		}
		return null;
	}
}
