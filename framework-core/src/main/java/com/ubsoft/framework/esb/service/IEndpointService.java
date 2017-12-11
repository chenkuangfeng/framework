package com.ubsoft.framework.esb.service;

import com.ubsoft.framework.core.service.IBaseService;
import com.ubsoft.framework.esb.entity.Endpoint;
/**
 * 
* @ClassName: IEndpointService
* @Description: 接口服务操作类，初始化接口信息，启动和停用接口
* @author chenkf
* @date 2017-2-22 下午05:03:18
* @version V1.0
 */
public interface IEndpointService extends IBaseService<Endpoint>{
//	/**
//	 * 
//	* @Title: saveEndpoint
//	* @Description: 保存，如果类型是job类型就添加到调度中，后台自动运行。
//	* @author chenkf
//	* @date  2017-2-22 下午04:57:39
//	* @param ep
//	* @return
//	 */
//	Endpoint saveEndpoint(Endpoint ep);
//	
	/**
	 * 
	* @Title: removeEndpoints
	* @Description: 删除，如果类型是job类型，就删除调度任务，
	* @author chenkf
	* @date  2017-2-22 下午04:58:31
	* @param epIds
	 */
	//void removeEndpoints(String [] epIds);
	/**
	 * 
	* @Title: resumeEndpoints
	* @Description: 重新启动调度任务
	* @author chenkf
	* @date  2017-2-22 下午04:59:49
	* @param epIds
	 */
//	void resumeEndpoints(String [] epIds);
	
	/**
	 * 
	* @Title: pauseEndpoints
	* @Description: 停用接口
	* @author chenkf
	* @date  2017-2-22 下午05:00:19
	* @param epIds
	 */
//	void pauseEndpoints(String [] epIds);
	
	/**
	 * 
	* @Title: startEndpoints
	* @Description: 启动接口
	* @author chenkf
	* @date  2017-2-22 下午05:00:39
	* @param epIds
	 */
//	void startEndpoints(String [] epIds);
	
	
}
