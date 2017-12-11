package com.ubsoft.framework.rpc.server.service;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.dubbo.rpc.RpcContext;
import com.ubsoft.framework.core.conf.AppConfig;
import com.ubsoft.framework.core.context.RequestContext;
import com.ubsoft.framework.core.service.ITransactionService;
import com.ubsoft.framework.rpc.api.ITransactionRemote;

public class TransactionRemote implements ITransactionRemote {

	public Object execute(String sessionId, String unitName, String serviceName, String methodName, Object[] params) {
		ITransactionService service = (ITransactionService) AppConfig.sprintContext.getBean("transactionService");
		return service.execute(sessionId, unitName, serviceName, methodName, params);
	}

	public Object execute(String unitName, String serviceName, String methodName, Object[] params) {
		try {
			Map<String, String> request = new HashMap<String, String>();
			request.put(RequestContext.P_REMOTESERVER, RpcContext.getContext().getRemoteHost());
			RequestContext.setRequest(request);
			ITransactionService service = (ITransactionService) AppConfig.sprintContext.getBean("transactionService");
			return service.execute(unitName, serviceName, methodName, params);
		} finally {
			RequestContext.remove();
		}
	}

}
