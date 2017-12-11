package com.ubsoft.framework.system.service;

import java.util.List;

import com.ubsoft.framework.core.service.IBaseService;
import com.ubsoft.framework.system.entity.Session;

public interface ISessionService extends IBaseService<Session> {

	Session getSession(String id);

	void attachSession(Session session);

	void killSession(String id);

	List<Session> getAllSessions();

	void clearAllSession();

	void clearTimeoutSession();

	void loadPermission(Session session);

}
