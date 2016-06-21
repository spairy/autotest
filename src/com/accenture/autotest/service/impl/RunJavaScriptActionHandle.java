package com.accenture.autotest.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import com.accenture.autotest.service.ActionHandle;

public class RunJavaScriptActionHandle extends ActionHandle {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(RunJavaScriptActionHandle.class);

	public boolean handle(WebDriver driver, String sheetName, List<String> rowData) {
		boolean res = true;
		try {
			((JavascriptExecutor) driver).executeScript(this.getValue());
		} catch (Exception ex) {
			this.errorMessageHandle(ex);
		}
		return res;

	}
}
