package com.goibibo.pages;

import utilities.Browser;
import utilities.ExtentReport;
import static utilities.Log.*;

public class Page {
	public Browser browser;
	public TopMenu topMenu;
	public ExtentReport report;

	public Page() {
		browser = Browser.getInstance();
		topMenu = new TopMenu();
		report = ExtentReport.getInstance();
	}

	public void debugLogger(String message) {
		debug(message);
		report.logInfo(message);
	}

	public void infoLogger(String message) {
		info(message);
		report.logInfo(message);
	}

	public void warnLogger(String message) {
		warn(message);
		report.logWarn(message);
	}
}
