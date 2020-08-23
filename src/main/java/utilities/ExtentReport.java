package utilities;

import static utilities.Log.error;

import java.io.IOException;
import java.util.Map;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReport {
	private static ExtentReport instance;
	private ExtentSparkReporter sparkReporter;
	private ExtentReports extentReports;
	private ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>();

	private ExtentReport() {
	}

	public static ExtentReport getInstance() {
		if (instance == null)
			instance = new ExtentReport();
		return instance;
	}

	/**
	 * This method is used to initiate the Extent Reporter and load the report level
	 * configuration from a extentConfiguration.xml file
	 * 
	 * @param filePath - absolute path to the file where the report will be created
	 */
	public void initiateSparkReporter(String filePath) {
		sparkReporter = new ExtentSparkReporter(filePath);
		try {
			sparkReporter.loadXMLConfig(Generic.getAbsolutePath("extentconfig"));
			extentReports = new ExtentReports();
			extentReports.attachReporter(sparkReporter);
		} catch (IOException e) {
			error("Could not load Extent Report configuration file from " + filePath);
		}
	}

	/**
	 * This method is used to add the system information to the extent report
	 * 
	 * @param systemInfo - Map containing the key-value pairs of system information
	 *                   and their values
	 */
	public void setReportsSystemInfo(Map<String, String> systemInfo) {
		for (Map.Entry<String, String> sysInfo : systemInfo.entrySet())
			extentReports.setSystemInfo(sysInfo.getKey(), sysInfo.getValue());
	}

	/**
	 * This method is used to create a new Extent Test
	 * 
	 * @param testName - name of the test
	 */
	public void createTest(String testName) {
		extentTest.set(extentReports.createTest(testName));
	}

	/**
	 * This method is used to log a failed step to the report
	 * 
	 * @param t - Exception that caused the error
	 */
	public void logFailure(Throwable t) {
		extentTest.get().log(Status.FAIL, t);
	}

	/**
	 * This method is used to log a failed step to the report
	 * 
	 * @param t              - Exception that caused the error
	 * @param screenshotPath - path to the file containing the screenshot to be
	 *                       posted
	 */
	public void logFailure(Throwable t, String screenshotPath) {
		extentTest.get().log(Status.FAIL, t, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
	}

	/**
	 * This method is used to set the status of the current test
	 * 
	 * @param status  - status to be set(pass/fail/warn)
	 * @param message - additional message to be printed in the report
	 */
	public void setTestStatus(String status, String message) {
		ThreadLocal<Markup> markup = new ThreadLocal<Markup>();
		switch (status.trim().toLowerCase()) {
		case "pass":
			markup.set(MarkupHelper.createLabel(message, ExtentColor.GREEN));
			extentTest.get().pass(markup.get());
			break;
		case "fail":
			markup.set(MarkupHelper.createLabel(message, ExtentColor.RED));
			extentTest.get().fail(markup.get());
			break;
		case "warn":
			markup.set(MarkupHelper.createLabel(message, ExtentColor.YELLOW));
			extentTest.get().warning(markup.get());
			break;
		}
	}

	public void flush() {
		extentReports.flush();
	}
}
