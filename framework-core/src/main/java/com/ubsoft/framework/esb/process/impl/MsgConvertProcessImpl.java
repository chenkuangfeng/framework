package com.ubsoft.framework.esb.process.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.ubsoft.framework.core.support.json.JsonHelper;
import com.ubsoft.framework.core.support.xml.XmlUtil;
import com.ubsoft.framework.esb.model.Exchange;
import com.ubsoft.framework.esb.model.Message;
import com.ubsoft.framework.esb.process.IProcess;

/**
 * 消息转换
 * @author chenkf
 *
 */
@Service("msgConvertProcess")
public class MsgConvertProcessImpl implements IProcess {

	@Override
	public void process(Exchange ex) throws Exception {
		String type= ex.getIn().getHeader("TYPE")+"";
		System.out.println("msg.......................");
		String beanName=ex.getIn().getHeader("BEANNAME")+"";
		Object bodyIn=ex.getIn().getBody();
		Message out=(Message)ex.getIn().clone();
		if(type.equals("X2B")){//xml2bean
			Object bean=XmlUtil.fromXML(bodyIn.toString(), Class.forName(beanName));
			out.setBody(bean);
			ex.setOut(out);
		}else if(type.equals("J2B")){//json2bean
			Object bean = JsonHelper.json2Bean(bodyIn.toString(), Class.forName(beanName));
			out.setBody(bean);
			ex.setOut(out);
		}else if(type.equals("B2X")){//bean2xml
			String xml=XmlUtil.toXML(bodyIn);
			out.setBody(xml);
			ex.setOut(out);
		}else if(type.equals("B2J")){//bean2json
			String json=JsonHelper.bean2Json(bodyIn);
			out.setBody(json);
			ex.setOut(out);
		}else if(type.equals("M2X")){//map2xml
			String xml=XmlUtil.map2xml((Map)bodyIn);
			out.setBody(xml);
			ex.setOut(out);
		}else if(type.equals("X2M")){//xml2map
			Map xml=XmlUtil.xml2map(bodyIn.toString(), true);
			out.setBody(xml);
			ex.setOut(out);
		}else{
			throw new Exception("转换类型配置错误，请检查。");
			
		}
	
	}

}
