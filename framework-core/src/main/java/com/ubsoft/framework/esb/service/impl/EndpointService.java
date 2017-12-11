package com.ubsoft.framework.esb.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubsoft.framework.core.service.impl.BaseService;
import com.ubsoft.framework.esb.cache.MemoryEndpoint;
import com.ubsoft.framework.esb.entity.Endpoint;
import com.ubsoft.framework.esb.service.IEndpointService;

@Service("endpointService")
@Transactional
public class EndpointService extends BaseService<Endpoint> implements IEndpointService {
//	@Autowired
//	IQrtzTaskService qrtzTaskService;
//
//	@Override
//	public void pauseEndpoints(String[] epIds) {
//		for (String id : epIds) {
//			Endpoint ep = get(id);
//			JobDetailModel jobDetail = getJobDetail(ep);
//			qrtzTaskService.pauseJob(jobDetail);
//			ep.setStatus("0");
//			save(ep);// 更新状态
//
//		}
//
//	}
//
//	@Override
//	public void removeEndpoints(String[] epIds) {
//		for (String id : epIds) {
//			Endpoint ep = get(id);
//			if (ep.getEpType().equals("JOB")) {
//				JobDetailModel jobDetail = getJobDetail(ep);
//				qrtzTaskService.removeJob(jobDetail);
//			}
//			delete(id);
//			MemoryEndpoint.getInstance().remove(ep.getEpKey());
//
//		}
//
//	}
//
//	@Override
//	public void resumeEndpoints(String[] epIds) {
//		for (String id : epIds) {
//			Endpoint ep = get(id);
//			if (ep.getEpType().equals("JOB")) {
//				JobDetailModel jobDetail = getJobDetail(ep);
//				qrtzTaskService.resumeJob(jobDetail);
//			}
//		}
//
//	}

//	@Override
//	public Endpoint saveEndpoint(Endpoint ep) {
//		ep.setStatus("0");// 默认社会不启用
//		this.save(ep);
//		// 放入缓存，供GeneralJob 用
//		MemoryEndpoint.getInstance().put(ep.getEpKey(), ep);
//		return ep;
//	}

//	@Override
//	public void startEndpoints(String[] epIds) {
//		for (String id : epIds) {
//			Endpoint ep = get(id);
//			if (ep.getEpType().equals("JOB")) {
//				JobDetailModel jobDetail = getJobDetail(ep);
//				qrtzTaskService.startJob(jobDetail);
//			}
//			ep.setStatus("1");
//			save(ep);
//			MemoryEndpoint.getInstance().put(ep.getEpKey(), ep);
//
//		}
//
//	}
//
//	private JobDetailModel getJobDetail(Endpoint ep) {
//		JobDetailModel jobDetail = new JobDetailModel();
//		jobDetail.setJobName(ep.getEpKey());
//		jobDetail.setDescription(ep.getEpName());
//		jobDetail.setCronExpression(ep.getCron());
//		jobDetail.setJobClassName("com.framework.esb.job.EsbJob");
//		jobDetail.setJobGroup("EsbJob");
//		jobDetail.setStartTime(ep.getStartTime());
//		jobDetail.setEndTime(ep.getEndTime());
//		return jobDetail;
//	}

}
