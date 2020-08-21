package utilities;

import static utilities.Log.*;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestEvents implements ITestListener {

	@Override
	public void onTestStart(ITestResult result) {
		String currentTest = result.getMethod().getMethodName();
		debug("|********************************************************************************************************|");
		debug("|***********************************STARTING TEST EXECUTION**********************************************|");
		debug(" ******************************************************************************************************** ");
		info("The test \"" + currentTest + "\" has been started with parameters - "
				+ result.getParameters()[0]);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		String currentTest = result.getMethod().getMethodName();
		info("The test \"" + currentTest + "\" HAS PASSED with parameters - " + result.getParameters()[0]);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		String currentTest = result.getMethod().getMethodName();
		error("The test \"" + currentTest + "\" HAS FAILED with parameters - " + result.getParameters()[0]);
	}

	@Override
	public void onTestSkipped(ITestResult result) {
	}

	@Override
	public void onStart(ITestContext context) {
		debug("|===================================STARTING SUITE EXECUTION==========================================|");
	}

	@Override
	public void onFinish(ITestContext context) {
		debug("|===================================ENDING SUITE EXECUTION============================================|");
	}

}
