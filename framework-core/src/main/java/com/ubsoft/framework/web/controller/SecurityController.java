package com.ubsoft.framework.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.support.json.JsonHelper;
import com.ubsoft.framework.core.support.util.BeanUtil;
import com.ubsoft.framework.core.support.util.TreeUtil;
import com.ubsoft.framework.system.entity.Org;
import com.ubsoft.framework.system.entity.Permission;
import com.ubsoft.framework.system.entity.Region;
import com.ubsoft.framework.system.model.Subject;
import com.ubsoft.framework.system.service.IOrgService;
import com.ubsoft.framework.system.service.IPermService;
import com.ubsoft.framework.system.service.IRoleService;
import com.ubsoft.framework.system.service.IUserService;

@RequestMapping("/")
@Controller
public class SecurityController extends BaseController {

	@Autowired
	IPermService permService;
	@Autowired
	IOrgService orgService;

	@Resource
	IUserService userService;

	@Resource
	IRoleService roleService;

	/**
	 * 获取有权限的所属组织树结构
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/security/getorg.ctrl")
	@ResponseBody
	public String getOrg() {
		List<Org> orgs = orgService.getDimensionOrgList();
		// 将bean集合转换成Map
		List<Map> listMap = new ArrayList<Map>();
		for (Org org : orgs) {
			Map item = new HashMap();
			item.put("id", org.getOrgKey());
			item.put("pId", org.getOwnerKey());
			item.put("name", "【"+org.getOrgKey()+"】"+org.getOrgName());
			item.put("ID", org.getId());
			item.put("ORGNAME", org.getId());
			item.put("ORGKEY", org.getOrgKey());
			item.put("ADDRESS", org.getAddress());
			item.put("DISTRICT", org.getDistrict());
			item.put("CITY", org.getCity());
			item.put("PROVINCE", org.getProvince());
			item.put("ORGTYPE", org.getOrgType());
			item.put("CONTACTPERSON", org.getContactPerson());
			listMap.add(item);
		}
		// 转换成treemap
		String result = JsonHelper.collection2json(listMap);

		return result;
	}

	/**
	 * 获取区域tree结构
	 * @return
	 */
	@RequestMapping("/security/getRegion.ctrl")
	@ResponseBody
	public String getRegion() {
		List<Region> regions = orgService.getRegionList();
		// 将bean集合转换成Map
		List<Map> listMap = new ArrayList<Map>();
		for (Region region : regions) {
			Map item = new HashMap();
			item.put("id", region.getRegionKey());
			item.put("pId", region.getOwnerKey());
			item.put("name", region.getRegionName());
			item.put("ID", region.getId());
			item.put("REGIONKEY", region.getRegionKey());
			item.put("REGIONNAME", region.getRegionName());			
			listMap.add(item);
		}
		// 转换成treemap
		String result = JsonHelper.collection2json(listMap);

		return result;
	}
	/**
	 * 
	 * 获取有权限的菜单列表
	 * 
	 * @return
	 */
	@RequestMapping("/form/menu.ctrl")
	@ResponseBody
	public String getMenus() {
		List<Permission> menus = permService.gets(new String[]{},new Object[]{},"seq");
		// 将bean集合转换成Map
		List<Map> listMap = new ArrayList<Map>();
		for (Permission perm : menus) {
			Map map = BeanUtil.bean2Map(perm);
			listMap.add(map);
		}
		// 转换成treemap
		listMap = TreeUtil.buildTree(listMap, "permKey", "parentPermKey");

		String result = TreeUtil.toEasyUITree(listMap, "permKey", "permName", null);
		result = "[{\"id\":\"ROOT\",\"text\":\"根目录\",\"children\":" + result + "}]";
		return result;
	}

	@RequestMapping("/security/getUserPermission.ctrl")
	@ResponseBody
	public String getUserPermission() {
		String userKey = request.getParameter("userKey");
		// 登陆用户所持有的功能权限
		List<Permission> loginUserMenu = userService.getUserPermission(Subject.getSubject().getUserKey());
		// 将要被分配的用户持有的功能权限
		List<Permission> allocUserMenu = userService.getUserPermission(userKey);
		// 将bean集合转换成Map
		List<Map> listMap = new ArrayList<Map>();
		for (Permission menu : loginUserMenu) {
			
			Map<String, String> item = new HashMap<String, String>();
			item.put("id", menu.getPermKey());
			item.put("pId", menu.getParentPermKey());
			item.put("name", menu.getPermName());
			item.put("module", menu.getPermModule());
			for (Permission menu2 : allocUserMenu) {
				if (menu2.getId().equals(menu.getId())) {
					item.put("checked", "true");
					break;
				}
			}
			listMap.add(item);
		}
		String result = JsonHelper.collection2json(listMap);
		return result;
	}

	@RequestMapping("/security/getUserDimension.ctrl")
	@ResponseBody
	public String getUserDimension() {
		String userKey = request.getParameter("userKey");
		String dimKey = request.getParameter("dimKey");
		Map<String, Object> res = new HashMap<String, Object>();
		try {
			// 登陆用户所持有的功能权限
			List<Bio> loginUserDimensions = userService.getUserDimension(dimKey, Subject.getSubject().getUserKey());
			// 将要被分配的用户持有的数据权限
			List<Bio> allocUserDimensions = userService.getUserDimension(dimKey, userKey);

			// 将bean集合转换成Map
			List<Map> listMap = new ArrayList<Map>();
			for (Bio bio : loginUserDimensions) {
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", bio.getString("DIMVALUE"));
				item.put("pId", bio.getString("PID"));
				item.put("name", "【"+bio.getString("DIMVALUE")+"】"+bio.getString("DIMTEXT"));
				for (Bio bio2 : allocUserDimensions) {
					if (bio2.getString("DIMVALUE").equals(bio.getString("DIMVALUE"))) {
						item.put("checked", "true");
						break;
					}
				}
				listMap.add(item);
			}		
			res.put("status", "S");
			res.put("ret", listMap);
			String result = JsonHelper.bean2Json(res);
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			return responseError(ex, res);
		}
	}

	@RequestMapping("/security/getRolePermission.ctrl")
	@ResponseBody
	public String getRolePermission() {
		String roleKey = request.getParameter("roleKey");
		// 登陆用户所持有的功能权限
		List<Permission> loginUserMenu = userService.getPermission(Subject.getSubject().getUserKey());
		// 将要被分配的用户持有的功能权限
		List<Permission> allocUserMenu = roleService.getRolePermission(roleKey);
		
		// 将bean集合转换成Map
		List<Map> listMap = new ArrayList<Map>();
		for (Permission menu : loginUserMenu) {
			Map<String, String> item = new HashMap<String, String>();
			item.put("id", menu.getPermKey());
			item.put("pId", menu.getParentPermKey());
			item.put("name", menu.getPermName());
			item.put("module", menu.getPermModule());
			for (Permission menu2 : allocUserMenu) {
				if (menu2.getId().equals(menu.getId())) {
					item.put("checked", "true");
					break;
				}
			}
			listMap.add(item);
		}
		String result = JsonHelper.collection2json(listMap);
		return result;
	}

	@RequestMapping("/security/getRoleDimension.ctrl")
	@ResponseBody
	public String getRoleDimension() {
		String roleKey = request.getParameter("roleKey");
		String dimKey = request.getParameter("dimKey");
		Map<String, Object> res = new HashMap<String, Object>();
		try{
		// 登陆用户所持有的数据权限
		List<Bio> loginUserDimensions = roleService.getRoleDimension(dimKey, roleKey, true);
		// 将要被分配的角色持有的数据权限
		List<Bio> allocUserDimensions = roleService.getRoleDimension(dimKey, roleKey, false);

		// 将bean集合转换成Map
		List<Map> listMap = new ArrayList<Map>();
		for (Bio bio : loginUserDimensions) {
			Map<String, String> item = new HashMap<String, String>();
			item.put("id", bio.getString("DIMVALUE"));
			item.put("pId", bio.getString("PID"));
			item.put("name", "【"+bio.getString("DIMVALUE")+"】"+bio.getString("DIMTEXT"));
			for (Bio bio2 : allocUserDimensions) {
				if (bio2.getString("DIMVALUE").equals(bio.getString("DIMVALUE"))) {
					item.put("checked", "true");
					break;
				}
			}
			listMap.add(item);
		}
		res.put("status", "S");
		res.put("ret", listMap);
		String result = JsonHelper.bean2Json(res);
		return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			return responseError(ex, res);
		}
	}

}