package com.accenture.autotest.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.accenture.autotest.constant.BusinessConstant;

public class PropertyUtil {
	private static Logger logger = Logger.getLogger(PropertyUtil.class);
	private static Properties properties = new Properties();
	private static String configPath = "";
	
	public static Properties load() {
		try {
			if(configPath.isEmpty()) {
				String classPath = PropertyUtil.class.getClassLoader().getResource("").getPath();
				logger.info(">>classPath:" + classPath);
				configPath = classPath + BusinessConstant.CONFIG_PROPERTY_NAME;
			} 
			properties.load(new FileInputStream(new File(configPath)));
		} catch (IOException ex) {
			logger.error(ex.getMessage(), ex);
		}
		return properties;
	}
	public static Properties getProperties() {
		return properties;
	}
	
	public static String getProperty(String PropertyKey) {
		return properties.getProperty(PropertyKey);
	}
	
	public static void setConfigPath(String filePath) {
		configPath = filePath;
	}
}
