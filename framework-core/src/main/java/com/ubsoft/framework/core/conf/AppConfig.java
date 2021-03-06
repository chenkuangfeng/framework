package com.ubsoft.framework.core.conf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ubsoft.framework.core.context.AppContext;
import com.ubsoft.framework.core.service.ITransactionService;
import com.ubsoft.framework.system.cache.MemoryPermission;
import com.ubsoft.framework.system.entity.Code;
import com.ubsoft.framework.system.entity.Permission;

public class AppConfig {
	private static Document document;
	// key,value配置
	private static Map<String, String> dataItem = new HashMap();
	// key 列表配置
	private static Map<String, List> dataList = new HashMap();
	// key map配置
	private static Map<String, Map> dataMap = new HashMap();

	public static ApplicationContext sprintContext;

	public static String webRootPath;

	public static boolean hadInitCache = false;

	/**
	 * 是否是调试模式
	 * 
	 * @return
	 */
	public static boolean isDebug() {
		return Boolean.parseBoolean((String) dataItem.get("debug"));
	}

	/**
	 * session是否缓存在全局共享缓存中
	 * 
	 * @return
	 */
	public static boolean isGlobalCache() {
		return AppConfig.getDataItem("cacheMode").equals("redis");
	}

	public static String getDefaultDs() {
		return AppConfig.getDataItem("DefaultDS");
	}

	/**
	 * 获取单个配置信息
	 * 
	 * @param key
	 * @return
	 */
	public static String getDataItem(String key) {
		String value = dataItem.get(key);
		if (value == null)
			value = "";

		return value;
	}

	public static Map<String, Map> getDataMap(String key) {
		return (Map) dataMap.get(key);
	}

	public static void putDataItem(String key, String value) {
		dataItem.put(key, value);
	}

	/**
	 * 初始化日志文件，本地调试用。
	 * 
	 * @param path
	 */
	public static void initLog4j(String path) {
		PropertyConfigurator.configure(path);

	}

	/**
	 * 初始化SpringContext，web程序之外用. Ejb服务通过此方法调用
	 * 
	 * @param path
	 */
	public static void initSpringContext(String path) {
		if (sprintContext == null) {
			sprintContext = new FileSystemXmlApplicationContext(path);
		}
	}

	/**
	 * 解析配置文件
	 */
	private static void parseXml() {
		Element root = document.getRootElement();
		for (Iterator it = root.elementIterator(); it.hasNext();) {
			Element item = (Element) it.next();
			if (item.getName().equals("dataItem")) {
				Attribute key = item.attribute("name");
				Attribute value = item.attribute("value");
				dataItem.put(key.getText(), value.getText());
			} else if (item.getName().equals("dataList")) {
				Attribute key = item.attribute("name");
				List list = new ArrayList();
				for (Iterator itList = item.elementIterator(); itList.hasNext();) {
					Element listItem = (Element) itList.next();
					Attribute listItemValue = listItem.attribute("value");
					list.add(listItemValue.getText());
				}
				dataList.put(key.toString(), list);
			} else if (item.getName().equals("dataMap")) {
				Attribute key = item.attribute("name");
				Map listMap = new HashMap();
				for (Iterator itList = item.elementIterator(); itList.hasNext();) {
					Element mapItem = (Element) itList.next();
					Attribute mapKey = mapItem.attribute("name");
					Attribute mapValue = mapItem.attribute("value");

					listMap.put(mapKey.getText(), mapValue.getText());
				}
				dataMap.put(key.getText(), listMap);
			}
		}
	}

	// public static void initConfig(InputStreamReader ireader) {
	// if (document == null) {
	// SAXReader reader = new SAXReader();
	// try {
	// document = reader.read(ireader);
	// parseXml();
	// // reader.sete
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }

	/**
	 * 加载并解析配置文件,先从config文件读取，然后从数据库读取，数据库配置覆盖配置文件配置。
	 * 
	 * @param strPath
	 */
	public static void initConfig(String strPath) {

		InputStreamReader readerInputStream = null;
		try {
			readerInputStream = new InputStreamReader(new FileInputStream(strPath), "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SAXReader reader = new SAXReader();
		reader.setEncoding("UTF-8");
		try {
			document = reader.read(readerInputStream);
			parseXml();
			// 重数据库读取
			initConfigFromDB();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 从数据库读取配置，并覆盖文件配置信息。
	 */
	private static void initConfigFromDB() {
		ITransactionService ds = (ITransactionService) AppContext.getBean("transactionService");
		List<Code> listCode = (List<Code>) ds.execute("codeService", "gets", new Object[] {});
		for (Code code : listCode) {
			AppConfig.putDataItem(code.getCodeKey(), code.getCodeName());
		}
	}

	/**
	 * 服务启动时候加载服务器端缓存
	 *
	 */
	public static void initCache(String path) {
		// 设置ehcache环境变量
		System.setProperty("net.sf.ehcache.path", path);
		MemoryPermission.getInstance();
		// 获取调度服务对象
		ITransactionService ds = (ITransactionService) AppContext.getBean("transactionService");

		// 清空并加载加载权限配置基础信息
		MemoryPermission.getInstance().clear();
		List<Permission> lstPermission = (List<Permission>) ds.execute("permService", "gets", new Object[] {});
		if (lstPermission != null) {
			for (Permission perm : lstPermission) {
				MemoryPermission.getInstance().put(perm.getPermKey(), perm.getPermModule());
				//功能权限需要ba module也放在缓存,记录serviceName+methodName;
				// 只有在权限表里面的才管理权限
				// 判断是否在权限表里面，服务名和方法放在value里面，permKey放在key里面
				if(perm.getPermType().equals("FUNC")){
					MemoryPermission.getInstance().put(perm.getPermModule(), perm.getPermKey());
				}

			}
		}
		// 加载数据维度基本信息，特殊处理
		ds.execute("dimService", "initDimension", new Object[] {});
		// 加载报表数据
		//ds.execute("rptService", "initReport", new Object[] {});

		// 清空并加载选择界面配置信息
		// MemoryLookupForm.getInstance().clear();
		// List<LookupForm> lstLookupForm = (List<LookupForm>)
		// ds.execute("lookupFormService", "getAll", new Object[] {});
		// if (lstLookupForm != null) {
		// for (LookupForm form : lstLookupForm) {
		// MemoryLookupForm.getInstance().put(form.getfKey(), form);
		// }
		// }

		// 杀掉过期session和subject ，考虑到负载均衡和redis
		ds.execute("sessionService", "clearTimeoutSession", new Object[] {});

		// 任务
		ds.execute("taskService", "initTask", new Object[] {});
		ds.execute("rptService", "initReport", new Object[] {});
		hadInitCache = true;
		// 清空页面缓存
		// MemoryPage.getInstance().clear();

	}

	public static void initEsb(String quartzPath) {
		//System.setProperty("org.quartz.properties", quartzPath);
		ITransactionService ds = (ITransactionService) AppContext.getBean("transactionService");
		ds.execute("esbEngine", "initEngine", new Object[] {});
	}

	

	/**
	 * 加载数据模型,界面模型, 界面配置
	 */
	public static void initMetadata() {
		ITransactionService ds = (ITransactionService) AppContext.getBean("transactionService");
		ds.execute("bioMetaService", "initBioMeta", new Object[] {});
		ds.execute("fdmMetaService", "initFdmMeta", new Object[] {});
		ds.execute("formService", "initFormMeta", new Object[] {});


	}

	
}