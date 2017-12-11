package com.ubsoft.framework.core.cache;

import java.util.List;

import com.ubsoft.framework.core.dal.entity.BioMeta;

public class MemoryBioMeta {
	private static MemoryBioMeta instance;
	private  static String cacheName="biometa";
	private CacheManager storeSpace = new CacheManager();
	private MemoryBioMeta() {

	}
	public static MemoryBioMeta getInstance() {
		if (null == instance) {
			synchronized (MemoryBioMeta.class) {
				if (null == instance) {
					instance = new MemoryBioMeta();
				}
			}
		}
		return instance;
	}

	public BioMeta get(String key) {
		Object obj = storeSpace.get(cacheName, key, true);
		if (obj != null) {
			return (BioMeta) obj;
		} else {
		  return null;
		}	
	}

	public synchronized void put(String key,BioMeta bioMeta) {
		storeSpace.put(cacheName, key, bioMeta);
	}

	public synchronized void remove(String key) {
		storeSpace.remove(cacheName, key, true);
	}

	
	public List<BioMeta> getAllBioMeta() {
		return storeSpace.getAll(cacheName, true);
	
	}
	
	public void clear() {
		storeSpace.clear(cacheName, true);
	}
}
