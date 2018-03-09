package com.ubsoft.framework.core.support.json;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

import org.apache.log4j.Logger;

import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.dal.model.BioSet;
import com.ubsoft.framework.core.dal.util.TypeUtil;
import com.ubsoft.framework.core.support.util.DateUtil;
import com.ubsoft.framework.core.support.util.StringUtil;

public class JsonHelper {
	private static final Logger log = Logger.getLogger(JsonHelper.class);

	// 将数组转换成JSON
	private static JsonConfig getJsonConfig() {
		JsonConfig cfg = new JsonConfig();
		cfg.registerJsonValueProcessor(Timestamp.class, new JsonValueProcessorImpl());
		cfg.registerJsonValueProcessor(Date.class, new JsonValueProcessorImpl());
		cfg.registerJsonValueProcessor(java.sql.Date.class, new JsonValueProcessorImpl());
		return cfg;
	}

	// 将Collection转换成JSON
	public static String collection2json(Object obj) {
		String jsonString = "[]";
		if (StringUtil.isEmpty(obj)) {
			return jsonString;
		}
		JsonConfig cfg = getJsonConfig();
		JSONArray jsonArray = JSONArray.fromObject(obj, cfg);
		return jsonArray.toString();
	}

	public static String bean2Json(Object obj) {
		String jsonString = "{}";
		if (StringUtil.isEmpty(obj)) {
			return jsonString;
		}

		JsonConfig cfg = getJsonConfig();
		JSONObject jsonObject = JSONObject.fromObject(obj, cfg);
		return jsonObject.toString();
	}

	public static String bio2Json(Bio bio) {
		StringBuffer sb = new StringBuffer();

		JsonConfig cfg = getJsonConfig();
		JSONObject jsonObject = JSONObject.fromObject(bio, cfg);
		return jsonObject.toString();

	}

	public static String bioSet2Json(BioSet bio) {
		JsonConfig cfg = getJsonConfig();
		JSONObject list = JSONObject.fromObject(bio, cfg);
		return list.toString();

	}

	

	// 将JSON转换成POJO,其中beanClz为POJO的Class
	public static Object json2Bean(String json, Class beanClz) {

		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd't'HH:mm:ss", "yyyy-MM-dd" }));
		JSONObject jsonObject = JSONObject.fromObject(json);
		return JSONObject.toBean(jsonObject, beanClz);
	}

	public static Object json2Bean(String json, Class beanClz, Map classMap) {

		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd't'HH:mm:ss", "yyyy-MM-dd" }));
		JSONObject jsonObject = JSONObject.fromObject(json);

		return JSONObject.toBean(jsonObject, beanClz, classMap);
	}

	// 将JSON转换成Collection,其中collectionClz为Collection具体子类的Class,
	// valueClz为Collection中存放的对象的Class
	public static Collection json2Collection(String json, Class valueClz) {
		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd" }));
		JSONArray jsonArray = JSONArray.fromObject(json);
		return JSONArray.toCollection(jsonArray, valueClz);
	}

	public static List json2List(String json, Class valueClz) {
		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd" }));
		JSONArray jsonArray = JSONArray.fromObject(json);
		return (List)JSONArray.toCollection(jsonArray, valueClz);
	}

	public static final String array2Json(Object[] obj) {
		String jsonString = "[]";
		if (StringUtil.isEmpty(obj)) {
			return jsonString;
		}
		JsonConfig cfg = getJsonConfig();
		JSONArray jsonArray = JSONArray.fromObject(obj, cfg);
		return jsonArray.toString();
	}

	// 将JSON转换成数组,其中valueClz为数组中存放的对象的Class
	public static Object json2Array(String json, Class valueClz) {
		JSONArray jsonArray = JSONArray.fromObject(json);
		return JSONArray.toArray(jsonArray, valueClz);
	}

	// 将String转换成JSON
	public static String string2Json(String key, String value) {
		JSONObject object = new JSONObject();
		object.put(key, value);
		return object.toString();

	}

	// 将JSON转换成String
	public static String json2String(String json, String key) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		return jsonObject.get(key).toString();
	}

	// 将String转换成JSON
	public static JSONObject string2Object(String value) {
		JSONObject object = JSONObject.fromObject(value);
		return object;

	}

	/**
	 * 解析前端传递过来的json格式的参数转换成java参数，格式如下 [ {"type":"Integer","value":"1"},
	 * {"type":"Object[]","value":[{type:"Integer",value:4}]},
	 * {"type":"com.xxx..entity",value:{p1:1,p2:4}},
	 * {"type":"com.xxx..entity",value:[{p1:1,p2:4}]} ]
	 * 
	 * @param params
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Object[] transferParams(String params) throws ClassNotFoundException {
		if (StringUtil.isEmpty(params)) {
			return new Object[] {};
		}
		JSONArray jsonArray = JSONArray.fromObject(params);
		Object[] result = new Object[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject param = jsonArray.getJSONObject(i);
			String type = param.getString("type");
			Object value = param.get("value");
			// object数据，注：object数组里面不能再包含object 数组；
			if (type.equals("Object[]")) {
				JSONArray objs = (JSONArray) value;
				Object[] objParams = new Object[objs.size()];
				for (int j = 0; j < objs.size(); j++) {
					String typeObj = param.getString("type");
					Object valueObj = param.get("value");
					objParams[j] = getGeneralValue(typeObj, valueObj);
				}
				result[i] = objParams;

			} else {
				result[i] = getGeneralValue(type, value);
			}

		}
		return result;

	}

	/**
	 * 
	 * @Title: transferMapParams
	 * @Description: 把格式的参数转换成map类型的Java参数，只支持java基础类型 [
	 *               {"type":"Integer","value":"1","name":"key1"}
	 * @author chenkf
	 * @date 2017-2-28 下午02:35:21
	 * @param params
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Map transferMapParams(String params) throws ClassNotFoundException {
		Map mapParam = new HashMap();
		JSONArray jsonArray = JSONArray.fromObject(params);
		Object[] result = new Object[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject param = jsonArray.getJSONObject(i);
			String type = param.getString("type");
			Object value = param.get("value");
			String name = param.getString("name");
			mapParam.put(name, TypeUtil.convert(type, value));

		}
		return mapParam;

	}

	public static Object getGeneralValue(String type, Object value) {
		String val = value + "";
		if (value instanceof JSONNull)
			val = "";
		if (type.equals(TypeUtil.INTEGER)) {
			int p = Integer.parseInt(val);
			return p;
		} else if (type.equals(TypeUtil.FLOAT)) {
			Float p = Float.parseFloat(val);
			return p;
		} else if (type.equals(TypeUtil.STRING)) {
			return val;
		} else if (type.equals(TypeUtil.DATE)) {
			Date p = DateUtil.string2Date(val, "yyyy-MM-dd");
			return p;
		} else if (type.equals(TypeUtil.TIMESTAMP)) {
			Timestamp p = Timestamp.valueOf(val);
			return p;
		} else if (type.equals(TypeUtil.TIME)) {
			Date p = DateUtil.string2Date(val, DateUtil.dateTimeFormat);
			return p;
		} else if (type.equals(TypeUtil.DOUBLE)) {
			Double p = Double.parseDouble(val);
			return p;
		} else if (type.equals(TypeUtil.LONG)) {
			Long p = Long.parseLong(val);
			return p;
		} else if (type.equals(TypeUtil.STRING_ARRAY)) {
			String[] p = (String[]) JsonHelper.json2Array(value.toString(), String.class);
			return p;
		} else {
			Class clazz = null;
			try {
				clazz = Class.forName(type);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (value instanceof JSONArray) {

				Object[] p = (Object[]) JsonHelper.json2Array(value.toString(), clazz);
				return p;
			} else {
				Object p = JsonHelper.json2Bean(value.toString(), clazz);
				return p;
			}
		}

	}

	public static List<Map<String, Object>> parseJSON2List(String jsonStr) {
		JSONArray jsonArr = JSONArray.fromObject(jsonStr);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Iterator<JSONObject> it = jsonArr.iterator();
		while (it.hasNext()) {
			JSONObject json2 = it.next();
			list.add(parseJSON2Map(json2.toString()));
		}
		return list;
	}

	public static Map<String, Object> parseJSON2Map(String jsonStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 最外层解析
		JSONObject json = JSONObject.fromObject(jsonStr);
		for (Object k : json.keySet()) {
			Object v = json.get(k);
			// 如果内层还是数组的话，继续解析
			if (v instanceof JSONArray) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				Iterator<JSONObject> it = ((JSONArray) v).iterator();
				while (it.hasNext()) {
					JSONObject json2 = it.next();
					list.add(parseJSON2Map(json2.toString()));
				}
				map.put(k.toString(), list);
			} else {
				map.put(k.toString(), v);
			}
		}
		return map;
	}

	public static void main(String[] args) {
		String ss = "[{\"type\":\"Integer\",\"value\":{a:\"fff\"}}]";
		JSONArray jsonArray = JSONArray.fromObject(ss);
		Object[] result = new Object[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject param = jsonArray.getJSONObject(i);
			String type = param.getString("type");
			Object value = param.get("value");
			if (value instanceof JSONArray) {
				System.out.println(value.toString());
			} else {
				System.out.println(value.toString());

			}

		}

	}
}