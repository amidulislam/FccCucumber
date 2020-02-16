package com.fcc.test;

import java.awt.AWTException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.tavant.base.DriverFactory;
import com.tavant.base.WebPage;
import com.tavant.kwutils.CustomKW;
import com.tavant.kwutils.CustomStep;
import com.tavant.kwutils.KWVariables;
import com.tavant.utils.TwfException;
import com.fcc.test.SoftAssert;

import jxl.read.biff.BiffException;

//If we extends a class from WebPage then we can call only parameterized methods
public class SLAP extends WebPage {
	String command;
	WebDriver driver;
	WebElement ele;
	String sheetName;
	String sheetName_tcID;
	String month;
	String regions[];
	CommonUtils objUtils = new CommonUtils();
	DataBaseUtils dbUtils = new DataBaseUtils();
	String ddlMonth = "//ul[@class='ui-igcombo-listitemholder'][1]//li[text()='";
	SlapCustomMethods slap = new SlapCustomMethods();
	String lblMTDActivity = "//div[text()='MTD ACTIVITY']";
	String strLO = "//div[text()='LO Name']";
	String strLOName_overBarGraph = "//*[@id='chartsOne']/div/span[contains(text(),'LO Name')]";
	static int calculatedRunRate;
//String bar_creditPulled="(//*[name()='g']/*[name()='rect'])[index]";						 
	public void selectRegionSlap(Map<String, String> data)
			throws BiffException, IOException, TwfException, InterruptedException, InvalidFormatException {
		String regions;
		objUtils = new CommonUtils();
		regions = data.get("Branches");
		objUtils.waitTillClickable(getElementTargetID("ddl_region"), 60);
		getElementByUsing("txtRegion").click();
		objUtils.selectRegions(regions);
		getElementByUsing("btnSubmit").click();
		objUtils.waitTillLoadSymbolBlast(" ");
	}

	public void selectDateAndRegion(Map<String, String> data)
			throws TwfException, IOException, InterruptedException, BiffException, InvalidFormatException {
		String regions;
		objUtils = new CommonUtils();
		month = data.get("Month");
		regions = data.get("Branches");
		for (int i = 0; i < 10; i++) {
			Thread.sleep(1000);
			System.out.println("Execution of select Date and Region method started");
		}

		objUtils.switchToCurrentWindow(" ");
		objUtils.waitTillClickable(getElementTargetID("ddl_region"), 60);

		System.out.println("Switched to SLAP Date selection window");
		getElementByUsing("ddlDateDropdown").click();

		ddlMonth = ddlMonth + month + "']";

		objUtils.waitForVisibility(ddlMonth, 120);

		objUtils.clickObject(ddlMonth); // Select the respective month passed in the TestData Sheet
		objUtils.selectRegionsFromDropdown(regions);
		getElementByUsing("btnSubmit").click();
		objUtils.waitTillLoadSymbol();
		objUtils.scrollToBottom();
		Thread.sleep(5000);

	}

	public void clickBarChart(Map<String, String> data) throws BiffException, InvalidFormatException, IOException, TwfException, InterruptedException {
		String stackedBarType = data.get("LoanType");
		CommonUtils cobj = new CommonUtils();
		String StackedBar = KWVariables.getVariables().get("StackedBar");
		switch (stackedBarType) {
		case "Credit Pulled":
			StackedBar = StackedBar.replace("index", "6");
			break;
		case "Locks":
			StackedBar = StackedBar.replace("index", "21");
			break;
		case "LE Application":
			StackedBar = StackedBar.replace("index", "36");
			break;
		case "Funded":
			StackedBar = StackedBar.replace("index", "57");
			//StackedBar = StackedBar.replace("index", "51");
		}

		cobj.clickObjectByActions(StackedBar);
	}

	public void clickBarChartMonth(Map<String, String> data)
			throws BiffException, InvalidFormatException, IOException, TwfException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, InterruptedException, SQLException, ParseException {
		int datediff = 5;
		CommonUtils cobj = new CommonUtils();
		int pos = 1;
		String xpathBarType = "(//*[name()='text']//*[text()='BarType'])[2]//..//preceding-sibling::*[@class='highcharts-series-group']//*[name()='g'][3]//child::*";
		String loanType = data.get("LoanType");
		String selectedMonth = data.get("Month");
		String selectedMonth_Year = getSelectedMonthYear(selectedMonth);
		String selectedBarchartMonth;
		String selectedBarchartYear;
		xpathBarType = xpathBarType.replace("BarType", loanType);

		List<WebElement> element_bars = cobj.CreateObjectList(xpathBarType);
		for (WebElement bar : element_bars) {
			cobj.clickObjectByActions(bar);
			selectedBarchartMonth = cobj.backMonth(selectedMonth, datediff--);
			if (selectedBarchartMonth.equals("Jul"))
				System.out.println("wait...");
			if (Integer.parseInt(selectedMonth_Year) < cobj.getCurrentYear())
				selectedBarchartYear = selectedMonth_Year;
			else
				selectedBarchartYear = getSelectedMonthYear(selectedBarchartMonth);
			this.drillDownNormalBarChart(String.valueOf(pos), loanType, data.get("Branches"), selectedBarchartMonth,
					selectedBarchartYear);
			pos++;
		}
	}

	public String getSelectedMonthYear(String selectedMonth) throws ParseException {
		String strCurrentMonth, strCurrentYear;
		int currentMonth_Number;
		int selectedMonth_Number;
		int selectedMonthYear_Number = 0;
		// int selctedMonth_Year=getMonthForSelectedYear(selectedMonth);

		CommonUtils cobj = new CommonUtils();
		currentMonth_Number = cobj.getCurrentMonth() + 1;
		strCurrentMonth = cobj.formatMonth(String.valueOf(currentMonth_Number));
		strCurrentYear = String.valueOf(cobj.getCurrentYear());
		selectedMonth_Number = cobj.getMonthNumber(selectedMonth);
		if (selectedMonth_Number > currentMonth_Number)
			selectedMonthYear_Number = Integer.parseInt(strCurrentYear) - 1;
		else
			selectedMonthYear_Number = Integer.parseInt(strCurrentYear);
		return String.valueOf(selectedMonthYear_Number);
	}

	public void clickLO(String LO) throws TwfException, AWTException {
		CommonUtils cobj = new CommonUtils();
		WebElement Lo;
		cobj.scrollToTop();
		String strLO = "//div[text()='LO Name']";
		strLO = strLO.replace("LO Name", LO);
		Lo=objUtils.getObject(strLO);
		if(!Lo.isDisplayed())
			slap.scrollToLoanOfficer(Lo);		
		cobj.clickObject(strLO);
		strLOName_overBarGraph = strLOName_overBarGraph.replace("LO Name", LO);
		cobj.waitForVisibility(strLOName_overBarGraph, 30);
		cobj.scrollToBottom();
	}

	public void drillDownNormalBarChart(String pos, String loanType, String branchName, String selectedBarchartMonth,
			String selectedBarChartYear)
			throws BiffException, IOException, TwfException, InterruptedException, InvalidFormatException,
			InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, ParseException {
		String xpathBarChartRecord = "(//*[name()='text']//*[text()='LoanType'])[2]/..//following-sibling::*[@class=\"highcharts-stack-labels\"]//child::*[name()='text'][index]//*[name()='tspan'][1]";
		xpathBarChartRecord = xpathBarChartRecord.replace("LoanType", loanType).replace("index", pos);
		String strStackedBarchartCount = "(((//*[name()='tspan'][text()='LoanType'])[2]/../preceding-sibling::*[1]//following-sibling::*[name()='g'])[6]//child::*[name()='tspan'][2])[6]";
		strStackedBarchartCount = strStackedBarchartCount.replace("LoanType", loanType);
		Boolean flagSecondLien = false;
		CommonUtils cobj = new CommonUtils();
		System.out.println("Drill down for" + loanType + " Loan.. execution started...");
		Thread.sleep(2000);
		String recordCount_barchart = "";
		if (selectedBarchartMonth.equals(cobj.formatMonth(String.valueOf(cobj.getCurrentMonth() + 1)))
				&& selectedBarChartYear.equals(String.valueOf(cobj.getCurrentYear())))
			recordCount_barchart = cobj.getObject(strStackedBarchartCount).getText();
		else
			recordCount_barchart = cobj.getObject(xpathBarChartRecord).getText();
		System.out.println("Number of records for " + loanType + " Bar chart=" + recordCount_barchart);

		String count_afterDrillDown = getElementByUsing("drilDown_recordCount").getText();
		count_afterDrillDown = ((count_afterDrillDown.split("of"))[1].trim()).split(" ")[0];

		if (!recordCount_barchart.equals(count_afterDrillDown)) {
			flagSecondLien = checkForSecondLienLoan(loanType, branchName, recordCount_barchart, count_afterDrillDown,
					selectedBarchartMonth, selectedBarChartYear);
			if (!flagSecondLien)
				this.verifyRecordCount(flagSecondLien, recordCount_barchart, count_afterDrillDown, loanType, branchName,
						selectedBarchartMonth, selectedBarChartYear);
		} else {
			System.out.println("Record Count are Matching for " + loanType + " and Before Drill Down recourd count="
					+ recordCount_barchart + " After Drill down record count=" + count_afterDrillDown);
		}

		getElementByUsing("btnClose_CreditPulled").click();

	}
	public void verifySlapPage(Map<String, String> data) throws TwfException
	{
		String loanStage[]={"Credit Pulled","LE Application","Locks","Funded"};
		String strLoanStage="//*[@class='highcharts-title'][contains(text(),'loanStageType')]";
		
		String strSlapGrid="//div[@id='div-grid_container']";
		CommonUtils cobj = new CommonUtils();
		//verification of Grid
		if(cobj.isElementExist(strSlapGrid,10))
			System.out.println("Grid Displayed succesfully");
		else
			addExceptionToReport("Salp Grid Verification", "Not Visible", "It Should be visible");
		
		cobj.scrollToBottom();
		//Verification of loan stage label
		for(String stageInIteration:loanStage)
		{
			strLoanStage=strLoanStage.replace("loanStageType", stageInIteration);
					if(cobj.isElementExist(strLoanStage,10))
						System.out.println(stageInIteration+" Loan is Displayed succesfully");
					else
						addExceptionToReport(stageInIteration+"visibility Verification", "Not Visible", "It Should be visible");
		}
	}

	public void drillDown(Map<String, String> data)
			throws BiffException, IOException, TwfException, InterruptedException, InvalidFormatException,
			InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, ParseException {
		Boolean flagSecondLien = false;
		CommonUtils cobj = new CommonUtils();
		String loanType = data.get("LoanType");
		System.out.println("Drill down for" + loanType + " Loan.. execution started...");
		Thread.sleep(5000);
		driver = DriverFactory.getDriver();
		String value = KWVariables.getVariables().get("recordCountCurrentMonth");
		String xapth_value_Of_CurrentBarChart = value.replace("LoanCategory", loanType);
		String recordCount_barchart = cobj.getObject(xapth_value_Of_CurrentBarChart).getText();
		System.out.println("Number of records for " + loanType + " stacked bar chart=" + recordCount_barchart);
		//driver.findElement(By.xpath(xapth_value_Of_CurrentBarChart)).click();
		Thread.sleep(3000);
		
		String count_afterDrillDown = getElementByUsing("drilDown_recordCount").getText();
		count_afterDrillDown = ((count_afterDrillDown.split("of"))[1].trim()).split(" ")[0];

		if (!recordCount_barchart.equals(count_afterDrillDown)) {
			flagSecondLien = checkForSecondLienLoan(loanType, data.get("Branches"), recordCount_barchart,
					count_afterDrillDown, "", "");
			if (!flagSecondLien)
				this.verifyRecordCount(flagSecondLien, recordCount_barchart, count_afterDrillDown);
		} else {
			System.out.println("Record Count are Matching for " + loanType + " and Before Drill Down recourd count="
					+ recordCount_barchart + " After Drill down record count=" + count_afterDrillDown);
		}

		getElementByUsing("btnClose_CreditPulled").click();
		System.out.println("Drill down for" + data.get("LoanType") + " Loan.. execution completed...");
	}

	private Boolean checkForSecondLienLoan(String loanType, String branchName, String recordCount_barchart,
			String count_afterDrillDown, String selectedBarchartMonth, String selectedBarchartYear)
			throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException,
			TwfException, ParseException {

		Boolean flag = false;
		slap = new SlapCustomMethods();
		String strLoanType = slap.getLoanTypeForSqlQuery(loanType);
		ArrayList<String> loanCount = getLoanCountFirstLienVsSecondLien(strLoanType, branchName, selectedBarchartMonth,
				selectedBarchartYear);
		int firstLienLoanCount, secondLienLoanCount = 0;
		firstLienLoanCount = Integer.parseInt(loanCount.get(0));
		if (loanCount.size() > 0)
			secondLienLoanCount = Integer.parseInt(loanCount.get(1));

		if (Integer.parseInt(count_afterDrillDown) == (firstLienLoanCount + secondLienLoanCount)) {
			flag = true;
			System.out.println("First Lien Loan count=" + firstLienLoanCount);
			System.out.println("Second Lien Loan count=" + secondLienLoanCount);
			System.out.println(
					"Toatal Loan Count Matching (First Lien+Second Lien)" + (firstLienLoanCount + secondLienLoanCount));
			if ((firstLienLoanCount != Integer.parseInt(recordCount_barchart)))
				flag = false;
		}

		else
			System.out.println("First Lien=" + firstLienLoanCount + "\tSecond Lien=" + secondLienLoanCount
					+ "\tLoan Count is not Matching first lien+Second Lien");// , count_afterDrillDown,
																				// String.valueOf(firstLienLoanCount+secondLienLoanCount));
		return flag;
	}

	ArrayList<String> getLoanCountFirstLienVsSecondLien(String loanType, String branchName,
			String selectedBarchartMonth, String selectedBarchartYear) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException, IOException, ParseException {
		Statement st;
		ResultSet rs = null;
		String startDate = null, endDate = null;
		CommonUtils objUtils = new CommonUtils();
		DataBaseUtils dbUtils = new DataBaseUtils();
		Connection con = dbUtils.init();
		String selectedMonth_Number;
		ArrayList<String> loanCount = new ArrayList<String>();
		if (selectedBarchartMonth.equals("") || selectedBarchartYear.equals("")) {
			startDate = (objUtils.getCurrentMonth() + 1) + "/" + "01/" + objUtils.getCurrentYear();
			endDate = (objUtils.getCurrentMonth() + 1) + "/" + objUtils.getLastDayOfCurrentMonth() + "/"
					+ objUtils.getCurrentYear();
		} else {
			selectedMonth_Number = String.valueOf(objUtils.getMonthNumber(selectedBarchartMonth));
			startDate = selectedMonth_Number + "/" + "01/" + selectedBarchartYear;
			endDate = selectedMonth_Number + "/" + objUtils.getLatDayOfGivenMonthNumber(selectedMonth_Number,selectedBarchartYear) + "/"
					+ selectedBarchartYear;
		}
		branchName = branchName.replace(" ", "_");
		String region_numbers = objUtils.getBranchNumbers("regionList.properties", branchName);
		String query = "";
		if (loanType.equals("FundedDate") || loanType.equals("LockDate")) {
			query = "Select  count (*) from ATI_BEM_DW_DIM_Slap with (nolock) where " + loanType + " between '"
					+ startDate + "' and '" + endDate + "'  and TerminationDate is null and  Branch in("
					+ region_numbers + ")" + "and Channel not in ('Brokered') group by LienPosition";
		} else {
			query = "Select  count (*) from ATI_BEM_DW_DIM_Slap with (nolock) where " + loanType + " between '"
					+ startDate + "' and '" + endDate + "'  and TerminationDate is null and  Branch in("
					+ region_numbers + ")" + "group by LienPosition";

		}
 
		System.out.println("Query to be executed=" + query);
		System.out.println("Branch Name=" + branchName);
		if (con != null) {
			st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = st.executeQuery(query);
			System.out.println("Result retrieved successfully");
		}

		while (rs.next()) // It is true of the branch contains second lien loans
		{
			loanCount.add(rs.getString(1));
		}

		return loanCount;

	}

	/*
	 * Mthod Name:verifyStackedBarChartRemainingTargetValue(Map<String, String>
	 * data) Author: syed Amidul Islam Purpose: To verify the target projected Loan
	 * for the remaining days of the month at branch level 
	 * Input: Test data pool Output:
	 * Verification result display
	 */
	public void verifyStackedBarChartRemainingTargetValue(Map<String, String> data) throws TwfException,
			InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException, BiffException, InvalidFormatException, ParseException, NoSuchFieldException, SecurityException, IllegalArgumentException {
		System.out.println("********************Inside verifyStackedBarChartRemainingTargetValue**********************");
		int remainingWorkingDays;
		Boolean flagFundedRunRate = true;
		FccUtility objFccUtil = new FccUtility();

		String strStackedBarchartTarget = "(//*[name()='svg'])[BarType]/*[name()='g'][7]/*[name()='g'][6]/*/*[1]";
		String strStackedBarchartLoanCompleted = "(//*[name()='svg'])[BarType]/*[name()='g'][8]/*[name()='g'][6]/*[name()='text']//*[name()='tspan'][2]";

		int actual_TargetLoan_Count_ForRemainingMonth, completed_Loan_Count;
		int projectedFundedLoanCount_EntireMonth;
		String barChartType = data.get("LoanType");

		// For scrolling to desired bar chart type
		String strBarChartLocator = KWVariables.getVariables().get("BarType").replace("BarType", barChartType);
		WebElement ele_BarChartGroup = objUtils.getObject(strBarChartLocator);
		objUtils.scrollToAnElement(ele_BarChartGroup);

		actual_TargetLoan_Count_ForRemainingMonth = objFccUtil.getTargetLoan(barChartType, strStackedBarchartTarget);

		completed_Loan_Count = objFccUtil.getTargetLoan(barChartType, strStackedBarchartLoanCompleted);

		remainingWorkingDays = objFccUtil.getRemainingWorkingDayDB(barChartType);
		
		
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date dateobj = new Date();
		String strDateToday=df.format(dateobj);
		
		boolean flagHoliday=objFccUtil.isTheDateIsSupremeHoliday(strDateToday);
		System.out.println("**************************************************************======="+flagHoliday);
		if(flagHoliday) {
			remainingWorkingDays=remainingWorkingDays+1;
		}
		
		Boolean flagRunrate;
		if (!barChartType.equals("Funded")) {
			// It will verify the target loan count for LE Application, Locked and funded
			// Loan
			flagRunrate = objFccUtil.verifyTargetLoanForRemainingDays(completed_Loan_Count,
					actual_TargetLoan_Count_ForRemainingMonth, remainingWorkingDays, barChartType);

			if (flagRunrate != true) {
				SoftAssert.assertUnEqual(actual_TargetLoan_Count_ForRemainingMonth, calculatedRunRate,
						" TargetLoan_Count_ForRemainingMonth Not Matching (Not Funded)");
			}

		} else {
			projectedFundedLoanCount_EntireMonth = slap.getProjetedLoanCountEntireMonth(data.get("Branches"), "", "",barChartType);
			flagFundedRunRate = slap.verifyFundedRunRate(completed_Loan_Count,
					actual_TargetLoan_Count_ForRemainingMonth, projectedFundedLoanCount_EntireMonth);

			if (flagFundedRunRate != true) {
				SoftAssert.assertUnEqual(completed_Loan_Count, actual_TargetLoan_Count_ForRemainingMonth,
						"TargetLoan_Count_ForRemainingMonth Not Matching (Funded)");
				System.out.println("Ui Projected score:"
						+ (actual_TargetLoan_Count_ForRemainingMonth - completed_Loan_Count)
						+ " is not matching with the DB Calculated Value: " + projectedFundedLoanCount_EntireMonth);
			}
		}

		SoftAssert.assertAll();

	}

	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~End Of
	 * verifyStackedBarChartRemainingTargetValue(Map<String, String>
	 * data)~~~~~~~~~~~~~~~~~~~~~~
	 */
	//***************************************************************************************************************************************************************************
	
	/*
	 * Method Name:verifyStackedBarChartRemainingTargetValue_IndividualLO(Map<String, String> data) 
	 * 
	 * Author: syed Amidul Islam
	 *  
	 * Purpose: To verify the target projected Loan
	 * for the remaining days of the month Input: Test data pool Output:
	 * Verification result display
	 */
	public void verifyStackedBarChartRemainingTargetValue_IndividualLO(Map<String, String> data) throws TwfException, AWTException, InterruptedException, BiffException, InvalidFormatException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, NoSuchFieldException, SecurityException, IllegalArgumentException, ParseException
			{
		int actual_TargetLoan_Count_ForRemainingMonth, completed_Loan_Count;
		int projectedFundedLoanCount_EntireMonth = 0;
		int remainingWorkingDays;
		Boolean flagFundedRunRate=true;
		
		FccUtility objFccUtil = new FccUtility();

		String strStackedBarchartTarget = "(//*[name()='svg'])[BarType]/*[name()='g'][7]/*[name()='g'][6]/*/*[1]";
		String strStackedBarchartLoanCompleted = "(//*[name()='svg'])[BarType]/*[name()='g'][8]/*[name()='g'][6]/*[name()='text']//*[name()='tspan'][2]";
		String barChartType = data.get("LoanType");
		
		/*String strBarChartLocator=KWVariables.getVariables().get("BarType").replace("BarType", barChartType);
		WebElement ele_BarChartGroup=objUtils.getObject(strBarChartLocator);
		objUtils.scrollToAnElement(ele_BarChartGroup);*/

		String LoanOfficerName;
		List<WebElement> elems_LoanOfficer = slap.getElementsLoanOfficer();

		String InputBranchNames = data.get("Branches");
		Boolean flagBranchContainNumber = objUtils.stringContainsNumber(InputBranchNames);
		Boolean flagSubBranch = InputBranchNames.contains("~");

		System.out.println("Total Number Of Loan Officer Displayed in the selected Branch=" + elems_LoanOfficer.size());
		WebElement previousLoanOfficer = null;
		Boolean flagMatchingloanCount = false;
		for (WebElement LoanOfficer : elems_LoanOfficer) {
			LoanOfficerName = slap.getLoanOfficerName(LoanOfficer, previousLoanOfficer);
			if (LoanOfficerName.equals("Bridget Tracy"))
				System.out.println("wait");
			System.out.println("\n\nTarget Count verification started for Loan Officer: " + LoanOfficerName + "\n\n");

			String branchNumber = slap.getBranchNumberOfLoanOfficer(LoanOfficer, LoanOfficerName, previousLoanOfficer,
					flagBranchContainNumber);
			if (branchNumber.equals("")) {
				System.out.println("Iteration skip for " + LoanOfficerName + " due to name mismatch");
				continue;
			}
			if (!LoanOfficer.isDisplayed()) {
				slap.scrollToEndOfGrid(LoanOfficer, previousLoanOfficer);
				objUtils.scrollToTop();

			}

			LoanOfficer.click();
			objUtils.waitTillLoadSymbolBlast("");
			objUtils.scrollToBottom();

			actual_TargetLoan_Count_ForRemainingMonth = objFccUtil.getTargetLoan(barChartType,
					strStackedBarchartTarget);

			completed_Loan_Count = objFccUtil.getTargetLoan(barChartType, strStackedBarchartLoanCompleted);

			remainingWorkingDays = objFccUtil.getRemainingWorkingDayDB(barChartType);
			Boolean flagRunrate = true;

			if (!barChartType.equals("Funded")) {
				// It will verify the target loan count for LE Application, Locked and funded
				// Loan
				flagRunrate = objFccUtil.verifyTargetLoanForRemainingDays(completed_Loan_Count,
						actual_TargetLoan_Count_ForRemainingMonth, remainingWorkingDays, barChartType);

				if (flagRunrate != true) {

					SoftAssert.assertUnEqual(actual_TargetLoan_Count_ForRemainingMonth, calculatedRunRate,
							"Not Matching");
				}
			} else {

				projectedFundedLoanCount_EntireMonth = slap.getProjetedLoanCountEntireMonth(data.get("Branches"),
						LoanOfficerName, branchNumber,barChartType);
				flagFundedRunRate = slap.verifyFundedRunRate(completed_Loan_Count,
						actual_TargetLoan_Count_ForRemainingMonth, projectedFundedLoanCount_EntireMonth);

				if (flagFundedRunRate != true) {
					System.out.println("Ui Projected score:"
							+ (actual_TargetLoan_Count_ForRemainingMonth - completed_Loan_Count)
							+ " is not matching with the DB Calculated Value: " + projectedFundedLoanCount_EntireMonth);
					//Syed
					
					int remaingLoanCountTargetForTheMonth=actual_TargetLoan_Count_ForRemainingMonth - completed_Loan_Count;
					if(remaingLoanCountTargetForTheMonth<0)
						remaingLoanCountTargetForTheMonth=0;
					
				//End Syed
					SoftAssert.assertUnEqual(remaingLoanCountTargetForTheMonth,
							actual_TargetLoan_Count_ForRemainingMonth, "Not Matching");
				}
			}

			previousLoanOfficer = LoanOfficer;
			objUtils.scrollToTop();

			System.out.println("\n\nTarget Count verification completed for Loan Officer: " + LoanOfficerName + "\n");
			System.out.println(
					"\n\n******************************************************************************************************************************************\n");
		}
		SoftAssert.assertAll();
	}
	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~End Of verifyStackedBarChartRemainingTargetValue_IndividualLO(Map<String, String> data)~~~~~~~~~~~~~~~~~~~~~~
	 */
	//***************************************************************************************************************************************************************************
	public void selectRegions(Map<String, String> data)
			throws BiffException, IOException, TwfException, InterruptedException, InvalidFormatException {
		String regions;
		objUtils = new CommonUtils();
		regions = data.get("Branches");
		objUtils.switchToCurrentWindow(" ");
		objUtils.waitTillClickable(getElementTargetID("ddl_region"), 60);
		objUtils.selectRegionsFromDropdown(regions);
		getElementByUsing("btnSubmit").click();
		objUtils.waitTillLoadSymbol();
		objUtils.scrollToBottom();
		Thread.sleep(5000);
	}

	void verifyRecordCount(Boolean flag, String before, String after) throws TwfException {
//		Assert.assertEquals(after, before,"Record Match not Found");	
		addExceptionToReport("Record Match Not Found", before, after);
	}

	void verifyRecordCount(Boolean flag, String before, String after, String loanType, String branchName,
			String selectedBarchartMonth, String selectedBarChartYear) throws TwfException {
//		Assert.assertEquals(after, before,"Record Match not Found");	
		addExceptionToReport(
				"Record Match Not Found for " + loanType + " having Branch-" + branchName + " Selected Barchart Month-"
						+ selectedBarchartMonth + " Selected Bar Chart Year-" + selectedBarChartYear,
				before, after);
	}

	public void verifySlapDashboardGrid(Map<String, String> data)
			throws TwfException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException,
			SQLException, ParseException, AWTException, NoSuchFieldException, SecurityException, IllegalArgumentException {

		List<WebElement> elems_LoanOfficer = slap.getElementsLoanOfficer();
		comPareLoanCountForIndividualLo(elems_LoanOfficer, data);

	}

	@SuppressWarnings("static-access")
	private void comPareLoanCountForIndividualLo(List<WebElement> elems_LoanOfficer, Map<String, String> data)
			throws TwfException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException,
			SQLException, ParseException, AWTException, NoSuchFieldException, SecurityException, IllegalArgumentException {
		String startDate = null;
		String endDate = null;
		String LoanOfficerName;
		String columnName;
		String slapDashboardQuery_LoanCount;
		int loanCount_ui, loanCount_db = 0;
		String loanType;
		SoftAssert asserts = new SoftAssert();
		System.out.println("Total Number Of Loan Officer Displayed in the selected Branch=" + elems_LoanOfficer.size());

		Connection con = dbUtils.init();

		int i = 1, j, k = 10;
		
		String selectedMonth = data.get("Month");
		
		String InputBranchNames = data.get("Branches");
		
		Boolean flagBranchContainNumber = objUtils.stringContainsNumber(InputBranchNames);
		Boolean flagSubBranch=InputBranchNames.contains("~");
		
		String selectedMonth_Year = getSelectedMonthYear(selectedMonth);
		
		startDate = objUtils.getStartDate(selectedMonth, selectedMonth_Year);
		
		endDate = objUtils.getEndDate(selectedMonth, selectedMonth_Year);
		/*
		 * startDate =
		 * (objUtils.getCurrentMonth()+1)+"/"+"01/"+objUtils.getCurrentYear(); endDate =
		 * (objUtils.getCurrentMonth() +1)+"/" + objUtils.getLastDayOfCurrentMonth() +
		 * "/" + objUtils.getCurrentYear();
		 */

		String branchNumbers = slap.getBranchNumbers(InputBranchNames);
		System.out.println(branchNumbers);
		String strLoanTypeCount;
		
		System.out.println("Total Loan Officer=" + elems_LoanOfficer.size());
		
		WebElement previousLoanOfficer = null;
		Boolean flagMatchingloanCount = false;
		
		String uiColumnNameUnderProcess;
		
		for (WebElement LoanOfficer : elems_LoanOfficer) {
			System.out.println("i=" + i);
			if (i == 49)
				System.out.println("wait");
			if (previousLoanOfficer != null)
				System.out.println("Previous Loan Officer=" + previousLoanOfficer.getText());

			LoanOfficerName = slap.getLoanOfficerName(LoanOfficer, previousLoanOfficer);
			System.out.println("Processing Started For Loan Officer: " + LoanOfficerName);

			if (LoanOfficerName.equals("Rickie Dee Martin"))
				System.out.println("Louis Rios");
			String branchNumber = slap.getBranchNumberOfLoanOfficer(LoanOfficer, LoanOfficerName, previousLoanOfficer,
					flagBranchContainNumber);
			if (branchNumber == null) {
				i++;
				continue;
			}

			for (j = 1; j <=k; j++) {
				
				if(j==5)
					System.out.println("Funded Target BPS column identified");
				
				columnName = slap.getColumnName(j)[0]; //Here columnName refers to database column name
				uiColumnNameUnderProcess=slap.getColumnName(j)[1]; //Here columnName refers to UI column name
				System.out.println("Processing Started for Column="+j);
				System.out.println("Processing Started for Column="+uiColumnNameUnderProcess);
				
				
				slap.moveToCurrentColumnn(uiColumnNameUnderProcess,j); //Move the focus to Current column under processing
				loanType = slap.getLoanTypeFromSlapDashboardGrid(columnName);
				

				if (flagBranchContainNumber && !flagSubBranch)
					strLoanTypeCount = "((//table[@id='div-grid']//tr[contains(@class,'ui-igtreegrid-rowlevel1')])[LoNumber]/td/div)[LoanType]";
				else
					strLoanTypeCount = "(//table[@id='div-grid']//tr[contains(@class,'ui-igtreegrid-rowlevel2')][LoNumber]//child::div[text()=.])[LoanType]";
				
				strLoanTypeCount = strLoanTypeCount.replace("LoNumber", String.valueOf(i)).replace("LoanType",
						String.valueOf(j));
				WebElement elemAttributeCount=objUtils.getObject(strLoanTypeCount);
				loanCount_ui = Integer.parseInt(objUtils.getTextFromWebElement(elemAttributeCount));

				System.out.println(branchNumber);

				if (branchNumber.contains(",")) {
					System.out.println("loan Officer present in multiple branch");
					String arrBranchNumber[] = branchNumber.split(",");
					for (int bnoCount = 0; bnoCount < arrBranchNumber.length; bnoCount++) {
						slapDashboardQuery_LoanCount = slap.getQueryForLoanOfficer(LoanOfficerName, columnName,
								startDate, endDate, arrBranchNumber[bnoCount],uiColumnNameUnderProcess);
						loanCount_db = slap.getLoanCount_SlapDashboard(con, slapDashboardQuery_LoanCount);
						if (loanCount_ui == loanCount_db) {
							flagMatchingloanCount = true;
							break;
						}

					}
					if (!flagMatchingloanCount) {
						System.out.println("Count Not Matching Ui=" + loanCount_ui + " Db Value=" + loanCount_db);
						SoftAssert.assertUnEqual(String.valueOf(loanCount_db), String.valueOf(loanCount_ui),
								"\nSlap Grid value is not matching against the Fcc Database for Branch: "
										+ branchNumber + " Loan Officer=" + LoanOfficerName + " Slap Column: "
										+ uiColumnNameUnderProcess + " Month=" + selectedMonth + "\n\n");
					} else {
						System.out.println("Count Matching Ui=" + loanCount_ui + " Db Value=" + loanCount_db);
					}

				} else {
					slapDashboardQuery_LoanCount = slap.getQueryForLoanOfficer(LoanOfficerName, columnName, startDate,
							endDate, branchNumber,uiColumnNameUnderProcess);
					loanCount_db = slap.getLoanCount_SlapDashboard(con, slapDashboardQuery_LoanCount);
					if (loanCount_ui != loanCount_db) {
	//		addExceptionToReport("Loan Count Of Slap Grid is not matching against the Encompass Database for Branch: "+data.get("Branches")+" Loan Officer="+LoanOfficerName+" Loan Type: "+loanType+" Month="+selectedMonth, String.valueOf(loanCount_db), String.valueOf(loanCount_ui));
						System.out.println("Count Not Matching for "+" Loan Type: "+ loanType + " Ui=" + loanCount_ui + " Db Value=" + loanCount_db);
						System.out.println(slapDashboardQuery_LoanCount);
						/*SoftAssert.assertUnEqual(String.valueOf(loanCount_db), String.valueOf(loanCount_ui),
								"\nLoan Count is not matching for Branch: "
										+ data.get("Branches") + " Loan Officer=" + LoanOfficerName + " Loan Type: "
										+ loanType + " Month=" + selectedMonth + "\n\n");*/
						SoftAssert.assertUnEqual(String.valueOf(loanCount_db), String.valueOf(loanCount_ui),
								"\nSlap Grid value is not matching against the Fcc Database for Branch: "
										+ branchNumber + " Loan Officer=" + LoanOfficerName + " Slap Column: "
										+ uiColumnNameUnderProcess + " Month=" + selectedMonth + "\n\n");
					} else {
						System.out.println("Count Matching Ui=" + loanCount_ui + " Db Value=" + loanCount_db);
					}
				}			
			}
			previousLoanOfficer = LoanOfficer;
			i++;
		}
		con.close();
		SoftAssert.assertAll();
	}
	
	
/* cus_verifyDrilldownPageGrandTotal 
 * Purpose-It will verify the drill down page Grand total row in Slap bar chart.
 */
	public void verifyDrilldownPageGrandTotal(Map<String,String> data) throws ParseException, TwfException, BiffException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, SecurityException, IllegalArgumentException, SQLException
	{
		int datediff = 5;
		CommonUtils cobj = new CommonUtils();
		boolean flag=true;
		String xpathBarType = "(//*[name()='text']//*[text()='BarType'])[2]//..//preceding-sibling::*[@class='highcharts-series-group']//*[name()='g'][3]//child::*";
		
		String InputBranchNames=data.get("Branches");
		String branchNumbers = slap.getBranchNumbers(InputBranchNames);
		if((branchNumbers.chars().filter(ch->ch=='\'').count())>2)
			branchNumbers="'"+branchNumbers.replace("'","")+"'";
			
		
		String loanType = data.get("LoanType");
		String selectedMonth = data.get("Month");
		String selectedMonth_Year = getSelectedMonthYear(selectedMonth);
		String selectedBarchartMonth;
		String selectedBarchartYear;
		xpathBarType = xpathBarType.replace("BarType", loanType);

		List<WebElement> element_bars = cobj.CreateObjectList(xpathBarType);
		System.out.println("Number of Bars "+element_bars.size() );
		
		for (WebElement bar : element_bars) 
		{
			cobj.clickObjectByActions(bar);
			waitForElement(getElementByUsing("btnClose_CreditPulled"), 10);			
			
			selectedBarchartMonth = cobj.backMonth(selectedMonth, datediff--);
			
			if (Integer.parseInt(selectedMonth_Year) < cobj.getCurrentYear())
				selectedBarchartYear = selectedMonth_Year;
			else
				selectedBarchartYear = getSelectedMonthYear(selectedBarchartMonth);
			flag=slap.verifyGrandTotalRow(loanType, data.get("Branches"), selectedBarchartMonth,selectedBarchartYear,branchNumbers);		
			
			getElementByUsing("btnClose_CreditPulled").click();//It will close the drill down page
		}
		SoftAssert.assertAll();
		
	}

/*************************End of cus_verifyDrilldownPageGrandTotal   ************************/
	public void checkPage() {
	}
}// end Of class
