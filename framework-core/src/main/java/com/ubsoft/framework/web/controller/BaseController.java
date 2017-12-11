package com.ubsoft.framework.web.controller;

import com.ubsoft.framework.core.exception.ComException;
import com.ubsoft.framework.core.service.ITransactionService;
import com.ubsoft.framework.core.support.json.JsonHelper;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.web.context.WebContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
	
	protected final Logger logger = Logger.getLogger(getClass());
	@Autowired
	protected ITransactionService transactionService;

	@Autowired
	protected HttpServletRequest request;

	public String getSessionId() {
		String sessionId = request.getParameter("sid");
		if (sessionId == null) {
			sessionId = request.getParameter("sessionId");
		}
		return sessionId;
	}

	public String getUnitName() {
		String unitName = request.getParameter("unitName");
		if (StringUtil.isEmpty(unitName)||unitName.equals("null")) {
			unitName = "DefaultDS";
		}
		return unitName;
	}

	public HttpSession getSession() {
		return this.request.getSession();
	}

	public void setCookie(String key, String value) {
		// 先设置cookie
		Cookie cookie = new Cookie(key, value);
		// 设置Cookie的生命周期,如果设置为负值的话,关闭浏览器就失效.
		cookie.setMaxAge(-1);
		WebContext.getResponse().addCookie(cookie);

	}

	public void setSession(String key, String value) {
		this.getSession().setAttribute(key, value);
	}

	/**
	 * 获取客户端Key，先考虑cookie，可以多服务器负载均衡支持， 再考虑Session，单服务器支持
	 * 
	 * @param key
	 * @return
	 */
	public String getCookieValue(String key) {
		Cookie[] cookies = request.getCookies();
		String value = null;
		// 遍历数组,获得具体的Cookie
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				// 获得具体的Cookie
				Cookie cookie = cookies[i];
				// 获得Cookie的名称
				String name = cookie.getName();

				value = null;
				if (name.equals(key)) {
					value = cookie.getValue();
					return value;
				}
			}
		}

		return value;
	}
	
	public void deleteCookie(String key) {
		Cookie[] cookies = request.getCookies();
		String value = null;
		// 遍历数组,获得具体的Cookie
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				// 获得具体的Cookie
				// 获得Cookie的名称
				String name = cookie.getName();

				value = null;
				if (name.equals(key)) {
					cookie.setMaxAge(0);
				}
			}
		}
		
	}

	public Map getAllParameter(){
		Map map=new HashMap();
		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();

			String[] paramValues = request.getParameterValues(paramName);
			if (paramValues.length == 1) {
				String paramValue = paramValues[0];
				if (paramValue.length() != 0) {
					map.put(paramName, paramValue);
					System.out.println("参数：" + paramName + "=" + paramValue);

				}
			}
		}
		return map;
	}
	public String getSessionValue(String key) {
		String value = null;
		if (value == null) {
			value = this.getSession().getAttribute(key) + "";
			if (value.equals("")) {
				value = null;
			}
		}
		return value;
	}

	public String getRemoteIp() {
		//request.getRemoteHost();//远程主机名
		//request.getRemoteAddr();//远程主机地址
		//request.getRemoteUser();//远程主机用户名
		//request.getRemotePort();//远程主机请求端口
		//反向代理会添加此header
		if (request.getHeader("x-forwarded-for") == null) {
			return request.getRemoteAddr();
		}
		
		return request.getHeader("x-forwarded-for");
	}
	

	protected String responseError(Throwable e, Map<String, Object> res) {
		e.printStackTrace();
		String code = null;
		String message = null;
		if (e instanceof ComException) {
			ComException comex = (ComException) e;
			code = comex.getCode() + "";
			message = comex.getMessage();
		} else {
			code = "-1";
			message = e.getMessage();
		}
		res.put("status", "E");
		res.put("errCode", code);
		res.put("errMsg", message);
		String result = JsonHelper.bean2Json(res);
		return result;
	}
	
}