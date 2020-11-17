package com.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.Utils.Xls_Reader;

public class Resources {

	public static WebDriver dr;
	public static EventFiringWebDriver driver;
	public static Properties Repository = new Properties();
	public static Properties suiteProperties;
	public static Xls_Reader suiteData;
	public static Xls_Reader testStepData;
	public static Xls_Reader testStep;
	public static String keyword;
	public static String webElement;
	public static String testDataField;
	public static String testData;
	public static String proceedOnFail;
	public static String testStepID;
	public static String descriptionOfTest;
	public static String testcase_ID;
	public static String testCaseName;
	public static String testcaseSheetName;
	public static File file;
	public static FileInputStream fileInput;
	public static String testSuite = System.getProperty("user.dir") + "\\TestSuite&Testcases\\TestSuite1.xlsx";
	public static String inputData = System.getProperty("user.dir") + "\\TestSuite&Testcases\\TestSuite1Data.xlsx";
	public static String objectRepository = System.getProperty("user.dir") + "\\ObjectRepository\\object.properties";
	public static String reportfilename = "Test_Results_";
	public static String reportfilePath = System.getProperty("user.dir") + "\\TestResults\\";
	public static String TestResults = System.getProperty("user.dir") + "\\TestResults\\TestRunResults.html";
	public static String chromeDriver = System.getProperty("user.dir") + "\\BrowserDrivers\\chromedriver 86.exe";
	public static String ieDriver = System.getProperty("user.dir") + "\\BrowserDrivers\\IEDriverServer.exe";
	public static String firefoxDriver = System.getProperty("user.dir") + "\\BrowserDrivers\\geckodriver.exe";
	// public static String BROWSER_NAME = "IE";
	// public static String BROWSER_NAME = "firefox";
	public static String BROWSER_NAME = "chrome";

	public static void Initialize() throws IOException {
		testStepData = new Xls_Reader(inputData);
		suiteData = new Xls_Reader(testSuite);
		suiteProperties = readPropertiesFile("runSuite.properties");
		file = new File(objectRepository);
		fileInput = new FileInputStream(file);
		Repository.load(fileInput);
	}

	// Create a new file
	public static String CreateFileWithTimeStamp() {

		// create a new file with Time Stamp
		File file = new File(reportfilePath + "\\" + reportfilename
				+ GetCurrentTimeStamp().replace(":", "_").replace(".", "_") + ".html");
		String filepath = file.toString();

		try {
			if (!file.exists()) {
				file.createNewFile();
				System.out.println("File is created; file name is " + file.getName());
			} else {
				System.out.println("File already exist");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filepath;

	}

	// Get current system time
	public static String GetCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");// dd/MM/yyyy
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	// Get Current Host Name
	public static String GetCurrentTestHostName() throws UnknownHostException {
		InetAddress localMachine = InetAddress.getLocalHost();
		String hostName = localMachine.getHostName();
		return hostName;
	}

	// Get Current User Name
	public static String GetCurrentTestUserName() {
		return System.getProperty("user.name");
	}

	public static Properties readPropertiesFile(String fileName) throws IOException {
		FileInputStream fis = null;
		Properties prop = null;
		try {
			fis = new FileInputStream(fileName);
			prop = new Properties();
			prop.load(fis);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			fis.close();
		}
		return prop;
	}
}
