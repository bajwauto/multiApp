package com.goibibo;

import java.io.File;
import static utilities.Log.*;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import utilities.Browser;
import utilities.ExcelFile;
import utilities.Generic;

public class Base {
	public Browser browser;

	@BeforeMethod(alwaysRun = true)
	@Parameters("browser")
	public void testSetup(@Optional("chrome") String browserName) {
		browser = Browser.getInstance();
		browser.set(browserName, false);
		browser.maximize();
		browser.deleteCookies();
		browser.get("https://www.goibibo.com");
	}

	@AfterMethod(alwaysRun = true)
	public void testTeardown() {
		browser.quit();
	}

	@DataProvider(name = "excel", parallel = false)
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
}
