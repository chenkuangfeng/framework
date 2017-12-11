package com.ubsoft.framework.system.cache;

import java.util.List;

import com.ubsoft.framework.core.cache.CacheManager;
import com.ubsoft.framework.system.entity.Session;

public class MemorySession {
	private static MemorySession instance;

	private  static String cacheName="session";
	private CacheManager storeSpace = new CacheManager();

	private MemorySession() {

	}

	public static MemorySession getInstance() {
		if (null == instance) {
			synchronized (MemorySession.class) {
				if (null == instance) {
					instance = new MemorySession();
				}
			}
		}
		return instance;
	}

	public Session get(String sessionId) {
		Object obj = storeSpace.get(cacheName, sessionId, true);
		if (obj != null) {
			return (Session) obj;
		} else {
		  return null;
		}	
	}

	public synchronized void put(Session session) {
		storeSpace.put(cacheName, session.getSessionId(), session);
	}

	public synchronized void remove(String sessionId) {
		storeSpace.remove(cacheName, sessionId, true);
	}

	public List<Session> getAllSessions() {

		return storeSpace.getAll(cacheName, true);
	
	}

	public void clear() {
		storeSpace.clear(cacheName, true);
	}
}
