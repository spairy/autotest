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

public class VerifyInputBoxValueActionHandle extends ActionHandle {
	private static Logger logger = Logger.getLogger(VerifyInputBoxValueActionHandle.class);

	public boolean handle(WebDriver driver, String sheetName, List<String> rowData) {
		boolean res = false;
		try {
			String xpath = this.getXpath();
			WebDriverWait wait = new WebDriverWait(driver,30);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			WebElement webElement = driver.findElement(By.xpath(xpath));
			
			if (webElement != null) {
			    String actual = webElement.getAttribute("value");
			    String expected = this.getValue();
			    boolean matcherResult = actual.equals(expected);

                logger.info(">>expected content:" + expected + "|");
                logger.info(">>actual content:" + actual + "|");
				logger.info(">>matcher result:" + matcherResult);
				
                if (matcherResult) {
                    //Cannot highlight input box, so highlight the parent element.
                    WebElement parent = webElement.findElement(By.xpath("..")); 
					ScreenUtil.highlightElement(driver, parent);
					ScreenUtil.captureScreen(this.getPrintPath(true), driver);
    				res = true;
                } else {
                    this.errorMessageHandle(">>sheet:" + sheetName + ",VerifyInputBoxValueActionHandle fail, not match:" + webElement);                 
                    //Cannot highlight input box, so highlight the parent element.
                    WebElement parent = webElement.findElement(By.xpath("..")); 
                    ScreenUtil.highlightElement(driver, parent);                    
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
