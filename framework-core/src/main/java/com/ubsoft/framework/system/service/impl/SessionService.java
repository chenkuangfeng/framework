package com.ubsoft.framework.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubsoft.framework.core.conf.AppConfig;
import com.ubsoft.framework.core.service.impl.BaseService;
import com.ubsoft.framework.system.cache.MemorySession;
import com.ubsoft.framework.system.entity.Permission;
import com.ubsoft.framework.system.entity.Session;
import com.ubsoft.framework.system.service.ISessionService;

@Service("sessionService")
@Transactional
public class SessionService extends BaseService<Session> implements ISessionService {

	/**
	 * 重全局缓存中获取并更新session
	 * 
	 * @param sessionId
	 * @return
	 */
	private Session getCacheSession(String sessionId) {
		Session session = MemorySession.getInstance().get(sessionId);
		if (session != null) {
			Date lastTime = session.getLastAccessTime();
			// 转换成分钟
			long diff = ((System.currentTimeMillis() - lastTime.getTime()) / 1000 / 60);
			long timeout = Long.parseLong(AppConfig.getDataItem("sessionTimeout"));
			if (diff > timeout) {
				MemorySession.getInstance().remove(sessionId);
				return null;
			} else {
				boolean isGlobalCache = AppConfig.isGlobalCache();
				// 为了提高效率，数据库里面根据当前访问间隔超过10分钟更新一次
				if (isGlobalCache) {
					if ((System.currentTimeMillis() - session.getLastAccessTime().getTime()) / 1000 / 60 >= 10) {
						session.setLastAccessTime(new Date(System.currentTimeMillis()));
						save(session);
					}
				}
				session.setLastAccessTime(new Date(System.currentTimeMillis()));
				MemorySession.getInstance().put(session);

			}
		}
		return session;
	}

	public Session getSession(String sessionId) {
		boolean isGlobalCache = AppConfig.isGlobalCache();
		// session超时时间（分钟）
		long sessionTimout = Long.parseLong(AppConfig.getDataItem("sessionTimeout"));
		Session session = null;
		// 如果是全局共享缓存session，从缓存中取。
		if (isGlobalCache) {
			session = this.getCacheSession(sessionId);
		} else {

			// 考虑分布式多服务器访问，先从访问的服务器缓存中取，如果缓存中没有，从数据库中取，然后再保存在缓存和数据库中。
			session = getCacheSession(sessionId);
			if (session == null) {
				Date minDate = new Date(System.currentTimeMillis() - sessionTimout * 60 * 1000);
				// 不超时的session
				session = this.get("sessionId=? and lastAccessTime>=?", new Object[] { sessionId, minDate });
				if (null != session) {
					session.setLastAccessTime(new Date(System.currentTimeMillis()));
					save(session);
					this.loadPermission(session);
					MemorySession.getInstance().put(session);
				}
			}

		}
		return session;

	}

	/**
	 * 加载所有权限
	 */
	public void loadPermission(Session session) {
		List<Permission> userPermissions = getAllPermission(session.getUserKey());
		Map permKey = new HashMap();
		for (Permission per : userPermissions) {
			permKey.put(per.getPermKey(), per.getPermModule());
		}
		session.setPermissions(permKey);

	}

	public List<Permission> getAllPermission(String userKey) {
		List<Permission> listResult = new ArrayList<Permission>();
		// 查询用户的权限
		String sql = "select distinct T.*  from (";
		sql += "	select P.* from SA_USER_PERMISSION UP join SA_PERMISSION P on UP.PERMKEY=P.PERMKEY where UP.USERKEY=?";
		sql += "	union ";
		sql += "	select P.* from SA_ROLE_PERMISSION  RP join SA_USER_ROLE   R on R.ROLEKEY=RP.ROLEKEY and R.USERKEY=? join SA_PERMISSION P on P.PERMKEY=RP.PERMKEY";
		sql += "	) T";

		List<Permission> listPerm = dataSession.select(sql, new Object[] { userKey, userKey }, Permission.class);
		return listPerm;
		// List<Bio> listPermission = dataSession.queryForBio(sql, new Object[]
		// { userKey, userKey });
		// for (Bio bio : listPermission) {
		// Permission per = new Permission();
		// per.setPermKey(bio.getString("PERMKEY"));
		// per.setPermModule(bio.getString("PERMMODULE"));
		// per.setPermName(bio.getString("PERMNAME"));
		// per.setPermType(bio.getString("PERMTYPE"));
		// listResult.add(per);
		// }
		//
		// return listResult;
	}

	@Override
	public void attachSession(Session session) {
		boolean isGlobalCache = AppConfig.isGlobalCache();
		if (session.getLastAccessTime() == null) {
			session.setLastAccessTime(new Date(System.currentTimeMillis()));
		}
		// 放在缓存中,供subject用
		loadPermission(session);
		MemorySession.getInstance().put(session);
		// //不是全局共享缓存，保存在数据库
		if (!isGlobalCache) {
			this.save(session);
		}
		// 清空所有超时Session
		clearTimeoutSession();

	}

	@Override
	public void killSession(String sessionId) {
		MemorySession.getInstance().remove(sessionId);
		boolean isGlobalCache = AppConfig.isGlobalCache();
		if (!isGlobalCache) {
			this.deleteBio("sessionId=?", new Object[] { sessionId });
		}
	}

	@Override
	public List<Session> getAllSessions() {
		boolean isGlobalCache = AppConfig.isGlobalCache();
		if (isGlobalCache) {
			return MemorySession.getInstance().getAllSessions();
		} else {
			return gets();
		}
	}

	@Override
	public void clearAllSession() {
		boolean isGlobalCache = AppConfig.isGlobalCache();
		MemorySession.getInstance().clear();
		if (!isGlobalCache) {
			dataSession.executeUpdate("delete from SA_Session", new Object[] {});
		}

	}

	@Override
	// 检查超时的Session数据库删除掉
	public void clearTimeoutSession() {
		boolean isGlobalCache = AppConfig.isGlobalCache();
		List<Session> listSession = MemorySession.getInstance().getAllSessions();
		for (Session session : listSession) {
			long diff = ((System.currentTimeMillis() - session.getLastAccessTime().getTime()) / 1000 / 60);
			long timeout = Long.parseLong(AppConfig.getDataItem("sessionTimeout"));
			if (diff > timeout) {
				MemorySession.getInstance().remove(session.getSessionId());
			}
		}
		if (!isGlobalCache) {
			listSession = this.gets();
			for (Session session : listSession) {
				long diff = ((System.currentTimeMillis() - session.getLastAccessTime().getTime()) / 1000 / 60);
				long timeout = Long.parseLong(AppConfig.getDataItem("sessionTimeout"));
				if (diff > timeout) {
					MemorySession.getInstance().remove(session.getSessionId());
					this.dataSession.delete(session);
				}

			}
		}

	}

}
