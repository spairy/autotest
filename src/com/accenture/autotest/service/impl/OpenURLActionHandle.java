package com.accenture.autotest.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.accenture.autotest.constant.BusinessConstant;
import com.accenture.autotest.service.ActionHandle;
import com.accenture.autotest.util.PropertyUtil;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OpenURLActionHandle extends ActionHandle {
	private static Logger logger = Logger.getLogger(OpenURLActionHandle.class);

	public boolean handle(WebDriver driver, String sheetName, List<String> rowData) {
		boolean res = false;
		try {
			driver.get(this.getValue());
			res = true;
		} catch (Exception ex) {
			this.errorMessageHandle(ex);
		}
		
		// to ignore security certificate in IE9 and IE10
		if(!this.getAttribute().isEmpty()) {
			String IEAction = "";
			String data[] = this.getAttribute().split(",");
			for(String value : data) {
				if (value.trim().equalsIgnoreCase(BusinessConstant.IE_HANDLE_SSL)) {
					IEAction = BusinessConstant.IE_HANDLE_SSL;
					break;
				}
			}
			
			String driverName = PropertyUtil.getProperty(BusinessConstant.CONFIG_DRIVER_KEY);
			if (BusinessConstant.IE_DRIVER.equalsIgnoreCase(driverName) && IEAction.equalsIgnoreCase(BusinessConstant.IE_HANDLE_SSL)) {
				try {
					driver.navigate().to("javascript:var id=document.getElementById('overridelink'); if(id!==null) id.click();"); // the alert box is a workaround for IE without certificate.
					Alert alert = driver.switchTo().alert();
					if (alert!=null) {
						alert.accept();
						Thread.sleep(2000);
					}
				} catch (Exception ex) {
					logger.error(ex);
				}
			}
		}
		
		// to eliminate the "Closed and continue" div in the homepage
		String xpathCheckbox = ".//*[@id='close-dark-site-pop-up']";
		String xpathCloseBtn = ".//*[@id='dimforeground']/div[1]";
		try {
			WebElement webElementCheckbox = driver.findElement(By.xpath(xpathCheckbox));
			WebElement webElementCloseBtn = driver.findElement(By.xpath(xpathCloseBtn));
			WebDriverWait wait = new WebDriverWait(driver,2);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathCheckbox)));
			webElementCheckbox.click();
			webElementCloseBtn.click();
		} catch (Exception ex) {}
		
		return res;
	}
}