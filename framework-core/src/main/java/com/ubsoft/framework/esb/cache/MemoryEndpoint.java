package com.ubsoft.framework.esb.cache;

import java.util.List;

import com.ubsoft.framework.core.cache.CacheManager;
import com.ubsoft.framework.esb.entity.Endpoint;
public class MemoryEndpoint {
	private static MemoryEndpoint instance;
	private  static String cacheName="endpoint";
	private CacheManager storeSpace = new CacheManager();	
	private MemoryEndpoint() {

	}	
	public static MemoryEndpoint getInstance() {
		if (null == instance) {
			synchronized (MemoryEndpoint.class) {
				if (null == instance) {
					instance = new MemoryEndpoint();
				}
			}
		}
		return instance;
	}
	
	public Endpoint get(String key) {
		Object obj = storeSpace.get(cacheName, key, true);
		if (obj != null) {
			return (Endpoint) obj;
		} else {
		  return null;
		}	
	}

	public synchronized void put(String key,Endpoint ep) {
		storeSpace.put(cacheName, key, ep);
	}

	public synchronized void remove(String key) {
		storeSpace.remove(cacheName, key, true);
	}

	
	public void clear() {
		storeSpace.clear(cacheName, true);
	}
	
	public List<Endpoint> getAll(){
		return storeSpace.getAll(cacheName, true);
	}
}
