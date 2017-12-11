package com.ubsoft.framework.core.service.impl;

import java.io.Serializable;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.ubsoft.framework.core.conf.AppConfig;
import com.ubsoft.framework.core.dal.util.DynamicDataSource;
import com.ubsoft.framework.core.exception.ExceptionHelper;
import com.ubsoft.framework.core.exception.SessionException;
import com.ubsoft.framework.core.service.ITransactionService;
import com.ubsoft.framework.core.support.util.AopUtil;
import com.ubsoft.framework.core.support.util.ReflectUtil;
import com.ubsoft.framework.system.entity.Session;
import com.ubsoft.framework.system.model.Subject;
import com.ubsoft.framework.system.service.ISessionService;
import com.ubsoft.framework.system.service.IUserService;

@Service("transactionService")
// @Transactional
// //此处不能用事务，DynamicDataSource.setDataSource(unitName)必须在有事务的方法前切换
public class TransactionService implements ApplicationContextAware, ITransactionService {
	private ApplicationContext cxt;
	protected final Logger logger = Logger.getLogger(getClass());
	@Resource
	ISessionService sessionService;
	@Resource
	IUserService userService;

	/**
	 * 带Session的方式执行，用于CS架构
	 */
	@Override
	public Object execute(String sessionId, String unitName, String serviceName, String methodName, Object[] params) throws RuntimeException {
		// 调用服务前验证session，是否考虑在web filter里面做验证
		if (validateSession(sessionId)) {
			long start = System.currentTimeMillis();
			try {
				DynamicDataSource.setDataSource(unitName);

				Object bean = cxt.getBean(serviceName);
				Object result = callMethod(bean, methodName, params);
				return result;
			} catch (Throwable e) {
				logger.error("调用服务" + serviceName + "的方法" + methodName + "失败：" + e.getMessage());
				throw ExceptionHelper.dealRuntimeException(e);
			} finally {
				long time = System.currentTimeMillis() - start;
				if (AppConfig.isDebug() || time / 1000d > 30) {
					logger.info("服务[" + serviceName + "]中方法[" + methodName + "]用时:" + time / 1000d + "S");
				}
				removeContext();
			}
		} else {
			try {
				throw new SessionException(1, "会话超时，请重新登录。");
			} catch (Throwable e) {
				throw ExceptionHelper.dealRuntimeException(e);
			}
		}
	}

	/**
	 * 不带Session的执行,Session验证可以放在Web的filter，
	 */
	@Override
	public Object execute(String unitName, String serviceName, String methodName, Object[] params) {
		long start = System.currentTimeMillis();
		try {
			DynamicDataSource.setDataSource(unitName);
			Object bean = cxt.getBean(serviceName);
			// Method [] methods=bean.getClass().getDeclaredMethods();
			// for(Method md:methods){
			// System.out.println(md.getName());
			// Class [] clas=md.getParameterTypes();
			// for(Class clazz:clas){
			// System.out.println(clazz.getName());
			// }
			// //System.out.println(md.getParameterTypes());
			// }
			//

			Object result = callMethod(bean, methodName, params);
			return result;
		} catch (Throwable e) {
			// e.printStackTrace();
			logger.error("调用服务" + serviceName + "的方法" + methodName + "失败：" + e.getMessage());
			throw ExceptionHelper.dealRuntimeException(e);
		} finally {
			long time = System.currentTimeMillis() - start;
			if (AppConfig.isDebug() || time / 1000d > 30) {
				logger.info("服务[" + serviceName + "]中方法[" + methodName + "]用时:" + time / 1000d + "S");
			}
			// logger.info("服务[" + serviceName + "]中方法[" + methodName + "]用时:" +
			// time / 1000d + "S");
			removeContext();
		}
	}

	/**
	 * 非数据库应用
	 */
	public Object execute(String serviceName, String methodName, Object[] params) {
		long start = System.currentTimeMillis();
		try {
			Object bean = cxt.getBean(serviceName);
			Object result = callMethod(bean, methodName, params);
			return result;
		} catch (Throwable e) {
			logger.error("调用服务" + serviceName + "的方法" + methodName + "失败：" + e.getMessage());
			throw ExceptionHelper.dealRuntimeException(e);
		} finally {
			long time = System.currentTimeMillis() - start;
			if (AppConfig.isDebug() || time / 1000d > 30) {
				logger.info("服务[" + serviceName + "]中方法[" + methodName + "]用时:" + time / 1000d + "S");
			}

		}
	}

	@Override
	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		cxt = ac;

	}
	private boolean validateSession(String sessionId) {
		Session session = sessionService.getSession(sessionId);
		if (session == null) {
			return false;
		} else {
			Subject subject = new Subject(session);
			Subject.setSubject(subject);
			return true;
		}
	}

	private Object callMethod(Object bean, String methodName, Object[] params) throws Throwable {
		Object result = null;
		// 泛型参数方法特殊处理
		Object target=null;
		try {
			target = AopUtil.getTarget(bean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}
		//BaseService 泛型方法处理
		if (target instanceof BaseService && methodName.equals("save")) {
			result = ReflectUtil.callMethod(bean, methodName, params, new Class[] { Serializable.class });
		} else {
			result = ReflectUtil.callMethod(bean, methodName, params);
		}
		return result;

	}	
	

	private void removeContext() {
		// 数据源上下文
		DynamicDataSource.removeDataSource();
		// 用户信息和权限上下文
		Subject.removeSubject();
	}

}
