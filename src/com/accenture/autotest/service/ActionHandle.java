package com.accenture.autotest.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.accenture.autotest.constant.BusinessConstant;
import com.accenture.autotest.domain.TestCaseData;
import com.accenture.autotest.util.ScreenUtil;

public abstract class ActionHandle {
	private static Logger logger = Logger.getLogger(ActionHandle.class);
	private String xpath;
	private String attribute;
	private String value;
	private String variable;
	private String step;
	private String screenPath;
	private long startTime;
	private long timestamp;
	private String sheetName;
	private String fileRelativePaths;
	private String errorMessage;
	public abstract boolean handle(WebDriver driver, String sheetName, List<String> rowData);
	public boolean serviceHandle(WebDriver driver, String sheetName, List<String> rowData, List<TestCaseData> reportList, String screenShotPath, boolean preRes){
		parseRowData(rowData);
		this.sheetName = sheetName;
		screenPath = screenShotPath;
		startTime = System.currentTimeMillis();
		
		boolean res = false;
		if(preRes) {
			res =  handle(driver,sheetName,rowData);
		}
		
		timestamp = System.currentTimeMillis() - startTime;
		generateReport(reportList, res);
		if(!res){
			logger.error(">>sheet:" + sheetName + ",fail step:"
					+ this.step);
		}
		return res;
	}
	
	private void generateReport(List<TestCaseData> reportList, boolean res) {
		TestCaseData data = new TestCaseData();
		data.setSetp(step);
		data.setStruts(res);
		data.setTimestamp(timestamp);
		data.setFileRelativePaths(fileRelativePaths);
		data.setTimeElapsed(startTime);
		data.setErrorMessage(errorMessage);
		
		reportList.add(data);
	}
	public void parseRowData(List<String> rowData){
		xpath = rowData.get(BusinessConstant.EXCEL_XPATH);
		attribute = rowData.get(BusinessConstant.EXCEL_ATTRIBUTE);
		value = rowData.get(BusinessConstant.EXCEL_VALUE);
		step = rowData.get(BusinessConstant.EXCEL_STEP);
		variable = rowData.get(BusinessConstant.EXCEL_VARIABLE);
	}
	public String getXpath() {
		return xpath;
	}
	public void setXpath(String xpath) {
		this.xpath = xpath;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getVariable() {
		return variable;
	}
	public void setVariable(String variable) {
		this.variable = variable;
	}
	public String getStep() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		return step +"_"+ (sdf.format(new Date()));
	}
	public void setStep(String step) {
		this.step = step;
	}
	public String getScreenPath() {
		return screenPath;
	}
	public void setScreenPath(String screenPath) {
		this.screenPath = screenPath;
	}
	//ScreenUtil.getPrintPathForSuccess(this.getScreenPath(), sheetName, stepTimeStr)
	public String getPrintPath(boolean success) {
		String path;
		if(success) {
			path = ScreenUtil.getPrintPathForSuccess(this.getScreenPath(), sheetName, ScreenUtil.getSetpTime(step));
		}else {
			path = ScreenUtil.getPrintPathForFail(this.getScreenPath(), sheetName, ScreenUtil.getSetpTime(step));
		}
		
		fileRelativePaths = sheetName + File.separator + path.substring(path.lastIndexOf(File.separator)+1);
		
		return path;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	@SuppressWarnings("static-access")
	public void errorMessageHandle(String errorMessage) {
		this.logger.error(errorMessage);
		this.setErrorMessage(errorMessage);
	}
	
	@SuppressWarnings("static-access")
	public void errorMessageHandle(Exception e) {
		this.logger.error(e.getMessage(), e);
		this.setErrorMessage(e.getMessage());
	}
	
}
