package com.accenture.autotest.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;

import com.accenture.autotest.constant.BusinessConstant;
import com.accenture.autotest.domain.TestCaseData;

public class ExcelWriterUtil {
	private static Logger logger = Logger.getLogger(ExcelWriterUtil.class);
	private static WritableWorkbook book = null;
	private static WritableSheet sheet = null;
	private static String CurrentDriver = null;
	private static String versionNo = null;
	public static boolean writeToExcel(List<TestCaseData> reportList, String sheetName){
		createSheet(sheetName);
		
		for(int i = 0; i < reportList.size(); i++) {
			addContent(reportList.get(i), i);
		}
		
		try {
			addStatisticalFormula();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	public static WritableWorkbook initExcel(String fileName) {
		createExcel(fileName);
		createSheet(BusinessConstant.SUMMARY_SHEETNAME);
		return book;
	}
	
	private static void addStatisticalFormula() throws RowsExceededException, WriteException {
		int dataRows = sheet.getRows();
		int rows = dataRows + BusinessConstant.BLANK_ROWS;
		
		WritableCellFormat style = getStyle(Colour.GRAY_25);
		
		sheet.addCell(new Label(0, rows, "StepCount:", style));
		Formula formula = new Formula(1, rows, "COUNTA("+ getPoint(0, 1, dataRows) +")", style);
		sheet.addCell(formula);
		
		rows++;
		sheet.addCell(new Label(0, rows, "Success:", style));
		Formula formula2 = new Formula(1, rows, "COUNTIF("+ getPoint(1, 1, dataRows) +", \""+BusinessConstant.STEP_SUCCESS+"\")", style);
		sheet.addCell(formula2);
		
		rows++;
		sheet.addCell(new Label(0, rows, "Faill:", style));
		Formula formula3 = new Formula(1, rows, "COUNTIF("+ getPoint(1, 1, dataRows) +", \""+BusinessConstant.STEP_FAIL+"\")", style);
		sheet.addCell(formula3);
		
		rows++;
		sheet.addCell(new Label(0, rows, "TimeCount:", style));
		Formula formula4 = new Formula(1, rows, "SUM("+ getPoint(3, 1, dataRows) +")", style);
		sheet.addCell(formula4);
	}
	
	private static WritableCellFormat getStyle(Colour colure) {
		WritableCellFormat wc = null;
		try {
			wc = new WritableCellFormat();
			wc.setBorder(Border.ALL, BorderLineStyle.THIN);
			wc.setBackground(colure);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wc;
	}

	private static String getPoint(int col, int rowStart, int rowEnd) {
		byte[] colByte = {(byte) (col + 65)};
		String colStr = new String(colByte);
		
		String start = colStr + (rowStart + 1);
		String end = colStr + (rowEnd);
		return start + ":" + end;
	}
	
	private static void addContent(TestCaseData data, int row) {
		try {
			WritableCellFormat style = null;
			if(row == 0) {
				style = getStyle(Colour.YELLOW);
				sheet.addCell(new Label(0, row, "Step", style));
				sheet.addCell(new Label(1, row, "Struts", style));
				sheet.addCell(new Label(2, row, "Timestamp", style));
				sheet.addCell(new Label(3, row, "TimeElapsed", style));
				sheet.addCell(new Label(4, row, "Screenshot", style));
				sheet.addCell(new Label(5, row, "ErrorMessage", style));
				
				sheet.setColumnView(0, 10);
				sheet.setColumnView(2, 15);
				sheet.setColumnView(3, 10);
				sheet.setColumnView(4, 50);
				sheet.setColumnView(5, 100);
			}
			row += 1;
			style = getStyle(Colour.WHITE);
			sheet.addCell(new Label(0, row, data.getSetp(), style));
			sheet.addCell(new Label(1, row, data.getStruts() ? BusinessConstant.STEP_SUCCESS : BusinessConstant.STEP_FAIL, style));
			sheet.addCell(new Label(2, row, getFormatDate(data.getTimeElapsed(), BusinessConstant.DATA_FORMAT_STR), style));
			sheet.addCell(new jxl.write.Number(3, row, data.getTimestamp(), style));
			sheet.addCell(new Label(4, row, data.getFileRelativePaths(), style));
			sheet.addCell(new Label(5, row, data.getErrorMessage(), style));
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}
	
	public static String getFormatDate(long time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(time));
	}
	
	private static void createSheet(String sheetName) {
    	sheet  =  book.createSheet(sheetName, book.getNumberOfSheets());
    	logger.info(">>creat a sheet:" + sheetName);
	}
	private static void createExcel(String fileName) {
		try {
			String version = PropertyUtil.getProperty(BusinessConstant.VERSION);
			Properties properties = PropertyUtil.load();
			String osName = System.getProperty("os.name").toLowerCase();
			boolean isMacOs = osName.startsWith("mac os x");
			String reportDir = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String folderName = null;
			if (!isMacOs) {
				folderName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.lastIndexOf("." + BusinessConstant.XLS_EXTENSION));
			} else {
				folderName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.lastIndexOf("." + BusinessConstant.XLS_EXTENSION));
			}
			if(!isMacOs) {
				reportDir = properties.getProperty(BusinessConstant.EXCEL_REPORT_DIR) + "\\"  + folderName + "\\" + properties.getProperty(BusinessConstant.CONFIG_DRIVER_KEY) + "_" + version + "_" +sdf.format(new Date());
			} else {
				reportDir = properties.getProperty(BusinessConstant.EXCEL_REPORT_DIR) + "/" + folderName + "/" + properties.getProperty(BusinessConstant.CONFIG_DRIVER_KEY) + "_" + version + "_" +sdf.format(new Date());	
			}
			File reportExcel = new File(getReportExcelPath(fileName, reportDir));
			if(!reportExcel.getParentFile().exists() || !reportExcel.getParentFile().isDirectory()) {
				reportExcel.getParentFile().mkdirs();
			}
			
			book  =  Workbook.createWorkbook(reportExcel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getReportExcelPath(String excelPath, String reportDir) {
		String reportPre = "";
		if(CurrentDriver.equals(BusinessConstant.IE_DRIVER)) {
			reportPre = BusinessConstant.IE_REPORT_PRE;
		} else if(CurrentDriver.equals(BusinessConstant.FIREFOX_DRIVER)) {
			reportPre = BusinessConstant.FIREFOX_REPORT_PRE;
		} else if(CurrentDriver.equals(BusinessConstant.CHROME_DRIVER)) {
			reportPre = BusinessConstant.CHROME_REPORT_PRE;
		} else if(CurrentDriver.equals(BusinessConstant.SAFARI_DRIVER)) {
			reportPre = BusinessConstant.SAFARI_REPORT_PRE;
		} else if(CurrentDriver.equals(BusinessConstant.SAFARIIOS_DRIVER)) {
			reportPre = BusinessConstant.SAFARIIOS_REPORT_PRE;
		} else if(CurrentDriver.equals(BusinessConstant.CHROMEANDROID_DRIVER)) {
			reportPre = BusinessConstant.CHROMEANDROID_REPORT_PRE;
		}
		if (null != versionNo && versionNo.length() > 0){
		    reportPre += BusinessConstant.REPORT_VER + versionNo + "_";
		}
		String fileName = reportPre
							+ excelPath.substring(excelPath.lastIndexOf(File.separator)+1);
		
		return reportDir + File.separator + fileName;
	}

	public static void close() {
		try {
			book.write();
			book.close();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setCurrentDriver(String currentDriver) {
		CurrentDriver = currentDriver;
	}
    
	public static void setVersionNo(String version) {
        versionNo = version;
    }
    
	public static WritableWorkbook getBook() {
		return book;
	}
}
