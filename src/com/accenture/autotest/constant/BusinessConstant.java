package com.accenture.autotest.constant;

public interface BusinessConstant {
	public final static String CONFIG_PROPERTY_NAME = "conf.properties";
	public final static String EXCEL_PROPERTY_NAME = "excelName";
	public final static String EXCEL_REPORT_DIR = "reportDir";
	public final static String TEST_SCRIPT_DIR = "testScriptDirectory";
	public final static String VERSION = "version";
	public final static String HANDLE_CLASS_NAME_PREFIX = "com.accenture.autotest.service.impl.";
	public final static String SCREEN_PATH = "screenPath";
	public final static String TEST_PASS_SCREEN_NAME = "_Pass_.png";
	public final static String TEST_FAIL_SCREEN_NAME = "_Fail_.png";
	public final static int SELENIUM_WAIT_SECOND = 3;
	public final static int PAGE_SLEEP_SECOND = 10;
	public final static String XLS_EXTENSION = "xls";
	//column in excel
	public final static int EXCEL_STEP = 0;
	public final static int EXCEL_ACTION_TYPE = 1;
	public final static int EXCEL_XPATH = 2;
	public final static int EXCEL_ATTRIBUTE = 3;
	public final static int EXCEL_VALUE = 4;
	public final static int EXCEL_VARIABLE = 5;
	public final static int EXCEL_COLUMN_LENGTH = 5;
	
	public final static String DATA_FORMAT_STR = "HH:mm:ss";
	
	public final static String STEP_SUCCESS = "Success"; 
	public final static String STEP_FAIL = "Fail"; 
	public final static int BLANK_ROWS = 2; 
	public final static String IE_REPORT_PRE = "IE_Report_";
	public final static String FIREFOX_REPORT_PRE = "Firefox_Report_";
	public final static String CHROME_REPORT_PRE = "Chrome_Report_";
	public final static String SAFARI_REPORT_PRE = "Safari_Report_";
	public final static String SAFARIIOS_REPORT_PRE = "SafariiOS_Report_";
	public final static String CHROMEANDROID_REPORT_PRE = "ChromeAndroid_Report_";
	public final static String REPORT_VER = "Ver.";
	
	public final static String IE_DRIVER = "IE";
	public final static String FIREFOX_DRIVER = "Firefox";
	public final static String CHROME_DRIVER = "Chrome";
	public final static String SAFARI_DRIVER = "Safari";
	public final static String SAFARIIOS_DRIVER = "SafariiOS";
	public final static String CHROMEANDROID_DRIVER = "ChromeAndroid";
	
	public final static String CONFIG_DRIVER_KEY = "runtimeEnvironment";
	
	public final static String CHROME_DRIVER_PATH = "drivers\\chromedriver.exe";
	public final static String CHROME_DRIVER_MAC_PATH = "drivers/chromedriver2.9";
	public final static String IE_DRIVER_PATH = "drivers\\IEDriverServer.exe";
	
	public final static String SUMMARY_SHEETNAME = "summary";
	public final static String SUMMARY_SHEET_HEADER_CASE_NAME = "Test Case Name";
	public final static String SUMMARY_SHEET_HEADER_STRUTS = "Overall Status ";
	public final static String SUMMARY_SHEET_HEADER_TOTAL_STEP = "Total of Test Steps";
	public final static String SUMMARY_SHEET_HEADER_PASSED_COUNT = "No of Passed Test Steps";
	public final static String SUMMARY_SHEET_HEADER_FAILURE_COUNT = "No of Failed Test Steps";
	
	public final static String SUMARY_STATUS_COMPLETED = "Completed";
	public final static String SUMARY_STATUS_PARTIALLY_EXECUTED = "Partially Executed";
	public final static String SUMARY_STATUS_NO_RUN = "No Run";
	
	public final static String SUMARY_TOTAL_NO_OF_PASSED_TEST_CASES = "Total No of Passed Test Cases:";
	public final static String SUMARY_TOTAL_NO_OF_FAILED_TEST_CASES = "Total No of Failed Test Cases:";
	
	public final static String TEST_CASE_FILTER_LIST_KEY = "testCaseList";
	
	public final static String CONTINUE_BUTTON = "FF_CONTINUE";
	public final static String CONTINUE_BUTTON_POPUP = "FF_CONTINUE_POPUP";
	public final static String CANCEL_BUTTON = "FF_CANCEL";
	public final static String IE_HANDLE_SSL = "IE_HANDLE_SSL";
	
	public final static String HTML_VALUE = "HTML_VALUE";
	public final static String HTML_PATTERN = "HTML_PATTERN";
	public final static String ELEMENT_TEXT_VALUE = "ELEMENT_TEXT_VALUE";
	public final static String ELEMENT_TEXT_PATTERN = "ELEMENT_TEXT_PATTERN";
	//Window Size
	public final static String WINDOW_SMALL = "SMALL";
	public final static int WINDOW_SMALL_WIDTH = 560;
	public final static int WINDOW_SMALL_HEIGHT = 728;
	public final static String WINDOW_MEDIUM = "MEDIUM";
    public final static int WINDOW_MEDIUM_WIDTH = 859;
    public final static int WINDOW_MEDIUM_HEIGHT = 728;	
	public final static String WINDOW_LARGE = "LARGE";
    public final static int WINDOW_LARGE_WIDTH = 1054;
    public final static int WINDOW_LARGE_HEIGHT = 728;
    public final static String WINDOW_MAXIMIUM = "MAXIMUM";    
    //Window Scroll
	public final static String WINDOW_SCROLL_TOP = "TOP";
	public final static String WINDOW_SCROLL_BOTTOM = "BOTTOM";
	//Verify Size Offset
}
