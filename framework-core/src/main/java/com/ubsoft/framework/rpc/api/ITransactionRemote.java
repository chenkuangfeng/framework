package com.ubsoft.framework.rpc.api;

import com.ubsoft.framework.core.exception.ComException;

public interface ITransactionRemote {

    Object execute(String sessionId, String unitName, String serviceName, String methodName, Object[] params) throws ComException;

    Object execute(String unitName, String serviceName, String methodName, Object[] params) throws ComException;

}
