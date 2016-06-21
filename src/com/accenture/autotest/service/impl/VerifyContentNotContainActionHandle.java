package com.accenture.autotest.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.accenture.autotest.constant.BusinessConstant;
import com.accenture.autotest.service.ActionHandle;
import com.accenture.autotest.util.ScreenUtil;
import com.accenture.autotest.util.UserVariables;

public class VerifyContentNotContainActionHandle extends ActionHandle {
	private static Logger logger = Logger.getLogger(VerifyContentNotContainActionHandle.class);

	public boolean handle(WebDriver driver, String sheetName, List<String> rowData) {
		boolean res = false;
		try {
			String xpath = this.getXpath();
			String attribute = "";
			WebDriverWait wait = new WebDriverWait(driver,10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			WebElement webElement = driver.findElement(By.xpath(xpath));
			if (webElement != null) {
				if(!this.getAttribute().equals("")){   
					attribute= this.getAttribute();
				}
				String content = "";
				String pattern = "";
				if(this.getVariable() != null && !this.getVariable().isEmpty()){
					pattern = UserVariables.get(this.getVariable());
				} else {
					pattern = this.getValue().trim();
				}
				Boolean validateContent = false;
				if(attribute.equalsIgnoreCase(BusinessConstant.HTML_VALUE)){
					content = (String)((JavascriptExecutor)driver).executeScript("return arguments[0].innerHTML;", webElement);
					validateContent = !content.contains(pattern);
				} else if(attribute.equalsIgnoreCase(BusinessConstant.HTML_PATTERN)){
					content = (String)((JavascriptExecutor)driver).executeScript("return arguments[0].innerHTML;", webElement);
					validateContent = !content.matches(pattern);
				} if(attribute.equalsIgnoreCase(BusinessConstant.ELEMENT_TEXT_VALUE)){
					content = webElement.getText().trim();
					validateContent = !content.contains(pattern);
				} else if(attribute.equalsIgnoreCase(BusinessConstant.ELEMENT_TEXT_PATTERN)){
					content = webElement.getText().trim();
					validateContent = !content.matches(pattern);
				}
				logger.info(">>not expected content:" + pattern);
				logger.info(">>actual content:" + content);
				logger.info(">>validate result:" + validateContent);
				if (validateContent) {
					ScreenUtil.highlightElement(driver, webElement);
					ScreenUtil.captureScreen(this.getPrintPath(true), driver);
					res = true;
				} else {
					this.errorMessageHandle(">>sheet:" + sheetName + ",VerifyContentNotExistActionHandle fail, content exist:" + pattern);
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
