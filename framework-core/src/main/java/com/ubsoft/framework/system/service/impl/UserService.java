package com.ubsoft.framework.system.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubsoft.framework.core.conf.AppConfig;
import com.ubsoft.framework.core.context.RequestContext;
import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.exception.UserException;
import com.ubsoft.framework.core.service.impl.BaseService;
import com.ubsoft.framework.web.context.WebContext;
import com.ubsoft.framework.core.support.util.PasswordUtil;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.system.entity.Dimension;
import com.ubsoft.framework.system.entity.Org;
import com.ubsoft.framework.system.entity.Permission;
import com.ubsoft.framework.system.entity.Role;
import com.ubsoft.framework.system.entity.Session;
import com.ubsoft.framework.system.entity.User;
import com.ubsoft.framework.system.entity.UserDimension;
import com.ubsoft.framework.system.entity.UserPermission;
import com.ubsoft.framework.system.model.Subject;
import com.ubsoft.framework.system.service.IOrgService;
import com.ubsoft.framework.system.service.ISessionService;
import com.ubsoft.framework.system.service.IUserService;

@Service("userService")
@Transactional
public class UserService extends BaseService<User> implements IUserService {

	@Autowired
	ISessionService sessionService;

	@Autowired
	IOrgService orgService;

	@Override
	public Session login(String userKey, String pwd) {

		User user = this.get("userKey", userKey);
		if (user == null) {
			throw new UserException(10, "用户【" + userKey + "】不存在。");
		} else {
			if (user.getStatus() == null || !user.getStatus().equals("1")) {
				throw new UserException(10, "用户【" + userKey + "】未激活。");

			}
		}

		String uPwd = user.getPwd();
		pwd = PasswordUtil.string2MD5(pwd);
		if (!uPwd.equals(pwd)) {
			throw new UserException(20, "用户【" + userKey + "】密码不正确。");
		}

		Session session = new Session();
		//session.setId(UUID.randomUUID().toString().replace("-", ""));
		session.setSessionId(UUID.randomUUID().toString());
		session.setUserKey(userKey);
		session.setUserName(user.getUserName());

		Org org = orgService.get("orgKey", user.getOrgKey());
		if (org != null) {
			session.setOrgKey(org.getOrgKey());
			session.setOrgName(org.getOrgName());
			session.setOrgId(org.getId());
		}
		session.setStartTimestamp(new Date());
		// 过期间隔默认为分钟
		session.setTimeout(Long.parseLong(AppConfig.getDataItem("sessionTimeout")));
		if (WebContext.getRequest() != null) {
			if (WebContext.getRequest().getHeader("x-forwarded-for") == null) {
				session.setClienthost(WebContext.getRequest().getRemoteAddr());
			} else {
				session.setClienthost(WebContext.getRequest().getHeader("x-forwarded-for"));
			}
		}
		// rpc端登陆
		if (session.getClienthost() == null) {
			if (RequestContext.getRequest() != null) {
				session.setClienthost(RequestContext.getRequestItem(RequestContext.P_REMOTESERVER));
			}
		}

		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		session.setServerHost(addr.getHostName() + "[" + addr.getAddress() + "]");
		sessionService.attachSession(session);

		return session;

	}

	@Override
	public boolean loginout(String sessionId) {
		try {
			sessionService.killSession(sessionId);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public String changePassword(String userKey, String password) {
		User user = get("userKey", userKey);
		user.setPwd(PasswordUtil.string2MD5(password));
		this.save(user);
		return user.getPwd();

	}

	@Override
	public void changePassword(String userKey, String oldPassword, String newPassword) {
		Subject subject = Subject.getSubject();
		if (subject != null) {
			userKey = subject.getUserKey();
		}
		User user = get("userKey", userKey);
		String uPwd = user.getPwd();
		oldPassword = PasswordUtil.string2MD5(oldPassword);
		if (!uPwd.equals(oldPassword)) {
			throw new UserException(20, "用户【" + userKey + "】密码不正确。");
		}
		user.setPwd(PasswordUtil.string2MD5(newPassword));
		this.save(user);
	}

	/*
	 * 获取用户的角色
	 */
	@Override
	public List<Role> getRoles(String userKey) {
		String sql = null;
		if (userKey.equals("admin")) {
			return dataSession.gets(Role.class);
		} else {
			sql = " select r from SA_Role r,SA_Role_User u where u.roleKey=r.roleKey and u.userKey=?";
			return dataSession.select(sql, new Object[] { userKey }, Role.class);

		}
	}

	/**
	 * 获取用户所有功能权限
	 */
	@Override
	public List<Permission> getPermission(String userKey) {
		List<Permission> listPerm = null;
		// 如果是管理员,可以看到所有权限
		if (userKey.equals("admin")) {
			listPerm = dataSession.gets(Permission.class, "", "seq", new Object[] {});
		} else {
			// 查询用户的权限
			String sql = "select distinct T.*  from (";
			sql += "	SELECT P.* FROM SA_USER_PERMISSION UP JOIN SA_PERMISSION P ON UP.PERMKEY=P.PERMKEY WHERE UP.USERKEY=?";
			sql += "	union ";
			sql += "	SELECT P.* FROM SA_ROLE_PERMISSION  RP JOIN SA_USER_ROLE   R ON R.ROLEKEY=RP.ROLEKEY AND R.USERKEY=? JOIN SA_PERMISSION P ON P.PERMKEY=RP.PERMKEY";
			sql += "	) T order by seq";
			listPerm = dataSession.select(sql, new Object[] { userKey, userKey }, Permission.class);
		}
		return listPerm;
	}

	/**
	 * 获取用户分配的功能权限不包含角色拥有的
	 */
	public List<Permission> getUserPermission(String userKey) {
		List<Permission> listPerm = null;
		// 如果是管理员,可以看到所有权限
		if (userKey.equals("admin")) {
			listPerm = dataSession.gets(Permission.class, "status=?", "seq", new Object[] { "1" });
		} else {
			// 查询用户的权限
			String sql = "	SELECT P.* FROM SA_USER_PERMISSION UP JOIN SA_PERMISSION P ON UP.PERMKEY=P.PERMKEY WHERE UP.USERKEY=? order by P.seq";
			listPerm = dataSession.select(sql, new Object[] { userKey }, Permission.class);
		}
		return listPerm;
	}

	public List<Bio> getUserDimension(String dimKey, String userKey) {
		Dimension dm = dataSession.get(Dimension.class, "dimKey", dimKey);
		String tableName = dm.getTableName();
		String sql = null;
		List<Bio> dataList = null;
		// 动态组装sql
		if (userKey.equals("admin")) {
			sql = " select T." + dm.getValueField() + " DIMVALUE," + dm.getTextField() + " DIMTEXT,T." + dm.getOwnerDimKey() + " PID from "
					+ tableName + " T order by T." + dm.getValueField();
			dataList = dataSession.query(sql, new Object[] {});
		} else {
			sql = " select T." + dm.getValueField() + " DIMVALUE," + dm.getTextField() + " DIMTEXT,T." + dm.getOwnerDimKey() + " PID from "
					+ tableName + " T";
			sql += "  JOIN SA_USER_DIMENSION U on U.DIMVALUE=T." + dm.getValueField() + "  and   U.DIMKEY=? AND U.USERKEY=? order by T."
					+ dm.getValueField();
			dataList = dataSession.query(sql, new Object[] { dimKey, userKey });
		}
		return dataList;
	}

	public void setUserDimension(String userKey, String dimKey, String[] addDimValue, String[] delDimValue) {
		String sql = "delete SA_User_Dimension where userKey=? and dimKey=? and dimValue=?";
		for (String dimValue : delDimValue) {
			dataSession.executeUpdate(sql, new Object[] { userKey, dimKey, dimValue });
		}
		for (String dimValue : addDimValue) {
			UserDimension ud = new UserDimension();
			ud.setDimKey(dimKey);
			ud.setUserKey(userKey);
			ud.setDimValue(dimValue);
			dataSession.save(ud);
		}
	}

	public void setUserPermission(String userKey, String[] addPermKeys, String[] delPermKeys) {
		String sql = " delete from UserPermission where userKey=? and permKey=?";
		for (String permKey : delPermKeys) {
			dataSession.executeUpdate(sql, new Object[] { userKey, permKey });
		}
		for (String permKey : addPermKeys) {
			UserPermission ud = new UserPermission();
			ud.setPermKey(permKey);
			ud.setUserKey(userKey);
			dataSession.save(ud);
		}
	}

	/**
	 * 获取用户树形数据权限
	 */
	@Override
	public List<Bio> getDimension(String userKey) {
		List<Bio> dataList = new ArrayList<Bio>();
		List<Dimension> dms = dataSession.gets(Dimension.class, "", "", new Object[] {});
		List<Bio> subDataList = new ArrayList<Bio>();
		for (Dimension dm : dms) {
			String tableName = dm.getTableName();
			String sql = null;
			Bio dmBio = new Bio();
			dmBio.setString("DIMVALUE", dm.getDimKey());
			dmBio.setString("DIMTEXT", dm.getDimName());
			dmBio.setString("PID", "ROOT");

			String ownerDimKey = dm.getOwnerDimKey();
			if (StringUtil.isEmpty(ownerDimKey)) {
				ownerDimKey = "'" + dm.getDimKey() + "' PID";
			} else {
				ownerDimKey = "T." + dm.getOwnerDimKey() + " PID";
			}
			// 动态组装sql
			if (userKey.equals("admin")) {
				sql = " select T." + dm.getValueField() + " DIMVALUE,'" + dm.getDimKey() + "' DIMKEY,T." + dm.getTextField() + " DIMTEXT, "
						+ ownerDimKey + " from " + tableName + " T order by T." + dm.getValueField();
				subDataList = dataSession.query(sql, new Object[] {});
				if (subDataList.size() > 0) {
					dataList.add(dmBio);
				}
			} else {
				sql = " select distinct T.*  from(";
				sql += " select  T." + dm.getValueField() + " DIMVALUE,'" + dm.getDimKey() + "' DIMKEY,T." + dm.getTextField() + " DIMTEXT,"
						+ ownerDimKey + "  from " + tableName + " T";
				sql += "  JOIN SA_USER_DIMENSION U on U.DIMVALUE=T." + dm.getValueField() + "  AND U.USERKEY=? ";
				sql += " union ";
				sql += " select  T." + dm.getValueField() + " DIMVALUE,'" + dm.getDimKey() + "' DIMKEY,T." + dm.getTextField() + " DIMTEXT,"
						+ ownerDimKey + "  from " + tableName + " T";
				sql += "  JOIN SA_ROLE_DIMENSION U on U.DIMVALUE=T." + dm.getValueField();
				sql += "  JOIN SA_USER_ROLE   R ON R.ROLEKEY=U.ROLEKEY  AND R.USERKEY=? ";
				sql += ") T";
				subDataList = dataSession.query(sql, new Object[] { userKey, userKey });
				if (subDataList.size() > 0) {
					dataList.add(dmBio);
				}
			}
			for (Bio bio : subDataList) {
				if (bio.getString("PID").equals("ROOT")) {
					bio.setString("PID", dm.getDimKey());
				}
			}

			dataList.addAll(subDataList);
		}
		return dataList;
	}

	@Override
	public List<Bio> getUserDimension(String userKey) {
		List<Bio> dataList = new ArrayList<Bio>();
		List<Dimension> dms = dataSession.gets(Dimension.class, "", "", new Object[] {});
		List<Bio> subDataList = new ArrayList<Bio>();
		for (Dimension dm : dms) {
			String tableName = dm.getTableName();
			String sql = null;
			// Bio dmBio = new Bio();
			// dmBio.setString("DIMVALUE", dm.getDimKey());
			// dmBio.setString("DIMTEXT", dm.getDimName());
			// dmBio.setString("PID", "ROOT");
			// dataList.add(dmBio);
			String ownerDimKey = dm.getOwnerDimKey();
			if (StringUtil.isEmpty(ownerDimKey)) {
				ownerDimKey = "'" + dm.getDimKey() + "' PID";
			} else {
				ownerDimKey = "T." + dm.getOwnerDimKey() + " PID";
			}
			// 动态组装sql
			if (userKey.equals("admin")) {
				sql = " select T." + dm.getValueField() + " DIMVALUE,'" + dm.getDimKey() + "' DIMKEY,T." + dm.getTextField() + " DIMTEXT, "
						+ ownerDimKey + " from " + tableName + " T order by T." + dm.getValueField();
				subDataList = dataSession.query(sql, new Object[] {});
			} else {
				sql = " select  T." + dm.getValueField() + " DIMVALUE,'" + dm.getDimKey() + "' DIMKEY,T." + dm.getTextField() + " DIMTEXT,"
						+ ownerDimKey + "  from " + tableName + " T";
				sql += "  JOIN SA_USER_DIMENSION U on U.DIMVALUE=T." + dm.getValueField() + "  AND U.USERKEY=? order by T." + dm.getValueField();
				subDataList = dataSession.query(sql, new Object[] { userKey });
			}
			for (Bio bio : subDataList) {
				if (bio.getString("PID").equals("ROOT")) {
					bio.setString("PID", dm.getDimKey());
				}
			}
			dataList.addAll(subDataList);
		}
		return dataList;
	}

	@Override
	public void saveSecurity(String userKey, List<UserPermission> perms, List<UserDimension> dimensions) {

		String sql2 = "delete UserPermission where userKey=?";
		dataSession.executeUpdate(sql2, new Object[] { userKey });
		dataSession.save(perms);
		String sql = "delete UserDimension where userKey=?";
		dataSession.executeUpdate(sql, new Object[] { userKey });
		dataSession.save(dimensions);

	}

}
