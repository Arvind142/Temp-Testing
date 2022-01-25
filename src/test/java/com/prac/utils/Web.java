package com.prac.utils;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import com.google.common.base.Function;
import com.prac.framework.util.Constants;
import com.prac.framework.util.ListenerClass;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Class to deal with all web related application and operations
 * 
 * @author arvin
 *
 */
public class Web {
	/***
	 * to initialize webdriver based on sent browserName
	 * 
	 * @param globalProperty global property to help in identification of how execution works
	 * @return webdriver variable
	 */
	public synchronized WebDriver initializeWebDriver(Properties globalProperty) {
		WebDriver driver = null;
		String browserName= globalProperty.getProperty("browserName");
		try {
			if(globalProperty.getProperty("remoteExecution").toLowerCase().startsWith("n")) {
				//WDM use confirmation
				if(globalProperty.getProperty("useWebDriverManger").toLowerCase().startsWith("y")){
					switch (browserName.toUpperCase()) {
						case "CHROME":
							WebDriverManager.chromedriver().setup();
							driver = new ChromeDriver();
							break;
						case "EDGE":
						case "MSEDGE":
							WebDriverManager.edgedriver().setup();
							driver = new EdgeDriver();
							break;
						case "FIREFOX":
							WebDriverManager.firefoxdriver().setup();
							driver = new FirefoxDriver();
							break;
						case "IE":
						case "INTERNETEXPLORER":
							WebDriverManager.iedriver().setup();
							driver = new InternetExplorerDriver();
							break;
						default:
							throw new InvalidArgumentException("Invalid BrowserName");
					}
				}
				else{
					switch (browserName.toUpperCase()) {
						case "CHROME":
							System.setProperty("webdriver.chrome.driver","Drivers/ChromeDriver.exe");
							driver = new ChromeDriver();
							break;
						case "EDGE":
						case "MSEDGE":
							System.setProperty("webdriver.edge.driver","Drivers/EdgeDriver.exe");
							driver = new EdgeDriver();
							break;
						case "FIREFOX":
							System.setProperty("webdriver.gecko.driver","Drivers/GeckoDriver.exe");
							driver = new FirefoxDriver();
							break;
						case "IE":
						case "INTERNETEXPLORER":
							System.setProperty("webdriver.ie.driver","Drivers/IEDriver.exe");
							driver = new InternetExplorerDriver();
							break;
						default:
							throw new InvalidArgumentException("Invalid BrowserName");
					}
				}
			}
			else{
				URL url = new URL(globalProperty.getProperty("remoteUrl"));
				switch (browserName.toUpperCase()) {
					case "CHROME":
						driver = new RemoteWebDriver(url,new ChromeOptions());
						break;
					case "EDGE":
					case "MSEDGE":
						driver = new RemoteWebDriver(url,new EdgeOptions());
						break;
					case "FIREFOX":
						driver = new RemoteWebDriver(url,new FirefoxOptions());
						break;
					default:
						throw new InvalidArgumentException("Invalid BrowserName");
				}
			}
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
			driver = null;
			return driver;
		} catch (Exception e) {
			e.printStackTrace();
			driver = null;
			return driver;
		}

		return driver;
	}

	/***
	 * 
	 * @param <T>         any class Object i.e. CHROMEOPTIONS/EDGEOPTIONS
	 * @param browserName CHROME/EDGE
	 * @param options     browserOptions which holds reference for object type of
	 *                    ChromeOptions/EdgeOptions
	 * @return web driver reference initialized
	 */
	public <T> WebDriver initializeLocalChromiumWebBrowsers(String browserName, ChromiumOptions<?> options) {
		WebDriver driver = null;
		try {
			switch (browserName.toUpperCase()) {
			case "CHROME":
				WebDriverManager.chromedriver().setup();
				ChromeOptions cOptions = (ChromeOptions) options;
				driver = new ChromeDriver(cOptions);
				break;
			case "EDGE":
			case "MSEDGE":
				WebDriverManager.edgedriver().setup();
				EdgeOptions eOptions = (EdgeOptions) options;
				driver = new EdgeDriver(eOptions);
				break;
			default:
				throw new InvalidArgumentException("Invalid BrowserName");
			}
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
			driver = null;
			return driver;
		}
		return driver;
	}

	/***
	 * to open any url
	 * 
	 * @param driver web driver variable
	 * @param url    url to open
	 */
	public void openURL(WebDriver driver, String url) {
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}

	/**
	 * confirms if page is loaded or not with fluent wait and java script executor
	 * 
	 * @param driver
	 */
	public void waitForPageLoad(RemoteWebDriver driver) {
		FluentWait<RemoteWebDriver> wait = new FluentWait<RemoteWebDriver>(driver).withTimeout(Duration.ofSeconds(20))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Exception.class);
		wait.until(new Function<WebDriver, Boolean>() {
			@Override
			public Boolean apply(WebDriver input) {
				return ((JavascriptExecutor) input).executeScript("return document.readyState").equals("complete");
			}
		});
	}

	/**
	 * to close all browser instances
	 * 
	 * @param driver web driver object
	 */
	public void destroyWebDriver(WebDriver driver) {
		if (driver != null)
			driver.quit();
	}

	/**
	 * to close all browser instances
	 * 
	 * @param driver remoteWebDriver object is taken as param
	 */
	public void destroyWebDriver(RemoteWebDriver driver) {
		if (driver != null)
			driver.quit();
	}

	/**
	 * to work with htmlunitdriver
	 * 
	 * @return htmlUnitDriver to do headless browsing
	 */
	public synchronized HtmlUnitDriver initializeHeadlessWebDriver() {
		return (new HtmlUnitDriver());
	}

	/***
	 * to open any url
	 * 
	 * @param driver htmlunitdriver variable
	 * @param url    url to open
	 */
	public void openURL(HtmlUnitDriver driver, String url) {
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}

	/***
	 * method return by method with adequate locator and location value
	 * 
	 * @param locator constant identifier and locator
	 * @return By class method
	 */
	public By by(String locator) {
		switch (locator.split("~")[0]) {
		case Constants.WebLocator.ID:
			return By.id(locator.split("~")[1]);
		case Constants.WebLocator.NAME:
			return By.name(locator.split("~")[1]);
		case Constants.WebLocator.XPATH:
			return By.xpath(locator.split("~")[1]);
		case Constants.WebLocator.TAGNAME:
			return By.tagName(locator.split("~")[1]);
		case Constants.WebLocator.LINKTEXT:
			return By.linkText(locator.split("~")[1]);
		case Constants.WebLocator.CLASS_NAME:
			return By.className(locator.split("~")[1]);
		case Constants.WebLocator.CSS_SELECTOR:
			return By.cssSelector(locator.split("~")[1]);
		default:
			new InvalidArgumentException("Invalid locator used, kindly use ID,NAME, XPATH, CSS,TAGNAME");
			break;
		}
		return null;
	}

	/***
	 * to get webElement present on UI
	 * 
	 * @param driver         WebDriver reference
	 * @param locator        element locator
	 * @param throwException boolean var to return exception when exception found or
	 *                       skip exception
	 * @param timeOUT        time to wait for element
	 * @return WebElement/null
	 */
	public WebElement getWebElement(WebDriver driver, String locator, Boolean throwException, Integer timeOUT) {
		WebElement element = null;
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeOUT))
					.pollingEvery(Duration.ofSeconds(1));
			element = wait.until(new Function<WebDriver, WebElement>() {
				@Override
				public WebElement apply(WebDriver input) {
					return driver.findElement(by(locator));
				}
			});
			return element;
		} catch (Exception e) {
			if (throwException) {
				throw e;
			} else {
				return null;
			}
		}
	}

	/**
	 * to get webelement
	 * 
	 * @param driver         webdriver reference
	 * @param locator        POM object (by reference)
	 * @param throwException true implies throw exception in problem else return
	 *                       null
	 * @param timeOUT        timeout
	 * @return webElement
	 */

	public WebElement getWebElement(WebDriver driver, By locator, Boolean throwException, Integer timeOUT) {
		WebElement element = null;
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeOUT))
					.pollingEvery(Duration.ofSeconds(1));
			element = wait.until(new Function<WebDriver, WebElement>() {
				@Override
				public WebElement apply(WebDriver input) {
					return driver.findElement(locator);
				}
			});
			return element;
		} catch (Exception e) {
			if (throwException) {
				throw e;
			} else {
				return null;
			}
		}
	}

	/***
	 * method to send values to fields
	 * 
	 * @param driver         webdriver reference
	 * @param locator        element identifier
	 * @param text           text to send
	 * @param throwException rue if you want exception in case of some exception or
	 *                       false to return null in case of exception
	 * @param timeOUT        time to wait for element to be visible
	 * @return boolean status of true - worked and false some issue.
	 */
	public boolean sendKeys(WebDriver driver, String locator, String text, Boolean throwException, Integer timeOUT) {
		WebElement element = null;
		try {
			element = getWebElement(driver, locator, throwException, timeOUT);
			element.clear();
			element.sendKeys(text);
			return true;
		} catch (Exception e) {
			if (throwException) {
				throw e;
			} else {
				return false;
			}
		}
	}

	/**
	 * take screenshot of webpage
	 * 
	 * @param driver   webdriver
	 * @param fileName screenshotName
	 * @return image path
	 */
	public String takeSceenShotWebPage(RemoteWebDriver driver, String fileName) {
		String folderName = ListenerClass.reportingFolder.getPath() + "/Screenshot/";
		File f = (new File(folderName));
		if (!f.exists()) {
			f.mkdirs();
		}
		String imgPath = folderName + fileName + ".jpg";
		File s = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(s, new File(imgPath));
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return "Screenshot/" + fileName + ".jpg";
	}

	/***
	 *  take webpage snapshot
	 * @param driver webdriver instance
	 * @param fileName filename
	 * @return return path for screenshot
	 */
	public String takeSceenShotWebPage(WebDriver driver, String fileName) {
		String folderName = ListenerClass.reportingFolder.getPath() + "/Screenshot/";
		File f = (new File(folderName));
		if (!f.exists()) {
			f.mkdirs();
		}
		String imgPath = folderName + fileName + ".jpg";
		File s = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(s, new File(imgPath));
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return "Screenshot/" + fileName + ".jpg";
	}

	/**
	 * take screenshot of desktop
	 *
	 * @param fileName screenshotName
	 * @return image path
	 */

	public String takeScreenShotScreenSnip(String fileName) {
		String folderName = ListenerClass.reportingFolder.getPath() + "/Screenshot/";
		File f = (new File(folderName));
		if (!f.exists()) {
			f.mkdirs();
		}
		String imgPath = folderName + fileName + ".png";
		try {
			BufferedImage img = new Robot()
					.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			ImageIO.write(img, "png", new File(imgPath));
			return "Screenshot/" + fileName + ".png";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
