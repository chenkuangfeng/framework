package com.ubsoft.framework.esb.service;

import javax.jws.WebService;

/**
 * ws webservice
 * @author chenkf
 *
 */
@WebService
public interface IWsService{
	/**
	 * 所有webservice 入口
	 * @param request
	 * @return
	 */
	public String process(String request);
}
