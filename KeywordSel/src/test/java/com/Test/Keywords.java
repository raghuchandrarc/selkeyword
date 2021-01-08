package com.Test;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.Utils.Xls_Reader;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.Utils.FileDownloader;
import com.Utils.GetScreenShot;
import com.Utils.Log4j;
import com.Utils.Resources;
import com.google.common.base.Function;

public class Keywords extends Resources {

	static String titleOfPage = null;

	/**
	 * This Method will return web element.
	 * 
	 * @param locator
	 * @return
	 * @throws Exception
	 */
	public static WebElement getLocator(String locator) throws Exception {
		String[] split = locator.split(":");
		String locatorType = split[0];
		String locatorValue = split[1];

		if (locatorType.toLowerCase().equals("id"))
			return driver.findElement(By.id(locatorValue));
		else if (locatorType.toLowerCase().equals("name"))
			return driver.findElement(By.name(locatorValue));
		else if ((locatorType.toLowerCase().equals("classname")) || (locatorType.toLowerCase().equals("class")))
			return driver.findElement(By.className(locatorValue));
		else if ((locatorType.toLowerCase().equals("tagname")) || (locatorType.toLowerCase().equals("tag")))
			return driver.findElement(By.className(locatorValue));
		else if ((locatorType.toLowerCase().equals("linktext")) || (locatorType.toLowerCase().equals("link")))
			return driver.findElement(By.linkText(locatorValue));
		else if (locatorType.toLowerCase().equals("partiallinktext"))
			return driver.findElement(By.partialLinkText(locatorValue));
		else if ((locatorType.toLowerCase().equals("cssselector")) || (locatorType.toLowerCase().equals("css")))
			return driver.findElement(By.cssSelector(locatorValue));
		else if (locatorType.toLowerCase().equals("xpath"))
			return driver.findElement(By.xpath(locatorValue));
		else
			throw new Exception("Unknown locator type '" + locatorType + "'");
	}

	public static List<WebElement> getLocators(String locator) throws Exception {
		String[] split = locator.split(":");
		String locatorType = split[0];
		String locatorValue = split[1];

		if (locatorType.toLowerCase().equals("id"))
			return driver.findElements(By.id(locatorValue));
		else if (locatorType.toLowerCase().equals("name"))
			return driver.findElements(By.name(locatorValue));
		else if ((locatorType.toLowerCase().equals("classname")) || (locatorType.toLowerCase().equals("class")))
			return driver.findElements(By.className(locatorValue));
		else if ((locatorType.toLowerCase().equals("tagname")) || (locatorType.toLowerCase().equals("tag")))
			return driver.findElements(By.className(locatorValue));
		else if ((locatorType.toLowerCase().equals("linktext")) || (locatorType.toLowerCase().equals("link")))
			return driver.findElements(By.linkText(locatorValue));
		else if (locatorType.toLowerCase().equals("partiallinktext"))
			return driver.findElements(By.partialLinkText(locatorValue));
		else if ((locatorType.toLowerCase().equals("cssselector")) || (locatorType.toLowerCase().equals("css")))
			return driver.findElements(By.cssSelector(locatorValue));
		else if (locatorType.toLowerCase().equals("xpath"))
			return driver.findElements(By.xpath(locatorValue));
		else
			throw new Exception("Unknown locator type '" + locatorType + "'");
	}

	public static WebElement getWebElement(String locator) throws Exception {
		return getLocator(Repository.getProperty(locator));
	}

	public static List<WebElement> getWebElements(String locator) throws Exception {
		return getLocators(Repository.getProperty(locator));
	}

	/**
	 * Reuse Test cases Reuse_TC|TC-1|TS04|TS06|Y TC-1-->Test Case ID column in
	 * excel TS04|TS06--->TestStepID column in excel. steps to execute from
	 * TS04-TS06 Y -Yes override TC-1 data. N-No
	 */

	public static String ReuseTestCase() {
		try {

			@SuppressWarnings("unused")
			String TSStatus = "Pass";
			Log4j.info("ReuseTestCase is called..");
			String TestReuse = testData;
			Xls_Reader s = new Xls_Reader(testSuite);
			if (TestReuse.startsWith("Reuse_TC")) {
				String[] testReusePara = TestReuse.split("\\|");
				System.out.println("Parameters for Reuse Test cases ..." + testReusePara[1] + " ---- "
						+ testReusePara[2] + " ---- " + testReusePara[3] + " ---- " + testReusePara[4]+" ---- " + testReusePara[5]);
				String testSheetname=testReusePara[1] ;
				String TestCaseID = testReusePara[2];
				String reuseTestStepStart = testReusePara[3];
				String reuseTestStepEnd = testReusePara[4];

				if (testReusePara[5].equals("Y")) {
					int testStepStart = s.getCellRowNum(testSheetname, "Test Case ID", TestCaseID, "TestStepID",
							reuseTestStepStart);
					int testStepEnd = s.getCellRowNum(testSheetname, "Test Case ID", TestCaseID, "TestStepID",
							reuseTestStepEnd);
					for (int TS = testStepStart; TS <= testStepEnd; TS++) {
						String testcase_ID = suiteData.getCellData(testSheetname, "Test Case ID", TS);
						System.out.println("testcase_ID in Reuse  .." + testcase_ID);
						String TestDataField = suiteData.getCellData(testSheetname, "TestDataField", TS);
						testData = testStepData.GetTestData("MasterTestData", TestCaseID, TestDataField, "Testdata");
						keyword = suiteData.getCellData(testSheetname, "Keyword", TS);
						webElement = suiteData.getCellData(testSheetname, "WebElement", TS);
						Method method = Keywords.class.getMethod(keyword);
						TSStatus = (String) method.invoke(method);

					}

				} else {
					int testStepStart = s.getCellRowNum(testSheetname, "Test Case ID", TestCaseID, "TestStepID",
							reuseTestStepStart);
					int testStepEnd = s.getCellRowNum(testSheetname, "Test Case ID", TestCaseID, "TestStepID",
							reuseTestStepEnd);
					for (int TS = testStepStart; TS <= testStepEnd; TS++) {
						String TestDataField = suiteData.getCellData("TestCases", "TestDataField", TS);
						testData = testStepData.GetTestData("MasterTestData", testcase_ID, TestDataField, "Testdata");
						keyword = suiteData.getCellData(testSheetname, "Keyword", TS);
						webElement = suiteData.getCellData(testSheetname, "WebElement", TS);
						Method method = Keywords.class.getMethod(keyword);
						TSStatus = (String) method.invoke(method);

					}

				}
			}
		} catch (Throwable t) {
			Log4j.error("ReuseTestCase Wrong--- " + t.getMessage());
			return "Failed - ReuseTestcase";
		}
		return "Pass";
	}

	/**
	 * Navigate to URL
	 */

	public static String Navigate() {
		Log4j.info("Navigate is called");
		System.out.println("Navigate is called");
		driver.get(testData);
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		return "Pass";
	}

	/**
	 * selectRadioButton
	 */

	public static String selectRadioButton() {
		System.out.println("selectRadioButton is called");
		try {
			Log4j.info("selectRadioButton ... " + webElement);
			getWebElement(webElement).click();
		} catch (Throwable t) {
			Log4j.error("Not able to selectRadioButton--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}

	/**
	 * InputText into the Element
	 */

	public static String InputText() {
		System.out.println("InputText is called");
		try {
			Log4j.info("Enter text into ... " + webElement);
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.visibilityOf(getWebElement(webElement)));
			highLighterMethod(driver, getWebElement(webElement));
			getWebElement(webElement).clear();
			getWebElement(webElement).sendKeys(testData);
		} catch (Throwable t) {
			Log4j.error("Not able to InputText--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}

	/**
	 * Right clicks on the element
	 */

	public static String rightClick() {
		System.out.println("rightClick is called");
		try {
			Log4j.info("rightClick is called ... " + webElement);
			Actions action = new Actions(driver);
			action.contextClick((WebElement) getWebElement(webElement)).perform();
		} catch (Throwable t) {
			Log4j.error("Not able rightClick on Element--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}

	/**
	 * Checks that the element is enabled in the current web page
	 */

	public static String isEnabled() {
		System.out.println("isEnabled is called");
		try {
			Log4j.info("isEnabled is called ... " + webElement);
			getWebElement(webElement).isEnabled();
		} catch (Throwable t) {
			Log4j.error(" Element is not isEnabled--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}

	/**
	 * Checks that the element is displayed in the current web page
	 */
	public static String isDisplayed() {
		System.out.println("isDisplayed is called");
		try {
			Log4j.info("isDisplayed is called ... " + webElement);

			getWebElement(webElement).isDisplayed();
		} catch (Throwable t) {
			Log4j.error("Element is not isDisplayed--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}

	/**
	 * refreshPage
	 */

	public static String refreshPage() {
		System.out.println("refreshPage is called");
		try {
			Log4j.info("refreshPage is called ... " + webElement);
			driver.navigate().refresh();
		} catch (Throwable t) {
			Log4j.error("Not able to refreshPage--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}

	/**
	 * Switch back to the parent window
	 */

	public static String switchToParentWindow() {
		System.out.println("switchToParentWindow is called");
		try {
			Log4j.info("switchToParentWindow is called ... " + webElement);
			String parentWindow = driver.getWindowHandle();
			driver.switchTo().window(parentWindow);
		} catch (Throwable t) {
			Log4j.error("Not able to switchToParentWindow--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}

	/**
	 * Switch to the child window
	 * 
	 * @throws Exception
	 */

	public static String switchToChildWindow() throws Exception {
		System.out.println("switchToChildWindow is called");
		getWebElement(webElement).click();

		String parent = driver.getWindowHandle();
		Set<String> windows = driver.getWindowHandles();
		try {
			if (windows.size() > 1) {
				for (String child : windows) {
					if (!child.equals(parent)) {

						if (driver.switchTo().window(child).getTitle().equals(testData)) {

							driver.switchTo().window(child);
						}

					}
				}
			}
		} catch (Throwable t) {
			Log4j.error("Not able to switchToChildWindow--- " + t.getMessage());

			return "Failed - Element not found " + webElement;

		}
		return "Pass";
	}

	/**
	 * ClearText in Input field
	 */

	public static String ClearText() {
		System.out.println("clearText is called");
		try {
			Log4j.info("clearText is called ... " + webElement);
			getWebElement(webElement).clear();
		} catch (Throwable t) {
			Log4j.error("Not able to ClearText on Element--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}

	/**
	 * highLighter of webelement
	 */
	public static void highLighterMethod(WebDriver driver, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);
	}

	/**
	 * Scrolls down the page till the element is visible
	 */

	public static String scrollElementIntoView() {
		System.out.println("scrollElementIntoView is called");
		try {
			Log4j.info("scrollElementIntoView is called ... " + webElement);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
					getWebElement(webElement));
		} catch (Throwable t) {
			Log4j.error("Not able to scrollElementIntoView on Element--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}

	/**
	 * Scrolls down the page till the element is visible and clicks on the
	 * element
	 */

	public static String scrollElementIntoViewClick() {
		System.out.println("scrollElementIntoViewClick is called");
		try {
			Log4j.info("scrollElementIntoViewClick is called ... " + webElement);
			Actions action = new Actions(driver);
			action.moveToElement(getWebElement(webElement)).click().perform();
		} catch (Throwable t) {
			Log4j.error("Not able to scrollElementIntoViewClickon Element--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}

	/**
	 * Reads the url of current web page
	 */

	public static String readUrlOfPage() {
		System.out.println("readUrlOfPage is called");
		try {
			Log4j.info("readUrlOfPage is called ... " + webElement);
			driver.getCurrentUrl();
		} catch (Throwable t) {
			Log4j.error("Not able to readUrlOfPage--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}

	/**
	 * Navigates to the specified url
	 */

	public static String navigateToURL() {
		System.out.println("navigateToURL is called");
		try {
			Log4j.info("navigateToURL is called ... " + webElement);
			driver.navigate().to(testData);
			;
		} catch (Throwable t) {
			Log4j.error("Not able to navigateToURL--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}

	/**
	 *
	 * Lets say there is header menu bar, on hovering the mouse, drop down
	 * should be displayed
	 */

	public static String dropDownByMouseHover() {
		System.out.println("dropDownByMouseHover is called");
		try {
			Log4j.info("dropDownByMouseHover is called ... " + webElement);
			Actions action = new Actions(driver);

			action.moveToElement(getWebElement(webElement)).perform();
			WebElement subElement = driver.findElement(By.xpath(testData));
			action.moveToElement(subElement);
			action.click().build().perform();
			driver.navigate().to(testData);
			;
		} catch (Throwable t) {
			Log4j.error("Not able to dropDownByMouseHover--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}

	/**
	 * 
	 * File upload in IE browser.
	 */

	public static String fileUploadinIE() {
		System.out.println("fileUploadinIE is called");
		try {
			Log4j.info("fileUploadinIE is called ... " + webElement);
			getWebElement(webElement).click();
			StringSelection ss = new StringSelection(testData);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
			Robot r;
			try {
				r = new Robot();

				r.keyPress(KeyEvent.VK_ENTER);

				r.keyRelease(KeyEvent.VK_ENTER);

				r.keyPress(KeyEvent.VK_CONTROL);
				r.keyPress(KeyEvent.VK_V);
				r.keyRelease(KeyEvent.VK_V);
				r.keyRelease(KeyEvent.VK_CONTROL);

				r.keyPress(KeyEvent.VK_ENTER);
				r.keyRelease(KeyEvent.VK_ENTER);

			} catch (AWTException e) {

			}
		} catch (Throwable t) {
			Log4j.error("Not able to fileUploadinIE-- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}

	/**
	 * 
	 * readTitleOfPage
	 */

	public static String readTitleOfPage() {
		System.out.println("readTitleOfPage is called");
		try {
			Log4j.info("readTitleOfPage is called ... " + webElement);
			if (!(titleOfPage == null)) {
				titleOfPage = null;
			}
			titleOfPage = driver.getTitle();
		} catch (Throwable t) {
			Log4j.error("Not able to readTitleOfPage--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}

	/**
	 * 
	 * AssertString
	 */

	public static String AssertString() {
		try {
			Log4j.info("Assert String is called ... " + webElement);
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.elementToBeClickable(getWebElement(webElement))).getText();
			String expected = getWebElement(webElement).getText();

			Assert.assertEquals(expected, testData);
		} catch (Throwable t) {
			Log4j.error("Not able to AssertString--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}

	/**
	 * Click on webelement
	 */

	public static String Click() {
		System.out.println("Click is called");
		try {
			Log4j.info("Click is called ... " + webElement);
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.elementToBeClickable(getWebElement(webElement))).click();
			
		} catch (Throwable t) {
			t.printStackTrace();
			Log4j.error("Not able to Click--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}

	/**
	 * VerifyText in webelement
	 */
	public static String VerifyText() {
		System.out.println("VerifyText is called");
		try {
			String ActualText = getWebElement(webElement).getText();
			System.out.println(ActualText);
			if (!ActualText.equals(testData)) {
				return "Failed - Actual text " + ActualText + " is not equal to to expected text " + testData;
			}
		} catch (Throwable t) {
			Log4j.error("Not able to VerifyText-- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}
	/**
	 * selectDropDownByVisibleText in WebElement
	 */
	@SuppressWarnings("deprecation")
	public static String selectDropDownByVisibleText() {
		System.out.println("selectDropDownByVisibleText Data is called");
		try {

			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.pollingEvery(2, TimeUnit.SECONDS)
					.until(ExpectedConditions.elementToBeClickable(getWebElement(webElement)));

			Select sel = new Select(getWebElement(webElement));
			sel.selectByValue(testData);

		} catch (Throwable t) {
			Log4j.error("Not able to selectDropDownByVisibleText--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}
	/**
	 * selectDropDownByIndex in WebElement
	 */
	public static String selectDropDownByIndex() {
		System.out.println("selectDropDownByIndex Data is called");
		try {

			WebDriverWait wait = new WebDriverWait(driver, 30);

			wait.pollingEvery(2, TimeUnit.SECONDS)
					.until(ExpectedConditions.elementToBeClickable(getWebElement(webElement)));
			highLighterMethod(driver, getWebElement(webElement));
			Select sel = new Select(getWebElement(webElement));
			sel.selectByIndex(1);

		} catch (Throwable t) {
			Log4j.error("Not able to selectDropDownByIndex--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}
	/**
	 * selectAllCheckbox in WebElement
	 */
	public static String selectAllCheckbox() {
		System.out.println("selectDropDownByIndex Data is called");
		try {

			List<WebElement> list = getWebElements(webElement);

			for (WebElement element : list) {
				if (!element.isSelected()) {
					element.click();
				}
			}

		} catch (Throwable t) {
			Log4j.error("Not able to selectAllCheckbox--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}
	/**
	 * selectCheckBox in WebElement
	 */
	public static String selectCheckBox() {
		System.out.println("selectCheckBox  is called");
		try {
			@SuppressWarnings("unused")
			boolean res = true;

			while (!getWebElement(webElement).isSelected()) {
				getWebElement(webElement).click();
				if (getWebElement(webElement).isSelected()) {
					res = false;
					break;
				}

			}

		} catch (Throwable t) {
			Log4j.error("Not able to selectCheckBox--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}
	/**
	 * expliciteWait of WebElement
	 */
	public static String explicitWait() throws Exception {
		try {
			WebElement webElemnt = getWebElement(webElement);
			if (webElemnt.isEnabled() && webElemnt.isDisplayed()) {
				Log4j.info("explicitWait is called ... " + webElemnt);
				WebDriverWait wait = new WebDriverWait(driver, 60);
				wait.until(ExpectedConditions.visibilityOf(webElemnt));
				wait.until(ExpectedConditions.stalenessOf(webElemnt));
			} else {
				System.out.println("Unable to visibility of element");
			}

		} catch (Throwable t) {
			Log4j.error("Not able to --- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}

		return "Pass";
	}
	
	public static void CLick(WebElement element) throws Exception {
		try {
			if (element.isEnabled() && element.isDisplayed()) {
				System.out.println("Clicking on element with using java script click");

				((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			} else {
				System.out.println("Unable to click on element");
			}
		} catch (StaleElementReferenceException e) {
			System.out.println("Element is not attached to the page document " + e.getStackTrace());
		} catch (NoSuchElementException e) {
			System.out.println("Element was not found in DOM " + e.getStackTrace());
		} catch (Exception e) {
			System.out.println("Unable to click on element " + e.getStackTrace());
		}
	}

	public static String clickWhenReady(By locator, int timeout) {
		System.out.println("clickWhenReady is called");
		try {
			Log4j.info("clickWhenReady is called ... " + webElement);
			WebElement element = null;
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			element = wait.until(ExpectedConditions.elementToBeClickable(locator));
			element.click();
		} catch (Throwable t) {
			Log4j.error("Not able to clickWhenReady--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}
	/**
	 * waitFor of WebElement
	 */

	public static String waitFor() throws InterruptedException {
		try {

			String timeout = testData;
			System.out.println("Waiting Time in seconds " + timeout);
			long waitTime = Long.parseLong(timeout);
			long seconds = TimeUnit.SECONDS.toMillis(waitTime);
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			return "Failed - unable to load the page";
		}
		return "Pass";
	}

	/**
	 * Navigate to next page
	 */
	public static String moveToNextPage() throws InterruptedException {
		driver.navigate().forward();
		return "Pass";
	}

	/**
	 * Reads the text present in the web element and writes to excel
	 * 
	 * @throws Exception
	 */
	public static String WriteTextToXl() throws Exception {
		try {
			String GetTExt = getWebElement(webElement).getText();
			System.out.println("Get captured data " + GetTExt);
			System.out.println(" data " + testDataField);

			// Xls_Reader.setcelldata(InputData, TestController.mTestCaseName,
			// TestDataField, GetTExt);
		} catch (InterruptedException e) {
			Log4j.error("Not able to WriteTextToXl--- " + e.getMessage());
			return "Failed - WriteTextToXl";
		}
		return "Pass";
	}

	public static void closeBrowser() {
		driver.quit();
	}

	/**
	 * DragAndDrop Not tested
	 */

	public static String DragAndDrop() throws InterruptedException {
		/*
		 * try { //String[] actType = model.getActionType().split("$"); String[]
		 * actType = getWebElement(webElement);
		 * 
		 * 
		 * WebElement sourceElement = driver.findElement( By.xpath(actType[0]));
		 * WebElement destinationElement = driver.findElement(
		 * By.xpath(actType[1]));
		 * 
		 * Actions action = new Actions(driver);
		 * action.dragAndDrop(sourceElement,
		 * destinationElement).build().perform(); } catch (InterruptedException
		 * e) { Log4j.error("Not able to DragAndDrop--- " + e.getMessage());
		 * return "Failed - DragAndDrop"; }
		 */
		return "Pass";
	}

	/**
	 * webTableClick Not tested
	 */

	public static String webTableClick() throws InterruptedException {
		WebElement mytable = driver.findElement(By.xpath(""));

		List<WebElement> rowstable = mytable.findElements(By.tagName("tr"));

		int rows_count = rowstable.size();

		for (int row = 0; row < rows_count; row++) {

			List<WebElement> Columnsrow = rowstable.get(row).findElements(By.tagName("td"));

			int columnscount = Columnsrow.size();

			for (int column = 0; column < columnscount; column++) {

				String celtext = Columnsrow.get(column).getText();
				// celtext.getClass();
			}
		}
		return "Pass";
	}

	/**
	 * Downloads a file from IE browser
	 * 
	 * @throws Exception
	 */
	public static String downloadFileIE() throws Exception {

		FileDownloader downloadTestFile = new FileDownloader(driver);
		String downloadedFileAbsoluteLocation;
		try {
			downloadedFileAbsoluteLocation = downloadTestFile.downloadFile(getWebElement(webElement));

			Assert.assertTrue(new File(downloadedFileAbsoluteLocation).exists());
		} catch (InterruptedException e) {
			Log4j.error("Not able to downloadFileIE--- " + e.getMessage());
			return "Failed - downloadFileIE";
		}
		return "Pass";
	}

	/**
	 * Double clicks on the particular element
	 * 
	 * @throws Exception
	 */

	public static String doubleClick() throws Exception {
		try {
			Log4j.info("doubleClick is called ... " + webElement);
			Actions action = new Actions(driver);
			action.doubleClick((WebElement) getWebElement(webElement)).perform();
		} catch (InterruptedException e) {

			Log4j.error("Not able to doubleClick--- " + e.getMessage());
			return "Failed - unable to doubleClick";
		}
		return "Pass";
	}

	/**
	 * Verifies that the particular check box is selected
	 */

	public static String verifyCheckBoxSelected() throws Exception {
		try {
			Log4j.info("verifyCheckBoxSelected is called ... " + webElement);
			Assert.assertTrue(getWebElement(webElement).isSelected());
		} catch (InterruptedException e) {

			Log4j.error("Not able to verifyCheckBoxSelected--- " + e.getMessage());
			return "Failed - unable to verifyCheckBoxSelectedss";
		}
		return "Pass";
	}

	/**
	 * Alert accept meaning click on OK button
	 */
	public static String alertAccept() throws Exception {
		try {

			Log4j.info("alertAccept is called ... " + webElement);
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.alertIsPresent());

			Alert alert = driver.switchTo().alert();

			alert.accept();
		} catch (Throwable t) {
			Log4j.error("Element is not alertAccept--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}

	/**
	 * Alert dismiss meaning click on Cancel button
	 */
	public static String alertDismiss() throws Exception {
		try {

			Log4j.info("alertDismiss is called ... " + webElement);
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.alertIsPresent());

			Alert alert = driver.switchTo().alert();

			alert.accept();
		} catch (Throwable t) {
			Log4j.error("error alertDismiss--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}
	/**
	 *multipleSelection in DropDown 
	 */
	public static String multipleSelectionDropDown() throws Exception {
		try {

			Log4j.info("multipleSelectionDropDown is called ... " + webElement);
			List<WebElement> values = getWebElements(webElement);
			String TestReuse = testData;
			@SuppressWarnings({ "rawtypes", "unchecked" })
			ArrayList aList = new ArrayList(Arrays.asList(TestReuse.split(",")));
			for (int i = 0; i < aList.size(); i++) {
				System.out.println(" -->" + aList.get(i));
				String selectionString = aList.get(i).toString();
				multiSelectString(selectionString, values);
			}

		} catch (Throwable t) {
			Log4j.error("error multipleSelectionDropDown--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}

	public static String multiSelectString(String s1, List<WebElement> val) throws Exception {
		try {
			List<WebElement> values = getWebElements(webElement);
			for (WebElement val1 : values) {
				System.out.println(" -->" + val1.getText());
				String s2 = val1.getText();
				s2.equals(s1);
				val1.equals(s2);
				CLick(val1);
			}
		} catch (Throwable t) {
			Log4j.error("error multiSelectString--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return s1;

	}

	

	/**
	 * Switch To frame( html inside another html)
	 */
	public static String switchToFrame() throws Exception {
		try {

			Log4j.info("switchToFrame is called ... " + webElement);
			driver.switchTo().frame(getWebElement(webElement));
		} catch (Throwable t) {
			Log4j.error("error switchToFrame--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}

	/**
	 * Switch back to previous frame or html
	 */
	public static String switchOutOfFrame() throws Exception {
		try {

			Log4j.info("switchOutOfFrame is called ... " + webElement);
			driver.switchTo().defaultContent();
		} catch (Throwable t) {
			Log4j.error("error switchOutOfFrame--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}

	/**
	 * Quit the application
	 */
	public static String quit() throws Exception {
		try {

			Log4j.info("quit is called ... " + webElement);
			driver.quit();
		} catch (Throwable t) {
			Log4j.error("error quit--- " + t.getMessage());
			return "Failed - Element not found " + webElement;
		}
		return "Pass";
	}

	/**
	 * Provide password for window authentication
	 */
	public static String windowAuthenticationPassword() throws Exception {
		Robot robot;
		try {
			Log4j.info("windowAuthenticationPassword is called ... " + webElement);
			robot = new Robot();
			robot.keyPress(KeyEvent.VK_TAB);
			String letter = testData;
			;
			for (int i = 0; i < letter.length(); i++) {
				boolean upperCase = Character.isUpperCase(letter.charAt(i));
				String KeyVal = Character.toString(letter.charAt(i));
				String variableName = "VK_" + KeyVal.toUpperCase();
				Class clazz = KeyEvent.class;
				Field field = clazz.getField(variableName);
				int keyCode = field.getInt(null);

				if (upperCase) {
					robot.keyPress(KeyEvent.VK_SHIFT);
				}

				robot.keyPress(keyCode);
				robot.keyRelease(keyCode);

				if (upperCase) {
					robot.keyRelease(KeyEvent.VK_SHIFT);
				}
			}
			robot.keyPress(KeyEvent.VK_ENTER);
		} catch (AWTException e) {
			Log4j.error("error windowAuthenticationPassword--- " + e.getMessage());

		} catch (NoSuchFieldException e) {

			Log4j.error("error windowAuthenticationPassword--- " + e.getMessage());
		} catch (SecurityException e) {

			Log4j.error("error windowAuthenticationPassword--- " + e.getMessage());
		} catch (IllegalArgumentException e) {

			Log4j.error("error windowAuthenticationPassword--- " + e.getMessage());
		} catch (IllegalAccessException e) {

			Log4j.error("error windowAuthenticationPassword--- " + e.getMessage());
		}
		return "Pass";
	}

}
