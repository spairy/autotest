package com.accenture.autotest.util;

import java.util.List;

public class ExcelData {
	private String sheetName;
	private List<List<String>> sheetData;
	private int runStepCount = 0;
	private boolean isPass = true;
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public List<List<String>> getSheetData() {
		return sheetData;
	}
	public void setSheetData(List<List<String>> sheetData) {
		this.sheetData = sheetData;
	}
	public ExcelData(String sheetName, List<List<String>> sheetData) {
		this.sheetName = sheetName;
		this.sheetData = sheetData;
	}
	public int getRunStepCount() {
		return runStepCount;
	}
	public void addRunStepCount() {
		this.runStepCount++;
	}
	public boolean isPass() {
		return isPass;
	}
	public void setPass(boolean isPass) {
		this.isPass = isPass;
	}
	

}
