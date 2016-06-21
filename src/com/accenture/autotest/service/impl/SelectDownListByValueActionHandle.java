package com.accenture.autotest.service.impl;

import java.util.List;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.accenture.autotest.service.ActionHandle;
public class SelectDownListByValueActionHandle extends ActionHandle {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(SelectDownListByValueActionHandle.class);

	public boolean handle(WebDriver driver, String sheetName, List<String> rowData) {
		boolean res = false;
		try {
			WebDriverWait wait = new WebDriverWait(driver,10);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(this.getXpath())));
			WebElement webElement = driver.findElement(By.xpath(this.getXpath()));
			
			if (webElement != null) {
				Select select = new Select(driver.findElement(By.xpath(this.getXpath())));
				select.selectByValue(this.getValue());
				//select.selectByVisibleText(this.getValue());
				res = true;
			} else {
				this.errorMessageHandle(">>sheet:" + sheetName + ",can not find xpath:"
						+ this.getXpath());
			}

		} catch (Exception ex) {
			this.errorMessageHandle(ex);
		}
		return res;

	}
}
