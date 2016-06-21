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

public class VerifyRadioValueActionHandle extends ActionHandle {
	private static Logger logger = Logger.getLogger(VerifyRadioValueActionHandle.class);

	public boolean handle(WebDriver driver, String sheetName, List<String> rowData) {
		boolean res = false;
		try {
			String xpath = this.getXpath();
			String name = this.getAttribute();
			boolean byXpath = false;
			if (null != xpath && !xpath.isEmpty()) {
			    byXpath = true;
			}
//			WebDriverWait wait = new WebDriverWait(driver,30);
			WebElement webElement = null;
			if (byXpath) {
//    			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    			webElement = driver.findElement(By.xpath(xpath));
			} else {
//                wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(name)));
                List<WebElement> webElementList = driver.findElements(By.name(name));
                for (WebElement element : webElementList) {
                    if (element.isSelected()) {
                        webElement = element;
                    }
                }
                		    
			}
			
			if (webElement != null) {
                boolean matcherResult = false;
			    if (byXpath) {
    			    boolean isSelected = webElement.isSelected();
    			    boolean expected = Boolean.parseBoolean(this.getValue());
    			    if (isSelected == expected) {
    			        matcherResult = true;
    			    }
                    logger.info(">>expected content:" + expected);
                    logger.info(">>actual content:" + isSelected); 			    
			    } else {
			        String actualValue = webElement.getAttribute("value");
			        String expected = this.getValue();
			        matcherResult = expected.matches(actualValue);
                    logger.info(">>expected content:" + expected);
                    logger.info(">>actual content:" + actualValue);
			    }
                logger.info(">>matcher result:" + matcherResult);   	
                
                if (matcherResult) {
                    //Cannot highlight checkbox, so highlight the parent element.
                    WebElement parent = webElement.findElement(By.xpath("..")); 
					ScreenUtil.highlightElement(driver, parent);
					ScreenUtil.captureScreen(this.getPrintPath(true), driver);
    				res = true;
                } else {
                    this.errorMessageHandle(">>sheet:" + sheetName + ",VerifyRadioValueActionHandle fail, not match:" + webElement);                 
                    //Cannot highlight checkbox, so highlight the parent element.
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
