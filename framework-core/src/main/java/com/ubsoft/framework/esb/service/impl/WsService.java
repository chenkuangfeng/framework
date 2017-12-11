package com.ubsoft.framework.esb.service.impl;

import java.util.UUID;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.ubsoft.framework.core.support.xml.XmlUtil;
import com.ubsoft.framework.esb.cache.MemoryEndpoint;
import com.ubsoft.framework.esb.entity.Endpoint;
import com.ubsoft.framework.esb.model.Exchange;
import com.ubsoft.framework.esb.model.Message;
import com.ubsoft.framework.esb.model.Response;
import com.ubsoft.framework.esb.service.IEsbEngine;
import com.ubsoft.framework.esb.service.IWsService;

/**
 * webservice标准接口，统一接收消息格式如下： <request> <header> <apiName>useRegister</apiName>
 * <uId>sa</uId> <pwd>1223</pwd> <msgId>1223</msgId> </header> <body> <user>
 * <userId>dfdfd</userId> <userName>fdfd</userName> <user> </body> </request>
 * 统一返回消息如下： <response> <header> <status>S</status>//S:成功；E:失败；
 * <errCode></errCode> <errMsg></errMsg> </header> <body>
 * 
 * </body> </response>
 * 
 * @author chenkf basic认证 一是在请求头中添加Authorization： Authorization:
 *         "Basic 用户名和密码的base64加密字符串" 二是在url中添加用户名和密码：
 *         http://userName:password@api.
 *         minicloud.com.cn/statuses/friends_timeline.xml
 */
@WebService(endpointInterface = "com.framework.server.esb.service.IWsService")
public class WsService implements IWsService {
	@Resource
	IEsbEngine engine;

	@Override
	public String process(String requestXml) {
		try {
			Exchange ex = new Exchange();
			Message msg = null;
			try {
				msg = this.getXmlInMessage(requestXml);
			} catch (Exception e) {
				return this.getXmlErrorResponse("500", "消息格式错误:" + e.getMessage());
			}
			ex.setIn(msg);
			String apiName = ex.getIn().getHeader("apiName") + "";
			String appKey = ex.getIn().getHeader("appKey") + "";
			String sign = ex.getIn().getHeader("sign") + "";
			String ver = ex.getIn().getHeader("ver") + "";
			Endpoint ep = MemoryEndpoint.getInstance().get(apiName);
			ex.setEndpoint(ep);
			if (ep != null) {
				engine.process(ep, ex);
			} else {
				return this.getXmlErrorResponse("404", "接口服务【" + apiName + "】不存在。");
			}
			return ex.getOut().getBody().toString();
		} catch (Exception e) {
			e.printStackTrace();
			return this.getXmlErrorResponse("500", "系统内部错误：" + e.getMessage());
		}
	}

	protected Message getXmlInMessage(String requestXml) throws Exception {
		Document doc = DocumentHelper.parseText(requestXml);
		Element root = doc.getRootElement();
		String appKey = root.attributeValue("appKey");
		String sign = root.attributeValue("sign");
		String apiName = root.attributeValue("apiName");
		String timestamp = root.attributeValue("timestamp");
		Message in = new Message();
		in.setHeader("appKey", appKey);
		in.setHeader("sign", sign);
		in.setHeader("apiName", apiName);
		in.setHeader("timestamp", timestamp);
		in.setMessageId(UUID.randomUUID().toString());
		// 去除sign，到后面做签名计算比较，签名=secret+sign属性的字符串
		Attribute signAtt = root.attribute("sign");
		if (signAtt != null) {
			root.remove(signAtt);

		}
		in.setBody(root.asXML());
		return in;
	}
	protected String getXmlErrorResponse(String errCode, String errMsg) {
		Response rp = new Response();
		rp.setStatus("E");
		rp.setCode(errCode);
		rp.setMessage(errMsg);

		try {
			return XmlUtil.toXML(rp);
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}
}
