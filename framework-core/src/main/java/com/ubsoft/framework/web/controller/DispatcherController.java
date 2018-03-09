package com.ubsoft.framework.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ubsoft.framework.core.exception.ComException;
import com.ubsoft.framework.core.support.json.JsonHelper;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.system.model.Subject;

@RequestMapping("/")
@Controller
public class DispatcherController extends BaseController {
	/**
	 * 标准接口入口，返回json格式消息；
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/dispatcher.ctrl")
	@ResponseBody
	public String dispatcher() throws UnsupportedEncodingException {
		String serviceName = request.getParameter("serviceName");
		String methodName = request.getParameter("methodName");		
		try {
			if(StringUtil.isNotEmpty(serviceName)){
				if(!Subject.getSubject().isPermitted(serviceName+"."+methodName)){
					throw new ComException(ComException.MIN_ERROR_CODE_SECURITY,"没有调用权限.");
				}
			}
			String unitName = this.getUnitName();
			String params = request.getParameter("params");
			Object[] resultParmas = null;
			if (params != null) {
				resultParmas = JsonHelper.transferParams(params);
			} else {
				resultParmas = new Object[] {};
			}
			
			Object obj = transactionService.execute(unitName, serviceName, methodName, resultParmas);
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("status", "S");
			res.put("ret", obj);
			String result = JsonHelper.bean2Json(res);
			return result;
		} catch (Throwable e) {
			ComException ex = null;
			if (e instanceof ComException) {
				ex = (ComException) e;
			} else {
				ex = new ComException(-2, e.getMessage());
			}
			int code = ex.getCode();
			String source = ex.getSource();
			String message = ex.getMessage();
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("status", "E");
			res.put("errCode", code);
			res.put("errMsg", message);
			String result = JsonHelper.bean2Json(res);
			return result;
		}

	}

}
