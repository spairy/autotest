package com.accenture.autotest.service.impl;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.accenture.autotest.constant.BusinessConstant;
import com.accenture.autotest.service.ActionHandle;
import com.accenture.autotest.util.PropertyUtil;

public class ObjectClickActionHandle extends ActionHandle {
	private static Logger logger = Logger.getLogger(ObjectClickActionHandle.class);

	public boolean handle(WebDriver driver, String sheetName, List<String> rowData) {
		boolean res = false;
		String driverName = PropertyUtil.getProperty(BusinessConstant.CONFIG_DRIVER_KEY);
		boolean isRemoteWebDriver = false;
		if(BusinessConstant.SAFARIIOS_DRIVER.equalsIgnoreCase(driverName) || 
		        BusinessConstant.CHROMEANDROID_DRIVER.equalsIgnoreCase(driverName)) {
		    isRemoteWebDriver = true;
		}

		if (this.getAttribute().isEmpty()) {
			try {
				WebDriverWait wait = new WebDriverWait(driver,200);
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(this.getXpath())));
				Actions action = new Actions(driver);
				WebElement webElement = driver.findElement(By.xpath(this.getXpath()));
				
				if (webElement != null) {
					if (isRemoteWebDriver) {
						webElement.sendKeys(Keys.CONTROL);
					}
					if (driver instanceof ChromeDriver) {
    	                action.moveToElement(webElement);
    	                action.click();
    	                action.perform();
    	                Thread.sleep(700);
					} else {
					    webElement.click();
					}
					res = true;
				} else {
					this.errorMessageHandle(">>sheet:" + sheetName + ",can not find xpath:" + this.getXpath());
				}

			} catch (Exception ex) {
				this.errorMessageHandle(ex);
			}
		} else {
			try {
				String IEAction = "";
				String firefoxAction = "";
				String data[] = this.getAttribute().split(",");
				for(String value : data) {
					if (value.trim().equalsIgnoreCase(BusinessConstant.CONTINUE_BUTTON)) {
						firefoxAction = BusinessConstant.CONTINUE_BUTTON;
					} else if (value.trim().equalsIgnoreCase(BusinessConstant.CONTINUE_BUTTON_POPUP)) {
						firefoxAction = BusinessConstant.CONTINUE_BUTTON_POPUP;
					} else if (value.trim().equalsIgnoreCase(BusinessConstant.CANCEL_BUTTON)) {
						firefoxAction = BusinessConstant.CANCEL_BUTTON;
					} else if (value.trim().equalsIgnoreCase(BusinessConstant.IE_HANDLE_SSL)) {
						IEAction = BusinessConstant.IE_HANDLE_SSL;
					}
				}

				WebDriverWait wait = new WebDriverWait(driver,10);
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(this.getXpath())));
				WebElement webElement = driver.findElement(By.xpath(this.getXpath()));
				
				if (webElement != null) {
					webElement.click();
					res = true;
				} else {
					this.errorMessageHandle(">>sheet:" + sheetName + ",can not find xpath:" + this.getXpath());
				}

				/* Handle the exception in Firefox */
				if (BusinessConstant.FIREFOX_DRIVER.equalsIgnoreCase(driverName)) {
					/* Switch to the new window with alert box, click the "Continue" button */
					if(firefoxAction.equalsIgnoreCase(BusinessConstant.CONTINUE_BUTTON_POPUP)){
						Set<String> windowhandles = driver.getWindowHandles();
						String nextWindow = windowhandles.iterator().next();
						driver.switchTo().window(nextWindow);
						firefoxAction = BusinessConstant.CONTINUE_BUTTON;
					}
					
					/* Alert box, click the "Continue" or "Cancel" button */
					Alert alert = driver.switchTo().alert();
					if(firefoxAction.equalsIgnoreCase(BusinessConstant.CONTINUE_BUTTON)){
						alert.accept();
						res=true;
					} else if(firefoxAction.equalsIgnoreCase(BusinessConstant.CANCEL_BUTTON)){
						alert.dismiss();
						res=true;
					}
				/* Handle the certificate issue for payment in IE, click the "Continue to this website (not recommended)" link */
				} else if (BusinessConstant.IE_DRIVER.equalsIgnoreCase(driverName) & IEAction.equalsIgnoreCase(BusinessConstant.IE_HANDLE_SSL)) {
					try {
						driver.navigate().to("javascript:var id=document.getElementById('overridelink'); if(id!==null) id.click(); else alert('No Certificate');"); // the alert box is a workaround for IE without certificate.
						Alert alert = driver.switchTo().alert();
						if (alert!=null) {
							alert.accept();
							Thread.sleep(2000);
						}
					} catch (Exception ex) {
						logger.error(ex);
					}
				}

			} catch (Exception ex) {
				this.errorMessageHandle(ex);
			}

		}
		return res;
	}
}