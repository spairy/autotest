package com.accenture.autotest.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.log4j.Logger;

import com.accenture.autotest.constant.BusinessConstant;

public class ExcelReaderUtil {
	private static Logger logger = Logger.getLogger(ExcelReaderUtil.class);
	public static Map<String, List<ExcelData>> readExcel(String path){
		Map<String, List<ExcelData>> excelDataMap = null;
		List<ExcelData> excelDataList = null;
		List<List<String>> list = null;
		List<String> innerList = null;
		Workbook workbook = null;
		try {
			excelDataMap = new HashMap<String, List<ExcelData>>();
			List<String> fileList = getFileList(path);
			logger.info("-->path-->"+path);
			logger.info("--># of files-->"+fileList.size());
			for(String fileName : fileList){
				logger.info("-->fileName-->"+fileName);
				excelDataList = new ArrayList<ExcelData>();
				workbook = Workbook.getWorkbook(new File(fileName));
				Sheet[] allSheets = workbook.getSheets();
				Cell cell = null;
				for(Sheet sheet : allSheets){
					String sheetName = sheet.getName();
					list = new ArrayList<List<String>>();
					int col = sheet.getColumns();
					int row = sheet.getRows();
					for(int i = 1;i < row; i++){
						innerList = new ArrayList<String>();
						for(int j = 0; j < col;j++){
							cell = sheet.getCell(j,i);
							innerList.add(cell.getContents());
						}
						list.add(innerList);
						
					}
					ExcelData excelData = new ExcelData(sheetName, list);
					excelDataList.add(excelData);
				}
				excelDataMap.put(fileName, excelDataList);
			}
		} catch (Exception e) {
			excelDataList = null;
			excelDataMap = null;
			logger.error(e.getMessage(),e);
		}finally{
			if(workbook != null){
				workbook.close();
			}
		}
		return excelDataMap;
	}
	
	private static List<String> getFileList(String path){
		List<String> results = new ArrayList<String>();
		File file = new File(path);
		
		if(path != null && file.exists() && file.isFile()){
			results.add(file.getPath());
		} else if(path != null && file.exists() && file.isDirectory()) {
			for (File f : file.listFiles()) {
				String fileName = f.getName();
				String extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

			    if (f.isFile() && extension.equalsIgnoreCase(BusinessConstant.XLS_EXTENSION)) {
			        results.add(f.getPath());
			    }
			}
		}
		
		return results;
	}
}
