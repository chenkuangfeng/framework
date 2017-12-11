package com.ubsoft.framework.system.cache;

import com.ubsoft.framework.core.cache.CacheManager;


public class MemoryPermission {
	private static MemoryPermission instance;

	private static String cacheName = "permission";
	private CacheManager storeSpace = new CacheManager();

	private MemoryPermission() {

	}

	public static MemoryPermission getInstance() {
		if (null == instance) {
			synchronized (MemoryPermission.class) {
				if (null == instance) {
					instance = new MemoryPermission();
				}
			}
		}
		return instance;
	}

	public String get(String permKey) {
		Object obj = storeSpace.get(cacheName, permKey, false);
		if (obj != null) {
			return (String) obj;
		} else {
			return null;
		}
	}

	public synchronized void put(String permKey, String permModule) {
		storeSpace.put(cacheName, permKey, permModule);
	}

	public synchronized void remove(String permKey) {
		storeSpace.remove(cacheName, permKey, false);
	}

	public void clear() {
		storeSpace.clear(cacheName, false);
	}

	public boolean containsValue(Object value) {
		return storeSpace.containsValue(cacheName, value);
	}
}
