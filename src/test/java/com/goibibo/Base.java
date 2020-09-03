package com.goibibo;

import static utilities.Log.error;
import static utilities.Log.info;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import utilities.Browser;
import utilities.ExcelFile;
import utilities.Generic;

public class Base {
	public static Browser browser;
	public static String timeStampFolder = Generic.getFormattedDate(new Date(),
			"dd.MM.yy" + File.separator + "hh.mm.ss a");
	public static Boolean generateReports;
	protected static String baseScreenshotPath = Generic.getAbsolutePath("screenshots") + File.separator
			+ timeStampFolder;
	protected static boolean enableScreenshots;
	public static ThreadLocal<Integer> datasetRunning = new ThreadLocal<Integer>();
	protected static ThreadLocal<Integer> ssCounter = new ThreadLocal<Integer>();
	public static ThreadLocal<String> currentSSPath = new ThreadLocal<String>() {
		@Override
		protected String initialValue() {
			return baseScreenshotPath;
		}
	};

	@Parameters("enableScreenshots")
	@BeforeSuite(alwaysRun = true)
	public void suiteSetup(@Optional("true") String captureScreenshots) {
		System.setProperty("org.uncommons.reportng.escape-output", "false");
		enableScreenshots = Boolean.parseBoolean(captureScreenshots);
	}

	@BeforeMethod(alwaysRun = true)
	@Parameters({ "executeTestsRemotely", "browser", "url" })
	public void testSetup(@Optional("false") String executeTestsRemotely, @Optional("chrome") String browserName,
			String url, Method method) {
		if (enableScreenshots) {
			currentSSPath.set(baseScreenshotPath + File.separator + method.getName());
			Generic.createDirectory(currentSSPath.get());
			currentSSPath.set(currentSSPath.get() + File.separator + "DataSetyyy_SSxxx.jpg");
			datasetRunning.set(1);
			ssCounter.set(0);
		}
		browser = Browser.getInstance();
		browser.set(browserName, Boolean.parseBoolean(executeTestsRemotely.trim()));
		browser.maximize();
		browser.deleteCookies();
		browser.get(url);
	}

	@AfterMethod(alwaysRun = true)
	public void testTeardown() {
		browser.quit();
	}

	@DataProvider(name = "excel", parallel = true)
	public Object[][] getTestDataFromExcel(Method method) {
		Object[][] testData;
		String dataSheetPath = Generic.getAbsolutePath("datasheets") + File.separator + method.getName() + ".xlsx";
		ExcelFile excel = new ExcelFile(dataSheetPath, "Data");
		try {
			List<Map<String, Object>> data = excel.read();
			testData = new Object[data.size()][1];
			for (int i = 0; i < data.size(); i++)
				testData[i][0] = data.get(i);
		} catch (Exception e) {
			e.printStackTrace();
			testData = null;
			error("Could not read data from the dataSheet - " + dataSheetPath);
		}
		info("The data for the test \"" + method.getName() + "\" was read from the file - " + dataSheetPath);
		return testData;
	}

	protected static void takeScreenshot() throws Exception {
		if (enableScreenshots) {
			ssCounter.set(ssCounter.get() + 1);
			browser.takeVisibleScreenshot(currentSSPath.get().replaceAll("yyy", "" + datasetRunning.get())
					.replaceAll("xxx", "" + ssCounter.get()));
		}
	}

	protected static void takeScreenshot(WebElement element) throws Exception {
		if (enableScreenshots) {
			ssCounter.set(ssCounter.get() + 1);
			browser.takeVisibleScreenshot(element, currentSSPath.get().replaceAll("yyy", "" + datasetRunning.get())
					.replaceAll("xxx", "" + ssCounter.get()));
		}
	}

	protected static void takeFullPageScreenshot() throws Exception {
		if (enableScreenshots) {
			ssCounter.set(ssCounter.get() + 1);
			browser.takeFullPageScreenshot(currentSSPath.get().replaceAll("yyy", "" + datasetRunning.get())
					.replaceAll("xxx", "" + ssCounter.get()));
		}
	}
}
