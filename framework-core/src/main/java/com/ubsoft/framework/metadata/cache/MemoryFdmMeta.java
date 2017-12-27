package com.ubsoft.framework.metadata.cache;

import com.ubsoft.framework.core.cache.CacheManager;
import com.ubsoft.framework.metadata.model.form.fdm.FdmMeta;
public class MemoryFdmMeta {
	private static MemoryFdmMeta instance;

	private  static String cacheName="FDMMETA";
	private CacheManager storeSpace = new CacheManager();

	private MemoryFdmMeta() {

	}
	
	public static MemoryFdmMeta getInstance() {
		if (null == instance) {
			synchronized (MemoryFdmMeta.class) {
				if (null == instance) {
					instance = new MemoryFdmMeta();
				}
			}
		}
		return instance;
	}

	public FdmMeta get(String fdmId) {
		Object obj = storeSpace.get(cacheName, fdmId, false);
		if (obj != null) {
			return  (FdmMeta)obj;
		} else {
		  return null;
		}	
	}
	
	public synchronized void put(String fdmId,FdmMeta meta) {
		storeSpace.put(cacheName, fdmId, meta);
	}

	public synchronized void remove(String fdmId) {
		storeSpace.remove(cacheName, fdmId, false);
	}
	public void clear() {
		storeSpace.clear(cacheName, false);
	}
}
