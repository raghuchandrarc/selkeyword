package com.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Xls_Reader {
	public  String path;
	public  FileInputStream fis = null;
	public  FileOutputStream fileOut =null;
	public  XSSFWorkbook workbook = null;
	public  XSSFSheet sheet = null;
	public  XSSFRow row   =null;
	public  XSSFCell cell = null;
	
	public Xls_Reader(String path) {
		
		this.path=path;
		try {
			fis = new FileInputStream(path);
			workbook = new XSSFWorkbook(fis);

			//sheet = workbook.getSheetAt(0);
			fis.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	// returns the row count in a sheet
	public int getRowCount(String sheetName){
		int index = workbook.getSheetIndex(sheetName);
		if(index==-1){
			return 0;
		}
		else{
		sheet = workbook.getSheetAt(index);
		int number=sheet.getLastRowNum()+1;
		return number;
		}
		
	}
	
	// returns the data from a cell
	public String getCellData(String sheetName,String colName,int rowNum){
		 FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		try{
			if(rowNum <=0){
				return "";
			}
		
		int index = workbook.getSheetIndex(sheetName);
		
		int col_Num=-1;
		
		if(index==-1){
			System.out.println("given sheet is not present:-"+sheetName);
			return "";
		}
		
		sheet = workbook.getSheetAt(index);
		
		row=sheet.getRow(0);
		
		for(int i=0;i<row.getLastCellNum();i++){
			if(row.getCell(i).getStringCellValue().trim().equals(colName.trim())){
				col_Num=i;
			}
		}
		
		if(col_Num==-1){
			return "";
		}
		
		sheet = workbook.getSheetAt(index);
		
		row = sheet.getRow(rowNum-1);
		
		if(row==null){
			return "";
		}
		
		cell = row.getCell(col_Num);
		
		if(cell==null){
			System.out.println("cell is not present");
			return "";
		}
		//System.out.println(cell.getCellType());
		if(cell.getCellType()==Cell.CELL_TYPE_STRING){
			  return cell.getStringCellValue();
		}
		else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC || cell.getCellType()==Cell.CELL_TYPE_FORMULA ){
			  
			  String cellText  = String.valueOf(cell.getNumericCellValue());
			  
			  if (HSSFDateUtil.isCellDateFormatted(cell)) {
		           // format in form of M/D/YY
				  double d = cell.getNumericCellValue();

				  Calendar cal =Calendar.getInstance();
				  
				  cal.setTime(HSSFDateUtil.getJavaDate(d));
		            cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
		            
		           cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" +cal.get(Calendar.MONTH)+1 + "/" + cellText;
		           
		           //System.out.println(cellText);

		         }
			  return cellText;
		  }else if(cell.getCellType()==Cell.CELL_TYPE_BLANK)
		      return ""; 
		  /*else if(cell.getCellType()==Cell.CELL_TYPE_FORMULA) {
			  switch(cell.getCachedFormulaResultType()) {
			  
	            case Cell.CELL_TYPE_STRING:
	                System.out.println("Last evaluated as \"" + cell.getStringCellValue() + "\"");
	                break;
			  }
			  return  cell.getCellFormula();
		  }*/
		  else 
			  return String.valueOf(cell.getBooleanCellValue());
		
		}
		catch(Exception e){
			
			e.printStackTrace();
			return "row "+rowNum+" or column "+colName +" does not exist in xls";
		}
	}
	public String getCellDataTestcaseName(String sheetName,String colName,String testCaseID,int rowNum){
		 FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		try{
			if(rowNum <=0){
				return "";
			}
		
		int index = workbook.getSheetIndex(sheetName);
		
		int col_Num=-1;
		
		if(index==-1){
			System.out.println("given sheet is not present:-"+sheetName);
			return "";
		}
		
		sheet = workbook.getSheetAt(index);
		
		row=sheet.getRow(0);
		
		for(int i=0;i<row.getLastCellNum();i++){
			if(row.getCell(i).getStringCellValue().trim().equals(colName.trim())&&row.getCell(i).getStringCellValue().trim().equals(testCaseID.trim())){
				col_Num=i;
			}
		}
		
		if(col_Num==-1){
			return "";
		}
		
		sheet = workbook.getSheetAt(index);
		
		row = sheet.getRow(rowNum-1);
		
		if(row==null){
			return "";
		}
		
		cell = row.getCell(col_Num);
		
		if(cell==null){
			System.out.println("cell is not present");
			return "";
		}
		//System.out.println(cell.getCellType());
		if(cell.getCellType()==Cell.CELL_TYPE_STRING){
			  return cell.getStringCellValue();
		}
		else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC || cell.getCellType()==Cell.CELL_TYPE_FORMULA ){
			  
			  String cellText  = String.valueOf(cell.getNumericCellValue());
			  
			  if (HSSFDateUtil.isCellDateFormatted(cell)) {
		           // format in form of M/D/YY
				  double d = cell.getNumericCellValue();

				  Calendar cal =Calendar.getInstance();
				  
				  cal.setTime(HSSFDateUtil.getJavaDate(d));
		            cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
		            
		           cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" +cal.get(Calendar.MONTH)+1 + "/" + cellText;
		           
		           //System.out.println(cellText);

		         }
			  return cellText;
		  }else if(cell.getCellType()==Cell.CELL_TYPE_BLANK)
		      return ""; 
		  /*else if(cell.getCellType()==Cell.CELL_TYPE_FORMULA) {
			  switch(cell.getCachedFormulaResultType()) {
			  
	            case Cell.CELL_TYPE_STRING:
	                System.out.println("Last evaluated as \"" + cell.getStringCellValue() + "\"");
	                break;
			  }
			  return  cell.getCellFormula();
		  }*/
		  else 
			  return String.valueOf(cell.getBooleanCellValue());
		
		}
		catch(Exception e){
			
			e.printStackTrace();
			return "row "+rowNum+" or column "+colName +" does not exist in xls";
		}
	}
	
	// returns the data from a cell
	public String getCellData(String sheetName,int colNum,int rowNum){
		try{
			if(rowNum <=0)
				return "";
		
		int index = workbook.getSheetIndex(sheetName);

		if(index==-1)
			return "";
		
	
		sheet = workbook.getSheetAt(index);
		row = sheet.getRow(rowNum-1);
		if(row==null)
			return "";
		cell = row.getCell(colNum);
		if(cell==null)
			return "";
		
	  if(cell.getCellType()==Cell.CELL_TYPE_STRING)
		  return cell.getStringCellValue();
	  else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC || cell.getCellType()==Cell.CELL_TYPE_FORMULA ){
		  
		  String cellText  = String.valueOf(cell.getNumericCellValue());
		  if (HSSFDateUtil.isCellDateFormatted(cell)) {
	           // format in form of M/D/YY
			  double d = cell.getNumericCellValue();

			  Calendar cal =Calendar.getInstance();
			  cal.setTime(HSSFDateUtil.getJavaDate(d));
	            cellText =
	             (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
	           cellText = cal.get(Calendar.MONTH)+1 + "/" +
	                      cal.get(Calendar.DAY_OF_MONTH) + "/" +
	                      cellText;
	           
	          // System.out.println(cellText);

	         }

		  
		  
		  return cellText;
	  }else if(cell.getCellType()==Cell.CELL_TYPE_BLANK)
	      return "";
	  else 
		  return String.valueOf(cell.getBooleanCellValue());
		}
		catch(Exception e){
			
			e.printStackTrace();
			return "row "+rowNum+" or column "+colNum +" does not exist  in xls";
		}
	}
	
	// returns true if data is set successfully else false
	public boolean setCellData(String sheetName,String colName,int rowNum, String data){
		try{
		fis = new FileInputStream(path); 
		workbook = new XSSFWorkbook(fis);

		if(rowNum<=0)
			return false;
		
		int index = workbook.getSheetIndex(sheetName);
		int colNum=-1;
		if(index==-1)
			return false;
		
		
		sheet = workbook.getSheetAt(index);
		

		row=sheet.getRow(0);
		for(int i=0;i<row.getLastCellNum();i++){
			//System.out.println(row.getCell(i).getStringCellValue().trim());
			if(row.getCell(i).getStringCellValue().trim().equals(colName))
				colNum=i;
		}
		if(colNum==-1)
			return false;

		sheet.autoSizeColumn(colNum); 
		row = sheet.getRow(rowNum-1);
		if (row == null)
			row = sheet.createRow(rowNum-1);
		
		cell = row.getCell(colNum);	
		if (cell == null)
	        cell = row.createCell(colNum);
	    cell.setCellValue(data);

	    fileOut = new FileOutputStream(path);

		workbook.write(fileOut);

	    fileOut.close();	

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	// returns true if data is set successfully else false
	public boolean setCellData(String sheetName,String colName,int rowNum, String data,String url){
		try{
		fis = new FileInputStream(path); 
		workbook = new XSSFWorkbook(fis);

		if(rowNum<=0)
			return false;
		
		int index = workbook.getSheetIndex(sheetName);
		int colNum=-1;
		if(index==-1)
			return false;
		
		
		sheet = workbook.getSheetAt(index);
		//System.out.println("A");
		row=sheet.getRow(0);
		for(int i=0;i<row.getLastCellNum();i++){
			//System.out.println(row.getCell(i).getStringCellValue().trim());
			if(row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName))
				colNum=i;
		}
		
		if(colNum==-1)
			return false;
		sheet.autoSizeColumn(colNum); //ashish
		row = sheet.getRow(rowNum-1);
		if (row == null)
			row = sheet.createRow(rowNum-1);
		
		cell = row.getCell(colNum);	
		if (cell == null)
	        cell = row.createCell(colNum);
			
	    cell.setCellValue(data);
	    XSSFCreationHelper createHelper = workbook.getCreationHelper();

	    //cell style for hyperlinks
	    //by default hypelrinks are blue and underlined
	    CellStyle hlink_style = workbook.createCellStyle();
	    XSSFFont hlink_font = workbook.createFont();
	    hlink_font.setUnderline(XSSFFont.U_SINGLE);
	    hlink_font.setColor(IndexedColors.BLUE.getIndex());
	    hlink_style.setFont(hlink_font);
	    //hlink_style.setWrapText(true);

	    XSSFHyperlink link = createHelper.createHyperlink(XSSFHyperlink.LINK_FILE);
	    link.setAddress(url);
	    cell.setHyperlink(link);
	    cell.setCellStyle(hlink_style);
	      
	    fileOut = new FileOutputStream(path);
		workbook.write(fileOut);

	    fileOut.close();	

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
	// returns true if sheet is created successfully else false
	public boolean addSheet(String  sheetname){		
		
		FileOutputStream fileOut;
		try {
			 workbook.createSheet(sheetname);	
			 fileOut = new FileOutputStream(path);
			 workbook.write(fileOut);
		     fileOut.close();		    
		} catch (Exception e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// returns true if sheet is removed successfully else false if sheet does not exist
	public boolean removeSheet(String sheetName){		
		int index = workbook.getSheetIndex(sheetName);
		if(index==-1)
			return false;
		
		FileOutputStream fileOut;
		try {
			workbook.removeSheetAt(index);
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
		    fileOut.close();		    
		} catch (Exception e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	// returns true if column is created successfully
	public boolean addColumn(String sheetName,String colName){
		//System.out.println("**************addColumn*********************");
		
		try{				
			fis = new FileInputStream(path); 
			workbook = new XSSFWorkbook(fis);
			int index = workbook.getSheetIndex(sheetName);
			if(index==-1)
				return false;
			
		XSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		sheet=workbook.getSheetAt(index);
		
		row = sheet.getRow(0);
		if (row == null)
			row = sheet.createRow(0);
		
		//cell = row.getCell();	
		//if (cell == null)
		//System.out.println(row.getLastCellNum());
		if(row.getLastCellNum() == -1)
			cell = row.createCell(0);
		else
			cell = row.createCell(row.getLastCellNum());
	        
	        cell.setCellValue(colName);
	        cell.setCellStyle(style);
	        
	        fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
		    fileOut.close();		    

		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		return true;
		
		
	}
	// removes a column and all the contents
	public boolean removeColumn(String sheetName, int colNum) {
		try{
		if(!isSheetExist(sheetName))
			return false;
		fis = new FileInputStream(path); 
		workbook = new XSSFWorkbook(fis);
		sheet=workbook.getSheet(sheetName);
		XSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		XSSFCreationHelper createHelper = workbook.getCreationHelper();
		style.setFillPattern(HSSFCellStyle.NO_FILL);
		
	    
	
		for(int i =0;i<getRowCount(sheetName);i++){
			row=sheet.getRow(i);	
			if(row!=null){
				cell=row.getCell(colNum);
				if(cell!=null){
					cell.setCellStyle(style);
					row.removeCell(cell);
				}
			}
		}
		fileOut = new FileOutputStream(path);
		workbook.write(fileOut);
	    fileOut.close();
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
  // find whether sheets exists	
	public boolean isSheetExist(String sheetName){
		int index = workbook.getSheetIndex(sheetName);
		if(index==-1){
			index=workbook.getSheetIndex(sheetName.toUpperCase());
				if(index==-1)
					return false;
				else
					return true;
		}
		else
			return true;
	}
	
	// returns number of columns in a sheet	
	public int getColumnCount(String sheetName){
		// check if sheet exists
		if(!isSheetExist(sheetName))
		 return -1;
		
		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(0);
		
		if(row==null)
			return -1;
		
		return row.getLastCellNum();
		
		
		
	}
	//String sheetName, String testCaseName,String keyword ,String URL,String message
	public boolean addHyperLink(String sheetName,String screenShotColName,String testCaseName,int index,String url,String message){
		//System.out.println("ADDING addHyperLink******************");
		
		url=url.replace('\\', '/');
		if(!isSheetExist(sheetName))
			 return false;
		
	    sheet = workbook.getSheet(sheetName);
	    
	    for(int i=2;i<=getRowCount(sheetName);i++){
	    	if(getCellData(sheetName, 0, i).equalsIgnoreCase(testCaseName)){
	    		//System.out.println("**caught "+(i+index));
	    		setCellData(sheetName, screenShotColName, i+index, message,url);
	    		break;
	    	}
	    }


		return true; 
	}
	public int getCellRowNum(String sheetName,String colName,String cellValue){
		for(int i=2;i<=getRowCount(sheetName);i++){
	    	if(getCellData(sheetName,colName , i).equalsIgnoreCase(cellValue)){
	    		
	    		return i;
	    		
	    	}
	    }
		return -1;
		
	}
	
	public int getCellRowNum(String sheetName,String  testCaseIDcolumn,String testCaseIdvalue,String TestStepIDcolName, String TestStepIDValue){
		for(int i=2;i<=getRowCount(sheetName);i++){
	    	if(getCellData(sheetName,testCaseIDcolumn , i).equalsIgnoreCase(testCaseIdvalue)&&getCellData(sheetName,TestStepIDcolName , i).equalsIgnoreCase(TestStepIDValue)){
	    		
	    		return i;
	    		
	    	}
	    }
		return -1;
		
	}
	public  void SearchTst(String toFind, String TestCaseMainSheet, String testPath,String TestCaseID) throws Exception {

		System.out.println("inside  " + toFind + " " + TestCaseMainSheet + " " + testPath+" "+TestCaseID);
		String store = null;
		DataFormatter formatter = new DataFormatter();
		FileInputStream fis = new FileInputStream(testPath);
		workbook = new XSSFWorkbook(fis);

		sheet = workbook.getSheet(TestCaseMainSheet);
		for (Row row : sheet) {
			for (Cell cell : row) {
				CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());

				// get the text that appears in the cell by getting the cell value and applying
				String text = formatter.formatCellValue(cell);

				String[] testDataArray = toFind.split("\\|");
				List<String> list = Arrays.asList(testDataArray);

				for (int i = 0; i < list.size(); i++) {
					store = "" + list.get(i).toString();
					System.out.println("Reuse variable " + store);
					SearchValue(store, TestCaseMainSheet, testPath,TestCaseID);

				}

			}
		}

	}

	public  void SearchValue(String value1, String TestCaseMainSheet, String excelpath,String TestCaseID) throws Exception {

		
		String s1 = value1;
		String TestCaseId = " ";
		String reuseVariable = " ";
		String[] arrSplit = s1.split(" ", 0);
		for (int i = 0; i < arrSplit.length; i++) {
			System.out.println("spliit"+arrSplit[i]);

		}
		TestCaseId = arrSplit[0];
		reuseVariable = arrSplit[1];
		System.out.println(" ssssss " +TestCaseId+" cccc "+reuseVariable+"");
		FileInputStream fis = new FileInputStream(excelpath);
		workbook = new XSSFWorkbook(fis);

		sheet = workbook.getSheet("test");
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					if (cell.getRichStringCellValue().getString().trim().equals(TestCaseId)) {
						CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
						int rownum = row.getRowNum();
						cell = sheet.getRow(rownum).getCell(1);
						cell.setCellValue(reuseVariable);
					}
				}
			}
		}
		FileOutputStream output_file = new FileOutputStream(new File(excelpath));
		workbook.write(output_file);
		output_file.flush();
		output_file.close();
	    // Closing the workbook
	   
	}

	public  String searchString(String fileName, String phrase) throws IOException {
		String line = "";
		Scanner fileScanner = new Scanner(new File(fileName));
		int lineID = 0;
		List lineNumbers = new ArrayList();
		Pattern pattern = Pattern.compile(phrase);
		Matcher matcher = null;
		while (fileScanner.hasNextLine()) {
			line = fileScanner.nextLine();
			lineID++;
			matcher = pattern.matcher(line);
			if (matcher.find()) {
				// lineNumbers.add(lineID);
				System.out.println("found " + line);
				return line;

			}

		}
		return line;

	}
	public  void writeReuse(String strs [], String reuse) throws IOException {
		

		 FileWriter fw = new FileWriter(reuse);
		    

		    for (int i = 0; i < strs.length; i++) {
		      fw.write(strs[i] + "\n");
		    }
		    fw.close();
		  }
	

	public static  void setcelldata(String path, String sheetName, String colName,  String data)
			throws Exception {
		// create an object of Workbook and pass the FileInputStream object into
		// it to create a pipeline between the sheet and eclipse.
		System.out.println("Writing Innertext in to" +path+" --" +sheetName+" --"+colName+"-- "+data+"");
		FileInputStream fis = new FileInputStream(path);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet  sheet = workbook.getSheet(sheetName);
		int col_Num = -1;
		XSSFRow row = sheet.getRow(0);
		for (int i = 0; i < row.getLastCellNum(); i++) {
			if (row.getCell(i).getStringCellValue().trim().equals(colName)) {
				col_Num = i;
			}
		}
		//int rowNum =ExcelLibrary.getRowCount(sheetName);
		int rowNum =1;

		//sheet.autoSizeColumn(col_Num);
		 row = sheet.getRow(rowNum);
		if (row == null)
			row = sheet.createRow(rowNum);

		XSSFCell cell = row.getCell(col_Num);
		if (cell == null)
			cell = row.createCell(col_Num);
		cell.setCellType(cell.CELL_TYPE_STRING);
		cell.setCellValue(data);
		FileOutputStream fos = new FileOutputStream(path);
		workbook.write(fos);
		fos.close();
		System.out.println("End writing " + data + " into this file path: "+ path +"   Sheet name: " + sheetName +"   Column name: " + colName +"");
	}

	
	
	public  String findTestData( String sheetName, String cellContent) throws IOException {
		System.out.println("inside findTestData " + path + " --" + sheetName + " --" + cellContent + "");
		//FileInputStream fis = new FileInputStream(path);
		//XSSFWorkbook workbook = new XSSFWorkbook(fis);
		 sheet = workbook.getSheet(sheetName);
		DataFormatter formatter = new DataFormatter();
		for (Row row : sheet) {
			for (Cell cell : row) {

				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					if (cell.getRichStringCellValue().getString().trim().equals(cellContent)) {
						int rownum = row.getRowNum();

						System.out.println(cellContent+ " Test data Found in rownum --> " + rownum );
						int val = cell.getColumnIndex();
						cell = row.getCell(val + 1);
						String srow = formatter.formatCellValue(cell);
						System.out.println("Test data is --> " + srow);
						return srow;
					}
				}
			}
		}
		return null;
	}
	
	public static String writeTestData(String path, String sheetName, String cellContent,String TestData) throws IOException {
		System.out.println("inside writeTestData " + path + " --" + sheetName + " --" + cellContent + "--" +TestData);
		FileInputStream fis = new FileInputStream(path);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheet(sheetName);
		DataFormatter formatter = new DataFormatter();
		for (Row row : sheet) {
			for (Cell cell : row) {

				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					if (cell.getRichStringCellValue().getString().trim().equals(cellContent)) {
						int rownum = row.getRowNum();

						System.out.println(cellContent+ " Test data Found in rownum --> " + rownum );
						int val = cell.getColumnIndex();
						int colVal=val+1;
						cell = row.createCell(colVal);
						cell.setCellValue(TestData);
						FileOutputStream fos = new FileOutputStream(path);
						workbook.write(fos);
						fos.close();
						System.out.println("End writing TestData" + TestData + " into this file path: "+ path +"   Sheet name: " + sheetName +" ");

						return TestData;
					}
				}
			}
		}
		return null;
	}
	public String GetTestData(String sheetName, String testCaseID, String TestDataField, String testDataColumnName) throws Exception {
		sheet = workbook.getSheet(sheetName);
		DataFormatter formatter = new DataFormatter();
		int colnum = getColNum(sheetName, testDataColumnName);
		if (colnum != -1) {
			for (Row row : sheet) {
				if (row.getCell(0) != null && row.getCell(1) != null && row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING && row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING) {
					if (row.getCell(0).getRichStringCellValue().getString().trim().equalsIgnoreCase(testCaseID.trim()) && row.getCell(1).getRichStringCellValue().getString().trim().equalsIgnoreCase(TestDataField.trim())) {
						System.out.println(row.getCell(0).getRichStringCellValue().getString().trim() + " & " + row.getCell(1).getRichStringCellValue().getString().trim() + " - Test data Found in rownum --> " + row.getRowNum());
						String srow = formatter.formatCellValue(row.getCell(colnum)).trim();
						System.out.println("Test data is --> " + srow);
						return srow;
					}
				}
			}
		}
		throw new Exception("Data not found in the data sheet.Sheet:" + sheetName + " search testCaseID:" + testCaseID + " search TestDataField:" + TestDataField + " testDataColumnName :" + testDataColumnName);
	}
	/*public String GetTestData(String sheetName, String testCaseID,String teststepIDFrom, String teststepIDTo,String testDataFiled, String testDataColumnName) throws Exception {
		System.out.println("Inside Get Test data..." +sheetName+"--->"+testCaseID+"--->"+teststepIDFrom+"--->"+teststepIDTo+"--->"+testDataFiled+"--->"+testDataColumnName);
		sheet = workbook.getSheet(sheetName);
		
		DataFormatter formatter = new DataFormatter();
		int colnum = getColNum(sheetName, testDataColumnName);
		if (colnum != -1) {
			for (Row row : sheet) {
				if (row.getCell(0) != null && row.getCell(1) != null && row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING && row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING) {
					if (row.getCell(0).getRichStringCellValue().getString().trim().equalsIgnoreCase(testCaseID.trim()) && row.getCell(1).getRichStringCellValue().getString().trim().equalsIgnoreCase(testDataFiled.trim())) {
						System.out.println(row.getCell(0).getRichStringCellValue().getString().trim() + " & " + row.getCell(1).getRichStringCellValue().getString().trim() + " - Test data Found in rownum --> " + row.getRowNum());
						String srow = formatter.formatCellValue(row.getCell(colnum)).trim();
						System.out.println("Test data is --> " + srow);
						return srow;
					}
				}
			}
		}
		throw new Exception("Data not found in the data sheet.Sheet:" + sheetName + " search testCaseID:" + testCaseID + " search TestDataField:" + testDataFiled + " testDataColumnName :" + testDataColumnName);
	}*/
	
	public int getColNum(String sheetName, String colName) throws IOException {

		sheet = workbook.getSheet(sheetName);
		int colNum = -1;

		row = sheet.getRow(0);
		for (int i = 0; i < row.getLastCellNum(); i++) {
			if (row.getCell(i) != null && row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName)) {
				colNum = i;
				return colNum;
			}
		}
		return colNum;
	}


	public static void main(String args[]) throws IOException {
	      Properties prop = readPropertiesFile("runSuite.properties");
	      System.out.println("Suite Name: "+ prop.getProperty("runSuite"));
	   }
	   public static Properties readPropertiesFile(String fileName) throws IOException {
	      FileInputStream fis = null;
	      Properties prop = null;
	      try {
	         fis = new FileInputStream(fileName);
	         prop = new Properties();
	         prop.load(fis);
	      } catch(FileNotFoundException fnfe) {
	         fnfe.printStackTrace();
	      } catch(IOException ioe) {
	         ioe.printStackTrace();
	      } finally {
	         fis.close();
	      }
	      return prop;
	   }
}