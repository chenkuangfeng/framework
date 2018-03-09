package com.ubsoft.framework.core.support.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.ubsoft.framework.core.conf.AppConfig;
import com.ubsoft.framework.core.exception.ComException;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerUtil {

	private static Configuration cfg;
	private static Configuration strConfig;
	private static StringTemplateLoader stringTemplateLoader;

	private static String getId(String template) {
		return "id=[" + template + "]";
	}

	private static StringTemplateLoader getStringTemplateLoader() {
		if (null == stringTemplateLoader) {
			stringTemplateLoader = new StringTemplateLoader();
			if (null == strConfig) {
				// 如果是第一次使用 parseString(), 初始化相应的 Configuration
				strConfig = new Configuration();
			}
			strConfig.setTemplateLoader(stringTemplateLoader);
		}
		return stringTemplateLoader;
	}

	public static String parseString(String template, Map root) {
		StringTemplateLoader stringLoader = getStringTemplateLoader();
		stringLoader.putTemplate(getId(template), template);
		try {
			Template tmp = strConfig.getTemplate(getId(template));
			Writer w = new StringWriter();
			tmp.process(root, w);
			return w.toString();
		} catch (Exception e) {

			throw new ComException(ComException.MIN_ERROR_CODE_GLOBAL, e);
		}

	}

	/**
	 * 解析一个字符串模板
	 * 
	 * @param template
	 * @return
	 * @throws TemplateException
	 */
	public static String parseString(String template, Object obj) {
		StringTemplateLoader stringLoader = getStringTemplateLoader();
		stringLoader.putTemplate(getId(template), template);
		try {
			Template tmp = strConfig.getTemplate(getId(template));
			Writer w = new StringWriter();
			tmp.process(obj, w);
			return w.toString();
		} catch (Exception e) {
			throw new ComException(ComException.MIN_ERROR_CODE_GLOBAL, e);
		}

	}

	private static List<FileTemplateLoader> fileLoader = new ArrayList<FileTemplateLoader>();

	private static void loadTemplates(String ftlPath) throws Exception {
		File root = new File(ftlPath);
		if (root.isDirectory()) {
			FileTemplateLoader loader = new FileTemplateLoader(root);
			for (File file : root.listFiles()) {
				loadTemplates(file.getPath());
			}
			fileLoader.add(loader);
		}
	}

	private static Configuration getCfg() {
		if (cfg == null) {
			if (StringUtil.isNotEmpty(AppConfig.getDataItem("ftlDir")) && StringUtil.isNotEmpty(AppConfig.webRootPath)) {
				String ftlPath = AppConfig.webRootPath + AppConfig.getDataItem("ftlDir");
				if (StringUtil.isNotEmpty(ftlPath)) {
					cfg = new Configuration();
					cfg.setEncoding(Locale.CHINA, "UTF-8");
					try {
						loadTemplates(ftlPath);
						TemplateLoader[] fileloadders = new TemplateLoader[fileLoader.size()];
						int i = 0;
						for (FileTemplateLoader loader : fileLoader) {
							fileloadders[i] = loader;
							i++;
						}
						MultiTemplateLoader fmtl = new MultiTemplateLoader(fileloadders);
						cfg.setTemplateLoader(fmtl);						
					} catch (Exception e) {

						e.printStackTrace();
					}

				}
			} else {
				cfg = new Configuration();
				cfg.setEncoding(Locale.CHINA, "UTF-8");
				cfg.setClassForTemplateLoading(FreemarkerUtil.class, "/ftl");
			}
		}
		return cfg;
	}

	public static String getTemplateResult(String key, Map model) {
		try {
			Template tmp = getCfg().getTemplate(key);
			Writer w = new StringWriter();
			tmp.process(model, w);
			return w.toString();
		} catch (Exception e) {
			throw new ComException(ComException.MIN_ERROR_CODE_GLOBAL, e);
		}
	}

	public static void genFile(String key, Map model, String targetFile) {
		try {
			Template tmp = getCfg().getTemplate(key);

			tmp.setEncoding("UTF-8");
			Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8"));
			tmp.process(model, w);
		} catch (Exception e) {
			throw new ComException(ComException.MIN_ERROR_CODE_GLOBAL, e);
		}
	}

}
