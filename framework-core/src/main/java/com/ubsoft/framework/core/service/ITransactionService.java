package com.ubsoft.framework.core.service;





public interface ITransactionService{
	
	/**
	 * 从spring中反射执行一个类的一个方法
	 * @param serviceName
	 * @param methodName
	 * @param params
	 * @return
	 */
	public Object execute(String sessionId, String unitName, String serviceName, String methodName, Object[] params) throws RuntimeException;
	/**
	 * 
	 * @param unitName
	 * @param serviceName
	 * @param methodName
	 * @param params
	 * @return
	 */
	public Object execute(String unitName, String serviceName, String methodName, Object[] params);
	/**
	 * 
	 * @param serviceName
	 * @param methodName
	 * @param params
	 * @return
	 */
	public Object execute(String serviceName, String methodName, Object[] params);

	
	
}
