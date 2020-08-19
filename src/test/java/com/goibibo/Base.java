package com.goibibo;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import utilities.Browser;

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
}
