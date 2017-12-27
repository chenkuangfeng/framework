package com.ubsoft.framework.bi.cache;

import java.util.List;

import com.ubsoft.framework.bi.entity.Report;
import com.ubsoft.framework.core.cache.CacheManager;

public class MemoryReport {
	private static MemoryReport instance;

	private  static String cacheName="REPORT";
	private CacheManager storeSpace = new CacheManager();

	private MemoryReport() {

	}

	public static MemoryReport getInstance() {
		if (null == instance) {
			synchronized (MemoryReport.class) {
				if (null == instance) {
					instance = new MemoryReport();
				}
			}
		}
		return instance;
	}

	public Report get(String reportKey) {
		Object obj = storeSpace.get(cacheName, reportKey, true);
		if (obj != null) {
			return (Report) obj;
		} else {
		  return null;
		}	
	}

	public synchronized void put(Report report) {
		storeSpace.put(cacheName, report.getReportKey(), report);
	}

	public synchronized void remove(String reportKey) {
		storeSpace.remove(cacheName, reportKey, true);
	}

	
	public List<Report> getAllSessions() {

		return storeSpace.getAll(cacheName, true);
	
	}


	public void clear() {
		storeSpace.clear(cacheName, true);
	}
}
