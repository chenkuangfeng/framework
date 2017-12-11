package com.ubsoft.framework.esb.process.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MessageReceiver implements MessageListener {
	@Override
	public void onMessage(Message msg) {		
		if (msg instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) msg;
			try {
				System.out.println("接收到发送到responseQueue的一个文本消息，内容是：" + textMessage.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}
