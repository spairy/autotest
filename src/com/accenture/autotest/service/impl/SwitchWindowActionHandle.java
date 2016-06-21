package com.accenture.autotest.service.impl;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;

import com.accenture.autotest.constant.BusinessConstant;
import com.accenture.autotest.service.ActionHandle;
import com.accenture.autotest.util.PropertyUtil;

public class SwitchWindowActionHandle extends ActionHandle {
	private static Logger logger = Logger.getLogger(SwitchWindowActionHandle.class);

	public boolean handle(WebDriver driver, String sheetName, List<String> rowData) {
		boolean res = false;
		try {
			Set<String> windowhandles = driver.getWindowHandles();
			if(this.getValue().equalsIgnoreCase("Popup")) {
				String firstWindowHandle = driver.getWindowHandle();
				windowhandles.remove(firstWindowHandle);
			}
			try {
				String nextWindow = windowhandles.iterator().next();
				driver.switchTo().window(nextWindow);
			} catch (Exception ex) {
				logger.error(ex);
			}
			
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
				/* Handle the certificate issue in IE, click the "Continue to this website (not recommended)" link */
				if (BusinessConstant.IE_DRIVER.equalsIgnoreCase(driverName) && IEAction.equalsIgnoreCase(BusinessConstant.IE_HANDLE_SSL)) {
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
			}
			res = true;
		} catch (Exception ex) {
			this.errorMessageHandle(ex);
		}
		return res;
	}
}
