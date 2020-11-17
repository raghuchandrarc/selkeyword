
package com.Utils;
import org.apache.log4j.Logger;
 
	public class Log4j {
 
		//Initialize Log4j logs
		private static Logger Log = Logger.getLogger(Log4j.class.getName());//
 
	// This is to print log for the beginning of the test case, as we usually run so many test cases as a test suite
	public static void startTestCase(String testCaseId, String keyword,String webElement,String inputData){
 
	   Log.info("****************************************************************************************");
	   Log.info("***********************"+"BEGIN -----TEST-------CASE"+" ********************************");
	   Log.info("Test Case ID:------------->"+"    "+testCaseId+ "   ");
	   Log.info("Action Keyword:----------->"+"    "+keyword+ " ");
	   Log.info("WebElement:----------->"+"    "+webElement+ "   ");
	   Log.info("Input Data:--------------->"+"    "+inputData+ " ");

	
	   }
 
	//This is to print log for the ending of the test case
	public static void endTestCase(String testCaseId,String sTestCaseName){
	   Log.info("End of Test Case ID:------>"+"    "+testCaseId+ "   ");
	   Log.info("*************************"+"END---------TEST--------CASE"+"******************************");
	   Log.info("****************************************************************************************");
	   Log.info("																						 ");
	   Log.info("																						 ");
	   Log.info("																						 ");
 
	   }
 
    // Need to create these methods, so that they can be called  
	public static void info(String message) {
		   Log.info(message);
		   }
 
	public static void warn(String message) {
	   Log.warn(message);
	   }
 
	public static String error(String message) {
		Log.error(message);
		return message;
	}
 
	public static void fatal(String message) {
	   Log.fatal(message);
	   }
 
	public static void debug(String message) {
	   Log.debug(message);
	   }
 
	}