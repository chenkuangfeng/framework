package com.ubsoft.framework.esb.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubsoft.framework.core.service.impl.BaseService;
import com.ubsoft.framework.esb.entity.EsbLog;
import com.ubsoft.framework.esb.service.IEsbLogService;

@Service("esbLogService")
@Transactional
public class EsbLogService extends BaseService<EsbLog> implements IEsbLogService {

	

	@Override
	public void debug(EsbLog log) {
		log.setLogLevel("DEBUG");
		
		save(log);
	}

	@Override
	public void error(EsbLog log) {
		log.setLogLevel("ERROR");
		save(log);
		
	}

	@Override
	public void info(EsbLog log) {
		log.setLogLevel("INFO");
		save(log);
		
	}
	
	public String getHSQL(String name) {
		if(name.equals("esbJobQuery")){
			String sql=" select new EsbLog (id,createdBy,createdDate,delay,epKey,logLevel,messageId,routeKey)  from  EsbLog ";
			return sql;
		}
		return null;
	};

	
}
