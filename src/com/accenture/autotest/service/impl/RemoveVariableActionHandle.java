package com.accenture.autotest.service.impl;

import java.util.List;

import org.openqa.selenium.WebDriver;
import com.accenture.autotest.service.ActionHandle;
import com.accenture.autotest.util.UserVariables;

public class RemoveVariableActionHandle extends ActionHandle {

	public boolean handle(WebDriver driver, String sheetName, List<String> rowData) {
		boolean res = false;
		try {
				UserVariables.remove(this.getVariable());
				UserVariables.print();
				res = true;
		} catch (Exception ex) {

			this.errorMessageHandle(ex);
		}
		return res;

	}
}
