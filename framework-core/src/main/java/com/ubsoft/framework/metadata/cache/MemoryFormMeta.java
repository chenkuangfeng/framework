package com.ubsoft.framework.metadata.cache;

import com.ubsoft.framework.core.cache.CacheManager;
import com.ubsoft.framework.metadata.model.form.FormMeta;
public class MemoryFormMeta {
	private static MemoryFormMeta instance;

	private  static String cacheName="FORMMETA";
	private CacheManager storeSpace = new CacheManager();

	private MemoryFormMeta() {

	}
	
	public static MemoryFormMeta getInstance() {
		if (null == instance) {
			synchronized (MemoryFormMeta.class) {
				if (null == instance) {
					instance = new MemoryFormMeta();
				}
			}
		}
		return instance;
	}

	public FormMeta get(String formId) {
		Object obj = storeSpace.get(cacheName, formId, false);
		if (obj != null) {
			return  (FormMeta)obj;
		} else {
		  return null;
		}	
	}

	public synchronized void put(String formId,FormMeta meta) {
		storeSpace.put(cacheName, formId, meta);
	}

	public synchronized void remove(String formId) {
		storeSpace.remove(cacheName, formId, false);
	}
	public void clear() {
		storeSpace.clear(cacheName, false);
	}
}
