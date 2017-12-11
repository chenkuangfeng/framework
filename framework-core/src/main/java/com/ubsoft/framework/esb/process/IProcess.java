package com.ubsoft.framework.esb.process;

import com.ubsoft.framework.esb.model.Exchange;

public interface IProcess {

	/**
	 *
	* @Title: process
	* @Description: TODO
	* @author chenkf
	* @date  2017-2-21 下午04:18:46
	* @param ex
	* @throws Exception
	 */
	public void process(Exchange ex) throws Exception;
}
