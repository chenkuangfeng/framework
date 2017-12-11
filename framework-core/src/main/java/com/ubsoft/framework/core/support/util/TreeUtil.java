package com.ubsoft.framework.core.support.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ubsoft.framework.core.support.json.JsonHelper;

public class TreeUtil {

	/**
	 * 把list<map>封装成tree map
	 * 
	 * @param listMap
	 *            是数据库获取的List列表数据或者来自其他数据源的List
	 * @param idKey
	 *            id对应的map key
	 * @param pkey
	 *            父id对应的map key
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Map> buildTree(List<Map> listMap, String idkey, String pkey) {
		List<Map> nodeList = new ArrayList<Map>();
		for (Map firstNode : listMap) {
			boolean mark = false;
			for (Map secondNode : listMap) {
				if (firstNode.get(pkey) != null && firstNode.get(pkey).equals(secondNode.get(idkey))) {
					mark = true;
					if (secondNode.get("children") == null)
						secondNode.put("children", new ArrayList<Map>());
					List<Map> secondeChildren = (List<Map>) secondNode.get("children");
					secondeChildren.add(firstNode);
					break;
				}
			}
			if (!mark) {
				nodeList.add(firstNode);
			}
		}
		return nodeList;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void setEasyUITreeChildren(Map root, List<Map> items, String idkey, String textkey, String urlkey) {
		List<Map> children = new ArrayList<Map>();
		for (Map child : items) {
			Map easyMap = new HashMap();
			String id = child.get(idkey) + "";
			String text = child.get(textkey) + "";
			String url = child.get(urlkey) + "";
			String checked = child.get("checked") + "";
			if (StringUtil.isNotEmpty(id)) {
				easyMap.put("id", id);
			}
			if (StringUtil.isNotEmpty(text)) {
				easyMap.put("text", text);
			}
			if (StringUtil.isNotEmpty(url)) {
				Map att = new HashMap();
				att.put("url", url);
				easyMap.put("attributes", att);
			}
			if (StringUtil.isNotEmpty(checked)) {
				easyMap.put("checked", checked);
			}
			if (child.get("children") != null) {
				setEasyUITreeChildren(easyMap, (List<Map>) child.get("children"), idkey, textkey, urlkey);
			}
			children.add(easyMap);
		}
		root.put("children", children);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String toEasyUITree(List<Map> treeList, String idkey, String textkey, String urlkey) {
		List<Map> easyTreemap = new ArrayList<Map>();
		for (Map item : treeList) {
			Map easyMap = new HashMap();
			String id = item.get(idkey) + "";
			String text = item.get(textkey) + "";
			String url = item.get(urlkey) + "";
			String checked = item.get("checked") + "";
			if (StringUtil.isNotEmpty(id)) {
				easyMap.put("id", id);
			}
			if (StringUtil.isNotEmpty(text)) {
				easyMap.put("text", text);
			}
			if (StringUtil.isNotEmpty(url)) {
				Map att = new HashMap();
				att.put("url", url);
				easyMap.put("attributes", att);
			}
			if (StringUtil.isNotEmpty(checked)) {
				easyMap.put("checked", checked);
			}
			if (item.get("children") != null) {
				setEasyUITreeChildren(easyMap, (List<Map>) item.get("children"), idkey, textkey, urlkey);
			}
			easyTreemap.add(easyMap);

		}
		return JsonHelper.collection2json(easyTreemap);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String toJsonTree(List<Map> treeList, String idkey, String textkey, String urlkey, String iconKey) {
		List<Map> treeMap = new ArrayList<Map>();
		for (Map item : treeList) {
			Map itemMap = new HashMap();
			String id = item.get(idkey) + "";
			String text = item.get(textkey) + "";
			String url = item.get(urlkey) + "";
			String icon = item.get(iconKey) + "";
			if (StringUtil.isNotEmpty(id)) {
				itemMap.put("id", id);
			}
			if (StringUtil.isNotEmpty(text)) {
				itemMap.put("text", text);
			}
			if (StringUtil.isNotEmpty(url)) {

				itemMap.put("url", url);

			}
			if (StringUtil.isNotEmpty(icon)) {

				itemMap.put("icon", icon);

			}
			itemMap.put("targetType", "iframe-tab");

		
			if (item.get("children") != null) {
				setJsonTreeChildren(itemMap, (List<Map>) item.get("children"), idkey, textkey, urlkey, iconKey);
			}
			treeMap.add(itemMap);

		}
		return JsonHelper.collection2json(treeMap);
	}

	private static void setJsonTreeChildren(Map root, List<Map> items, String idkey, String textkey, String urlkey, String iconKey) {
		List<Map> children = new ArrayList<Map>();
		for (Map child : items) {
			Map itemMap = new HashMap();
			String id = child.get(idkey) + "";
			String text = child.get(textkey) + "";
			String url = child.get(urlkey) + "";
			String icon = child.get(iconKey) + "";

			String checked = child.get("checked") + "";
			if (StringUtil.isNotEmpty(id)) {
				itemMap.put("id", id);
			}
			if (StringUtil.isNotEmpty(text)) {
				itemMap.put("text", text);
			}
			if (StringUtil.isNotEmpty(icon)) {

				itemMap.put("icon", icon);

			}
			if (StringUtil.isNotEmpty(url)) {

				itemMap.put("url", url);

			}
			itemMap.put("targetType", "iframe-tab");
			if (child.get("children") != null) {
				setJsonTreeChildren(itemMap, (List<Map>) child.get("children"), idkey, textkey, urlkey,iconKey);
			}
			children.add(itemMap);
		}
		root.put("children", children);

	}

}
