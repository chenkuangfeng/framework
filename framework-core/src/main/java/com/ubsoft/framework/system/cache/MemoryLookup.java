package com.ubsoft.framework.system.cache;

import java.util.List;

import com.ubsoft.framework.core.cache.CacheManager;
import com.ubsoft.framework.system.entity.LookupDetail;

public class MemoryLookup {
	private static MemoryLookup instance;

	private  static String cacheName="LOOKUP";
	private CacheManager storeSpace = new CacheManager();

	private MemoryLookup() {

	}

	public static MemoryLookup getInstance() {
		if (null == instance) {
			synchronized (MemoryLookup.class) {
				if (null == instance) {
					instance = new MemoryLookup();
				}
			}
		}
		return instance;
	}

	public List<LookupDetail> get(String lkKey) {
		Object obj = storeSpace.get(cacheName, lkKey, true);
		if (obj != null) {
			return (List<LookupDetail>) obj;
		} else {
		  return null;
		}	
	}

	public synchronized void put(String lkKey,List<LookupDetail> lkup) {
		storeSpace.put(cacheName, lkKey, lkup);
	}

	public synchronized void remove(String lkKey) {
		storeSpace.remove(cacheName, lkKey, true);
	}

	

	public void clear() {
		storeSpace.clear(cacheName, true);
	}
	
	public List getAll(){
		return storeSpace.getAll(cacheName, true);
	}
}
