package com.ubsoft.framework.core.support.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.ubsoft.framework.core.conf.AppConfig;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkerUtil {
	private Map<String, Object> root = new HashMap<String, Object>();

	private Configuration cfg;
	private Configuration stringConfig;
	private StringTemplateLoader stringTemplateLoader;

	private static FreeMarkerUtil instance;

	public static FreeMarkerUtil getInstance() {
		if (instance == null) {
			instance = new FreeMarkerUtil();
		}
		return instance;
	}

	// 如果配置模板路径，从路径加载模板
	public FreeMarkerUtil() {
		if (StringUtil.isNotEmpty(AppConfig.getDataItem("ftlDir"))) {
			String ftlPath = AppConfig.webRootPath + AppConfig.getDataItem("ftlDir");
			if (StringUtil.isNotEmpty(ftlPath)) {
				cfg = new Configuration();
				cfg.setEncoding(Locale.CHINA, "UTF-8");
				try {
					cfg.setDirectoryForTemplateLoading(new File(ftlPath));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	/**
	 * 设置一个模板中使用的数据模型对象
	 * 
	 * @param name
	 *            数据模型名称
	 * @param obj
	 *            数据模型对象
	 */
	public void setModel(String name, Object obj) {
		root.put(name, obj);
	}

	/**
	 * 一次性设置多个数据模型对象到模板引擎中
	 * 
	 * @param models
	 */
	public void setModels(Map<String, Object> models) {
		root.putAll(models);
	}

	/**
	 * 按照名字从模板引擎中删除一个数据模型对象
	 * 
	 * @param name
	 */
	public void removeModel(String name) {
		root.remove(name);
	}

	/**
	 * 清除所有的数据模型对象
	 */
	public void removeModels() {
		root.clear();
	}

	/**
	 * 按照列表中的名称清除数据模型对象
	 * 
	 * @param modelNames
	 */
	public void removeModels(List<String> modelNames) {
		for (int i = 0; i < modelNames.size(); i++) {
			root.remove(modelNames.get(i));
		}
	}

	private String getId(String template) {
		return "id=[" + template + "]";
	}

	private StringTemplateLoader getStringTemplateLoader() {
		if (null == this.stringTemplateLoader) {
			this.stringTemplateLoader = new StringTemplateLoader();

			if (null == stringConfig) {
				// 如果是第一次使用 parseString(), 初始化相应的 Configuration
				stringConfig = new Configuration();
			}
			stringConfig.setTemplateLoader(this.stringTemplateLoader);
		}
		return this.stringTemplateLoader;
	}

	/**
	 * 解析一个字符串模板
	 * 
	 * @param template
	 * @return
	 * @throws TemplateException
	 */
	public String parseString(String template) throws TemplateException {
		StringTemplateLoader stringLoader = getStringTemplateLoader();
		stringLoader.putTemplate(getId(template), template);
		try {
			Template tmp = stringConfig.getTemplate(getId(template));
			Writer w = new StringWriter();
			tmp.process(root, w);
			return w.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new TemplateException(e, Environment.getCurrentEnvironment());
		}

	}

	public String parseString(String template, Map root) throws TemplateException {
		StringTemplateLoader stringLoader = getStringTemplateLoader();
		stringLoader.putTemplate(getId(template), template);
		try {
			Template tmp = stringConfig.getTemplate(getId(template));
			Writer w = new StringWriter();
			tmp.process(root, w);
			return w.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new TemplateException(e, Environment.getCurrentEnvironment());
		}

	}

	/**
	 * 解析一个字符串模板
	 * 
	 * @param template
	 * @return
	 * @throws TemplateException
	 */
	public String parseString(String template, Object obj) throws TemplateException {

		StringTemplateLoader stringLoader = getStringTemplateLoader();
		stringLoader.putTemplate(getId(template), template);

		try {
			Template tmp = stringConfig.getTemplate(getId(template));
			Writer w = new StringWriter();
			tmp.process(obj, w);
			return w.toString();
		} catch (IOException e) {
			throw new TemplateException(e, Environment.getCurrentEnvironment());
		}

	}

	/**
	 * 解析一个字符串模板, 字符串内容来自一个 InputStream
	 * 
	 * @param input
	 *            包含字符串内容的输入流
	 * @param charset
	 *            字符集编码
	 * @return
	 * @throws TemplateException
	 */
	public String parseString(InputStream input, String charset) throws TemplateException {
		try {
			Reader rd;
			if (null == charset) {
				rd = new InputStreamReader(input);
			} else {
				rd = new InputStreamReader(input, charset);
			}
			StringBuffer buf = new StringBuffer();
			int c = rd.read();
			while (-1 != c) {
				buf.append((char) c);
				c = rd.read();
			}
			rd.close();
			return parseString(buf.toString());
		} catch (IOException e) {
			throw new TemplateException(e, Environment.getCurrentEnvironment());
		}
	}

	/**
	 * 解析一个字符串模板(使用默认字符集), 字符串内容来自一个 InputStream
	 * 
	 * @param input
	 *            包含字符串内容的输入流
	 * @return
	 * @throws TemplateException
	 */
	public String parseString(InputStream input) throws TemplateException {
		return parseString(input, null);
	}

	public String getTemplateResult(String key) throws TemplateException {
		try {

			Template tmp = cfg.getTemplate(key);
			Writer w = new StringWriter();
			tmp.process(root, w);
			return w.toString();
		} catch (IOException e) {
			throw new TemplateException(e, Environment.getCurrentEnvironment());
		}
	}

	public String getTemplateResult(String key, Map model) throws TemplateException {
		try {
			Template tmp = cfg.getTemplate(key);
			Writer w = new StringWriter();
			tmp.process(model, w);
			return w.toString();
		} catch (IOException e) {
			throw new TemplateException(e, Environment.getCurrentEnvironment());
		}
	}
	public void genFile(String key, Map model,String targetFile) throws TemplateException {
		try {
			Template tmp = cfg.getTemplate(key);
			tmp.setEncoding("UTF-8");  
		    Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8"));  
			tmp.process(model, w);
			
		} catch (IOException e) {
			throw new TemplateException(e, Environment.getCurrentEnvironment());
		}
	}
	 

}
