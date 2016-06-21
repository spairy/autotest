package com.accenture.autotest.service.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.accenture.autotest.service.ActionHandle;
import com.accenture.autotest.util.UserVariables;

public class SaveVariableActionHandle extends ActionHandle {

	@Override
	public boolean handle(WebDriver driver, String sheetName, List<String> rowData) {
		boolean res = false;
		try {
			String xpath = this.getXpath();
			WebDriverWait wait = new WebDriverWait(driver,10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			WebElement webElement = driver.findElement(By.xpath(xpath));
			if (webElement != null) {
				UserVariables.add(this.getVariable(), webElement.getText().trim());
				UserVariables.print();
				res = true;
			} else {
				this.errorMessageHandle(">>sheet:" + sheetName + ",can not find xpath:"	+ xpath);
			}

		} catch (Exception ex) {

			this.errorMessageHandle(ex);
		}
		return res;

	}
}
