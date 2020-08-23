package utilities;

import static utilities.Log.info;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import utilities.customAnnotations.Retry;

public class RetryTest implements IRetryAnalyzer {
	private ThreadLocal<Integer> timesRetried = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			return 0;
		}
	};

	@Override
	public boolean retry(ITestResult result) {
		Retry retry = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Retry.class);
		if (retry != null && timesRetried.get() < retry.value()) {
			timesRetried.set(timesRetried.get() + 1);
			info("Retrying(Attempt " + timesRetried.get() + ") the test \"" + result.getMethod().getMethodName()
					+ "\" with the parameters \"" + result.getParameters()[0] + "\"");
			return true;
		} else
			return false;
	}
}
