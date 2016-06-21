package com.accenture.autotest.util;

import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.accenture.autotest.constant.BusinessConstant;

public class TestCaseFilter {

	private static Logger logger = Logger.getLogger(TestCaseFilter.class);
	private static Set<String> caseSet = null;
	
	static{
			Properties properties = PropertyUtil.load();
			String testCaseFilterListKey = BusinessConstant.TEST_CASE_FILTER_LIST_KEY;
			if(properties.containsKey(testCaseFilterListKey)) {
				String key = properties.getProperty(testCaseFilterListKey);
				if(!"".equals(key)) {
					caseSet = new LinkedHashSet<String>();
					for(String val : key.split(",")) {
						logger.info(">>add case:" + val);
						caseSet.add(val.toLowerCase());
					}
				} else {
					logger.info(">>run all case!");
				}
			} else {
				logger.info(">>" + testCaseFilterListKey + " not found in " + BusinessConstant.CONFIG_PROPERTY_NAME);
			}
	}
	
	public static boolean doFilter(String key) {
		if(caseSet == null) {
			return true;
		}
		return caseSet.contains(key.toLowerCase());
	}
	
}
