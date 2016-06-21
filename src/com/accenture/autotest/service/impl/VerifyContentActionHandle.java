package com.accenture.autotest.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.accenture.autotest.service.ActionHandle;
import com.accenture.autotest.util.ScreenUtil;
import com.accenture.autotest.util.UserVariables;

public class VerifyContentActionHandle extends ActionHandle {
	private static Logger logger = Logger.getLogger(VerifyContentActionHandle.class);

	public boolean handle(WebDriver driver, String sheetName, List<String> rowData) {
		boolean res = false;
		String contextValue= null;
		try {
			String xpath = this.getXpath();
			WebDriverWait wait = new WebDriverWait(driver,100);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			WebElement webElement = driver.findElement(By.xpath(xpath));
			if (webElement != null) {
				if(!this.getAttribute().equals(""))     
                    contextValue= webElement.getAttribute(this.getAttribute());
				String text = webElement.getText().trim();
				String pattern = "";
				if(this.getVariable() != null && !this.getVariable().isEmpty()){
					pattern = UserVariables.get(this.getVariable());
				} else {
					pattern = this.getValue().trim();
				}
				Boolean bResult = text.matches(pattern);
				logger.info(">>expected content:" + pattern);
				logger.info(">>actual content:" + text);
				logger.info(">>matcher result:" + bResult);
				if (bResult) {
					ScreenUtil.highlightElement(driver, webElement);
					ScreenUtil.captureScreen(this.getPrintPath(true), driver);
					res = true;
				} else if (contextValue != null && contextValue.matches(pattern)) {
					ScreenUtil.highlightElement(driver, webElement);
					ScreenUtil.captureScreen(this.getPrintPath(false), driver);
					res = true;

				} else {
					this.errorMessageHandle(">>sheet:" + sheetName + ",VerifyContentActionHandle fail, content doesn't exist:" + pattern);
					ScreenUtil.captureScreen(this.getPrintPath(false), driver);
				}
			} else {
				this.errorMessageHandle(">>sheet:" + sheetName + ",can not find xpath:"	+ xpath);
			}

		} catch (Exception ex) {

			this.errorMessageHandle(ex);
		}
		return res;

	}
}
