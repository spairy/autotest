package com.accenture.autotest.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.accenture.autotest.constant.BusinessConstant;
import com.accenture.autotest.service.ActionHandle;
import com.accenture.autotest.util.PropertyUtil;

public class ResizeWindowActionHandle extends ActionHandle {

	private static Logger logger = Logger.getLogger(ResizeWindowActionHandle.class);

	public boolean handle(WebDriver driver, String sheetName, List<String> rowData) {
		boolean res = false;
      String driverName = PropertyUtil.getProperty(BusinessConstant.CONFIG_DRIVER_KEY);
        boolean isRemoteWebDriver = false;
        if(BusinessConstant.SAFARIIOS_DRIVER.equalsIgnoreCase(driverName) || 
                BusinessConstant.CHROMEANDROID_DRIVER.equalsIgnoreCase(driverName)) {
            isRemoteWebDriver = true;
        }
        if (!isRemoteWebDriver) {
    		try {
    			logger.info("ResizeWindow Starts.");
    			String windowSize = this.getValue();
    			int width = 0;
    			int height = 0;
    			if (null != windowSize && !windowSize.isEmpty()){
    			    if (BusinessConstant.WINDOW_SMALL.equalsIgnoreCase(windowSize)){
    			        width = BusinessConstant.WINDOW_SMALL_WIDTH;
    			        height = BusinessConstant.WINDOW_SMALL_HEIGHT;
    			    } else if (BusinessConstant.WINDOW_MEDIUM.equalsIgnoreCase(windowSize)) {
                        width = BusinessConstant.WINDOW_MEDIUM_WIDTH;
                        height = BusinessConstant.WINDOW_MEDIUM_HEIGHT;			        
    			    } else if (BusinessConstant.WINDOW_LARGE.equalsIgnoreCase(windowSize)) {
                        width = BusinessConstant.WINDOW_LARGE_WIDTH;
                        height = BusinessConstant.WINDOW_LARGE_HEIGHT;			        
    			    } else {
    			        String[] windowSizeValues = windowSize.split(",");
    			        if (windowSizeValues.length == 2) {
    			            width = Integer.parseInt(windowSizeValues[0].trim());
    			            height = Integer.parseInt(windowSizeValues[1].trim());
    			        }
    			    }
    		        logger.info("Resize Window to " + windowSize);
    		        //Wait to resolve Firefox issue
    		        if (driver instanceof FirefoxDriver){
    		            Thread.sleep(2000);
    		        }
    		        if (BusinessConstant.WINDOW_MAXIMIUM.equalsIgnoreCase(windowSize)){
    		            driver.manage().window().maximize();
    		        } else {
        		        driver.manage().window().setPosition(new Point(0, 0));
        		        driver.manage().window().setSize(new Dimension(width, height));
        		        driver.manage().window().setPosition(new Point(0, 0));	   
        		        //Set twice to resolve Firefox issue
        		        driver.manage().window().setSize(new Dimension(width, height));
        		        driver.manage().window().setPosition(new Point(0, 0));
    		        }
    			}
    			res = true;
    		} catch (Exception ex) {
    			this.errorMessageHandle(ex);
    		}
        } else {
            res = true;
        }
		return res;
	}
}
