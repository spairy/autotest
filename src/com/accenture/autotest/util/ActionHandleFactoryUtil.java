package com.accenture.autotest.util;

import org.apache.log4j.Logger;

import com.accenture.autotest.constant.BusinessConstant;
import com.accenture.autotest.service.ActionHandle;

public class ActionHandleFactoryUtil {
	private static Logger logger = Logger.getLogger(ActionHandleFactoryUtil.class);

	public static ActionHandle getActionHandle(String actionType) {
		ActionHandle actionHandle = null;
		String fullClassName = BusinessConstant.HANDLE_CLASS_NAME_PREFIX + actionType + "ActionHandle";
		try {
			actionHandle = (ActionHandle) Class.forName(fullClassName).newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
		return actionHandle;
	}
	
	public static boolean isNull(String input) {
		return input.trim().length() == 0;
	}
}
