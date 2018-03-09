package com.ubsoft.framework.esb.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ubsoft.framework.core.service.ITransactionService;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.esb.cache.MemoryAppKey;
import com.ubsoft.framework.esb.cache.MemoryEndpoint;
import com.ubsoft.framework.esb.entity.AppKey;
import com.ubsoft.framework.esb.entity.Endpoint;
import com.ubsoft.framework.esb.entity.EsbLog;
import com.ubsoft.framework.esb.entity.Route;
import com.ubsoft.framework.esb.model.Exchange;
import com.ubsoft.framework.esb.model.Message;
import com.ubsoft.framework.esb.service.IAppKeyService;
import com.ubsoft.framework.esb.service.IEndpointService;
import com.ubsoft.framework.esb.service.IEsbEngine;
import com.ubsoft.framework.esb.service.IEsbLogService;
import com.ubsoft.framework.esb.service.IRouteService;

@Service("esbEngine")
public class EsbEngine  implements IEsbEngine {
	@Autowired
	ITransactionService tsService;
	@Autowired
	IEndpointService epService;
	@Autowired
	IRouteService routeService;
	@Autowired
	IEsbLogService logService;	
	@Autowired
	IAppKeyService appKeyService;
	@Override
	/**
	 * 加载接口和route并初始化服务，容器启动时候执行。
	 */
	public void initEngine() {
		List<Endpoint> listEP = epService.gets("status","1"); 		
		//缓存接口
		for (Endpoint ep : listEP) {
			List<Route> listRoute = routeService.gets(new String[]{"epId","status"} , new Object[] { ep.getId(),"1" },"seq");
			ep.setRoutes(listRoute);
			MemoryEndpoint.getInstance().put(ep.getEpKey(), ep);			
		}
		//缓存APPKEY
		List<AppKey> listAK=appKeyService.gets("status", "1");
		for(AppKey ak : listAK){
			MemoryAppKey.getInstance().put(ak.getAppKey(), ak);
		}
		
	}
	@Override
	public void process(Endpoint ep, Exchange eg) throws Exception {
		for (Route rp : ep.getRoutes()) {
			long start = System.currentTimeMillis();
			try {
				if (rp.getRouteType().equals(IEsbEngine.ROUTE_BEAN)) {
					String bean = rp.getRouteModule();
					String unitName = null;
					String beanName = null;
					if (bean.indexOf(":") != -1) {
						unitName = bean.split(":")[1];
						beanName = bean.split(":")[0];
						tsService.execute(beanName, IEsbEngine.BEAN_METHOD, new Object[] { eg });
					} else {
						beanName=bean;
						tsService.execute(beanName, IEsbEngine.BEAN_METHOD, new Object[] { eg });
					}

				} else if (rp.getRouteType().equals(IEsbEngine.ROUTE_MQ_FROM)) {
					// 设置为从队列中取
					eg.getIn().setHeader("MQDIR", IEsbEngine.ROUTE_MQ_FROM);// MQ发送或者接收标记
					eg.getIn().setHeader("QUEUE", rp.getRouteModule());// MQ队列
					tsService.execute("mqProcess", IEsbEngine.BEAN_METHOD, new Object[] { eg });
				} else if (rp.getRouteType().equals(IEsbEngine.ROUTE_MQ_TO)) {
					// 设置为发送到队列
					eg.getIn().setHeader("MQDIR", IEsbEngine.ROUTE_MQ_TO);
					eg.getIn().setHeader("QUEUE", rp.getRouteModule());// MQ队列
					tsService.execute("mqProcess", IEsbEngine.BEAN_METHOD, new Object[] { eg });
				} else if (rp.getRouteType().equals(IEsbEngine.ROUTE_HTTP)) {// http调用，webService也可以通过此协议调用
					eg.getIn().setHeader("URL", rp.getRouteModule());
					tsService.execute("httpProcess", IEsbEngine.BEAN_METHOD, new Object[] { eg });
				}else if(rp.getRouteType().equals(IEsbEngine.ROUTE_MSG)){
					String module = rp.getRouteModule();
					String beanName = null;
					String type = null;
					if (module.indexOf(":") != -1) {
						type = module.split(":")[0];
						beanName = module.split(":")[1];
						eg.getIn().setHeader("TYPE", type);
						eg.getIn().setHeader("BEANNAME", beanName);
					}else{
						eg.getIn().setHeader("TYPE", module);
					}
					tsService.execute("msgConvertProcess", IEsbEngine.BEAN_METHOD, new Object[] { eg });					
				}
				long time = System.currentTimeMillis() - start;
				int delay = (int) time / 1000;// 单节点用时
				// 记录日志信息
				EsbLog log = this.getLogger(eg, rp);
				log.setDelay(delay);// 用时
				// 内部异常，从自定义process里面设置传递过来。
				if (eg.getOut().isFault()) {
					logService.error(log);
					break;// 退出循环
				} else {
					if (StringUtil.isTrue(rp.getLogable())) {
						logService.info(log);
					}
				}
				// 把本路由的out设置成下一路由的in。
				eg.setIn(eg.getOut());

			} catch (Exception e) {
				long time = System.currentTimeMillis() - start;
				int delay = (int) time / 1000;// 单节点用时
				// 记录日志信息
				if (eg.getOut() == null) {
					Message out=new Message();
					
					out.setBody(e.getMessage());
					eg.setOut(out);
				}
				EsbLog log = this.getLogger(eg, rp);
				log.setDelay(delay);// 用时
				logService.error(log);
				throw new Exception(e.getMessage());//抛出异常退出
			}
		}
	}

	private EsbLog getLogger(Exchange exchange, Route route) {
		Message in = exchange.getIn();
		Message out = exchange.getOut();
		Endpoint ep = exchange.getEndpoint();
		EsbLog log = new EsbLog();
		log.setEpKey(ep.getEpKey());
		log.setRouteKey(route.getRouteKey());
		log.setMessageId(in.getMessageId());
		log.setMsgIn(in.getBody() + "");
		if(out!=null){
			log.setMsgOut(out.getBody() + "");
		}
		
		return log;
	}

}
