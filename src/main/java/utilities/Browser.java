package utilities;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
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

	private WebDriver driver() {
		return driver.get();
	}

	private Actions actions() {
		return actions.get();
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
		if (propertyNameValue != null) {
			String property = propertyNameValue.split(":")[0].trim();
			String value = propertyNameValue.split(":")[1].trim();
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
				throw new Exception(
						"Invalid locator specified for the object \"" + objectName + "\" in the Object Repository");
			}
		} else {
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
		}
		actions.set(new Actions(driver()));
	}

	/**
	 * This method is used to navigate to the webpage with the given URL
	 * 
	 * @param url - URL of the webpage to be navigated to
	 */
	public void get(String url) {
		driver().get(url);
	}

	/**
	 * This method is used to maximize the browser window opened by the WebDriver
	 * instance
	 */
	public void maximize() {
		driver().manage().window().maximize();
	}

	/**
	 * This method is used to delete all the cookies
	 */
	public void deleteCookies() {
		driver().manage().deleteAllCookies();
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
		if (!disableVisibilityCheck.get())
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		else
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
	public WebElement FindElement(String objectName) throws Exception {
		By by = getStoredObjectLocator(objectName);
		return findElement(by);
	}

	/**
	 * This method is used to perform click operation on an object/element
	 * 
	 * @param element - reference to the object to be clicked
	 * @throws Exception
	 */
	public void click(WebElement element) throws Exception {
		if (element != null)
			element.click();
		else
			throw new Exception("Cannot click on a null object!!!");
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
		} else
			throw new Exception("Cannot perform sendkeys opeartion on a null object");
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
		if (element != null)
			return element.getText();
		else
			throw new Exception("Cannot fetch text of a null object");
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
		if (element != null)
			return element.getAttribute(attribute);
		else
			throw new Exception("Cannot fetch the attribute \"" + attribute + "\" of a null object");
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
		else
			throw new Exception("Cannot get the bounds of a null object");
	}

	/**
	 * This method is used select an option from a drop down list
	 * 
	 * @param element - reference to the drop down list object
	 * @param option  - option to be selected from the drop down list
	 * @throws Exception
	 */
	public void selectOptionFromList(WebElement element, String option) throws Exception {
		if (element == null)
			throw new Exception("Cannot select the option \"" + option + "\" from a null Drop down list object");
		else {
			Select select = new Select(element);
			try {
				select.selectByVisibleText(option);
			} catch (NoSuchElementException e1) {
				try {
					select.selectByValue(option);
				} catch (NoSuchElementException e2) {
					e2.printStackTrace();
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
		return driver().getWindowHandle();
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
	}

	/**
	 * This method is used to switch the control to a frame object
	 * 
	 * @param element - reference to the frame object
	 * @throws Exception
	 */
	public void switchToFrame(WebElement element) throws Exception {
		if (element != null)
			driver().switchTo().frame(element);
		else
			throw new Exception("Cannot switch to a null frame");
	}

	/**
	 * This method is used to switch to an active alert
	 * 
	 * @return - the reference to the Alert
	 */
	private Alert switchToAlert() {
		return driver().switchTo().alert();
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
			break;
		case "dismiss":
			alert.dismiss();
			break;
		default:
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
		if (element != null)
			actions().dragAndDropBy(element, xOffset, yOffset).build().perform();
		else
			throw new Exception("Cannot drag a null object");
	}

	/**
	 * This method is used to drag an object and drop it on top of another object
	 * 
	 * @param source      - reference to the object to be dragged
	 * @param destination - reference to the object over which the source object
	 *                    will be dropped
	 */
	public void dragAndDrop(WebElement source, WebElement destination) throws Exception {
		if (source != null && destination != null)
			actions().dragAndDrop(source, destination).build().perform();
		else
			throw new Exception("Cannot perform drag and drop operation on null objects");
	}

	/**
	 * This method is used to hover mouse over an object
	 * 
	 * @param element - reference to the object over which mouse is to be moved
	 * @throws Exception
	 */
	public void hoverMouse(WebElement element) throws Exception {
		if (element != null)
			actions().moveToElement(element);
		else
			throw new Exception("Cannot move the mouse pointer on the top of the null object");
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
		} else {
			throw new Exception("Cannot capture the screenshot of a null object");
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
	}

	/**
	 * This method is used to close the currently active window/tab opened by the
	 * WebDriver Instance
	 */
	public void close() {
		driver().close();
	}

	/**
	 * This method is used to close all the tabs/windows opened by the WebDriver
	 * instance
	 */
	public void quit() {
		driver().quit();
	}
}
