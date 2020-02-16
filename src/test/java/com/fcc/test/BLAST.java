package com.fcc.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import com.fcc.test.SoftAssert;
import com.tavant.base.DriverFactory;
import com.tavant.base.WebPage;
import com.tavant.kwutils.KWVariables;
import com.tavant.utils.TwfException;

import jxl.read.biff.BiffException;

public class BLAST extends WebPage {
	CommonUtils objUtils;
	DataBaseUtils objDB;
	WebDriver driver;
	WebElement element;
	LinkedHashMap<String, String> matching = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> notMatching = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> keyNotAvailable = new LinkedHashMap<String, String>();
	
	static LinkedHashMap<String, String> level3AttributeName_vs_DB_Value = new LinkedHashMap<String, String>();
	static LinkedHashMap<String, String> glGroupFormula = new LinkedHashMap<String, String>();
	
	LinkedHashMap<String, String> blastAttributeValues;
	HashMap<String, String> mapNonSum;
	ArrayList<String> arrMatching = new ArrayList<String>();
	ArrayList<String> arrNotMatching = new ArrayList<String>();
	ArrayList<String> listGlGroupList = new ArrayList<String>();
	Connection conSL_ACCT_DW;
	String startDate = null, endDate = null;
	String file_Level3ReportName=null;
	@SuppressWarnings("null")
	public void selectRegion(Map<String, String> data)
			throws BiffException, IOException, TwfException, InterruptedException, InvalidFormatException {
		String regions;
		
		
		objUtils = new CommonUtils();
		
		regions = data.get("Branches");
		objUtils.selectRegionsFromDropdown(regions);
		
		/*Logic for exclude region, it is commented due to drop down is getting hanged after the selection
		 * String excludeRegions=null;
		excludeRegions=data.get("ExcludeRegion");
		if(excludeRegions!=null || !excludeRegions.isEmpty())
			objUtils.selectRegionsFromDropdown(excludeRegions);*/
		
		getElementByUsing("btnSubmit").click();
		objUtils.waitTillLoadSymbolBlast(" ");
	}

	public void verfiyBlast(Map<String, String> data)
			throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException,
			BiffException, InvalidFormatException, TwfException, InterruptedException {
		HashMap<String, String> mapGbrList_DB = new HashMap<String, String>();
		HashMap<String, String> mapGbrList_Blast = new HashMap<String, String>();
		String loan_number, gbr;
		float gbrValue_db;
		System.out.println("verifyBlast method Execution started");
		String regions = data.get("Branches");
		String region_numbers = "";
		String r_num;
		objUtils = new CommonUtils();
		objDB = new DataBaseUtils();
		Connection con = objDB.init();

		if (regions.contains(",")) {
			String region_List[] = regions.split(",");
			for (String r : region_List) {
				r = r.replace(" ", "_");
				if (r.contains("~"))
					r_num = objUtils.getPropertyValue("regionList.properties", r.split("~")[1]);
				else
					r_num = objUtils.getPropertyValue("regionList.properties", r);
				region_numbers = region_numbers + r_num + ",";
			}
		} else if (regions.contains("~")) {
			String propRegions = regions.replace(" ", "_");
			region_numbers = objUtils.getPropertyValue("regionList.properties", propRegions.split("~")[1]);
		} else {
			regions = regions.replace(" ", "_");
			region_numbers = objUtils.getPropertyValue("regionList.properties", regions);
		}

		String startDate, endDate;
		startDate = data.get("StartDate");
		endDate = data.get("EndDate");
		String currentYear;
		currentYear = String.valueOf(objUtils.getCurrentYear());
		String testData_StartDate_Year = startDate.split("/")[2];
		if (!testData_StartDate_Year.equals(currentYear)) {
			startDate = "01/01/" + testData_StartDate_Year;
			endDate = "12/31/" + testData_StartDate_Year;
		}

		String sql = "exec usp_ati_slblast_LoanFunded_By_LoanNumber_with_user_security '" + startDate + "','" + endDate
				+ "'," + region_numbers + ",'','',1,'%%','Syed.islam'";
		if (con != null) {
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery(sql);
			System.out.println("Database connection is successful");
			String count_database;
			rs.last();
			count_database = String.valueOf(rs.getRow()-1);
			rs.first();

			System.out.println("Total number of records present in Database=" + count_database);
			String count_viewFundedLoan = getElementByUsing("drilDown_viewFundedLoanRecordCount").getText();
			count_viewFundedLoan = ((count_viewFundedLoan.split("of"))[1].trim()).split(" ")[0];

			System.out.println(
					"Total number of records prestn in the view funded loan BLAST page=" + count_viewFundedLoan);
			if (count_database.equals(count_viewFundedLoan)) {
				System.out.println(
						"Number of record present in the database is not matching with number number recrods in View Funded Loans Page");
			} else {
				System.out.println(
						"Number of record present in the database is matching with number number recrods in View Funded Loans Page");
			}

			while (rs.next()) {
				loan_number = rs.getString("LoanNumber").trim();
				gbrValue_db = Float.parseFloat(rs.getString("GBR"));
				gbrValue_db = Math.round(gbrValue_db);
				gbr = String.valueOf(Math.round(Float.parseFloat(rs.getString("GBR")))).trim();
				if (loan_number.contains("Grand Total"))
					continue;
				mapGbrList_DB.put(loan_number, gbr);
				System.out.println("\nLoan Number=" + loan_number + "     GBR=" + gbr);
			}
			rs.close();
			con.close();
		} else {
			System.out.println("Database connection is failed");
		}
		String xpth_gbr = KWVariables.getVariables().get("gbr");
		String xpth_loanNumber = KWVariables.getVariables().get("loanNumber");
		String xpth_pagesInViewFunded = KWVariables.getVariables().get("pagesInViewFunded");

		List<WebElement> pageList_ViewFunded_Blast;
		pageList_ViewFunded_Blast = objUtils.CreateObjectList(xpth_pagesInViewFunded);
		int noOfPages = pageList_ViewFunded_Blast.size();

		int totalNumberOfFundedLoans;
		WebElement btnNext;
		objUtils.scrollToBottom();
		while ((!(btnNext = getElementByUsing("btnNext")).getAttribute("class").contains("disabled"))
				|| noOfPages == 1) {

			// for(int p=0;p<noOfPages;p++)
			// {
			// if(p!=0)
			// {
			// pageList_ViewFunded_Blast=objUtils.CreateObjectList(xpth_pagesInViewFunded);
			// pageList_ViewFunded_Blast.get(p).click();
			// }

			Thread.sleep(1000);
			List<WebElement> loanList_Blast = objUtils.CreateObjectList(xpth_loanNumber);
			List<WebElement> gbrList_Blast = objUtils.CreateObjectList(xpth_gbr);
			totalNumberOfFundedLoans = loanList_Blast.size();
			String key_Loan;
			String gbr_Blast_Value;
			for (int i = 0; i < totalNumberOfFundedLoans; i++) {
				gbr_Blast_Value = gbrList_Blast.get(i).getText().replace("$", "").replace(",", "").trim();
				if (gbr_Blast_Value.contains("(")) {
					gbr_Blast_Value = gbr_Blast_Value.replace("(", "-").replace(")", "");
				}
				key_Loan = loanList_Blast.get(i).getText().trim();
				if (key_Loan.equals("Grand Total"))
					continue;
				mapGbrList_Blast.put(key_Loan, gbr_Blast_Value);
			}
			if (noOfPages != 1) {
				btnNext.click();
				Thread.sleep(1000);
			}
			noOfPages--;
		}
		boolean mapComparisionFlag = mapGbrList_DB.equals(mapGbrList_Blast);
		boolean flag_comparision_Status = true;
		String listOfIncorrectGBR_Loan = "";
		if (!mapComparisionFlag) {
			System.out.println("Map vaue is not matching=" + mapComparisionFlag);
			System.out.println("Comparision of individual map value is started");
			Set<String> keys = mapGbrList_DB.keySet();

			for (Iterator<String> it = keys.iterator(); it.hasNext();) {
				String key_DB = (String) it.next();
				String value_DB = (String) mapGbrList_DB.get(key_DB);
				String value_Blast = (String) mapGbrList_Blast.get(key_DB);
				if (!value_DB.equals(value_Blast)) {
					System.out.println("GBR Value not matching");
					System.out.println("Loan Number=" + key_DB);
					System.out.println("GBR$$ Value in BLAST PAGe=" + value_Blast);
					System.out.println("GBR$$ Value in Database=" + value_DB);
					listOfIncorrectGBR_Loan += "\nLoan Number=" + key_DB + "\tGBR$$ Value in BLAST PAGe=" + value_Blast
							+ "\tGBR$$ Value in Database=" + value_DB;
					flag_comparision_Status = false;
				}
			}
		}
		if (!flag_comparision_Status)
			Assert.assertTrue(false, listOfIncorrectGBR_Loan);
	}

	public void selectDates(Map<String, String> data) throws TwfException {

		String startDate = data.get("StartDate");
		String endDate = data.get("EndDate");
		driver = DriverFactory.getDriver();
		String strStartDate = "document.querySelector('#ContentPlaceHolder1_txtStartDt').value='" + startDate + "'";
		String strEndDate = "document.querySelector('#ContentPlaceHolder1_txtEndDt').value='" + endDate + "'";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(strStartDate);
		js.executeScript(strEndDate);
	}

	public void verifyCreditReportExpense(String str)
			throws TwfException, BiffException, IOException, InvalidFormatException, InterruptedException {
		objUtils = new CommonUtils();
		String valCreditExpenseReport;
		driver = DriverFactory.getDriver();
		element = getElementByUsing("linkCreditReportExpense_Value");
		valCreditExpenseReport = element.getText();
		System.out.println("Credit Expense Report Value=" + valCreditExpenseReport);
		objUtils.scrollToAnElement("linkCreditReportExpense_Value");
		objUtils.highlightMe("linkCreditReportExpense_Value");
		element.click();
		objUtils.waitTillLoadSymbolBlast(str);
		/* Place the logic for verification of credit expense report value */
		getElementByUsing("btnClose").click();
	}

	public void verifyLevel1Details(Map<String, String> data)
			throws TwfException, BiffException, InvalidFormatException, IOException, InterruptedException,
			InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException

	{
		String levelOneValue, levelThreeValue;
		LinkedHashMap<String, String> mapOfGlLevel1;
		LinkedHashMap<String, String> level1GlAmount = new LinkedHashMap<String, String>();
		objUtils = new CommonUtils();

		String fileName = System.getProperty("user.dir") + "\\test-output\\" + objUtils.getCurrentTimeStamp()
				+ "OverviewScreenVerification.xlsx";
		XSSFRow row;
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("ComparisionResult");

		mapOfGlLevel1 = getMapOfLevel1UiGl();
		this.getElementByUsing("linkDetail").click();
		objUtils.waitTillLoadSymbolBlast(" ");
		
		
		LinkedHashMap<String,String> map_Level3UiValues=this.getMap_Level3Details();
		
		
		this.getElementByUsing("linkOverview").click();
		Thread.sleep(3000);
//		objUtils.waitTillLoadSymbolBlast(" ");
		
		writeOverviewHeader(data, sheet);
		String glName;
		for (String uiGlKey : mapOfGlLevel1.keySet()) {
			System.out.println("\n\nCurrent GL=" + uiGlKey);
			glName=uiGlKey;			
			if (uiGlKey.equals("Cumulative Balance"))
				continue;
			levelOneValue = mapOfGlLevel1.get(uiGlKey);
			if (!map_Level3UiValues.containsKey(uiGlKey)) {
				if (uiGlKey.equals("Early payoff-EPO"))
					uiGlKey = "Early Payoffs";
				else if (uiGlKey.equals("Losses from Discontinued Branches"))
					uiGlKey = "Losses From Branches";
				else
					uiGlKey = "Total " + uiGlKey;
			}
			if(map_Level3UiValues.containsKey(uiGlKey))
				levelThreeValue = map_Level3UiValues.get(uiGlKey);
			else
				levelThreeValue="0";
			verifyLevelOnveVsLevelThreeAmount(sheet, data, glName, levelOneValue, levelThreeValue);
		} // End of Loop
		for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
			sheet.autoSizeColumn(i, true);
		}
		FileOutputStream out = new FileOutputStream(fileName);
		workbook.write(out);
		out.close();
		System.out.println("Excel Report of Comparision for Overview Screen has been generated at " + fileName);
		System.out.println("BLAST UI Level 1 Attribute values Verification is complete");
	}

	void writeOverviewHeader(Map<String, String> data, XSSFSheet sheet) {
		XSSFWorkbook workbook = sheet.getWorkbook();
		XSSFCellStyle style = workbook.createCellStyle();

		XSSFRow row;

		style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style.setBorderTop((short) 5); // double lines border
		style.setBorderBottom((short) 1); // single line border

		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 13);
		font.setBold(true);
		style.setFont(font);

		sheet.autoSizeColumn(0);

		XSSFCellStyle styleTestDataInfo = workbook.createCellStyle();
		styleTestDataInfo.setBorderTop((short) 3); // double lines border
		styleTestDataInfo.setBorderBottom((short) 1); // single line border

		XSSFFont fontTestDataInfo = workbook.createFont();
		fontTestDataInfo.setFontHeightInPoints((short) 11);
		fontTestDataInfo.setBold(true);
		styleTestDataInfo.setFont(fontTestDataInfo);
		styleTestDataInfo.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		styleTestDataInfo.setFillPattern(CellStyle.SOLID_FOREGROUND);

		XSSFRow tDInformation = sheet.createRow((short) 0);

		startDate = "01/01/" + objUtils.getCurrentYear();
		endDate = objUtils.getCurrentMonth() + 1 + "/" + objUtils.getLastDayOfCurrentMonth() + "/"
				+ objUtils.getCurrentYear();
		tDInformation.setHeightInPoints((3 * sheet.getDefaultRowHeightInPoints()));
		tDInformation.createCell(0).setCellValue("Test Case Id=" + data.get("TestCaseID"));
		tDInformation.createCell(1).setCellValue(" ");
		tDInformation.createCell(2).setCellValue("Selected Branch(s)=" + data.get("Branches"));
		tDInformation.createCell(3).setCellValue(" ");
		tDInformation.createCell(4).setCellValue("Start Date=" + startDate + "       End Date" + endDate);
		tDInformation.getCell(0).setCellStyle(styleTestDataInfo);
		tDInformation.getCell(1).setCellStyle(styleTestDataInfo);
		tDInformation.getCell(2).setCellStyle(styleTestDataInfo);
		tDInformation.getCell(3).setCellStyle(styleTestDataInfo);
		tDInformation.getCell(4).setCellStyle(styleTestDataInfo);

		XSSFRow rowhead = sheet.createRow((short) sheet.getLastRowNum() + 1);
		rowhead.createCell(0).setCellValue("GL Name");
		rowhead.createCell(1).setCellValue("Overview Amount");
		rowhead.createCell(2).setCellValue(" Details Amount");
		rowhead.createCell(3).setCellValue("Status");
		rowhead.createCell(4).setCellValue("Variance");

		for (int j = 0; j <= 4; j++)
			rowhead.getCell(j).setCellStyle(style);
	}

	private Boolean verifyLevelOnveVsLevelThreeAmount(XSSFSheet sheet, Map<String, String> data, String glName,
			String levelOneValue, String levelThreeValue) {

		XSSFWorkbook workbook = sheet.getWorkbook();
		XSSFCellStyle style = workbook.createCellStyle();
		XSSFCellStyle styleAccountName = workbook.createCellStyle();

		XSSFRow row;

		style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style.setBorderTop((short) 5); // double lines border
		style.setBorderBottom((short) 1); // single line border

		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 13);
		font.setBold(true);
		style.setFont(font);

		sheet.autoSizeColumn(0);

		XSSFCellStyle styleTestDataInfo = workbook.createCellStyle();
		styleTestDataInfo.setBorderTop((short) 3); // double lines border
		styleTestDataInfo.setBorderBottom((short) 1); // single line border

		XSSFFont fontTestDataInfo = workbook.createFont();
		fontTestDataInfo.setFontHeightInPoints((short) 11);
		fontTestDataInfo.setBold(true);
		styleTestDataInfo.setFont(fontTestDataInfo);
		styleTestDataInfo.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		styleTestDataInfo.setFillPattern(CellStyle.SOLID_FOREGROUND);

		font.setBold(true);
		styleAccountName.setFont(font);
		styleAccountName.setWrapText(true);

		long variance = Math.abs(Math.abs(Long.parseLong(levelOneValue)) - Math.abs(Long.parseLong(levelThreeValue)));
		Boolean flag = true;
		row = sheet.createRow((short) sheet.getLastRowNum() + 1);
		XSSFCellStyle styleStatus = sheet.getWorkbook().createCellStyle();
		XSSFFont f = sheet.getWorkbook().createFont();
		f.setBold(true);
		// styleStatus.setFont(f);
		if (levelOneValue.equals(levelThreeValue)) {
			System.out.println(glName + " Values are Matching against Details Screen value!! Overview Screen Value="
					+ levelOneValue + " and Details Screen value=" + levelThreeValue);

			row.createCell(0).setCellValue(glName);
			row.createCell(1).setCellValue(levelOneValue);
			row.createCell(2).setCellValue(levelThreeValue);

			f.setColor(IndexedColors.GREEN.getIndex());
			styleStatus.setFont(f);
			row.createCell(3).setCellValue("Pass");
			row.getCell(3).setCellStyle(styleStatus);

			row.createCell(4).setCellValue(String.valueOf(variance));
			row.getCell(4).setCellStyle(styleStatus);

		} else {
			System.out.println(glName + " Values are not Matching against Encompass Database Vaue!! UI Value="
					+ levelOneValue + " and Database value=" + levelThreeValue);
			row.createCell(0).setCellValue(glName);
			row.createCell(1).setCellValue(levelOneValue);
			row.createCell(2).setCellValue(levelThreeValue);

			f.setColor(IndexedColors.RED.getIndex());
			styleStatus.setFont(f);
			row.createCell(3).setCellValue("Fail");
			row.getCell(3).setCellStyle(styleStatus);

			row.createCell(4).setCellValue(String.valueOf(variance));
			row.getCell(4).setCellStyle(styleStatus);
			flag = false;
		}

		return flag;

	}

	@SuppressWarnings("null")
	private LinkedHashMap<String, String> getMapOfLevel1UiGl() throws TwfException, BiffException, IOException {
		List<WebElement> elemLevel1Gl;
		ArrayList<String> nameLevel1Gl = new ArrayList<String>();
		LinkedHashMap<String, String> mapLevel1GlVsAmount = new LinkedHashMap<String, String>();
		String glUiName = "", amount;
		String xpathLevel1Gl = "//table[@id='tblLevel1_fixed']//child::tr/td[1]/div";
		String xpathLevel1GlAmount;
		objUtils = new CommonUtils();
		elemLevel1Gl = objUtils.CreateObjectList(xpathLevel1Gl);
		for (WebElement element : elemLevel1Gl) {
			if (!element.isDisplayed()) {
				getElementByUsing("lblOverview").click();
				objUtils.scrollToAnElement(element);
			}
			glUiName = element.getText();			
			if (glUiName.contains("Cumulative Balance"))
				break;
			nameLevel1Gl.add(glUiName);
		}
		nameLevel1Gl.add(glUiName);
		for (String individualGl : nameLevel1Gl) {			
			//xpathLevel1GlAmount = "(//table[@id='tblLevel1_fixed']//child::div[contains(text(),'glName')]/../../td[2])[1]";
			xpathLevel1GlAmount="(//div[text()='glName']/../..//div[contains(@leveltype,'glName')])[1]";
			xpathLevel1GlAmount = xpathLevel1GlAmount.replace("glName", individualGl);
			amount = getAmount(xpathLevel1GlAmount);
			mapLevel1GlVsAmount.put(individualGl, amount);
		}
		return mapLevel1GlVsAmount;
	}

	private String getAmount(String xpathLevel1GlAmount) throws TwfException, BiffException, IOException {
		String amount;
		boolean flagParenthesis = false;
		element = objUtils.getObject(xpathLevel1GlAmount);
		if (!element.isDisplayed()) {
			getElementByUsing("lblOverview").click();
			objUtils.scrollToAnElement(element);
		}
		amount = element.getText();

		if (amount.contains("("))
			flagParenthesis = true;
		amount = amount.replaceAll("[^0-9]", "");
		if (flagParenthesis)
			amount = String.valueOf(Long.parseLong(amount) * -1);
		return amount;
	}

	public void verifyLevel3Details(Map<String, String> data)
			throws TwfException, BiffException, InvalidFormatException, IOException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException, InterruptedException, NoSuchFieldException, SecurityException, IllegalArgumentException {
		String glUiName;
		List<WebElement> elementUiAttributes;
		Boolean flagReport;
		blastAttributeValues = new LinkedHashMap<String, String>();
		objUtils = new CommonUtils();
		driver = DriverFactory.getDriver();
		if(objUtils.getCurrentMonth()==0||objUtils.getCurrentMonth()==1) {
			driver.findElement(By.xpath("//*[@id=\"ContentPlaceHolder1_chkPriorYear\"]")).click();
			objUtils.waitTillLoadSymbolBlast(" ");
		}
		// attributeList=KWVariables.getVariables().get("BlastAttributeListLevel3");
		
		elementUiAttributes = driver.findElements(By.xpath("//table[@id='tblLevel3_fixed']//child::tr/td[1]/div"));
		String attributeList = null;

		System.out.println("Number of webElements=" + elementUiAttributes.size());
		for (WebElement ele : elementUiAttributes) {
			if (!ele.isDisplayed()) {
				//getElementByUsing("lblDetail").click();
				objUtils.scrollToAnElement(ele);
			}
			glUiName = ele.getText();
			if (glUiName.contains("Cumulative Balance"))
				break;
			if (attributeList == null)
				attributeList = glUiName;
			else
				attributeList = attributeList + ":" + glUiName;
			System.out.println(glUiName);
		}
		attributeList = attributeList + ":" + "Cumulative Balance";

		String value;
		String xpathAmount;
		Boolean flagParenthesis;

		createGlGroupFormula(); // It will create hashmap for Gl Group and its respective Formula "glGroupFormula" static
		getMapNonSumNotFound(); //"mapNonSum" static
		
		String arrAttribute[] = attributeList.split(":");
		System.out.println(arrAttribute.length);
		for (String Attribute : arrAttribute) {
			xpathAmount = "//table[@id='tblLevel3_fixed']//child::div[contains(text(),'NameOfAttribute')]/../../td[2]";
			if (Attribute.contains("Loan Volume Amount"))
				xpathAmount = "//table[@id='tblLevel3_fixed']//child::div[contains(text(),'NameOfAttribute')]/../../td[2]";
			flagParenthesis = false;

			xpathAmount = xpathAmount.replace("NameOfAttribute", Attribute);
			element = objUtils.getObject(xpathAmount);
			if (!element.isDisplayed()) {
				//getElementByUsing("lblDetail").click();
				objUtils.scrollToAnElement(element);
			}
			value = element.getText();

			if (value.contains("("))
				flagParenthesis = true;
			value = value.replaceAll("[^0-9]", "");
			if (flagParenthesis)
				value = String.valueOf(Long.parseLong(value) * -1);

			blastAttributeValues.put(Attribute, value); // This map contains Gl displayed in the the Level 3 details Ui
														// and their respective Amount
			System.out.println(Attribute + "=" + value);
		}
		System.out.println("BLAST UI Level 3 GL values are ready");
		flagReport = this.verifyAgainstEncompassDatabase(blastAttributeValues, data);
		System.out.println("\n\n\n\n\n");
		this.writeGlAccountStatus();
		/*Code for checking Variance value exceeds the specified Range or not*/
		flagReport=	getLevel3VarianceStatus();
		
		if(!flagReport)
		  addExceptionToReport("GL Comparision against DB Failed for list of GLs", "Refer GlAccountVerification.xlsx","Refer GlAccountVerification.xlsx");	 

		
		System.out.println("Level 3 Verfication completed");

	}
	

	private void writeGlAccountStatus() {
		int i = 0;
		System.out.println("Values are Matching");
		for (String str : matching.keySet()) {
			System.out.println(str + "\t" + matching.get(str) + "\t" + arrMatching.get(i++));
		}
		System.out.println("\n\nValues are not Matching");
		i = 0;
		for (String str : notMatching.keySet()) {
			System.out.println(str + "\t" + notMatching.get(str) + "\t" + arrNotMatching.get(i++));
		}
		i = 0;
		System.out.println("\n\nMappingKey are not Avaiable");
		for (String str : keyNotAvailable.keySet()) {
			System.out.println(str + "\t" + keyNotAvailable.get(str));
		}

	}

	Boolean verifyAgainstEncompassDatabase(LinkedHashMap<String, String> blastAttributeValues, Map<String, String> data)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException,
			BiffException, InvalidFormatException, NoSuchFieldException, SecurityException, IllegalArgumentException {

		String GlGroupList = KWVariables.getVariables().get("GlGroupList");
		String arrGlGroupList[] = GlGroupList.split(":");
		listGlGroupList = new ArrayList<String>();
		objDB = new DataBaseUtils();
		Connection con = objDB.initEncompassDatabase();
		conSL_ACCT_DW = objDB.init();
		Boolean flag = true, tempFlag;
		objUtils = new CommonUtils();

		file_Level3ReportName = System.getProperty("user.dir") + "\\test-output\\" + objUtils.getCurrentTimeStamp()
				+ "GlAccountLevel3Verification.xlsx";
		XSSFRow row;
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("ComparisionResult");

		XSSFCellStyle style = workbook.createCellStyle();
		XSSFCellStyle styleAccountName = workbook.createCellStyle();

		style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style.setBorderTop((short) 5); // double lines border
		style.setBorderBottom((short) 1); // single line border

		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 13);
		font.setBold(true);
		style.setFont(font);

		sheet.autoSizeColumn(0);

		XSSFCellStyle styleTestDataInfo = workbook.createCellStyle();
		styleTestDataInfo.setBorderTop((short) 3); // double lines border
		styleTestDataInfo.setBorderBottom((short) 1); // single line border

		XSSFFont fontTestDataInfo = workbook.createFont();
		fontTestDataInfo.setFontHeightInPoints((short) 11);
		fontTestDataInfo.setBold(true);
		styleTestDataInfo.setFont(fontTestDataInfo);
		styleTestDataInfo.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		styleTestDataInfo.setFillPattern(CellStyle.SOLID_FOREGROUND);

		XSSFRow tDInformation = sheet.createRow((short) 0);

		startDate = "01/01/" + objUtils.getCurrentYear();
		endDate = objUtils.getCurrentMonth() + 1 + "/" + objUtils.getLastDayOfCurrentMonth() + "/"
				+ objUtils.getCurrentYear();
		tDInformation.setHeightInPoints((3 * sheet.getDefaultRowHeightInPoints()));
		tDInformation.createCell(0).setCellValue("Test Case Id=" + data.get("TestCaseID"));
		tDInformation.createCell(1).setCellValue(" ");
		tDInformation.createCell(2).setCellValue("Selected Branch(s)=" + data.get("Branches"));
		tDInformation.createCell(3).setCellValue(" ");
		tDInformation.createCell(4).setCellValue("Start Date=" + startDate + "       End Date" + endDate);
		tDInformation.getCell(0).setCellStyle(styleTestDataInfo);
		tDInformation.getCell(1).setCellStyle(styleTestDataInfo);
		tDInformation.getCell(2).setCellStyle(styleTestDataInfo);
		tDInformation.getCell(3).setCellStyle(styleTestDataInfo);
		tDInformation.getCell(4).setCellStyle(styleTestDataInfo);

		XSSFRow rowhead = sheet.createRow((short) sheet.getLastRowNum() + 1);
		rowhead.createCell(0).setCellValue("GL Name");
		rowhead.createCell(1).setCellValue("UI Value");
		rowhead.createCell(2).setCellValue("Database Value");
		rowhead.createCell(3).setCellValue("Status");
		rowhead.createCell(4).setCellValue("Variance");

		for (int j = 0; j <= 4; j++)
			rowhead.getCell(j).setCellStyle(style);

		XSSFFont fontRows = workbook.createFont();
		font.setBold(true);
		styleAccountName.setFont(font);
		styleAccountName.setWrapText(true);

		if (con != null) {
			System.out.println("Database connection is successful");
			for (int i = 0; i < arrGlGroupList.length; i++) {
				listGlGroupList.add(arrGlGroupList[i].trim());
			}

			HashMap<String, String> glKeyPair = new HashMap<String, String>();
			glKeyPair = createGlKeyPairMap();

			String attributeName, attributeValue, dbAttributeValue;
			String key;
			for (String str : blastAttributeValues.keySet()) {
				attributeName = str;
				if (attributeName.equals("Cumulative Balance"))
					System.out.println("Cumulative Balance execution startyed");
				attributeValue = blastAttributeValues.get(str);
				if (listGlGroupList.contains(attributeName)) // Gl Group calculation logic
				{
					dbAttributeValue = calculateFromUI(blastAttributeValues, attributeName);
					level3AttributeName_vs_DB_Value.put(attributeName, dbAttributeValue); // It contain DB value against
																							// each GL and GL Group
					// continue;
				} else { // Gl value extraction from dB logic

					dbAttributeValue = getGlValueFromDB(con, attributeName, data, glKeyPair); // It fetches value from
																								// DB using the mapping
																								// key for a Gl
					level3AttributeName_vs_DB_Value.put(attributeName, dbAttributeValue);
				}
				if (dbAttributeValue == null) {
					System.out.println(
							attributeName + " \nhaving UI Value=" + attributeValue + " Mapping key is not found!!!");
					keyNotAvailable.put(attributeName, attributeValue);
					row = sheet.createRow((short) sheet.getLastRowNum() + 1);
					row.createCell(0).setCellValue(attributeName);
					row.getCell(0).setCellStyle(styleAccountName);
					row.createCell(1).setCellValue(attributeValue);
					row.createCell(2).setCellValue("Key Not Found in the Mapping sheet");
					row.createCell(3).setCellValue("Key Not Found");
					continue;
				}
				tempFlag = compare(sheet, attributeName, attributeValue, dbAttributeValue, styleTestDataInfo);
				if (tempFlag == false && flag == true)
					flag = tempFlag;
			}
		} else {
			System.out.println("Database connection is failed");
		}
		for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
			sheet.autoSizeColumn(i, true);
		}
		FileOutputStream out = new FileOutputStream(file_Level3ReportName);
		workbook.write(out);
		out.close();
		System.out.println("Excel Report of Comparision has been generated at " + file_Level3ReportName);

		if (con != null)
			con.close();
		return flag;
	}

	String calculateFromUI(LinkedHashMap<String, String> blastAttributeValues, String attributeName) {
		String result = null;
		switch (attributeName) {
		case "Avg Loan Size":
			String loanVolumeAmount=level3AttributeName_vs_DB_Value.get("Loan Volume Amount");
			String loanVolumeUnits=level3AttributeName_vs_DB_Value.get("Loan Volume Units");
			if(loanVolumeAmount==null)
				loanVolumeAmount="0";
			if(loanVolumeUnits==null)
				loanVolumeUnits="0";
			try {
				if(loanVolumeUnits.equals("0"))
						throw new ArithmeticException("Loan volume units is zero");
			result = String.valueOf(Math.round(Long.parseLong(loanVolumeAmount)
					/ Long.parseLong(loanVolumeUnits)));
			
			}
			catch(ArithmeticException e)
			{
				result="0";
			}
			break;
		case "Cumulative Balance":
			result = String.valueOf(Long.parseLong(blastAttributeValues.get("Total Net Income (Loss)"))
					+ Long.parseLong(blastAttributeValues.get("Total Beginning Year Balance"))
					+ Long.parseLong(blastAttributeValues.get("Total Reserves")));
			if(blastAttributeValues.get("Escrow Advances Held")!=null)
				result=String.valueOf(Long.parseLong(result)+Long.parseLong(blastAttributeValues.get("Escrow Advances Held")));
			break;
		case "Total Revenue":
		case "Total Production Compensation":
		case "Total Direct Production Costs":
		case "Total Personnel Costs":
		case "Total Selling, General & Administrative (S, G & A)":
		case "Total BM Payroll-Paid":
			result = calculateGlGroupValue(blastAttributeValues, attributeName);
			break;
		case "Total Gross Margin":
		case "Total Gross Margin Before Personnel Costs":
		case "Total Gross Profit Before S, G & A":
		case "Total Operating Income (Loss) Before BM Payroll":
		case "Total Net Income (Loss)":
			result = calculateOtherGlGroupValue(attributeName);
			break;
		}
		return result;
	}

	private String calculateOtherGlGroupValue(String attributeName) {
		// TODO Auto-generated method stub
		long glAttributeValue = 0, sum = 0;
		String valAttributeGl;
		String formula = glGroupFormula.get(attributeName);
		String[] arrFormulaAttribute = formula.split(":");
		for (String attr : arrFormulaAttribute) {
			attr = attr.trim();
			if (attr.contains("-"))
				valAttributeGl = level3AttributeName_vs_DB_Value.get(attr.substring(1));
			else
				valAttributeGl = level3AttributeName_vs_DB_Value.get(attr);
			if(valAttributeGl==null)
			{
				System.out.println(attr+" value is 0");
				valAttributeGl="0";
			}
			if (attr.contains("-"))
				glAttributeValue = Long.parseLong(valAttributeGl) * -1;
			else
				glAttributeValue = Long.parseLong(valAttributeGl);
			sum = sum + glAttributeValue;

		}
		level3AttributeName_vs_DB_Value.put(attributeName, String.valueOf(sum));
		return String.valueOf(sum);
	}

	private Boolean compare(XSSFSheet sheet, String attributeName, String attributeValue, String dbAttributeValue,
			XSSFCellStyle style) throws NumberFormatException, IOException {
		System.out.println("Inside Compare Method");
		long variance = Math.abs(Math.abs(Long.parseLong(attributeValue)) - Math.abs(Long.parseLong(dbAttributeValue)));
		Boolean flag = true;
		CommonUtils obj = new CommonUtils();
		XSSFRow row = sheet.createRow((short) sheet.getLastRowNum() + 1);
		XSSFCellStyle styleStatus = sheet.getWorkbook().createCellStyle();
		XSSFFont f = sheet.getWorkbook().createFont();
		f.setBold(true);
		// styleStatus.setFont(f);
		System.out.println(attributeValue+"     :    "+dbAttributeValue);
		int variencecutoff= Integer
				.valueOf(obj.getPropertyValue("FccCommon", "BlastAmountVariance"));
		int varience =Integer.parseInt(attributeValue) - Integer.parseInt(dbAttributeValue);
		if (Math.abs(varience) <= variencecutoff) {
		//if (attributeValue.equals(dbAttributeValue)) {
			System.out.println(attributeName + " Values are Matching against Encompass Database Vaue!! UI Value="
					+ attributeValue + " and Database value=" + dbAttributeValue);
			matching.put(attributeName, attributeValue);
			arrMatching.add(dbAttributeValue);
			row.createCell(0).setCellValue(attributeName);
			row.createCell(1).setCellValue(attributeValue);
			row.createCell(2).setCellValue(dbAttributeValue);

			f.setColor(IndexedColors.GREEN.getIndex());
			styleStatus.setFont(f);
			row.createCell(3).setCellValue("Pass");
			row.getCell(3).setCellStyle(styleStatus);

			row.createCell(4).setCellValue(String.valueOf(variance));
			row.getCell(4).setCellStyle(styleStatus);

		} else {
			System.out.println(attributeName + " Values are not Matching against Encompass Database Vaue!! UI Value="
					+ attributeValue + " and Database value=" + dbAttributeValue);
			notMatching.put(attributeName, attributeValue);
			arrNotMatching.add(dbAttributeValue);
			row.createCell(0).setCellValue(attributeName);
			row.createCell(1).setCellValue(attributeValue);
			row.createCell(2).setCellValue(dbAttributeValue);

			f.setColor(IndexedColors.RED.getIndex());
			styleStatus.setFont(f);
			row.createCell(3).setCellValue("Fail");
			row.getCell(3).setCellStyle(styleStatus);

			row.createCell(4).setCellValue(String.valueOf(variance));
			row.getCell(4).setCellStyle(styleStatus);

			flag = false;
		}
		if (listGlGroupList.contains(attributeName)) {
			row.getCell(0).setCellStyle(style);
			row.getCell(1).setCellStyle(style);
			row.getCell(2).setCellStyle(style);
			if (row.getCell(3).getStringCellValue().equals("Pass")) {
				styleStatus.setBorderTop((short) 3);
				styleStatus.setBorderBottom((short) 1);
				styleStatus.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				styleStatus.setFillPattern(CellStyle.SOLID_FOREGROUND);
				row.getCell(3).setCellStyle(styleStatus);
				row.getCell(4).setCellStyle(styleStatus);
			}
			if (row.getCell(3).getStringCellValue().equals("Fail")) {
				styleStatus.setBorderTop((short) 3);
				styleStatus.setBorderBottom((short) 1);
				styleStatus.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				styleStatus.setFillPattern(CellStyle.SOLID_FOREGROUND);
				row.getCell(3).setCellStyle(styleStatus);
				row.getCell(4).setCellStyle(styleStatus);
			}

			// row.getCell(3).setCellStyle(style);
			// row.getCell(4).setCellStyle(style);}
		}
		return flag;
	}

	String getGlValueFromDB(Connection con, String attributeName, Map<String, String> data,
			HashMap<String, String> glKeyPair)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException, NoSuchFieldException, SecurityException, IllegalArgumentException {
		Statement st = null;
		ResultSet rs = null;
		objUtils = new CommonUtils();
		FccQuery objFccQuery=new FccQuery();
		String accountNumber, region_numbers = null, branchName = null;
		String dbAttributeValue = "0";
		if(!data.get("ConsiderDate").equalsIgnoreCase("yes")) {
				
			startDate = "01/01/" + objUtils.getCurrentYear();
			endDate = objUtils.getCurrentMonth() + 1 + "/" + objUtils.getLastDayOfCurrentMonth() + "/"
				+ objUtils.getCurrentYear();}
		else {
//			startDate=data.get("StartDate");
			startDate = "01/01/" + objUtils.getCurrentYear();
			endDate=data.get("EndDate");			
		}
			
		branchName = data.get("Branches");
		branchName = branchName.replace(" ", "_");
		// region_numbers=objUtils.getPropertyValue("regionList.properties",branchName);
		region_numbers = objUtils.getBranchNumbers("regionList.properties", branchName);
		if (attributeName.contains("LO Margin"))
			System.out.println("LO MARgin found");

		accountNumber = getAccountNumber(glKeyPair, attributeName);
		
		if (accountNumber == null)
			return null;
		System.out.println("GL=" + attributeName + " Account No=" + accountNumber);
		String temp[];
		
		accountNumber=accountNumber.replaceAll(",", "','");
		String strSql = "select sum(Amount) from Temp_GeneralLedger with(nolock) where GLAccount in('" + accountNumber
				+ "') and JournalPostDate between '" + startDate + "' and '" + endDate + "' and  GLBranch in("
				+ region_numbers + ") and (DebitGLAccount >= 30000 or DebitGLAccount < 10000 "
				+ "or creditglaccount >= 30000 OR creditglaccount < 10000)";
		Boolean flagSign = false;
		Connection newConnection=con;
		if(accountNumber.equals("91110")&&( objUtils.getCurrentMonth()<2)) {
			region_numbers=region_numbers.replaceAll("','",",");
			strSql=objFccQuery.getQueryByAttributeName("TotalBeginningYearBalance");
			strSql=strSql.replace("#RegionNumbers", region_numbers);
			newConnection=conSL_ACCT_DW;
		}
		if (newConnection != null) {
			st = newConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = st.executeQuery(strSql);
			System.out.println(strSql);
			if (rs.next()) {
				try {
					dbAttributeValue = rs.getString(1).trim();					
					dbAttributeValue = String.valueOf(Math.round(Double.parseDouble(dbAttributeValue)));
					flagSign = getSignForGl(attributeName);
					if(accountNumber.equals("91110")&&( objUtils.getCurrentMonth()<2))
						flagSign=false;
					if (flagSign) {
						dbAttributeValue = String.valueOf(Long.parseLong(dbAttributeValue) * (-1));
					}
				} catch (Exception e) {
					System.out.println("Database Value is Null");
					// e.printStackTrace();
				}
			}
		}
		
		
		if (rs != null)
			rs.close();
		if (st != null)
			st.close();
		
		return dbAttributeValue;
	}

	private Boolean getSignForGl(String attributeName) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException, IOException, BiffException, InvalidFormatException {
		/*
		 * Code for getting Sign information of individual GL from SL_ACCT_DW database
		 */
		Boolean flag = null;
		DataBaseUtils objDB = new DataBaseUtils();
		String valueNonSum, strNonSumMappingAttribute;
		Statement stSL_ACCT_DW;
		ResultSet rsSL_ACCT_DW = null;
		// HashMap <String,String>mapNonSum=getMapNonSumNotFound();
		if ((strNonSumMappingAttribute = mapNonSum.get(attributeName)) != null)
			attributeName = strNonSumMappingAttribute;
		String strSql = "select  NonSumOperator from v_dim_AccountTreeAccount( nolock) where  GLName='" + attributeName
				+ "' and NonSumOperator is NULL";
		stSL_ACCT_DW = conSL_ACCT_DW.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

		rsSL_ACCT_DW = stSL_ACCT_DW.executeQuery(strSql);
		try {
			if (rsSL_ACCT_DW.next()) {
				valueNonSum = rsSL_ACCT_DW.getString(1);
				flag = false;
			} else {
				flag = true;
			}
		} catch (Exception e) {
		} finally {

			if (rsSL_ACCT_DW != null)
				rsSL_ACCT_DW.close();
			if (stSL_ACCT_DW != null)
				stSL_ACCT_DW.close();
		}
		// ************************
		return flag;

	}

	void getMapNonSumNotFound() throws BiffException, InvalidFormatException, IOException {
		// It will return those list of GL,whose Name mapping is not found in Non sum
		// operator list.
		mapNonSum = new HashMap<String, String>();
		String strNonSumNotFoundList = KWVariables.getVariables().get("NonSumList");
		String arrNonSumNotFoundList[] = strNonSumNotFoundList.split(",");
		for (String str : arrNonSumNotFoundList) {
			mapNonSum.put(str.split("#")[0], str.split("#")[1]);
		}
	}

	String getAccountNumber(HashMap<String, String> glKeyPair, String attributeName) {
		if (attributeName.equals("Branch Payroll Expenses"))
			attributeName = "Payroll Taxes";
		if (attributeName.equals("Operations Staff Payroll"))
			attributeName = "Operations Payroll-Servicing";
		if (attributeName.equals("Courier & Overnight"))
			attributeName = "Courier/Overnight";
		if (attributeName.equals("Beginning Year Balance") || attributeName.equals("Total Beginning Year Balance"))
			attributeName = "Carry Over";
		if (attributeName.equals("Escrow Holdback") || attributeName.equals("Total Escrow Holdback"))
			attributeName = "Escrow Advances Held";
		return glKeyPair.get(attributeName);

	}

	public HashMap<String, String> createGlKeyPairMap() throws BiffException, InvalidFormatException, IOException {
		String str;
		HashMap<String, String> mapGlKeyPair = new HashMap<String, String>();
		String GlKeyPair, attributeName;
		String keyValue;
		str = KWVariables.getVariables().get("GlKey");
		String s[] = str.split(":");
		Set<String> keySet = new HashSet<String>();
		System.out.println("***************************************");
		for (int i = 0; i < s.length; i++) {
			System.out.println(s[i]);

			keySet.add(s[i].trim());
		}
		System.out.println("***************************************");
		System.out.println(keySet.size());

		Iterator<String> itr = keySet.iterator();
		while (itr.hasNext()) {
			GlKeyPair = itr.next();
			System.out.println(GlKeyPair);
			keyValue = GlKeyPair.replaceAll("[^,0-9]", "").trim();
			if (keyValue.charAt(0) == ',')
				keyValue = keyValue.substring(1);
			attributeName = GlKeyPair.split(keyValue)[0].trim();
			attributeName = attributeName.substring(0, attributeName.length() - 2);
			mapGlKeyPair.put(attributeName, keyValue);
		}

		System.out.println("I am Done with Creation of GlKeyPairMap");
		return mapGlKeyPair;
	}

	void createGlGroupFormula() throws BiffException, InvalidFormatException, IOException {
		String groupName, formula;
		String strGroupFormula = KWVariables.getVariables().get("GlGroupFormula");
		String[] arrGroupFormula = strGroupFormula.split("#");
		for (String str : arrGroupFormula) {
			groupName = str.split("=")[0];
			formula = str.split("=")[1];
			glGroupFormula.put(groupName, formula);
		}

	}

	String calculateGlGroupValue(LinkedHashMap<String, String> blastAttributeValues, String glGroupName) {
		long sum = 0, glAttributeValue;
		String valAttributeGl;
		String glAttribute;
		String formula, arrFormulaAttribute[];
		formula = glGroupFormula.get(glGroupName);
		arrFormulaAttribute = formula.split(":");
		for (int i = 0; i < arrFormulaAttribute.length; i++) {
			glAttributeValue = 0;
			glAttribute = arrFormulaAttribute[i].trim();
			valAttributeGl = blastAttributeValues.get(glAttribute);
			if (valAttributeGl != null)
				glAttributeValue = Long.parseLong(valAttributeGl);
			else
				glAttributeValue=0;
			sum = sum + glAttributeValue;
		}
		return String.valueOf(sum);
	}
	
	
	
	private Boolean getLevel3VarianceStatus() throws IOException {
		Boolean flagVariance=true;		
		objUtils=new CommonUtils();
		int limit_BlastVariance=Integer.parseInt(objUtils.getPropertyValue("FccCommon.properties","BlastAmountVariance"));
		int blastVarianceActual;
		FileInputStream fis=new FileInputStream(file_Level3ReportName);
		XSSFWorkbook wb=new XSSFWorkbook(fis);

		XSSFSheet sh=wb.getSheetAt(0);
		for(int i=2;i<sh.getLastRowNum();i++) {
			blastVarianceActual=Integer.parseInt(sh.getRow(i).getCell(4).getStringCellValue());
			if(blastVarianceActual>limit_BlastVariance)
				flagVariance=false;
			}
		fis.close();
		return flagVariance;
	}
	
	public LinkedHashMap<String,String> getMap_Level3Details()
			throws TwfException, BiffException, InvalidFormatException, IOException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		String glUiName;
		List<WebElement> elementUiAttributes;
		LinkedHashMap<String,String> blast_ui_GlValues=new LinkedHashMap<String, String>();

		objUtils = new CommonUtils();
		// attributeList=KWVariables.getVariables().get("BlastAttributeListLevel3");
		driver = DriverFactory.getDriver();
		elementUiAttributes = driver.findElements(By.xpath("//table[@id='tblLevel3_fixed']//child::tr/td[1]/div"));
		String attributeList = null;

		System.out.println("Number of webElements=" + elementUiAttributes.size());
		for (WebElement ele : elementUiAttributes) {
			if (!ele.isDisplayed()) {
				//getElementByUsing("lblDetail").click();
				objUtils.scrollToAnElement(ele);
			}
			glUiName = ele.getText();
			if (glUiName.contains("Cumulative Balance"))
				break;
			if (attributeList == null)
				attributeList = glUiName;
			else
				attributeList = attributeList + ":" + glUiName;
		}
		attributeList = attributeList + ":" + "Cumulative Balance";

		String value;
		String xpathAmount;
		Boolean flagParenthesis;

		createGlGroupFormula(); // It will create hashmap for Gl Group and its respective Formula
		getMapNonSumNotFound();
		String arrAttribute[] = attributeList.split(":");
		System.out.println(arrAttribute.length);
		for (String Attribute : arrAttribute) {
			xpathAmount = "//table[@id='tblLevel3_fixed']//child::div[contains(text(),'NameOfAttribute')]/../../td[2]";
			if (Attribute.contains("Loan Volume Amount"))
				xpathAmount = "//table[@id='tblLevel3_fixed']//child::div[contains(text(),'NameOfAttribute')]/../../td[2]";
			flagParenthesis = false;

			xpathAmount = xpathAmount.replace("NameOfAttribute", Attribute);
			element = objUtils.getObject(xpathAmount);
			if (!element.isDisplayed()) {
				//getElementByUsing("lblDetail").click();
				objUtils.scrollToAnElement(element);
			}
			value = element.getText();

			if (value.contains("("))
				flagParenthesis = true;
			value = value.replaceAll("[^0-9]", "");
			if (flagParenthesis)
				value = String.valueOf(Long.parseLong(value) * -1);

			blast_ui_GlValues.put(Attribute, value); // This map contains Gl displayed in the the Level 3 details Ui
														// and their respective Amount
			System.out.println(Attribute + "=" + value);
		}
			System.out.println("BLAST UI Level 3 GL values are ready");
		return blast_ui_GlValues;
		}
/*Verification of Variance Tab*/
	public void verifyBlastVariance(Map<String,String> data) throws BiffException, IOException, TwfException, InterruptedException, InvalidFormatException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, ParseException
	{		
		List<String> lsDisplyedGl;
		BlastCustomMethods blastc=new BlastCustomMethods();
		
		String varianceType=data.get("VarianceType");
		
		blastc.selectVarianceType(varianceType);
		
		lsDisplyedGl=blastc.getDisplayedGlListAtVarianceTab();
		
		
		createGlGroupFormula(); // It will create hash map for Gl Group and its respective Formula "glGroupFormula" static
		getMapNonSumNotFound(); //"mapNonSum" static
		String locator_FirstColumn=KWVariables.getVariables().get("varianceFirstColumn");
		String locator_SecondColumn=KWVariables.getVariables().get("varianceSecondColumn");
		String locator_ThirdColumn=KWVariables.getVariables().get("varinaceThirdColumn");

		ArrayList<LinkedHashMap<String, String>> mapAmount=blastc.getFirstOrSecondColumnAmount(lsDisplyedGl,locator_FirstColumn,locator_SecondColumn,locator_ThirdColumn);
		
		LinkedHashMap<String, String>  mapAmount_FirstColumn=mapAmount.get(0);
		LinkedHashMap<String, String>  mapAmount_SecondColumn=mapAmount.get(1);
		LinkedHashMap<String, String>  mapAmount_ThirdColumn=mapAmount.get(2);

		
		LinkedHashMap<String, String>  mapAmount_DailyVarianceUi=blastc.getDailyVarianceFromUi(mapAmount_FirstColumn,mapAmount_SecondColumn);
		
		
		String endDates=blastc.getEndDatesForVarianceCalculation(varianceType);
		
		String endDate1=endDates.split(":")[0];
		String endDate2=endDates.split(":")[1];
		
		String startDate1=blastc.getStartDate(endDate1);
		String startDate2=blastc.getStartDate(endDate2);
		if(varianceType.contains("Year over Year")) {//||varianceType.contains("Month over Month")) {
			startDate1=blastc.getStartDateYear(endDate1);
			startDate2=blastc.getStartDateYear(endDate2);
		}
		blastc.verifyVarianceAgainstEncompassDB(data,mapAmount_FirstColumn,mapAmount_SecondColumn,mapAmount_ThirdColumn,mapAmount_DailyVarianceUi,startDate1,endDate1,startDate2,endDate2);
		SoftAssert.assertAll();
		
		
		
		//Map<String,String> mapAmount_SecondColumn=blastc.getFirstOrSecondColumnAmount(lsDisplyedGl,locator_SecondColumn);
		
		
		
	}

	@Override
	public void checkPage() {
		// TODO Auto-generated method stub

	}
	

}
