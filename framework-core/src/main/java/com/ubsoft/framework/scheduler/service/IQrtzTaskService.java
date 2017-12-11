package com.ubsoft.framework.scheduler.service;

import com.ubsoft.framework.scheduler.model.JobDetailModel;
/**
 * 
* @ClassName: IQrtzTaskService
* @Description: qutz job实现类
* @author chenkf
* @date 2017-2-22 下午01:37:32
* @version V1.0
 */
public interface IQrtzTaskService {
	/**
	 *
	* @Title: addJob
	* @Description: 新增job
	* @author chenkf
	* @date  2017-2-22 上午09:10:44
	* @param jobDetail
	 */
	void addJob(JobDetailModel jobDetail);
	/**	 
	* @Title: removeJob
	* @Description: 删除Job
	* @author chenkf
	* @date  2017-2-22 上午09:11:09
	* @param jobDetail
	*/ 
	void removeJob(JobDetailModel jobDetail);
	/**
	 * 
	* @Title: resumeJob
	* @Description: 重新启动JOB
	* @author chenkf
	* @date  2017-2-22 上午09:22:06
	* @param jobDetail
	 */
	void resumeJob(JobDetailModel jobDetail);
	
	/**
	 * 
	* @Title: pauseJob
	* @Description: 暂停JOB
	* @author chenkf
	* @date  2017-2-22 上午09:24:19
	* @param jobDetail
	 */
	void pauseJob(JobDetailModel jobDetail);
	
	/**
	 * 
	* @Title: startJob
	* @Description: 启动JOB
	* @author chenkf
	* @date  2017-2-22 上午09:25:15
	* @param jobDetail
	 */
	void startJob(JobDetailModel jobDetail);
}
