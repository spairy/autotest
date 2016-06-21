package com.accenture.autotest.util;

import java.util.ArrayList;
import java.util.List;

import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;

import com.accenture.autotest.constant.BusinessConstant;
import com.accenture.autotest.domain.TestCaseData;

public class StatisticsUtil {
	private static Logger logger = Logger.getLogger(StatisticsUtil.class);

	private static ArrayList<SummaryData> summaryList = new ArrayList<SummaryData>();
	private static WritableWorkbook book = null;
	
	public static void clearStatistics() {
		summaryList = new ArrayList<SummaryData>();
	}
	
	public static void addCaseData(List<TestCaseData> reportList, String sheetname) {
		SummaryData summaryData = new SummaryData();
		summaryData.setSheetname(sheetname);
		summaryData.setFailedCount(getFailedTotal(reportList));
		summaryData.setPassedCount(getPassedTotal(reportList));
		
		summaryList.add(summaryData);
	}
	
	public static void writeToExcel(WritableWorkbook _book) {
		book = _book;
		WritableSheet sheet;
		
		sheet = book.getSheet(BusinessConstant.SUMMARY_SHEETNAME);
		addHeader(sheet);
		writeToSheet(sheet);
		writeSummary(sheet);
	}
	
	private static void writeSummary(WritableSheet sheet) {
		int noOfPassedTotal = getNoOfPassedTotal();
		int noOfFairedTotal = getNoOfFairedTotal();
		WritableCellFormat wcfTitle = getCommonStyle(Colour.GRAY_25);
		WritableCellFormat wcf = getCommonStyle(null);
		
		int row = sheet.getRows() + BusinessConstant.BLANK_ROWS;
		try {
			sheet.mergeCells(0, row, 1, row);
			sheet.addCell(new Label(0, row, BusinessConstant.SUMARY_TOTAL_NO_OF_PASSED_TEST_CASES, wcfTitle));
			sheet.addCell(new jxl.write.Number(2, row, noOfPassedTotal, wcf));
			
			row++;
			sheet.mergeCells(0, row, 1, row);
			sheet.addCell(new Label(0, row, BusinessConstant.SUMARY_TOTAL_NO_OF_FAILED_TEST_CASES, wcfTitle));
			sheet.addCell(new jxl.write.Number(2, row, noOfFairedTotal, wcf));
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}

	private static int getNoOfPassedTotal() {
		int count = 0;
		for(SummaryData sd: summaryList) {
			if(sd.getStepTotal() == sd.getPassedCount()) {
				count++;
			}
		}
		return count;
	}

	private static int getNoOfFairedTotal() {
		int count = 0;
		for(SummaryData sd: summaryList) {
			if(sd.getStepTotal() != sd.getPassedCount()) {
				count++;
			}
		}
		return count;
	}

	private static void writeToSheet(WritableSheet sheet) {
		WritableCellFormat wcf = getCommonStyle(null);
		for(int i = 0; i < summaryList.size(); i++) {
			int row = i + 1;
			SummaryData sd = summaryList.get(i);
			try {
				sheet.addCell(new Label(0, row, sd.getSheetname(), wcf));
				sheet.addCell(new Label(1, row, getStruts(sd), wcf));
				sheet.addCell(new jxl.write.Number(2, row, sd.getStepTotal(), wcf));
				sheet.addCell(new jxl.write.Number(3, row, sd.getPassedCount(), wcf));
				sheet.addCell(new jxl.write.Number(4, row, sd.getFailedCount(), wcf));
			} catch (RowsExceededException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static String getStruts(SummaryData sd) {
		if(sd.getStepTotal() == sd.getPassedCount() ) {
			return BusinessConstant.SUMARY_STATUS_COMPLETED;
		}
		if(sd.getStepTotal() > sd.getPassedCount()
				&& sd.getPassedCount() > 0) {
			return BusinessConstant.SUMARY_STATUS_PARTIALLY_EXECUTED;
		}
		if(sd.getStepTotal() == sd.getFailedCount()) {
			return BusinessConstant.SUMARY_STATUS_NO_RUN;
		}
		logger.info(">>can't get correct test case struts");
		return "";
	}

	private static void addHeader(WritableSheet sheet) {
		try {
			WritableCellFormat wcf = getHeaderStyle();
			sheet.addCell(new Label(0, 0, BusinessConstant.SUMMARY_SHEET_HEADER_CASE_NAME, wcf));
			sheet.addCell(new Label(1, 0, BusinessConstant.SUMMARY_SHEET_HEADER_STRUTS, wcf));
			sheet.addCell(new Label(2, 0, BusinessConstant.SUMMARY_SHEET_HEADER_TOTAL_STEP, wcf));
			sheet.addCell(new Label(3, 0, BusinessConstant.SUMMARY_SHEET_HEADER_PASSED_COUNT, wcf));
			sheet.addCell(new Label(4, 0, BusinessConstant.SUMMARY_SHEET_HEADER_FAILURE_COUNT, wcf));
			
			sheet.setColumnView(0, 15);
			sheet.setColumnView(1, 13);
			sheet.setColumnView(2, 16);
			sheet.setColumnView(3, 22);
			sheet.setColumnView(4, 20);
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
		
	}
	
	private static WritableCellFormat getHeaderStyle() {
		WritableCellFormat wc = null;
		try {
			wc = new WritableCellFormat();
			wc.setBorder(Border.ALL, BorderLineStyle.THIN);
			wc.setBackground(Colour.YELLOW);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wc;
	}
	
	private static WritableCellFormat getCommonStyle(Colour colour) {
		WritableCellFormat wc = null;
		try {
			wc = new WritableCellFormat();
			wc.setBorder(Border.ALL, BorderLineStyle.THIN);
			
			if(colour != null) {
				wc.setBackground(colour);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wc;
	}

	private static int getPassedTotal(List<TestCaseData> reportList) {
		int passedCount = 0;
		
		for(TestCaseData td : reportList) {
			if(td.getStruts()) {
				passedCount++;
			}
		}
		
		return passedCount;
	}
	private static int getFailedTotal(List<TestCaseData> reportList) {
		int failedCount = 0;
		
		for(TestCaseData td : reportList) {
			if(!td.getStruts()) {
				failedCount++;
			}
		}
		
		return failedCount;
	}
}

class SummaryData{
	private String sheetname;
	private int passedCount;
	private int failedCount;
	
	public String getSheetname() {
		return sheetname;
	}
	public void setSheetname(String sheetname) {
		this.sheetname = sheetname;
	}
	public int getPassedCount() {
		return passedCount;
	}
	public void setPassedCount(int passedCount) {
		this.passedCount = passedCount;
	}
	public int getFailedCount() {
		return failedCount;
	}
	public void setFailedCount(int failedCount) {
		this.failedCount = failedCount;
	}
	public int getStepTotal() {
		return this.passedCount + this.failedCount;
	}
	
	
}
