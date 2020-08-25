package utilities;

import static utilities.Log.debug;
import static utilities.Log.error;
import static utilities.Log.info;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class Browser {
	public static int waitTimeOut = 30;
	private String ORPath = Generic.getAbsolutePath("OR") + File.separator + "OR.properties";
	private static Browser browserInstance = null;
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
	private static ThreadLocal<Actions> actions = new ThreadLocal<Actions>();
	private static ThreadLocal<JavascriptExecutor> jse = new ThreadLocal<JavascriptExecutor>();
	private ThreadLocal<Boolean> disableVisibilityCheck = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return false;
		}
	};

	/**
	 * Constructor has been made private so that objects of this class cannot be
	 * created using the new keyword outside this class
	 */
	private Browser() {
	}

	public WebDriver driver() {
		return driver.get();
	}

	private Actions actions() {
		return actions.get();
	}

	private JavascriptExecutor jse() {
		return jse.get();
	}

	/**
	 * This method is used to get the reference to the locator of an object which is
	 * stored in the Object Repository
	 * 
	 * @param objectName - name of the object as mentioned in the OR
	 * @return - reference to the locator of the Object
	 */
	private By getStoredObjectLocator(String objectName) throws Exception {
		By by = null;
		String propertyNameValue = PropertiesFile.read(ORPath, objectName);
		debug("The object \"" + objectName + "\" is stored in the OR with the property-value pair - "
				+ propertyNameValue);
		if (propertyNameValue != null) {
			List<String> matchesNGroups = Generic.getRegexMatchesAndGroups(propertyNameValue, "^([^:]+):(.+)$").get(0);
			String property = matchesNGroups.get(1);
			String value = matchesNGroups.get(2);
			switch (property.toLowerCase()) {
			case "xpath":
				by = By.xpath(value);
				break;
			case "id":
				by = By.id(value);
				break;
			case "name":
				by = By.name(value);
				break;
			case "classname":
				by = By.className(value);
				break;
			case "cssselector":
				by = By.cssSelector(value);
				break;
			case "linktext":
				by = By.linkText(value);
				break;
			case "partiallinktext":
				by = By.partialLinkText(value);
				break;
			case "tagname":
				by = By.tagName(value);
				break;
			default:
				error("Invalid locator specified for the object \"" + objectName + "\" in the Object Repository");
				throw new Exception(
						"Invalid locator specified for the object \"" + objectName + "\" in the Object Repository");
			}
		} else {
			error("The object \"" + objectName + "\" is not present in the Object Repository");
			throw new Exception("The object \"" + objectName + "\" is not present in the Object Repository");
		}
		return by;
	}

	/**
	 * This method is used to get the same Browser instance no matter how many times
	 * the function is called
	 * 
	 * @return - the same browser instance
	 */
	public static Browser getInstance() {
		if (browserInstance == null)
			browserInstance = new Browser();
		return browserInstance;
	}

	/**
	 * This method is used to configure a flag so as to disable the visibility check
	 * while finding an element
	 * 
	 * @param flag - true to disable the check, false to enable it
	 */
	public void disableVisibilityCheck(boolean flag) {
		disableVisibilityCheck.set(flag);
	}

	/**
	 * This method is used to setup and initialize the Webdriver
	 * 
	 * @param browserName     - name of the browser on which the tests will be
	 *                        performed
	 * @param remoteExecution - true, iff the execution is required using the
	 *                        RemoteWebDriver, else false
	 */
	public void set(String browserName, boolean remoteExecution) {
		if (!remoteExecution) {
			switch (browserName.trim().toLowerCase()) {
			case "edge":
				WebDriverManager.edgedriver().setup();
				driver.set(new EdgeDriver());
				break;
			case "firefox":
				WebDriverManager.firefoxdriver().setup();
				driver.set(new FirefoxDriver());
				break;
			case "chrome":
			default:
				WebDriverManager.chromedriver().setup();
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("--disable-notifications");
				chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
				chromeOptions.setExperimentalOption("useAutomationExtension", false);
				driver.set(new ChromeDriver(chromeOptions));
				break;
			}
			info("Launched the \"" + browserName + "\" browser");
		} else {
			switch (browserName.trim().toLowerCase()) {
			case "edge":
				DesiredCapabilities ecapabilities = DesiredCapabilities.edge();
				try {
					driver.set(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), ecapabilities));
				} catch (Exception e) {
					e.printStackTrace();
					error("Could not connect to the Remote Webdriver");
				}
				break;
			case "firefox":
				FirefoxOptions ffOptions = new FirefoxOptions();
				try {
					driver.set(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), ffOptions));
				} catch (Exception e) {
					e.printStackTrace();
					error("Could not connect to the Remote Webdriver");
				}
				break;
			case "chrome":
			default:
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("--disable-notifications");
				chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
				chromeOptions.setExperimentalOption("useAutomationExtension", false);
				try {
					driver.set(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), chromeOptions));
				} catch (Exception e) {
					e.printStackTrace();
					error("Could not connect to the Remote Webdriver");
				}
			}
		}
		actions.set(new Actions(driver()));
		jse.set((JavascriptExecutor) driver());
	}

	/**
	 * This method is used to navigate to the webpage with the given URL
	 * 
	 * @param url - URL of the webpage to be navigated to
	 */
	public void get(String url) {
		driver().get(url);
		info("Launched URL - " + url);
	}

	/**
	 * This method is used to maximize the browser window opened by the WebDriver
	 * instance
	 */
	public void maximize() {
		driver().manage().window().maximize();
		info("Maximized the browser");
	}

	/**
	 * This method is used to delete all the cookies
	 */
	public void deleteCookies() {
		driver().manage().deleteAllCookies();
		debug("Deleted all cookies");
	}

	/**
	 * This method is used to wait for a page to get loaded before waitTimeOut
	 * expires
	 * 
	 * @throws Exception
	 */
	public void waitForPageToGetLoaded() throws Exception {
		WebDriverWait wait = new WebDriverWait(driver(), waitTimeOut);
		Boolean pageLoaded;
		pageLoaded = wait.until(new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driverRef) {
				return ((String) (jse().executeScript("return document.readyState"))).equalsIgnoreCase("complete");
			}
		});
		if (pageLoaded != true)
			throw new Exception("Page not loaded properly");
	}

	/**
	 * This method is used to scroll to the bottom of the webpage
	 */
	public void scrollToPageBottom() {
		jse().executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

	/**
	 * This method is used to scroll to the bottom of the page in small steps
	 * 
	 * @param pixels - size of each step
	 */
	public void scrollToPageBottom(long pixels) {
		long currentHeight, previousHeight = -1;
		while ((currentHeight = (long) jse().executeScript("return window.pageYOffset")) != previousHeight) {
			previousHeight = currentHeight;
			jse().executeScript("window.scrollBy(0," + pixels + ")");
		}
	}

	/**
	 * This method is used to scroll to the page top
	 */
	public void scrollToPageTop() {
		jse().executeScript("window.scrollTo(0,0)");
	}

	/**
	 * This method is used to find an element on the web page using a reference to
	 * its locator
	 * 
	 * @param by - reference to the locator of the object to be searched
	 * @return - reference to the element found
	 */
	public WebElement findElement(By by) {
		WebElement element = null;
		WebDriverWait wait = new WebDriverWait(driver(), waitTimeOut);
		element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
		if (!disableVisibilityCheck.get()) {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} else
			disableVisibilityCheck(false);
		return element;
	}

	/**
	 * This method is used to find an element(stored in the Object Repository) on a
	 * web page
	 * 
	 * @param objectName - name of the object as mentioned in the Object Repository
	 * @return - reference to the element found
	 * @throws Exception
	 */
	public WebElement findElement(String objectName) throws Exception {
		By by = getStoredObjectLocator(objectName);
		return findElement(by);
	}

	/**
	 * This method is used to find all the elements identified by the provided
	 * locator on the web page
	 * 
	 * @param by - reference to the locator to the object(s)
	 * @return - List of Objects identified by the given locator
	 */
	public List<WebElement> findElements(By by) {
		List<WebElement> elements = null;
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver()).withTimeout(Duration.ofSeconds(waitTimeOut))
				.pollingEvery(Duration.ofSeconds(2)).ignoring(NoSuchElementException.class);
		elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
		return elements;
	}

	/**
	 * This method is used to find all the elements on web page which can be
	 * identified by the provided property holder mentioned in the Object Repository
	 * 
	 * @param objectName - name of the property holder as in OR
	 * @return - List of Objects identified
	 * @throws Exception
	 */
	public List<WebElement> findElements(String objectName) throws Exception {
		By by = getStoredObjectLocator(objectName);
		return findElements(by);
	}

	/**
	 * This method is used to perform click operation on an object/element
	 * 
	 * @param element - reference to the object to be clicked
	 * @throws Exception
	 */
	public void click(WebElement element) throws Exception {
		if (element != null) {
			element.click();
			debug("Click operation performed successfully on the object \"" + element.toString() + "\"");
		} else {
			error(">>NULL OBJECT ALERT<< Cannot click on a null object!!!");
			throw new Exception(">>NULL OBJECT ALERT<< Cannot click on a null object!!!");
		}

	}

	/**
	 * This method is used to perform click operation on an object identified by the
	 * provided locator
	 * 
	 * @param by - reference to the object locator
	 * @throws Exception
	 */
	public void click(By by) throws Exception {
		WebElement element = findElement(by);
		click(element);
	}

	/**
	 * This method is used to perform click operation on the object specified in the
	 * Object Repository
	 * 
	 * @param objectName - name of the object as mentioned in the OR
	 * @throws Exception
	 */
	public void click(String objectName) throws Exception {
		By by = getStoredObjectLocator(objectName);
		click(by);
	}

	/**
	 * This method is used to perform sendkeys operation on an object
	 * 
	 * @param element - reference to the element on which sendkeys operation is to
	 *                be performed
	 * @param text    - text message to be sent to the object
	 * @throws Exception
	 */
	public void sendKeys(WebElement element, String text) throws Exception {
		if (element != null) {
			element.sendKeys(Keys.chord(Keys.CONTROL, "A"));
			element.sendKeys(text);
			debug("The text \"" + text + "\" was sent successfully to the object \"" + element.toString() + "\"");
		} else {
			error(">>NULL OBJECT ALERT<< Cannot perform sendkeys opeartion on a null object");
			throw new Exception(">>NULL OBJECT ALERT<< Cannot perform sendkeys opeartion on a null object");
		}
	}

	/**
	 * This method is used to perform sendkeys operation on an object identified by
	 * its locator
	 * 
	 * @param by   - reference to the locator of the object on which sendkeys
	 *             operation is to be performed
	 * @param text - text message to be sent to the object
	 * @throws Exception
	 */
	public void sendKeys(By by, String text) throws Exception {
		WebElement element = findElement(by);
		sendKeys(element, text);
	}

	/**
	 * This method is used to perform sendkeys operation on an object stored in the
	 * Object Repository
	 * 
	 * @param objectName - name of the object as stored in the Object Repository
	 * @param text       - text message to be sent to the object
	 * @throws Exception
	 */
	public void sendkeys(String objectName, String text) throws Exception {
		By by = getStoredObjectLocator(objectName);
		sendKeys(by, text);
	}

	/**
	 * This method is used to fetch the text from an object
	 * 
	 * @param element - reference to the object/element
	 * @return - text extracted from the object
	 * @throws Exception
	 */
	public String getText(WebElement element) throws Exception {
		String fetchedText;
		if (element != null) {
			fetchedText = element.getText();
			debug("The text \"" + fetchedText + "\" was extracted from the object \"" + element.toString() + "\"");
			return fetchedText;
		} else {
			error(">>NULL OBJECT ALERT<< Cannot fetch text of a null object");
			throw new Exception(">>NULL OBJECT ALERT<< Cannot fetch text of a null object");
		}
	}

	/**
	 * This method is used to get the value of the specified attribute from the
	 * given object
	 * 
	 * @param element   - reference to the object
	 * @param attribute - name of the attribute whose value is desired
	 * @return - desired attribute value of the desired object
	 * @throws Exception
	 */
	public String getAttribute(WebElement element, String attribute) throws Exception {
		String fetchedAttribute;
		if (element != null) {
			fetchedAttribute = element.getAttribute(attribute);
			debug("The attribute's value \"" + attribute + "\" fetched from the object \"" + element.toString()
					+ "\" is \"" + fetchedAttribute + "\"");
			return fetchedAttribute;
		} else {
			throw new Exception(
					">>NULL OBJECT ALERT<< Cannot fetch the attribute \"" + attribute + "\" of a null object");
		}
	}

	/**
	 * This method is used to get the bounds of an object
	 * 
	 * @param element - reference to the object whose rectangular bounds are desired
	 * @return - reference to the rectangular bound of the object
	 * @throws Exception
	 */
	public Rectangle getObjectBounds(WebElement element) throws Exception {
		if (element != null)
			return element.getRect();
		else {
			error(">>NULL OBJECT ALERT<< Cannot get the bounds of a null object");
			throw new Exception(">>NULL OBJECT ALERT<< Cannot get the bounds of a null object");
		}
	}

	/**
	 * This method is used select an option from a drop down list
	 * 
	 * @param element - reference to the drop down list object
	 * @param option  - option to be selected from the drop down list
	 * @throws Exception
	 */
	public void selectOptionFromList(WebElement element, String option) throws Exception {
		if (element == null) {
			error(">>NULL OBJECT ALERT<< Cannot select the option \"" + option
					+ "\" from a null Drop down list object");
			throw new Exception(">>NULL OBJECT ALERT<< Cannot select the option \"" + option
					+ "\" from a null Drop down list object");
		} else {
			Select select = new Select(element);
			try {
				select.selectByVisibleText(option);
			} catch (Exception e1) {
				try {
					select.selectByValue(option);
				} catch (Exception e2) {
					e2.printStackTrace();
					error("Could not select the option \"" + option + "\" from the drop down box");
					throw new Exception("Could not select the option \"" + option + "\" from the drop down box");
				}
			}
		}
	}

	/**
	 * This method is used to get the handle of the currently active window or tab
	 * 
	 * @return - the current window's handle
	 */
	public String getWindowHandle() {
		String currenWindowHandle = driver().getWindowHandle();
		debug("The currently active window's handle is \"" + currenWindowHandle + "\"");
		return currenWindowHandle;
	}

	/**
	 * This method is used to get the handles of all the windows opened by the
	 * WebDriver
	 * 
	 * @return - String iterator containing all the windows handles
	 */
	public Iterator<String> getWindowHandles() {
		return driver().getWindowHandles().iterator();
	}

	/**
	 * This method is used to switch the control to another window or tab with the
	 * specified window Handle
	 * 
	 * @param windowHandle - handle of the window to switch to
	 */
	public void switchToWindow(String windowHandle) {
		driver().switchTo().window(windowHandle);
		debug("Switched to the window/tab having the window handle - " + windowHandle);
	}

	/**
	 * This method is used to switch the control to a frame object
	 * 
	 * @param element - reference to the frame object
	 * @throws Exception
	 */
	public void switchToFrame(WebElement element) throws Exception {
		if (element != null) {
			driver().switchTo().frame(element);
			debug("Switched the control to the frame \"" + element.toString() + "\"");
		} else {
			error(">>NULL OBJECT ALERT<< Cannot switch to a null frame");
			throw new Exception(">>NULL OBJECT ALERT<< Cannot switch to a null frame");
		}
	}

	/**
	 * This method is used to switch to an active alert
	 * 
	 * @return - the reference to the Alert
	 */
	private Alert switchToAlert() {
		Alert alert = driver().switchTo().alert();
		debug("Switched the control to the active alert");
		return alert;
	}

	/**
	 * This method is used to perform various actions on the alert like
	 * accept/dismiss
	 * 
	 * @param action - action to be performed on the alert. Example - accept/dismiss
	 */
	public void manageAlert(String action) throws Exception {
		Alert alert = switchToAlert();
		switch (action.trim().toLowerCase()) {
		case "accept":
			alert.accept();
			debug("The active alert was accepted");
			break;
		case "dismiss":
			alert.dismiss();
			debug("The active alert was dismissed");
			break;
		default:
			error("The action \"" + action + "\" is not a valid operation on the alert");
			throw new Exception("The action \"" + action + "\" is not a valid operation on the alert");
		}
	}

	/**
	 * This method is used to drag an object and drop it to a position relative to
	 * the current position
	 * 
	 * @param element - reference to the object to be dragged
	 * @param xOffset - horizontal offset(in pixels)
	 * @param yOffset - vertical offset(in pixels)
	 */
	public void dragAndDropBy(WebElement element, int xOffset, int yOffset) throws Exception {
		if (element != null) {
			actions().dragAndDropBy(element, xOffset, yOffset).build().perform();
			debug("The object \"" + element.toString()
					+ "\" was dragged and dropped to the position which has the horizontal offset of " + xOffset
					+ " pixels and vertical offset of " + yOffset
					+ " pixels w.r.t. the current position of the object");
		} else {
			error(">>NULL OBJECT ALERT<< Cannot drag a null object");
			throw new Exception(">>NULL OBJECT ALERT<< Cannot drag a null object");
		}
	}

	/**
	 * This method is used to drag an object and drop it on top of another object
	 * 
	 * @param source      - reference to the object to be dragged
	 * @param destination - reference to the object over which the source object
	 *                    will be dropped
	 */
	public void dragAndDrop(WebElement source, WebElement destination) throws Exception {
		if (source != null && destination != null) {
			actions().dragAndDrop(source, destination).build().perform();
			debug("The object \"" + source + "\" was dragged and dropped on top of the object \"" + destination + "\"");
		} else {
			error(">>NULL OBJECT ALERT<< Cannot perform drag and drop operation on null objects");
			throw new Exception(">>NULL OBJECT ALERT<< Cannot perform drag and drop operation on null objects");
		}
	}

	/**
	 * This method is used to hover mouse over an object
	 * 
	 * @param element - reference to the object over which mouse is to be moved
	 * @throws Exception
	 */
	public void hoverMouse(WebElement element) throws Exception {
		if (element != null) {
			actions().moveToElement(element);
			debug("The mouse pointer was moved to the center point of the object \"" + element.toString() + "\"");
		} else {
			error(">>NULL OBJECT ALERT<< Cannot move the mouse pointer on the top of the null object");
			throw new Exception(">>NULL OBJECT ALERT<< Cannot move the mouse pointer on the top of the null object");
		}
	}

	/**
	 * This method is used capture the screenshot of the visible area
	 * 
	 * @param screenshotPath - path to the file where the screenshot will be saved
	 * @throws IOException
	 */
	public void takeVisibleScreenshot(String screenshotPath) throws IOException {
		File file = ((TakesScreenshot) driver()).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File(screenshotPath));
		info("Screenshot of the visible area was captured at path - " + screenshotPath);
	}

	/**
	 * This method is used to capture the screenshot of an element/object
	 * 
	 * @param element        - reference to the object
	 * @param screenshotPath - path to the file where the screenshot will be saved
	 * @throws Exception
	 */
	public void takeVisibleScreenshot(WebElement element, String screenshotPath) throws Exception {
		if (element != null) {
			File file = ((TakesScreenshot) element).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(file, new File(screenshotPath));
			info("Screenshot of the object \"" + element.toString() + "\" was captured at path - " + screenshotPath);
		} else {
			error(">>NULL OBJECT ALERT<< Cannot capture the screenshot of a null object");
			throw new Exception(">>NULL OBJECT ALERT<< Cannot capture the screenshot of a null object");
		}
	}

	/**
	 * This method is used to take the screenshot of the whole page by scrolling to
	 * the bottom of the page
	 * 
	 * @param screenshotPath - path to the file where the screenshot will be saved
	 * @throws IOException
	 */
	public void takeFullPageScreenshot(String screenshotPath) throws IOException {
		Screenshot screenshot = new AShot().coordsProvider(new WebDriverCoordsProvider())
				.shootingStrategy(ShootingStrategies.viewportPasting(500)).takeScreenshot(driver());
		ImageIO.write(screenshot.getImage(), "jpg", new File(screenshotPath));
		info("Screenshot of the full page was captured at path - " + screenshotPath);
	}

	/**
	 * This method is used to close the currently active window/tab opened by the
	 * WebDriver Instance
	 */
	public void close() {
		driver().close();
		info("Closed the current window/tab");
	}

	/**
	 * This method is used to close all the tabs/windows opened by the WebDriver
	 * instance
	 */
	public void quit() {
		driver().quit();
		info("Closed all the windows/tabs");
	}
}
