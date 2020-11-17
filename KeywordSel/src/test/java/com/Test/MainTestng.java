package com.Test;

import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;


public class MainTestng {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainTestng test = new MainTestng();
		/**
		 * testNG execution starts here
		 */
		test.testng();

	}
	/**
	 * adding listners, setting test-output folder Mentioning the TestSuite Name
	 */
	public void testng() {
		// RegressionSuite
		
		TestNG objTestNG = new TestNG();
		XmlSuite TSuite = new XmlSuite();
		TSuite.setName("Test Suite Report");
		TSuite.addListener("org.uncommons.reportng.HTMLReporter");
		TSuite.addListener("org.uncommons.reportng.JUnitXMLReporter");
		TSuite.addListener("com.Utils.TestListener");
		objTestNG.setOutputDirectory("test-output");
		XmlTest myTest = new XmlTest(TSuite);
		myTest.setName(" Test Suites Report");
		List<XmlClass> myClasses = new ArrayList<XmlClass>();
		myClasses.add(new XmlClass("com.Test.TestController"));
		myTest.setXmlClasses(myClasses);
		List<XmlTest> myTests = new ArrayList<XmlTest>();
		
		myTests.add(myTest);
		
		TSuite.setTests(myTests);
		List<XmlSuite> mySuites = new ArrayList<XmlSuite>();
		mySuites.add(TSuite);
		objTestNG.setXmlSuites(mySuites);
		//System.out.println("list xml test "+mySuites);
		objTestNG.setUseDefaultListeners(true);
		objTestNG.run();

	}


}
