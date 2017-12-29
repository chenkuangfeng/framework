package com.ubsoft.framework.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.service.impl.BaseService;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.system.entity.Dimension;
import com.ubsoft.framework.system.entity.Permission;
import com.ubsoft.framework.system.entity.Role;
import com.ubsoft.framework.system.entity.RoleDimension;
import com.ubsoft.framework.system.entity.RolePermission;
import com.ubsoft.framework.system.entity.User;
import com.ubsoft.framework.system.model.Subject;
import com.ubsoft.framework.system.service.IRoleService;

@Service("roleService")
@Transactional
public class RoleService extends BaseService<Role> implements IRoleService {
	@Override
	public List<User> getUsers(String roleKey) {
		String sql = " select u.* from SA_USER u,SA_USER_ROLE r where r.userKey=u.userKey and r.roleKey=?";
		List<User> u = this.dataSession.select(sql, new Object[] { roleKey },User.class);
		return u;
	}
	public List<Bio> getRoleDimension(String dimKey, String roleKey,boolean isCurrentRole) {
		Dimension dm = dataSession.get(Dimension.class, "dimKey", dimKey);	
		String tableName = dm.getTableName();
		String userKey=Subject.getSubject().getUserKey();
		String sql = null;
		List<Bio> dataList = null;
		if(isCurrentRole){
			if (userKey.equals("admin")) {
				sql = " select T." + dm.getValueField() + " DIMVALUE," + dm.getTextField() + " DIMTEXT,T."+dm.getOwnerDimKey()+" PID from " + tableName + " T order by T."+dm.getValueField();
				dataList = dataSession.query(sql, new Object[] {});
			}else{
				sql = " select distinct T." + dm.getValueField() + " DIMVALUE," + dm.getTextField() + " DIMTEXT,T."+dm.getOwnerDimKey()+" PID from " + tableName + " T ";
				sql += "  JOIN SA_ROLE_DIMENSION U on U.DIMVALUE=T." + dm.getValueField() + "  and   U.DIMKEY=? AND U.ROLEKEY=? order by T."+dm.getValueField();				
				sql+=" JOIN SA_USER_ROLE R ON R.roleKey=U.roleKey and userKey=?";
				dataList = dataSession.query(sql, new Object[] { dimKey, roleKey,Subject.getSubject().getUserKey() });
				
			}
		}else{
			sql = " select distinct T." + dm.getValueField() + " DIMVALUE," + dm.getTextField() + " DIMTEXT,T."+dm.getOwnerDimKey()+" PID from " + tableName + " T";
			sql += "  JOIN SA_ROLE_DIMENSION U on U.DIMVALUE=T." + dm.getValueField() + "  and   U.DIMKEY=? AND U.ROLEKEY=? order by T."+dm.getValueField();
			dataList = dataSession.query(sql, new Object[] { dimKey, roleKey});
		
		}
		
		return dataList;
	}
	
	public void setRoleDimension(String roleKey,String dimKey,String[] dimValue){		
		
		String sql="delete SA_Role_Dimension where roleKey=? and dimKey=?";
		dataSession.executeUpdate(sql, new Object[]{roleKey,dimKey});
		for(String val:dimValue){
			RoleDimension ud=new RoleDimension();
			ud.setDimKey(dimKey);
			ud.setRoleKey(roleKey);
			ud.setDimValue(val);
			dataSession.save(ud);
		}
		
	}

	@Override
	public List<Permission> getRolePermission(String roleKey) {
		String sql = "  select p from Sa_Permission p, SA_Role_Permission r where r.permKey=p.permKey and r.roleKey=? order by p.seq";
		List<Permission> up = this.dataSession.select(sql, new Object[] { roleKey },Permission.class);
		return up;
	}
	
	 
	@Override
	public void setRoleDimension(String roleKey, String dimKey, String[] advalues, String[] delvalues) {
		String sql = "delete SA_Role_Dimension where roleKey=? and dimKey=? and dimValue=?";
		for (String dimValue : delvalues) {
			dataSession.executeUpdate(sql, new Object[] { roleKey,dimKey, dimValue});
		}
		for (String dimValue : advalues) {
			RoleDimension ud = new RoleDimension();
			ud.setDimKey(dimKey);
			ud.setRoleKey(roleKey);
			ud.setDimValue(dimValue);
			dataSession.save(ud);
		}	

		
	}

	@Override
	public void setRolePermission(String roleKey, String[] addPermKeys, String[] delPermKeys) {
		String sql = " delete from RolePermission where roleKey=? and permKey=?";
		for (String permKey : delPermKeys) {
			dataSession.executeUpdate(sql, new Object[] { roleKey, permKey });
		}
		
		dataSession.flush();
		
		for (String permKey : addPermKeys) {
			RolePermission ud = new RolePermission();
			ud.setPermKey(permKey);
			ud.setRoleKey(roleKey);
			dataSession.save(ud);
			dataSession.flush();

		}
				
	}
	
	
	 /**
	  * 获取角色的数据权限
	  * @param userKey
	  * @return
	  */
	 public List<Bio> getRoleDimension(String roleKey){
		 List<Bio> dataList = new ArrayList<Bio>();
			List<Dimension> dms = dataSession.gets(Dimension.class);
			List<Bio> subDataList = new ArrayList<Bio>();
			String userKey=Subject.getSubject().getUserKey();
			for (Dimension dm : dms) {
				String tableName = dm.getTableName();
				String sql = null;

				String ownerDimKey = dm.getOwnerDimKey();
				if (StringUtil.isEmpty(ownerDimKey)) {
					ownerDimKey = "'" + dm.getDimKey() + "' PID";
				} else {
					ownerDimKey = "T." + dm.getOwnerDimKey() + " PID";
				}
				// 动态组装sql
			
					sql = " select  T." + dm.getValueField() + " DIMVALUE,'"+dm.getDimKey()+"' DIMKEY,T." + dm.getTextField() + " DIMTEXT," + ownerDimKey + "  from " + tableName + " T";
					sql += "  JOIN SA_ROLE_DIMENSION R on R.DIMVALUE=T." + dm.getValueField() + "  AND R.ROLEKEY=? order by T." + dm.getValueField();				
					subDataList = dataSession.query(sql, new Object[] { roleKey }); 
				
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
	public void saveSecurity(String roleKey, List<RolePermission> perms, List<RoleDimension> dimensions) {
		String sql2 = "delete SA_Role_Permission where roleKey=?";
		dataSession.executeUpdate(sql2, new Object[] { roleKey});
		dataSession.save(perms);				
		String sql = "delete SA_Role_Dimension where roleKey=?";
		dataSession.executeUpdate(sql, new Object[] { roleKey});
		dataSession.save(dimensions); 

		
	}

}
