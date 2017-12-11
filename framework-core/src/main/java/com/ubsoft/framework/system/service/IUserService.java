package com.ubsoft.framework.system.service;

import java.util.List;

import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.service.IBaseService;
import com.ubsoft.framework.system.entity.Permission;
import com.ubsoft.framework.system.entity.Role;
import com.ubsoft.framework.system.entity.Session;
import com.ubsoft.framework.system.entity.User;
import com.ubsoft.framework.system.entity.UserDimension;
import com.ubsoft.framework.system.entity.UserPermission;

public interface IUserService extends IBaseService<User> {
	Session login(String userKey, String pwd);

	boolean loginout(String sessionId);

	String changePassword(String userKey, String pasword);

	void changePassword(String userKey, String oldPasword, String newPassword);
	/**
	 * 获取用户所属的角色
	 * @param userKey
	 * @return
	 */
	List<Role> getRoles(String userKey);
	
	
	/**
	 * 获取用户所有功能权限，包含所属角色的权限
	 * @param userKey
	 * @return
	 */
	List<Permission> getPermission(String userKey);

	
	/**
	 * 获取用户功能权限 ，不包含所属角色的权限。web版本
	 * @param userKey
	 * @return
	 */
	List<Permission> getUserPermission(String userKey);
	/**
	 * 获取用户数据权限 不包含角色，web版本
	 * @param dimKey 权限维度
	 * @param userKey
	 * @return
	 */
	 List<Bio> getUserDimension(String dimKey, String userKey) ;	 
	 
	 /**
	  * 获取用户所有数据权限，包含所属角色的数据权限
	  * @param userKey
	  * @return
	  */
	 List<Bio> getDimension(String userKey);
	 /**
	  * 获取用户的数据权限，不包含所属角色的
	  * @param userKey
	  * @return
	  */
	 List<Bio> getUserDimension(String userKey);
	 
	 /**
	  * 设置用户数据权限维度
	  * @param userKey
	  * @param dimKey
	  * @param dimValue
	  */
    void setUserDimension(String userKey, String dimKey, String[] advalues, String[] delvalues);
	/**
	 * 设置用户功能权限
	 * @param userKey
	 * @param addPermKeys
	 * @param delPermKeys
	 */
    void setUserPermission(String userKey, String[] addPermKeys, String[] delPermKeys);
    
    /**
     * 设置用户功能和数据权限
     * @param userKey
     * @param permission
     * @param dimension
     */
    void saveSecurity(String userKey, List<UserPermission> perms, List<UserDimension> dimensions);
    
    
    
    
   
    
	

}
