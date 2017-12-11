package com.ubsoft.framework.core.dal.util;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.ubsoft.framework.core.context.AppContext;
import com.ubsoft.framework.core.exception.ComException;

public class NewTransactionTemplate extends TransactionTemplate{
	public NewTransactionTemplate()  {
	
		setTransactionManager((PlatformTransactionManager) AppContext.getBeanOfType(PlatformTransactionManager.class));
		setPropagationBehavior(3);
	}
	public Object execute(String dataSourceName, TransactionCallback action)
			throws TransactionException {
		try {
			DynamicDataSource.setDataSource(dataSourceName);
			Object obj = super.execute(action);
			return obj;
		} catch(Exception e){
			throw new ComException(1,e.getMessage());
		}finally {
			DynamicDataSource.removeDataSource();
		}
		
	}

}
