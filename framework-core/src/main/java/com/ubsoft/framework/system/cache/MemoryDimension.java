package com.ubsoft.framework.system.cache;

import java.util.List;

import com.ubsoft.framework.core.cache.CacheManager;
import com.ubsoft.framework.core.dal.model.Bio;

public class MemoryDimension {
	private static MemoryDimension instance;

	private  static String cacheName="dimension";
	private CacheManager storeSpace = new CacheManager();

	private MemoryDimension() {

	}

	public static MemoryDimension getInstance() {
		if (null == instance) {
			synchronized (MemoryDimension.class) {
				if (null == instance) {
					instance = new MemoryDimension();
				}
			}
		}
		return instance;
	}

	public List<Bio> get(String key) {
		Object obj = storeSpace.get(cacheName, key, true);
		if (obj != null) {
			return (List<Bio>) obj;
		} else {
		  return null;
		}	
	}

	public synchronized void put(String key,List<Bio> list) {
		storeSpace.put(cacheName, key, list);
	}

	public synchronized void remove(String key) {
		storeSpace.remove(cacheName, key, true);
	}

	
	public void clear() {
		storeSpace.clear(cacheName, true);
	}
}
