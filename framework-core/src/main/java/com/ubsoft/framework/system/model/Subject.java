package com.ubsoft.framework.system.model;

import java.io.Serializable;

import com.ubsoft.framework.system.cache.MemoryPermission;
import com.ubsoft.framework.system.entity.Session;

public class Subject implements Serializable {

	private static ThreadLocal<Subject> context = new ThreadLocal<Subject>();
	
	public Subject(Session session) {
		this.session = session;
	}

	public static Subject getSubject() {
		return (Subject) context.get();
	}

	public static void removeSubject() {
		context.remove();
	}

	public static void setSubject(Subject subject) {
		context.set(subject);
	}

	private Session session;

	public void setSession(Session session) {
		this.session = session;
	}

	public Session getSession() {
		return session;
	}
	public String getUserKey() {
		return session.getUserKey();
	}

	public boolean isPermitted(String permission) {
		// 只有在权限表里面的才管理权限
		//判断是否在权限表里面，服务名和方法放在value里面，permKey放在key里面
		if (MemoryPermission.getInstance().get(permission) == null || MemoryPermission.getInstance().containsValue(permission)) {
			return true;
		}
		return session.isPermitted(permission);
	}

	public boolean hasRole(String role) {
		return session.hasRole("role");
	}

	public String getAttribute(String key) {
		return session.getAttribute(key);
	}
}
