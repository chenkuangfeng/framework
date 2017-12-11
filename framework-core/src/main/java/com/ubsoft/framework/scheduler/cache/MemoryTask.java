package com.ubsoft.framework.scheduler.cache;

import java.util.List;

import com.ubsoft.framework.core.cache.CacheManager;
import com.ubsoft.framework.scheduler.entity.Task;

public class MemoryTask {
	private static MemoryTask instance;

	private static String cacheName = "task";
	private CacheManager storeSpace = new CacheManager();

	private MemoryTask() {

	}

	public static MemoryTask getInstance() {
		if (null == instance) {
			synchronized (MemoryTask.class) {
				if (null == instance) {
					instance = new MemoryTask();
				}
			}
		}
		return instance;
	}

	public Task get(String taskKey) {
		Object obj = null;
		try {
			//job执行的时候没有初始化错误
			obj = storeSpace.get(cacheName, taskKey, true);
		} catch (Exception ex) {
			return null;
		}
		if (obj != null) {
			return (Task) obj;
		} else {
			return null;
		}
	}

	public synchronized void put(String taskKey, Task task) {
		storeSpace.put(cacheName, taskKey, task);
	}

	public synchronized void remove(String taskKey) {
		storeSpace.remove(cacheName, taskKey, true);
	}

	public void clear() {
		storeSpace.clear(cacheName, true);
	}

	public List getAll() {
		return storeSpace.getAll(cacheName, true);
	}
}
