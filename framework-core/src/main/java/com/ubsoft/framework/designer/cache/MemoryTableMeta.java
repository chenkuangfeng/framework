package com.ubsoft.framework.designer.cache;

import java.util.List;

import com.ubsoft.framework.core.cache.CacheManager;
import com.ubsoft.framework.designer.model.DbTableMeta;

public class MemoryTableMeta {
	private static MemoryTableMeta instance;
	private  static String cacheName="TABLEMETA";
	private CacheManager storeSpace = new CacheManager();
	private MemoryTableMeta() {

	}
	public static MemoryTableMeta getInstance() {
		if (null == instance) {
			synchronized (MemoryTableMeta.class) {
				if (null == instance) {
					instance = new MemoryTableMeta();
				}
			}
		}
		return instance;
	}

	public DbTableMeta get(String key) {
		Object obj = storeSpace.get(cacheName, key, true);
		if (obj != null) {
			return (DbTableMeta) obj;
		} else {
		  return null;
		}	
	}

	public synchronized void put(String key,DbTableMeta tableMeta) {
		storeSpace.put(cacheName, key, tableMeta);
	}

	public synchronized void remove(String key) {
		storeSpace.remove(cacheName, key, true);
	}

	
	public List<DbTableMeta> getAllTableMeta() {
		return storeSpace.getAll(cacheName, true);
	
	}
	
	public void clear() {
		storeSpace.clear(cacheName, true);
	}
}
