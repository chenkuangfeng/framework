package com.ubsoft.framework.esb.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ubsoft.framework.core.support.json.JsonHelper;
import com.ubsoft.framework.core.support.util.PasswordUtil;
import com.ubsoft.framework.esb.cache.MemoryAppKey;
import com.ubsoft.framework.esb.cache.MemoryEndpoint;
import com.ubsoft.framework.esb.entity.AppKey;
import com.ubsoft.framework.esb.entity.Endpoint;
import com.ubsoft.framework.esb.model.Exchange;
import com.ubsoft.framework.esb.model.Message;
import com.ubsoft.framework.esb.model.Response;
import com.ubsoft.framework.esb.service.IEsbEngine;
import com.ubsoft.framework.esb.service.IRsService;
@RequestMapping("/")
@Controller
public class RsService implements IRsService {
	@Resource
	IEsbEngine engine;
	@Autowired
	protected HttpServletRequest request;

	@RequestMapping(value = "/rs", method = RequestMethod.POST)
	public @ResponseBody
	String process() {
		try {
			String appKey = request.getParameter("appKey");
			String sign = request.getParameter("sign");
			String api = request.getParameter("api");
			Exchange ex = new Exchange();
			Message msg = new Message();
			msg.setMessageId(UUID.randomUUID().toString());
			Endpoint ep = MemoryEndpoint.getInstance().get(api);
			String line = "";
			StringBuilder body = new StringBuilder();
			InputStream stream;
			stream = request.getInputStream();
			// 读取POST提交的数据内容
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					stream, "utf-8"));
			while ((line = reader.readLine()) != null) {
				body.append(line);
			}
			msg.setBody(body.toString());
			ex.setIn(msg);
			validate(appKey, api, msg.getBody() + "", sign);// 验证传递接口参数是否合法
			ex.setEndpoint(ep);
			if (ep != null) {
				engine.process(ep, ex);
			} else {
				return this.getJsonErrorResponse("404", "接口服务【" + api + "】不存在。");
			}
			return ex.getOut().getBody().toString();
		} catch (Exception e) {
			return this.getJsonErrorResponse("500", e.getMessage());
		}

	}

	/**
	 * 验证传递接口参数是否合法
	 * 
	 * @throws Exception
	 */
	private void validate(String appKey, String api, String msg, String sign)
			throws Exception {

		AppKey ak = MemoryAppKey.getInstance().get(appKey);
		if (ak == null) {
			throw new Exception("AppKey不存在。");
		}
		Endpoint ep = MemoryEndpoint.getInstance().get(api);
		if (ep == null) {
			throw new Exception("不存在API名为【" + api + "】的服务。");
		}
		String checkSign = PasswordUtil.sign(appKey + ak.getSecret() + api
				+ msg, "UTF-8");
		if (!checkSign.equals(sign)) {
			throw new Exception("数字签名不正确。");
		}

	}

	protected Message getJsonInMessage(String requestJson) throws Exception {

		Message inMsg = new Message();
		return inMsg;

	}

	protected String getJsonErrorResponse(String errCode, String errMsg) {
		Response rp = new Response();
		rp.setStatus("E");
		rp.setCode(errCode);
		rp.setMessage(errMsg);
		return JsonHelper.bean2Json(rp);
	}
}
