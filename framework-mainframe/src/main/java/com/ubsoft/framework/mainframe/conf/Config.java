package com.ubsoft.framework.mainframe.conf;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class Config {

	private static String path;
	private static Properties config;

	public static void load(String path) {
		if (config == null) {
			config = new Properties();
			loadConfig(path);
			Config.path = path;
		}
	}

	private static void loadConfig(String path) {
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(path);
			config.clear();
			config.load(fis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void saveConfig(String comments) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path);
			config.store(fos, comments);		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void saveProperty(String key, String value, String comments) {
		setProperty(key, value);
		saveConfig(comments);
	}

	public static void saveProperty(String key, String value) {
		saveProperty(key, value, null);
	}

	public static void setProperty(String key, String value) {
		config.setProperty(key, value);
	}

	public static String getProperty(String key) {
		return config.getProperty(key);
	}

	public String getProperty(String key, String defaultValue) {
		String value = getProperty(key);
		value = value == null ? defaultValue : value;
		return value;
	}

	public void remove(Object key) {
		config.remove(key);
		
	}
}