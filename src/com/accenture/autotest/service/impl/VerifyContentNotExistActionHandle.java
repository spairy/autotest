package com.accenture.autotest.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.accenture.autotest.service.ActionHandle;
import com.accenture.autotest.util.ScreenUtil;

public class VerifyContentNotExistActionHandle extends ActionHandle {
	private static Logger logger = Logger.getLogger(VerifyContentNotExistActionHandle.class);

	public boolean handle(WebDriver driver, String sheetName, List<String> rowData) {
		boolean res = false;
		try {
			String xpath = this.getXpath();
			WebDriverWait wait = new WebDriverWait(driver,10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			WebElement webElement = driver.findElement(By.xpath(xpath));
			if (webElement != null) {
				int posX = webElement.getLocation().getX();
				int posY = webElement.getLocation().getY();
				boolean posResult = (posX >= 0) && (posY >= 0);
                logger.info(">>position:" + webElement.getLocation());

                if (posResult) {				
					ScreenUtil.highlightElement(driver, webElement);
					ScreenUtil.captureScreen(this.getPrintPath(true), driver);
    				res = true;
                } else {
                    this.errorMessageHandle(">>sheet:" + sheetName + ",VerifyContentNotExistActionHandle fail, content is hidden:" + webElement);
                    ScreenUtil.captureScreen(this.getPrintPath(false), driver);                    
                }
			} else {
                logger.info(">>sheet:" + sheetName + ",can not find xpath:" + xpath);
			    res = true;
			}
		} catch (TimeoutException timeoutEx) {
            logger.info(">>sheet:" + sheetName + ",can not find element");
            res = true;
		} catch (Exception ex) {
            this.errorMessageHandle(ex);
        }
		return res;

	}
}
