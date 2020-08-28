package utilities;

import static utilities.Log.debug;
import static utilities.Log.error;
import static utilities.Log.info;
import static utilities.Log.warn;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.goibibo.Base;

public class TestEvents implements ITestListener {
	ExtentReport report;
	String reportPath = Generic.getAbsolutePath("reports") + File.separator + Base.timeStampFolder;

	@Override
	public void onStart(ITestContext context) {
		debug("|===================================STARTING SUITE EXECUTION==========================================|");
		Map<String, String> allTestParameters = context.getCurrentXmlTest().getAllParameters();
		String enableExtentReports = allTestParameters.get("generateReports");
		if (Boolean.parseBoolean(enableExtentReports)) {
			Map<String, String> systemInfo = new LinkedHashMap<String, String>();
			systemInfo.put("Developer", allTestParameters.get("developer"));
			systemInfo.put("Application", allTestParameters.get("application"));
			systemInfo.put("Application URL", allTestParameters.get("url"));
			report = ExtentReport.getInstance();
			report.initiateSparkReporter(reportPath + File.separator + context.getName() + ".html");
			report.setReportsSystemInfo(systemInfo);
		} else {
			report = null;
		}
	}

	@Override
	public void onTestStart(ITestResult result) {
		String currentTest = result.getMethod().getMethodName();
		debug("|********************************************************************************************************|");
		debug("|***********************************STARTING TEST EXECUTION**********************************************|");
		debug(" ******************************************************************************************************** ");
		info("The test \"" + currentTest + "\" has been started with parameters - " + result.getParameters()[0]);
		if (report != null)
			report.createTest(currentTest);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		String currentTest = result.getMethod().getMethodName();
		info("The test \"" + currentTest + "\" HAS PASSED with parameters - " + result.getParameters()[0]);
		if (report != null)
			report.setTestStatus("PASS",
					"The test \"" + currentTest + "\" HAS PASSED with parameters - " + result.getParameters()[0]);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		String currentTest = result.getMethod().getMethodName();
		String failureSSPath = Base.currentSSPath.get().replaceAll("yyy", Base.datasetRunning.get() + "")
				.replaceAll("SSxxx", "ERROR");
		error("The test \"" + currentTest + "\" HAS FAILED with parameters - " + result.getParameters()[0]);
		try {
			Browser.getInstance().takeFullPageScreenshot(failureSSPath);
		} catch (IOException e) {
			warn("Could not capture screenshot on failure at path - " + failureSSPath);
		}
		if (report != null) {
			report.logFailure(result.getThrowable(), failureSSPath);
			report.setTestStatus("FAIL",
					"The test \"" + currentTest + "\" HAS FAILED with parameters - " + result.getParameters()[0]);
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		String currentTest = result.getMethod().getMethodName();
		error("The test \"" + currentTest + "\" HAS BEEN SKIPPED with parameters - " + result.getParameters()[0]);
		if (report != null)
			report.setTestStatus("WARN",
					"The test \"" + currentTest + "\" HAS BEEN SKIPPED with parameters - " + result.getParameters()[0]);
	}

	@Override
	public void onFinish(ITestContext context) {
		if (report != null)
			report.flush();
		debug("|===================================ENDING SUITE EXECUTION============================================|");
	}

}
