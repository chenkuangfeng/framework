package com.ubsoft.framework.rpc.proxy;

import java.lang.reflect.Proxy;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ubsoft.framework.core.exception.ComException;
import com.ubsoft.framework.rpc.api.ITransactionRemote;

public class RpcProxy {

	private static ClassPathXmlApplicationContext context;
 
	/**
	 * 连接并注册到服务器
	 */
	public static synchronized void connect(){
		if(context==null){
			context = new ClassPathXmlApplicationContext(new String[] { "conf/client-context.xml" });
			context.start();
		}
		context.getBean("transactionRemote");
	}
	public static ITransactionRemote getRemote() {
		try {
			
			if (context == null) {
				connect();
			}
			ITransactionRemote remote = (ITransactionRemote) context.getBean("transactionRemote");
			
			return remote;
		} catch (Exception e) {
			context=null;
			throw new ComException(ComException.MIN_ERROR_CODE_GATEWAY, e);
		}

	}
	
	public static ITransactionRemote getRmiRemote() {
		try {
			if (context == null) {
				connect();

			}
			ITransactionRemote remote = (ITransactionRemote) context.getBean("transactionRmiRemote");
			//IFileRemote file = (IFileRemote) context.getBean("fileRemote");
			
			return remote;
		} catch (Exception e) {
			throw new ComException(ComException.MIN_ERROR_CODE_GATEWAY, e);
		}

	}

	public static <T> T getProxy(Class<T> clazz) {
		return ((T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, new RpcStub()));
	}

}
