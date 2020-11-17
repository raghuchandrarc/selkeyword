package com.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.Test.Keywords;
import com.Utils.ExcelUtils;
import com.Utils.GetScreenShot;
import com.Utils.Log4j;
import com.Utils.Resources;
import com.Utils.Xls_Reader;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;


public class TestController extends Resources {

	String TestSuites = testSuite;
	Xls_Reader s = new Xls_Reader(testSuite);
	public static final String FIREFOX = "firefox";
	public static final String IE = "IE";
	public static final String CHROME = "chrome";
	private String sTestCaseName;
	 
	 private int iTestCaseRow;
	 

	@BeforeClass
	public void initBrowser() throws IOException {

		Initialize();
		//selectBrowser(BROWSER_NAME);

	}

	/**
	 * Select the browser on which you want to execute tests
	 **/
	private void selectBrowser(String browserName) {

		switch (browserName) {

		case FIREFOX:

			firefoxProfile();
			break;

		case IE:
			ie();
			break;

		case CHROME:
			chrome();
			break;

		default:
			firefoxProfile();
			break;
		}

	}

	private void chrome() {
		ChromeOptions chromeOptions = new ChromeOptions();

		chromeOptions.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
		System.setProperty("webdriver.chrome.driver", chromeDriver);
		dr = new ChromeDriver(chromeOptions);
		driver = new EventFiringWebDriver(dr);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(20, TimeUnit.SECONDS);
	}

	@SuppressWarnings("deprecation")
	private void ie() {
		DesiredCapabilities returnCapabilities = DesiredCapabilities.internetExplorer();
		returnCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		returnCapabilities.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
		System.setProperty("webdriver.ie.driver", ieDriver);

		dr = new InternetExplorerDriver(returnCapabilities);
		driver = new EventFiringWebDriver(dr);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(20, TimeUnit.SECONDS);

	}

	/**
	 * Firefox profile will help in automatic download of files
	 */
	@SuppressWarnings("deprecation")
	private void firefoxProfile() {
		System.setProperty("webdriver.gecko.driver", firefoxDriver);

		FirefoxOptions options = new FirefoxOptions();
		options.addPreference("--log", "trace");
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setCapability("moz:firefoxOptions", options);

		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("browser.download.folderList", 1);
		profile.setPreference("browser.download.manager.showWhenStarting", false);
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
				"application/vnd.openxmlformats-officedocument.wordprocessingml.document");

		dr = new FirefoxDriver(capabilities);
		driver = new EventFiringWebDriver(dr);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(20, TimeUnit.SECONDS);

	}

	//@Test
	public void TestCaseController() throws Exception {
		DOMConfigurator.configure("log4j.xml");

		@SuppressWarnings("unused")
		String TCStatus = "Pass";
		ExtentTest test;
		ExtentReports extent = new ExtentReports();
		ExtentSparkReporter htmlReporter;

		// create ExtentReports and attach reporter(s)
		htmlReporter = new ExtentSparkReporter(CreateFileWithTimeStamp());
		extent.attachReporter(htmlReporter);
		String runSuiteName=suiteProperties.getProperty("runSuite");
		testcaseSheetName=suiteProperties.getProperty("testcaseSheetName");

		// loop through the test cases
		for (int TC = 2; TC <= suiteData.getRowCount(runSuiteName); TC++) {

			
			String testCaseDescription=suiteData.getCellData(runSuiteName, "Description", TC);
			String runMode = suiteData.getCellData(runSuiteName, "RunMode", TC);
			testCaseName = suiteData.getCellData(runSuiteName, "TestCaseName", TC);
				//System.out.println("First sheet "+testCaseName);
			
			if (runMode.equals("Y")) {
				
				String TSStatus = "Pass";

				// loop through the test steps
				System.out.println("SuiteData.getRowCount(TestCaseID)" + suiteData.getRowCount("TestCases"));
				
				test = extent.createTest(testCaseName, testCaseDescription );
				for (int TS = 2; TS <= suiteData.getRowCount("TestCases"); TS++) {
					testcase_ID = suiteData.getCellData(testcaseSheetName, "Test Case ID", TS);
					if(testcase_ID.equals(testCaseName)){
					keyword = suiteData.getCellData(testcaseSheetName, "Keyword", TS);
					webElement = suiteData.getCellData(testcaseSheetName, "WebElement", TS);
					proceedOnFail = suiteData.getCellData(testcaseSheetName, "ProceedOnFail", TS);
					testStepID = suiteData.getCellData(testcaseSheetName, "TestStepID", TS);
					descriptionOfTest = suiteData.getCellData(testcaseSheetName, "Description", TS);
					testDataField = suiteData.getCellData(testcaseSheetName, "TestDataField", TS);
					testData = testStepData.GetTestData("MasterTestData", testcase_ID, testDataField, "Testdata");
					Log4j.startTestCase(testcaseSheetName, keyword, webElement, testData);
					Method method = Keywords.class.getMethod(keyword);
					TSStatus = (String) method.invoke(method);
					
					if (TSStatus.contains("Failed")) {
						// take the screenshot
						String filename = "TestCases" + testStepID + "[" + testData + "]";
						TCStatus = TSStatus;
						Log4j.error(testCaseName);
						String screenShot = GetScreenShot.capture(driver, filename);
						test.fail( "<font color='black'style='font-size:12px' </font> "+testcase_ID + " ----->" + testStepID + " ----->" + descriptionOfTest + " ----->"+ testData,MediaEntityBuilder.createScreenCaptureFromPath(screenShot, "<font color='black'style='font-size:15px' </font> "+testcase_ID + " ----->" + testStepID + " ----->" + keyword + " ----->"+ testData + Log4j.error(testCaseName)).build());

					} else {
						
						test.log(Status.PASS,  "<font color='black'style='font-size:12px' </font> "+testcase_ID + " ----->" + testStepID + " ----->" + descriptionOfTest + " ----->"+ testData);
					}
					extent.flush();

					if (proceedOnFail.equals("N")) {
						break;
					}
					}

				}

			}
		}

		
	}
	
	@Test(dataProvider = "Authentication")
	public void Registration_data(String sUserName, String sPassword) throws Exception {
		chrome();
		// http://secure.smartbearsoftware.com/samples/testcomplete11/WebOrders/login.aspx
		driver.get("http://secure.smartbearsoftware.com/samples/testcomplete11/WebOrders/login.aspx");

		driver.findElement(By.xpath(".//input[@id='ctl00_MainContent_username']")).click();

		driver.findElement(By.xpath(".//input[@id='ctl00_MainContent_username']")).sendKeys(sUserName);

		System.out.println(sUserName);

		driver.findElement(By.xpath("//input[@id='ctl00_MainContent_password']")).sendKeys(sPassword);

		

	}
	
	@Test(dataProvider = "Authentication")
	public void Registration_data2(String sUserName, String sPassword) throws Exception {
		ie();
		// http://secure.smartbearsoftware.com/samples/testcomplete11/WebOrders/login.aspx
		driver.get("http://secure.smartbearsoftware.com/samples/testcomplete11/WebOrders/login.aspx");

		driver.findElement(By.xpath(".//input[@id='ctl00_MainContent_username']")).click();

		driver.findElement(By.xpath(".//input[@id='ctl00_MainContent_username']")).sendKeys(sUserName);

		System.out.println(sUserName);

		driver.findElement(By.xpath("//input[@id='ctl00_MainContent_password']")).sendKeys(sPassword);

		System.out.println(sPassword);
		

	}

	@DataProvider(name = "Authentication",parallel=true)

	public Object[][] Authentication() throws Exception {

		// Setting up the Test Data Excel file
		
		ExcelUtils.setExcelFile("D:/rcworksp/KeywordSel/TestSuite&Testcases/TestData.xlsx", "Sheet1");

		sTestCaseName = this.toString();

		// From above method we get long test case name including package and
		// class name etc.

		// The below method will refine your test case name, exactly the name
		// use have used

		sTestCaseName = ExcelUtils.getTestCaseName(this.toString());

		// Fetching the Test Case row number from the Test Data Sheet

		// Getting the Test Case name to get the TestCase row from the Test Data
		// Excel sheet

		iTestCaseRow = ExcelUtils.getRowContains(sTestCaseName, 0);

		Object[][] testObjArray = ExcelUtils.getTableArray("D:/rcworksp/KeywordSel/TestSuite&Testcases/TestData.xlsx", "Sheet1", iTestCaseRow);

		return (testObjArray);

	}
	

	@AfterTest
	public void quitBrowser() {
		System.out.println("In quitBrowser---------------------------");
		//driver.quit();
		driver.close();
	}

}
