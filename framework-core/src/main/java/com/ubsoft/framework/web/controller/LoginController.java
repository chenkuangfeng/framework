package com.ubsoft.framework.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ubsoft.framework.core.conf.AppConfig;
import com.ubsoft.framework.core.support.json.JsonHelper;
import com.ubsoft.framework.core.support.util.BeanUtil;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.core.support.util.TreeUtil;
import com.ubsoft.framework.system.entity.Permission;
import com.ubsoft.framework.system.entity.Session;
import com.ubsoft.framework.system.model.Subject;
import com.ubsoft.framework.system.service.IUserService;

@RequestMapping("/")
@Controller
public class LoginController extends BaseController {
	@Resource
	IUserService userService;

	/**
	 * 登陆
	 * 
	 * @return
	 */
	@RequestMapping("/login.ctrl")
	public ModelAndView login() {
		String uiType = request.getParameter("ui");//主页界面类型1:标准，2：bootstrap,3:mobile

		ModelAndView mv = new ModelAndView();
		String userKey = request.getParameter("userKey");
		// 是否是移动终端打开
		String mobile = request.getParameter("mobile");
		String pwd = request.getParameter("password");
		if (userKey != null) {
			try {
				Session session = userService.login(userKey, pwd);
				if (session != null) {
					setCookie("sessionId", session.getSessionId());
				}

				Subject subject = new Subject(session);
				Subject.setSubject(subject);
				if (StringUtil.isNotEmpty(mobile) && mobile.equals("true")) {
					mv.addObject("mobile", "true");
				}
				mv.setViewName("redirect:/app.ctrl?ui="+uiType);

			} catch (Exception e) {
				mv.addObject("logo", AppConfig.getDataItem("logo"));
				mv.addObject("appName", AppConfig.getDataItem("appName"));
				mv.addObject("loginError", e.getMessage());
				mv.setViewName("/view/login");
			}

		} else {
			mv.addObject("logo", AppConfig.getDataItem("logo"));
			mv.addObject("appName", AppConfig.getDataItem("appName"));
			mv.setViewName("/view/login");
		}

		return mv;

	}

	@RequestMapping("/loginout.ctrl")
	public ModelAndView loginout() {
		ModelAndView mv = new ModelAndView();
		String sessionId = Subject.getSubject().getSession().getSessionId();
		userService.loginout(sessionId);
		deleteCookie(sessionId);
		mv.setViewName("redirect:/login.ctrl");
		return mv;
	}

	// @RequestMapping("/loginIn.ctrl")
	// public @ResponseBody
	// String loginIn() {
	// String userKey = request.getParameter("userKey");
	// String pwd = request.getParameter("pwd");
	// try {
	// Session session = userService.login(userKey, pwd);
	// Map<String, Object> res = new HashMap<String, Object>();
	// res.put("status", "S");
	// res.put("data", session);
	// String result = JsonHelper.bean2Json(res);
	// setCookie("sid", session.getSessionId());
	// return result;
	// } catch (Exception e) {
	// ComException ex = null;
	// if (e instanceof ComException) {
	// ex = (ComException) e;
	// } else {
	// ex = new ComException(-2, e.getMessage());
	// }
	// int code = ex.getCode();
	// String source = ex.getSource();
	// String message = ex.getMessage();
	// Map<String, Object> res = new HashMap<String, Object>();
	// res.put("status", "E");
	// res.put("errCode", code);
	// res.put("errMsg", message);
	// String result = JsonHelper.bean2Json(res);
	// return result;
	// } finally {
	//
	// }
	//
	// }
	/**
	 * 主页
	 * 
	 * @return
	 */
	@RequestMapping("/app.ctrl")
	public ModelAndView index() {
		String uiType = request.getParameter("ui");//主页界面类型1:标准，2：bootstrap,3:mobile
		ModelAndView mv = new ModelAndView();
		mv.addObject("userKey", Subject.getSubject().getSession().getUserKey());
		mv.addObject("orgKey", Subject.getSubject().getSession().getOrgKey());
		mv.addObject("orgName", Subject.getSubject().getSession().getOrgName());
		mv.addObject("userName", Subject.getSubject().getSession().getUserName());
		mv.addObject("logo", AppConfig.getDataItem("logo"));
		mv.addObject("appName", AppConfig.getDataItem("appName"));
		if (StringUtil.isNotEmpty(uiType) && uiType.equals("1")) {
			mv.addObject("userMenu", this.getUserMenuTree(Subject.getSubject().getUserKey()));
			mv.setViewName("/view/index");
		} else if(StringUtil.isNotEmpty(uiType) && uiType.equals("2")) {
			mv.addObject("userMenu", this.getUserMenuTree2(Subject.getSubject().getUserKey()));
			mv.setViewName("/view/index2");
		}else if(StringUtil.isNotEmpty(uiType) && uiType.equals("3")) {
			//mv.addObject("userMenu", this.getUserMenuTree2(Subject.getSubject().getUserKey()));
			mv.setViewName("/viewmobile/index");
		}else{
			mv.addObject("userMenu", this.getUserMenuTree2(Subject.getSubject().getUserKey()));
			mv.setViewName("/view/index2");
		}
		Subject.removeSubject();
		return mv;
	}

	private String getUserMenuTree(String userKey) {
		List<Permission> menus = userService.getPermission(userKey);
		// 将bean集合转换成Map
		List<Map> listMap = new ArrayList<Map>();
		String result = null;
		// 转换成ztree格式
		for (Permission menu : menus) {
			if (menu.getPermType().equals("MENU")) {
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", menu.getPermKey());
				item.put("pId", menu.getParentPermKey());
				item.put("name", menu.getPermName());
				item.put("module", menu.getPermModule());
				listMap.add(item);
			}
		}
		result = JsonHelper.collection2json(listMap);
		/*
		 * easyui tree的拼接 for(Permission perm:menus){ Map
		 * map=BeanUtil.bean2Map(perm); listMap.add(map); } //转换成treemap
		 * listMap=TreeUtil.buildTree(listMap, "permKey", "parentPermKey");
		 * 
		 * String result =TreeUtil.toEasyUITree(listMap, "permKey", "permName",
		 * "permModule");
		 */
		return result;

	}

	/**
	 * 移动端的列表
	 * 
	 * @param userKey
	 * @return
	 */
	private String getUserMenuTree2(String userKey) {
		List<Permission> menus = userService.getPermission(userKey);
		List<Map> listMap = new ArrayList<Map>();

		for (Permission perm : menus) {
			if(perm.getPermType().equals("MENU")){
				Map map = BeanUtil.bean2Map(perm);
				listMap.add(map);
			}
		}

		// 转换成treemap
		listMap = TreeUtil.buildTree(listMap, "permKey", "parentPermKey");

		String result = TreeUtil.toJsonTree(listMap, "permKey", "permName", "permModule", "icon");

		return result;

	}

}