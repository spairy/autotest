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
import com.gargoylesoftware.htmlunit.javascript.host.Window;

public class VerifySizeActionHandle extends ActionHandle {
	private static Logger logger = Logger.getLogger(VerifySizeActionHandle.class);

    public final static int HEIGHT_PX_OFFSET = 4;
    public final static int WIDTH_PX_OFFSET = 4;
    public final static int HEIGHT_PERCENT_OFFSET = 1;
    public final static int WIDTH_PERCENT_OFFSET = 1;
    public final static String PERCENTAGE = "%";
	
	public boolean handle(WebDriver driver, String sheetName, List<String> rowData) {
		boolean res = false;
		try {
			String xpath = this.getXpath();
			WebDriverWait wait = new WebDriverWait(driver,10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			WebElement webElement = driver.findElement(By.xpath(xpath));
			if (webElement != null) {
			    String[] valueArray = this.getValue().split(",");
                int expectedWidth = 0;
			    int expectedHeight = 0;
			    boolean percentageCheckWidth = false;
			    boolean percentageCheckHeight = false;
			    if (valueArray.length > 1) {
			        if (valueArray[0].trim().endsWith(PERCENTAGE)) {
			            percentageCheckWidth = true;
			            String percentageString = valueArray[0].trim().substring(0, valueArray[0].trim().length() - 1);
			            expectedWidth = Integer.parseInt(percentageString);
			        } else {
			            expectedWidth = Integer.parseInt(valueArray[0].trim());
			        }
			        if (valueArray[1].trim().endsWith(PERCENTAGE)) {
			            percentageCheckHeight = true;
                        String percentageString = valueArray[1].trim().substring(0, valueArray[1].trim().length() - 1);
                        expectedHeight = Integer.parseInt(percentageString);			            
			        } else {
			            expectedHeight = Integer.parseInt(valueArray[1].trim());
			        }
			    }
			    int actualWidth = webElement.getSize().getWidth();
			    int actualHeight = webElement.getSize().getHeight();
			    logger.info(">>webElement:" + webElement);
			    logger.info(">>expected value:" + this.getValue());
			    logger.info(">>actual dimension:" + webElement.getSize());
			    
			    boolean widthResult = false;
			    boolean heightResult = false;
			    if (percentageCheckWidth) {
			        int windowWidth = driver.manage().window().getSize().getWidth();
			        double actualWidthPercentage = (actualWidth * 100 / windowWidth);
			        logger.info(">>actual width percentage:" + actualWidthPercentage);
			        widthResult = (expectedWidth - WIDTH_PERCENT_OFFSET) <= actualWidthPercentage && actualWidthPercentage <= (expectedWidth + WIDTH_PERCENT_OFFSET);
			    } else {
	                int widthDifference = Math.abs(actualWidth - expectedWidth);
	                widthResult = widthDifference <= WIDTH_PX_OFFSET;			        
			    }
			    if (percentageCheckHeight) {
			        int windowHeight = driver.manage().window().getSize().getHeight();
			        double actualHeightPercentage = (actualHeight * 100 / windowHeight);
			        logger.info(">>actual height percentage:" + actualHeightPercentage);
			        heightResult  = (expectedHeight - HEIGHT_PERCENT_OFFSET) <= actualHeightPercentage && actualHeightPercentage <= (expectedHeight + HEIGHT_PERCENT_OFFSET);
			    } else {
	                int heightDifference = Math.abs(actualHeight - expectedHeight);
	                heightResult = heightDifference <= HEIGHT_PX_OFFSET;			        
			    }

			    boolean bResult = heightResult && widthResult;
			    if (bResult) {               
                    ScreenUtil.highlightElement(driver, webElement);
                    ScreenUtil.captureScreen(this.getPrintPath(true), driver);
                    res = true;
                } else {
                    this.errorMessageHandle(">>sheet:" + sheetName + ",VerifySizeActionHandle fail");
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
