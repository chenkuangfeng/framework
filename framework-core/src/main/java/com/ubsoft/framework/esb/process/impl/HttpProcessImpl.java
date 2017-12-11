package com.ubsoft.framework.esb.process.impl;

import org.springframework.stereotype.Service;

import com.ubsoft.framework.core.support.util.HttpClientUtil;
import com.ubsoft.framework.esb.model.Exchange;
import com.ubsoft.framework.esb.model.Message;
import com.ubsoft.framework.esb.process.IProcess;

@Service("httpProcess")
public class HttpProcessImpl implements IProcess {
	
	@Override
	public void process(Exchange ex) throws Exception {
		Message in = ex.getIn();
		String host = in.getHeader("URL").toString();
		String contentType=in.getHeader("CONTENTTYPE")==null?null:in.getHeader("CONTENTTYPE").toString();
		String userName=in.getHeader("USERNAME")==null?null:in.getHeader("USERNAME").toString();
		String userPassword=in.getHeader("USERPWD")==null?null:in.getHeader("USERPWD").toString();
		String response = HttpClientUtil.post(host, in.getBody()+"",contentType,userName,userPassword);
		Message out = (Message)in.clone();		
		out.setBody(response);
		ex.setOut(out);
	}

}
