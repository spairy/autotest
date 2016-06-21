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

public class VerifyPositionActionHandle extends ActionHandle {
	private static Logger logger = Logger.getLogger(VerifyPositionActionHandle.class);

    public final static int Y_OFFSET = 5;
    public final static int X_OFFSET = 5;
    public final static String WILDCARD = "*";
    
	public boolean handle(WebDriver driver, String sheetName, List<String> rowData) {
		boolean res = false;
		try {
			String xpath = this.getXpath();
			WebDriverWait wait = new WebDriverWait(driver,10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			WebElement webElement = driver.findElement(By.xpath(xpath));

			String baselinexpath = this.getAttribute();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(baselinexpath)));
            WebElement baselineWebElement = driver.findElement(By.xpath(baselinexpath));

			if (webElement != null && baselineWebElement != null) {
			    logger.info(">>xpath:" + xpath);
			    logger.info(">>position:" + webElement.getLocation());
			    logger.info(">>baselinexpath:" + baselinexpath);
	            logger.info(">>baseline position:" + baselineWebElement.getLocation());
	            int actualDiffX = webElement.getLocation().getX() - baselineWebElement.getLocation().getX();
	            int actualDiffY = webElement.getLocation().getY() - baselineWebElement.getLocation().getY();
	            logger.info(">>actual diff:" + actualDiffX + "," + actualDiffY);
	            
	            logger.info(">>expected diff:" + this.getValue());
                String[] valueArray = this.getValue().split(",");
                int expectedDiffX = 0;
                boolean ignoreX = false;
                int expectedDiffY = 0;
                boolean ignoreY = false;
                if (valueArray.length > 1) {
                    if (WILDCARD.equalsIgnoreCase(valueArray[0].trim())) {
                        ignoreX = true;
                    } else {
                        expectedDiffX = Integer.parseInt(valueArray[0].trim());
                    }
                    if (WILDCARD.equalsIgnoreCase(valueArray[1].trim())) {
                        ignoreY = true;
                    } else {
                        expectedDiffY = Integer.parseInt(valueArray[1].trim());
                    }
                }
                
                int posDiffX = Math.abs(actualDiffX - expectedDiffX);
                int posDiffY = Math.abs(actualDiffY - expectedDiffY);
                boolean bResult = (ignoreX || posDiffX <= X_OFFSET) && (ignoreY || posDiffY <= Y_OFFSET);
                if (bResult) {               
                    ScreenUtil.highlightElement(driver, webElement);
                    ScreenUtil.captureScreen(this.getPrintPath(true), driver);
                    res = true;
                } else {
                    this.errorMessageHandle(">>sheet:" + sheetName + ",VerifyPositionActionHandle fail");
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
