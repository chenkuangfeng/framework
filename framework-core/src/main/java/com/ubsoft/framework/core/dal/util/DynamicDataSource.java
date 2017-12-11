package com.ubsoft.framework.core.dal.util;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

	private static ThreadLocal<String> context = new ThreadLocal<String>();

	// public static String
	// defaultDS=StringUtil.isEmpty(AppConfig.getDataItem("defaulstDS"))?"DefaultDS":AppConfig.getDataItem("defaulstDS");
	// public static String
	// enterpriseDS=StringUtil.isEmpty(AppConfig.getDataItem("enterpriseDS"))?"EnterpriseDS":AppConfig.getDataItem("enterpriseDS");
	@Override
	protected Object determineCurrentLookupKey() {
		return getDataSource();
	}

	public static void setDataSource(String dataSource) {
		context.remove();
		context.set(dataSource);
	}

	public static String getDataSource() {
		String data = context.get();		
		return data;
	}

	public static void removeDataSource() {
		context.remove();
	}

}
