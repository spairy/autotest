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

public class CaptureScreenActionHandle extends ActionHandle {
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(CaptureScreenActionHandle.class);

    @SuppressWarnings("unused")
    public boolean handle(WebDriver driver, String sheetName, List<String> rowData) {
        boolean res = false;
        try {
            String xpath = this.getXpath();
            if (null != xpath && !xpath.isEmpty()) {
              //Capture with highlight
                WebDriverWait wait = new WebDriverWait(driver, 10);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
                WebElement webElement = driver.findElement(By.xpath(xpath));      
                if (webElement != null) {
                    ScreenUtil.highlightElement(driver, webElement);
                    ScreenUtil.captureScreen(this.getPrintPath(true), driver);
                }
            } else {
                //Capture without highlight
                String stepTimeStr = ScreenUtil.getSetpTime(this.getStep());
                ScreenUtil.captureScreen(this.getPrintPath(true), driver);
            }
            res = true;
        } catch (Exception ex) {
            this.errorMessageHandle(ex);
        }
        return res;

    }

    // public boolean handle(WebDriver driver, String sheetName, List<String>
    // rowData) {
    // boolean res = false;
    // try {
    // ScreenUtil.captureScreen(ScreenUtil.getPrintPathForSuccess(this.getScreenPath(),
    // sheetName, this.getStep()),driver);
    // res = true;
    // } catch (Exception ex) {
    // logger.error(ex.getMessage(), ex);
    // }
    // return res;
    //
    // }
}
