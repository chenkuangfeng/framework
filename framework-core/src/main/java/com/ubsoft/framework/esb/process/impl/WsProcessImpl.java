package com.ubsoft.framework.esb.process.impl;

import org.springframework.stereotype.Service;

import com.ubsoft.framework.core.support.util.HttpClientUtil;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.esb.model.Exchange;
import com.ubsoft.framework.esb.model.Message;
import com.ubsoft.framework.esb.process.IProcess;

@Service("wsProcess")
public class WsProcessImpl implements IProcess {
	@Override
	public void process(Exchange ex) throws Exception {
		Message in = ex.getIn();
		String host = in.getHeader("URL").toString();	 
		String ftl=in.getHeader("FTL")+"";
		//如果需要消息转换,通过freemark转换消息称字符串
		String bodyStr=null;
		if(StringUtil.isNotEmpty(ftl)){
			//FreeMarkerUtil fu=FreeMarkerUtil.getInstance();
			//fu.setModel("msg", in.getBody());
			//bodyStr=fu.getTemplateResult(ftl);
		}else{
			bodyStr=in.getBody()+"";
		}
		String response = HttpClientUtil.post(host, bodyStr);
		Message out = (Message)in.clone();		
		out.setBody(response);
		ex.setOut(out);
	}

}
