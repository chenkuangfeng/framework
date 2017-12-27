package com.ubsoft.framework.esb.cache;

import java.util.List;

import com.ubsoft.framework.core.cache.CacheManager;
import com.ubsoft.framework.esb.entity.AppKey;
public class MemoryAppKey {
	private static MemoryAppKey instance;
	private  static String cacheName="APPKEY";
	private CacheManager storeSpace = new CacheManager();
	private MemoryAppKey() {
		
	}
	public static MemoryAppKey getInstance() {
		if (null == instance) {
			synchronized (MemoryAppKey.class) {
				if (null == instance) {
					instance = new MemoryAppKey();
				}
			}
		}
		return instance;
	}

	public AppKey get(String key) {
		Object obj = storeSpace.get(cacheName, key, true);
		if (obj != null) {
			return (AppKey) obj;
		} else {
		  return null;
		}	
	}

	public synchronized void put(String key,AppKey appKey) {
		storeSpace.put(cacheName, key, appKey);
	}

	public synchronized void remove(String key) {
		storeSpace.remove(cacheName, key, true);
	}

	
	public void clear() {
		storeSpace.clear(cacheName, true);
	}
	
	public List<AppKey> getAll(){
		return storeSpace.getAll(cacheName, true);
	}
}
