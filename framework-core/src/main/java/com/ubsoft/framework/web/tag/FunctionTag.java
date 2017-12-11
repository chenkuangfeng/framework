package com.ubsoft.framework.web.tag;

import com.ubsoft.framework.core.conf.AppConfig;
import com.ubsoft.framework.core.context.AppContext;
import com.ubsoft.framework.core.service.ITransactionService;

public class FunctionTag {

	public static Object invoke(String unitName,String serviceName,String methodName,Object[] params){
		ITransactionService ts= (ITransactionService)AppContext.getBean("transactionService");
		return ts.execute(unitName,serviceName, methodName, params);
	}
	
	public static String getConfig(String key){
		return AppConfig.getDataItem(key);
	}
	
}
