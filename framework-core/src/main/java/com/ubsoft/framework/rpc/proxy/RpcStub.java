package com.ubsoft.framework.rpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.ubsoft.framework.rpc.api.ITransactionRemote;
import com.ubsoft.framework.rpc.model.SessionContext;

public class RpcStub implements InvocationHandler {
	public Object invoke(Object object, Method method, Object[] param) throws Throwable {
		ITransactionRemote remote = RpcProxy.getRemote();
		// 获取接口
		Class[] interfaces = object.getClass().getInterfaces();
		String interfaceName = interfaces[0].getSimpleName();
		String methodName = method.getName();
		// 根据接口获取服务名
		String serviceName = interfaceName.substring(1, 2).toLowerCase() + interfaceName.substring(2);
		Object result = null;
	//	result = remote.execute("DefaultDS", serviceName, method.getName(), param);
		if (methodName.equals("login")) {
			result = remote.execute("DefaultDS", serviceName, method.getName(), param);
		} else {
			String sessionId = SessionContext.getContext().getSessionId();
			String unitName = SessionContext.getContext().getUnitName();
			if (unitName == null) {
				unitName = "DefaultDS";
			}
			result =remote.execute(sessionId, unitName, serviceName, method.getName(), param);
		}
		return result;

	}

}
