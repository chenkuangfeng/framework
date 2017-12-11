package com.ubsoft.framework.esb.process.impl;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.ubsoft.framework.core.context.AppContext;
import com.ubsoft.framework.core.service.ITransactionService;
import com.ubsoft.framework.esb.model.Exchange;
import com.ubsoft.framework.esb.process.IProcess;
import com.ubsoft.framework.esb.service.IEsbEngine;

@Service("mqProcess")
public class MQProcessImpl implements IProcess {
	@Resource
	ITransactionService rsService;
	
	@Override
	public void process(Exchange ex) throws Exception {
		com.ubsoft.framework.esb.model.Message in = ex.getIn();
		if (in.getHeader("MQDIR").equals(IEsbEngine.ROUTE_MQ_FROM)) {
			receiveMsg(ex);
		} else {
			sendMsg(ex);
		}		

	}	
	private void sendMsg(final Exchange ex) {
		
		com.ubsoft.framework.esb.model.Message in = ex.getIn();
		String queueName = in.getHeader("QUEUE") + "";		
		JmsTemplate jmsTemplate=(JmsTemplate)AppContext.getBean("jmsTemplate");
		jmsTemplate.send(queueName, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage();
				message.setText(ex.getIn().getBody().toString());
				return message;
			}
		});
		ex.setOut(in);
		
	}
	private void receiveMsg(final Exchange ex) throws Exception {
		JmsTemplate jmsTemplate=(JmsTemplate)AppContext.getBean("jmsTemplate");
		com.ubsoft.framework.esb.model.Message in = ex.getIn();
		String queueName = in.getHeader("QUEUE") + "";
		TextMessage message = (TextMessage) jmsTemplate.receive(queueName);	
		if(message!=null){
			com.ubsoft.framework.esb.model.Message out = (com.ubsoft.framework.esb.model.Message) in.clone();
			out.setBody(message.getText());
			ex.setOut(out);
		}else{
			com.ubsoft.framework.esb.model.Message out = (com.ubsoft.framework.esb.model.Message) in.clone();
			out.setBody("");
			ex.setOut(out);
		}
	}
}
