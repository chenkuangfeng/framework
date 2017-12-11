package com.ubsoft.framework.web.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ubsoft.framework.core.context.AppContext;
import com.ubsoft.framework.core.service.ITransactionService;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.system.cache.MemoryPermission;
import com.ubsoft.framework.system.entity.Session;
import com.ubsoft.framework.system.model.Subject;

public class SecurityInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
	 * 如果返回true 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链,
	 * 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion()
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String sessionId = this.getCookieValue(request, "sessionId");
		if (StringUtil.isEmpty(sessionId)) {
			sessionId = request.getParameter("sessionId");
		}
		if (StringUtil.isNotEmpty(sessionId)) {
			if (validateSession(sessionId)) {
				String moduleKey = request.getParameter("module");
				String moduleValue = null;
				moduleValue = MemoryPermission.getInstance().get(moduleKey);
				// 如果配置了权限码，判断权限并转到映射的jsp界面。
				if (moduleKey != null) {
					if (Subject.getSubject().isPermitted(moduleKey)) {							
						request.getRequestDispatcher("/WEB-INF/view/" + moduleValue + ".jsp?sessionId="+sessionId).forward(request, response);					
						return false;
					} else {						
						request.getRequestDispatcher("/WEB-INF/view/common/nopermission.jsp").forward(request, response);
						return false;
					}
				}else{
					String urlPath=request.getRequestURI();
					//String qs=request.getQueryString();					
					//没有配置权限的界面，需要没有后缀				
					if(urlPath.indexOf(".")==-1){
						urlPath=urlPath.substring(urlPath.indexOf("/",1)+1);
						request.getRequestDispatcher("/WEB-INF/view/" + urlPath + ".jsp?sessionId="+sessionId).forward(request, response); 
						return false;
					}else{
						return true;
					}
				}
			
			

			} else {
				request.getRequestDispatcher("/WEB-INF/view/common/nologin.jsp").forward(request, response);
				return false;
			}
		} else {
			request.getRequestDispatcher("/WEB-INF/view/common/nologin.jsp").forward(request, response);
			return false;
		}

	}

	/**
	 * 在业务处理器处理请求执行完成后,生成视图之前执行的动作 可在modelAndView中加入数据，比如当前时间
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等
	 * 
	 * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

	private boolean validateSession(String sessionId) {
		if (StringUtil.isEmpty(sessionId))
			return false;
		// 先从缓存中取
		ITransactionService ds = (ITransactionService) AppContext.getBean("transactionService");
		Object obj = ds.execute("sessionService", "getSession", new Object[] { sessionId });
		if (obj != null) {
			Subject subject = new Subject((Session) obj);
			Subject.setSubject(subject);
			return true;
		} else {
			return false;
		}
		
	}

	public String getCookieValue(HttpServletRequest request, String key) {
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
}