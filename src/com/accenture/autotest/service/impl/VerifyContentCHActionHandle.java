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

public class VerifyContentCHActionHandle extends ActionHandle {
	private static Logger logger = Logger.getLogger(VerifyContentCHActionHandle.class);

	public boolean handle(WebDriver driver, String sheetName, List<String> rowData) {
		boolean res = false;
		String contextValue= null;
		try {
			WebDriverWait wait = new WebDriverWait(driver,10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(this.getXpath())));
			WebElement webElement = driver.findElement(By.xpath(this.getXpath()));
			if (webElement != null) {
				if(!this.getAttribute().equals(""))     
                    contextValue= webElement.getAttribute(this.getAttribute());
				logger.info(">>webElement.getText():" + webElement.getText());
				logger.info(">>this.getValue():" + this.getValue());
				logger.info(">>result:" + webElement.getText().trim().equalsIgnoreCase(this.getValue().trim()));
				//verify a element contains some strings
				if (webElement.getText().trim().contains(this.getValue().trim())) {
					ScreenUtil.highlightElement(driver, driver.findElement(By.xpath(this.getXpath())));
					ScreenUtil.captureScreen(this.getPrintPath(true), driver);
					res = true;
				} else if (contextValue != null && contextValue.contains(this.getValue())) {
					ScreenUtil.highlightElement(driver, driver.findElement(By.xpath(this.getXpath())));
					ScreenUtil.captureScreen(this.getPrintPath(false), driver);
					res = true;

				} else {
					this.errorMessageHandle(">>sheet:" + sheetName
							+ ",VerifyContentActionHandle fail, content not exist:" + this.getValue());
					ScreenUtil.captureScreen(this.getPrintPath(false), driver);
				}
			} else {
				this.errorMessageHandle(">>sheet:" + sheetName + ",can not find xpath:"
						+ this.getXpath());
			}

		} catch (Exception ex) {
			this.errorMessageHandle(ex.getMessage());
		}
		return res;

	}
}
