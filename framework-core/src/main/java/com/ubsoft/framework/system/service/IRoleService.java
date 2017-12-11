package com.ubsoft.framework.system.service;

import java.util.List;

import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.service.IBaseService;
import com.ubsoft.framework.system.entity.Permission;
import com.ubsoft.framework.system.entity.Role;
import com.ubsoft.framework.system.entity.RoleDimension;
import com.ubsoft.framework.system.entity.RolePermission;
import com.ubsoft.framework.system.entity.User;

public interface IRoleService extends IBaseService<Role> {

	/**
	 * 获取角色所有用户
	 * @param roleKey
	 * @return
	 */
	List<User> getUsers(String roleKey);
	
	/**
	 *获取角色所有功能权限
	 * @param roleKey
	 * @return
	 */
	List<Permission> getRolePermission(String roleKey);
	
	/**
	 * 获取角色数据权限
	 * @param dimKey 权限维度
	 * @param roleKey
	 * @return
	 */
	 List<Bio> getRoleDimension(String dimKey, String roleKey, boolean isCurrentRole) ;
	
	 /**
	  * 设置角色数据权限
	  * @param roleKey
	  * @param dimKey
	  * @param advalues
	  * @param delvalues
	  */
	 void setRoleDimension(String roleKey, String dimKey, String[] advalues, String[] delvalues);
		
	 /**
	  * 设置角色功能权限
	  * @param roleKey
	  * @param addPermKeys
	  * @param delPermKeys
	  */
	 void setRolePermission(String roleKey, String[] addPermKeys, String[] delPermKeys);
		/**
		 * 获取角色数据权限	
		 * @param roleKey
		 * @return
		 */
	 List<Bio> getRoleDimension(String roleKey);
	 
	 /**
	     * 设置角色功能和数据权限
	     * @param userKey
	     * @param permission
	     * @param dimension
	     */
	    void saveSecurity(String roleKey, List<RolePermission> perms, List<RoleDimension> dimensions);
}
