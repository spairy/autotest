package com.accenture.autotest.util;

import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;


public class UserVariables {
	private static Logger logger = Logger.getLogger(UserVariables.class);
	private static HashMap<String, String> userVariables = new HashMap<String, String>();
	
	public static void add(String name, String value) {
		try {
			userVariables.put(name, value);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}
	
	public static void remove(String name) {
		try {
			userVariables.remove(name);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}
	
	public static void clear() {
		try {
			userVariables.clear();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}
	
	public static String get(String name){
		String value = "";
		try {
			value = userVariables.get(name);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return value;
	}
	
	public static void print() {
		try {
			logger.info(">>UserVariables: ");
			for(Entry<String, String> entry : userVariables.entrySet()) {
			    logger.info(entry.getKey() + "->" + entry.getValue());
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}
}
