package com.accenture.autotest.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.accenture.autotest.constant.BusinessConstant;

public class ScreenUtil {
	private static Logger logger = Logger.getLogger(ScreenUtil.class);
	private static String version = PropertyUtil.getProperty(BusinessConstant.VERSION);
	public static void captureScreen(String fileName,WebDriver driver) throws Exception {
		logger.info("screen filePath:" + fileName);
		File screenShotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		
		
		//Toolkit tk = Toolkit.getDefaultToolkit();
		//java.awt.Dimension screenSize = tk.getScreenSize();
		//Rectangle screenRectangle = new Rectangle(screenSize);
		//Robot robot = new Robot();
		//BufferedImage image = robot.createScreenCapture(screenRectangle);
		File file = new File(fileName);
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		FileUtils.copyFile(screenShotFile, file);
		
		//ImageIO.write(image, "png", file);
	}

	public static void highlightElement(WebDriver driver, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("element = arguments[0];" + "original_style = element.getAttribute('style');"
				+ "element.setAttribute('style', original_style + \";"
				+ "background: yellow; border: 2px solid red;\");"
				+ "setTimeout(function(){element.setAttribute('style', original_style);}, 1000);", element);
	}

	public static String getPrintPathForSuccess(String screenPath, String sheetName, String step) {
		return screenPath + File.separator + sheetName + File.separator + step + BusinessConstant.TEST_PASS_SCREEN_NAME;
	}
	public static String getPrintPathForFail(String screenPath, String sheetName, String step) {
		return screenPath + File.separator + sheetName + File.separator + step + BusinessConstant.TEST_FAIL_SCREEN_NAME;
	}

	public static String getSetpTime(String step) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		return step +"_"+ (sdf.format(new Date()));
	}

}
