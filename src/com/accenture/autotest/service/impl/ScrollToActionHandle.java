package com.accenture.autotest.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.accenture.autotest.constant.BusinessConstant;
import com.accenture.autotest.service.ActionHandle;

public class ScrollToActionHandle extends ActionHandle {

	private static Logger logger = Logger.getLogger(ScrollToActionHandle.class);

	public boolean handle(WebDriver driver, String sheetName, List<String> rowData) {
		boolean res = false;
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			String scrollToLocation = this.getValue();
			String script = "";
			if (BusinessConstant.WINDOW_SCROLL_TOP.equalsIgnoreCase(scrollToLocation)) {
			    script += "window.scrollTo(0,0);";
			} else if (BusinessConstant.WINDOW_SCROLL_BOTTOM.equalsIgnoreCase(scrollToLocation)) {
			    script += "window.scrollTo(0, Math.max(document.documentElement.scrollHeight, document.body.scrollHeight, document.documentElement.clientHeight));";
			} else {
			    script += "window.scrollBy(" + scrollToLocation + ");";
			}
			logger.info("scroll To:" + scrollToLocation);
			js.executeScript(script, "");
			res = true;
		} catch (Exception ex) {
			this.errorMessageHandle(ex);
		}
		return res;
	}
}
