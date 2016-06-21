package com.accenture.autotest.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.accenture.autotest.service.ActionHandle;

public class MouseHoverActionHandle extends ActionHandle {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(MouseHoverActionHandle.class);

	public boolean handle(WebDriver driver, String sheetName, List<String> rowData) {
		boolean res = false;
		String osName = System.getProperty("os.name").toLowerCase();
		boolean isMacOs = osName.startsWith("mac os x");
		if (isMacOs) {
			WebDriverWait wait = new WebDriverWait(driver,10);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(this.getXpath())));
			WebElement webElement = driver.findElement(By.xpath(this.getXpath()));

			try {
				String mouseOverScript = "if(document.createEvent) {var evObj = document.createEvent('MouseEvents'); evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) {arguments[0].fireEvent('onmouseover');}";
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript(mouseOverScript, webElement);
				/*((JavascriptExecutor)driver).executeScript("function simulate(f,c,d,e){var b,a=null;for(b in eventMatchers)if(eventMatchers[b].test(c)){a=b;break}if(!a)return!1;document.createEvent?(b=document.createEvent(a),a==\"HTMLEvents\"?b.initEvent(c,!0,!0):b.initMouseEvent(c,!0,!0,document.defaultView,0,d,e,d,e,!1,!1,!1,!1,0,null),f.dispatchEvent(b)):(a=document.createEventObject(),a.detail=0,a.screenX=d,a.screenY=e,a.clientX=d,a.clientY=e,a.ctrlKey=!1,a.altKey=!1,a.shiftKey=!1,a.metaKey=!1,a.button=1,f.fireEvent(\"on\"+c,a));return!0} var eventMatchers={HTMLEvents:/^(?:load|unload|abort|error|select|change|submit|reset|focus|blur|resize|scroll)$/,MouseEvents:/^(?:click|dblclick|mouse(?:down|up|over|move|out))$/}; " +
						 "simulate(arguments[0],\"mousemove\",arguments[1],arguments[2]);",webElement,webElement.getLocation().x,webElement.getLocation().y);*/
				res = true;
			}
			catch (Exception ex){
				this.errorMessageHandle(ex);
				res = false;
			}
		} else {
			try {
				WebDriverWait wait = new WebDriverWait(driver,10);
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(this.getXpath())));
				Actions action = new Actions(driver);
				WebElement webElement = driver.findElement(By.xpath(this.getXpath()));
				
				if (webElement != null) {
					action.moveToElement(webElement);
					action.perform();
					res = true;
				} else {
					this.errorMessageHandle(">>sheet:" + sheetName + ",can not find xpath:" + this.getXpath());
				}

			} catch (Exception ex) {
				this.errorMessageHandle(ex);
				res = false;
			}
		}
		return res;

	}
}
