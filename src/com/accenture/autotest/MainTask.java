package com.accenture.autotest;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;
//import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
// import org.openqa.selenium.os.WindowsUtils;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import com.accenture.autotest.constant.BusinessConstant;
import com.accenture.autotest.domain.TestCaseData;
import com.accenture.autotest.service.ActionHandle;
import com.accenture.autotest.util.ActionHandleFactoryUtil;
import com.accenture.autotest.util.ExcelData;
import com.accenture.autotest.util.ExcelReaderUtil;
import com.accenture.autotest.util.ExcelWriterUtil;
import com.accenture.autotest.util.PropertyUtil;
import com.accenture.autotest.util.StatisticsUtil;
import com.accenture.autotest.util.TestCaseFilter;
import com.accenture.autotest.util.UserVariables;


public class MainTask {
	private String excelPath;
	private Properties properties;
	// key is sheetName, value is List and innerList is one row in the sheet
	private List<ExcelData> excelDataList;
	private Map<String, List<ExcelData>> excelDataMap;
	private static Logger logger = Logger.getLogger(MainTask.class);
	private boolean init = false;
	private RemoteWebDriver driver;
	private WritableWorkbook book = null;
	private static String configPath = "";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length > 0) {
			configPath = args[0];
		}
		MainTask mainTask = new MainTask();
		mainTask.init();
		try {
			mainTask.service();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			mainTask.destroy();
		}
	}

	@SuppressWarnings("unchecked")
	private void service() throws MalformedURLException {
		if (!init) {
			return;
		}
		
		Iterator<Entry<String, List<ExcelData>>> iterator = excelDataMap.entrySet().iterator();
		
		while(iterator.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry mapEntry = (Map.Entry) iterator.next();
			String fileName = (String) mapEntry.getKey();
			excelDataList = (List<ExcelData>) mapEntry.getValue();
			book = ExcelWriterUtil.initExcel(fileName);
			StatisticsUtil.clearStatistics();
			UserVariables.clear();
			
			for (ExcelData excelData : excelDataList) {
				String sheetName = excelData.getSheetName();
				List<List<String>> sheetList = excelData.getSheetData();
				List<TestCaseData> reportList = new ArrayList<TestCaseData>();
				boolean sheetHandleRes = true;
				boolean res = true;
				
				if(!TestCaseFilter.doFilter(sheetName)) {
					continue;
				}
				createDriver();
				
				for (List<String> rowData : sheetList) {
					if (rowData != null && rowData.size() > 1) {
						String actionType = rowData.get(BusinessConstant.EXCEL_ACTION_TYPE);
						if(ActionHandleFactoryUtil.isNull(actionType)) {
							continue;
						}
						ActionHandle actionHandle = ActionHandleFactoryUtil.getActionHandle(actionType);
						if(actionHandle != null){
							String version = PropertyUtil.getProperty(BusinessConstant.VERSION);
							String folderName = null;
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							String osName = System.getProperty("os.name").toLowerCase();
							boolean isMacOs = osName.startsWith("mac os x");
							String screenPath = null;
							if (!isMacOs) {
								folderName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.lastIndexOf("." + BusinessConstant.XLS_EXTENSION));
								screenPath = PropertyUtil.getProperties().getProperty(BusinessConstant.SCREEN_PATH) + "\\" + folderName + "\\" + PropertyUtil.getProperty(BusinessConstant.CONFIG_DRIVER_KEY) + "_" + version + "_" + sdf.format(new Date());
							} else {
								folderName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.lastIndexOf("." + BusinessConstant.XLS_EXTENSION));
								screenPath = PropertyUtil.getProperties().getProperty(BusinessConstant.SCREEN_PATH) + "/" + folderName + "/" + PropertyUtil.getProperty(BusinessConstant.CONFIG_DRIVER_KEY) + "_" + version + "_" + sdf.format(new Date());							
							}
							res = actionHandle.serviceHandle(driver, sheetName, rowData, reportList, screenPath, res);
							if(!res){
								sheetHandleRes = false;
								//break;
							}
						}else{
							sheetHandleRes = false;
							logger.error(">>sheet:" + sheetName + ",Can not find actionHandle for actionType:" + actionType);
							break;
						}
					}
				}
				ExcelWriterUtil.writeToExcel(reportList, sheetName);
				StatisticsUtil.addCaseData(reportList, sheetName);
				
				if(sheetHandleRes){
					logger.info(">>sheet:" + sheetName +" handle success");
				}else{
					logger.error(">>sheet:" + sheetName +" handle fail");
				}
				driver.quit();
			}
			
			StatisticsUtil.writeToExcel(book);
			ExcelWriterUtil.close();
		}	
	}

	private void init() {
		PropertyUtil.setConfigPath(configPath);
		properties = PropertyUtil.load();
		excelPath = properties.getProperty(BusinessConstant.EXCEL_PROPERTY_NAME);
		if (excelPath != null) {
			excelDataMap = ExcelReaderUtil.readExcel(excelPath);
		}
		if (excelDataMap != null) {
			init = true;
		}
		ExcelWriterUtil.setCurrentDriver(properties.getProperty(BusinessConstant.CONFIG_DRIVER_KEY));
		ExcelWriterUtil.setVersionNo(properties.getProperty(BusinessConstant.VERSION));
		logger.info(">>init:" + init);
	}

	private void destroy() {
		//String driverName = PropertyUtil.getProperty(BusinessConstant.CONFIG_DRIVER_KEY);
		try {
			StatisticsUtil.writeToExcel(book);
			ExcelWriterUtil.close();
		} catch (Exception ex) {
		}
	}
	
	private void createDriver() throws MalformedURLException {

		String driverName = PropertyUtil.getProperty(BusinessConstant.CONFIG_DRIVER_KEY);
		String version = PropertyUtil.getProperty(BusinessConstant.VERSION);
		
		logger.info(">>runtimeEnvironment:" + driverName);
		logger.info(">>version:" + version);
		
		if(BusinessConstant.CHROME_DRIVER.equalsIgnoreCase(driverName)) {
			String osName = System.getProperty("os.name").toLowerCase();
			boolean isMacOs = osName.startsWith("mac os x");
			String chromeDriverPath = null;
			if (isMacOs)
				chromeDriverPath = BusinessConstant.CHROME_DRIVER_MAC_PATH;
			else 
				chromeDriverPath = BusinessConstant.CHROME_DRIVER_PATH;
			System.setProperty("webdriver.chrome.driver",chromeDriverPath);
			driver = new ChromeDriver();
			
		} else if(BusinessConstant.FIREFOX_DRIVER.equalsIgnoreCase(driverName)) {
			FirefoxProfile profile= new FirefoxProfile();
			//profile.setPreference("plugin.state.flash", 0);
			driver = new FirefoxDriver(profile);
			
		} else if(BusinessConstant.SAFARI_DRIVER.equalsIgnoreCase(driverName)) {
			DesiredCapabilities safariCapabilities = DesiredCapabilities.safari();
			safariCapabilities.setCapability(SafariOptions.CAPABILITY, true);
			driver = new SafariDriver(safariCapabilities);
			
		} else if(BusinessConstant.SAFARIIOS_DRIVER.equalsIgnoreCase(driverName)) {
		    final DesiredCapabilities capabilities = new DesiredCapabilities();
		    capabilities.setCapability("device", "iPhone Simulator");
		    capabilities.setCapability(CapabilityType.VERSION, version);
		    capabilities.setCapability("app", "safari");

		    driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"),
		        capabilities);
						
		} else if(BusinessConstant.CHROMEANDROID_DRIVER.equalsIgnoreCase(driverName)) {
		    final DesiredCapabilities capabilities = new DesiredCapabilities();
		    capabilities.setCapability("device", "Android");
		    capabilities.setCapability(CapabilityType.VERSION, version);
		    capabilities.setCapability("app", "chrome");

		    driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"),
		        capabilities);
						
		} else {
			System.setProperty("webdriver.ie.driver",BusinessConstant.IE_DRIVER_PATH);
			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
			driver = new InternetExplorerDriver(ieCapabilities);
		}
		driver.manage().timeouts().implicitlyWait(BusinessConstant.SELENIUM_WAIT_SECOND, TimeUnit.SECONDS);
		//driver.manage().window().maximize();
	}
}