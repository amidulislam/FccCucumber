package com.fcc.test;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.testng.Assert;

import com.tavant.base.DriverFactory;
import com.tavant.base.WebPage;
import com.tavant.kwutils.CustomStep;
import com.tavant.kwutils.KWVariables;
import com.tavant.kwutils.PageObject;
import com.tavant.utils.TwfException;

public class SlapCustomMethods extends CustomStep{
	WebDriver driver;
	Statement st;
	ResultSet rs;
	CommonUtils objUtils=new CommonUtils();
	FccQuery objFccQuery=new FccQuery();
	DataBaseUtils dbUtils=new DataBaseUtils();
	SLAP objSlap;
public String getLoanTypeForSqlQuery(String strLoanType)
{	
	switch(strLoanType)
	{
	case "LE Application":
		strLoanType="ApplicationDate";
		break;
	case "Credit Pulled":
		strLoanType="Document_DateOrdered_CREDITREPORT";
		break;
	case "Locks":
		strLoanType="LockDate";
		break;
	case "Funded":
		strLoanType="FundedDate";
		break;
	}
return strLoanType;	
}

@Override
	public void checkPage() {
		// TODO Auto-generated method stub
		
	}
public List<WebElement> getElementsLoanOfficer() throws TwfException
{
	String strListLoanOfficer="//div[@class='clText LoanOfficer'][text()=.]";
	return objUtils.CreateObjectList(strListLoanOfficer);
}
public String getLoanOfficerName(WebElement element, WebElement previousLoanOfficer) throws TwfException, AWTException 
{
	
		if(!element.isDisplayed())
		{
			
			Point coordinates=previousLoanOfficer.getLocation();
			Robot robot=new Robot();
			robot.mouseMove(coordinates.getX(), coordinates.getY()+120);
			objUtils.scrollToAnElement(element);			
		}
			
		return element.getText().trim(); 
}
public void scrollToEndOfGrid(WebElement element, WebElement previousLoanOfficer) throws TwfException, AWTException
{
	if(!element.isDisplayed())
	{
		
		Point coordinates=previousLoanOfficer.getLocation();
		Robot robot=new Robot();
		robot.mouseMove(coordinates.getX(), coordinates.getY()+120);
		objUtils.scrollToAnElement(element);
		objUtils.scrollToBottom();
	}
}

public void scrollToLoanOfficer(WebElement element) throws TwfException, AWTException
{		
		String strGridFirstCell="(//div[@class='clText bold regionndbranch'])[1]";
		Point coordinates=objUtils.getObject(strGridFirstCell).getLocation();
		Robot robot=new Robot();
		robot.mouseMove(coordinates.getX(), coordinates.getY()+120);
		objUtils.scrollToAnElement(element);		

}
String getQueryForLoanOfficer(String loanOfficerName, String columnName, String startDate, String endDate, String branchNumbers, String uiColumnNameUnderProcess) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException								    
{
	String fccQueryFieldName=getFccQueryFieldName(uiColumnNameUnderProcess);
	//String slapGridQuery=objFccQuery.getSlap_Dashboard_Grid_LoanCount_FccDB();
	String slapGridQuery=objFccQuery.getQueryByAttributeName(fccQueryFieldName);
	slapGridQuery=slapGridQuery.replace("#ColumnName", columnName);
	slapGridQuery=slapGridQuery.replace("#StartDate", startDate);
	slapGridQuery=slapGridQuery.replace("#EndDate", endDate);
	slapGridQuery=slapGridQuery.replace("#branchList", branchNumbers);
	slapGridQuery=slapGridQuery.replace("#LoName", loanOfficerName);
	//System.out.println(slapGridQuery);
	return slapGridQuery;
	
}
public String getBranchNumbers(String branchName) throws IOException
{
	if(branchName.contains("~"))
		branchName=branchName.split("~")[1];
	branchName = branchName.replace(" ", "_");
	return objUtils.getBranchNumbers("regionList.properties", branchName);
}

public String[] getColumnName(int j) {
	String columnName[]=new String[2];
/*	[CreditPulled]-->D01.[Document_DateOrdered_CREDITREPORT]
	[LockDate]-->D01._761
	[FundedDate]-->D02._MS_FUN               
	[ApplicationDate]--> D02._3142
*/
		switch (j) {
		case 1:
			// columnName="D01.Document_DateOrdered_CREDITREPORT";
			columnName[0] = "Document_DateOrdered_CREDITREPORT";
			columnName[1] = "Credit";
			break;
		case 2:
			// columnName="D02._3142";
			columnName[0] = "ApplicationDate";
			columnName[1] = "App";
			break;
		case 3:
			// columnName="D01._761";
			columnName[0] = "LockDate";
			columnName[1] = "Locks";
			break;
		case 4:
			// columnName="D02._MS_FUN";
			columnName[0] = "FundedDate";
			columnName[1] = "Funded";
			break;
		case 5:
			columnName[0] = "LockDate";
			columnName[1] = "Lock Vol";
			break;
		case 6:
			columnName[0] = "";
			columnName[1] = "Lock Target BPS";
			break;
		case 7:
			columnName[0] = "";
			columnName[1] = "Lock BPS";
			break;
		case 8:
			columnName[0] = "";
			columnName[1] = "Funded Vol";
			break;
		case 9:
			columnName[0] = "";
			columnName[1] = "Funded Target BPS";
			break;
		case 10:
			columnName[0] = "";
			columnName[1] = "Funded BPS";
			break;
		case 11:
			columnName[0] = "";
			columnName[1] = "Locked";
			break;
		case 12:
			columnName[0] = "";
			columnName[1] = "Prior to UW";
			break;
		case 13:
			columnName[0] = "";
			columnName[1] = "In UW";
			break;
		case 14:
			columnName[0] = "";
			columnName[1] = "CTC Docs";
			break;
		case 15:
			columnName[0] = "";
			columnName[1] = "All In";
			break;
		case 16:
			columnName[0] = "";
			columnName[1] = "Cycle Time";
			break;
		case 17:
			columnName[0] = "";
			columnName[1] = "Lock";
			break;
		case 18:
			columnName[0] = "";
			columnName[1] = "Revenue Funded";		
			break;
		case 19:
			columnName[0] = "";
			columnName[1] = "Revenue Blend";
		}
		return columnName;
	}

public int getLoanCount_SlapDashboard(Connection con, String slapDashboardQuery_LoanCount) throws SQLException {
	int count=0;
	String strDBValue = null;
	try {
	if (con != null) {
			st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = st.executeQuery(slapDashboardQuery_LoanCount);
			System.out.println("Result retrieved successfully");
		}
		
		if(rs.next())   
		{
			strDBValue=rs.getString(1);			
			if(strDBValue.startsWith("0E") || strDBValue==null||strDBValue.equals(""))				
				strDBValue="0";
			else if(strDBValue.contains("."))
				strDBValue=removeDotFromString(strDBValue);
		}
	}
	catch(NullPointerException e)
	{
		strDBValue="0";
	}
	catch(Exception e) {
		e.printStackTrace();
		System.out.println("Exception occur while executing or parsing the Query resut\n\n");
		System.out.println(slapDashboardQuery_LoanCount);
	}
	count=Integer.parseInt(strDBValue);
	return count;	
}

private String removeDotFromString(String strDBValue) {
	String val;
	val=String.valueOf(((int)Float.parseFloat(strDBValue)));
	return val;
}

public String getLoanTypeFromSlapDashboardGrid(String columnName) 
{
	String loanType="";
	switch(columnName)
	{	
	case "D01.Document_DateOrdered_CREDITREPORT":
	case "Document_DateOrdered_CREDITREPORT":
		loanType="Credit Pulled";
	break;
	case "D02._3142":
	case "ApplicationDate":
		loanType="LE Application";
	break;
	
	case "D01._761":
	case "LockDate":
		loanType="Locked Loan";
	break;
	case "D02._MS_FUN":
	case "FundedDate":
		loanType="Funded";
	break;
	default:
		loanType="";
	}
	return loanType;
}

public String getBranchNumberOfLoanOfficer(WebElement loanOfficer, String loanOfficerName,WebElement previousLoanOfficer, Boolean flagBranchContainNumber) throws TwfException 
{
	int noOfElement=1;
	WebElement branchNumber;
	List<WebElement> listBranchNumbers;
	String strBranchNumber=null,strBno;
	//String strLo="(//div[text()='"+loanOfficerName+"']/../../preceding-sibling::tr[contains(@class,'ui-igtreegrid-rowlevel1')])[position()=1]/child::td[@aria-describedby='div-grid_BranchName']/div";
	String strLo=null;
	if(!flagBranchContainNumber)
		strLo="//div[text()='"+loanOfficerName+"']/../../preceding-sibling::tr[contains(@class,'ui-igtreegrid-rowlevel1')][1]//child::td[@aria-describedby='div-grid_BranchName']/div";
	else
	{
		strLo="(//div[text()='"+loanOfficerName+"']/../../preceding-sibling::tr[contains(@class,'ui-igtreegrid-rowlevel0')])[last()]";		
	}	
	noOfElement=objUtils.getElementCount(strLo);	
	try
	{
		if(noOfElement>1)
		{
		System.out.println(loanOfficerName+" Present in multiple branch");
		listBranchNumbers=objUtils.CreateObjectList(strLo);
		strBranchNumber=getStrBranchNumbers(listBranchNumbers,previousLoanOfficer);
		}
		else
		{
			if(noOfElement!=0)
			{			
				System.out.println(loanOfficerName+" Present in single branch");
				branchNumber=objUtils.getObject(strLo);			
					if(!branchNumber.isDisplayed())
					{
						Point coordinates=previousLoanOfficer.getLocation();
						Robot robot=new Robot();
						robot.mouseMove(coordinates.getX(), coordinates.getY()+120);
						objUtils.scrollToAnElement(branchNumber);			
					}
				strBno=branchNumber.getText();
			if(!objUtils.stringContainsNumber(strBno))
				strBno=getCorrectBranchName(loanOfficerName);
		strBranchNumber="'"+getStrBranchNumbers(strBno)+"'";
			}
			else 
			{
				strBranchNumber="";
				System.out.println("Loan Officer Name is incorrect"+loanOfficerName);
			}
		}
	}
	catch(Exception e)
	{
		System.out.println("Loan Officer Name is incorrect"+loanOfficerName);
	}
	
	return strBranchNumber;
}

private String getCorrectBranchName(String loanOfficerName) throws TwfException {
	String branchName;
	String strLo="//div[text()='"+loanOfficerName+"']/../../preceding-sibling::tr[contains(@class,'ui-igtreegrid-rowlevel1')][1]//child::td[@aria-describedby='div-grid_BranchName']/div";
	branchName=objUtils.getObject(strLo).getText().trim();	
	return branchName;
}

private String getStrBranchNumbers(List<WebElement> listBranchNumbers,WebElement previousLoanOfficer) throws AWTException, TwfException {
	String strBranchNumbers="",numBranch,temp;
	int elementCount=listBranchNumbers.size();
	int count=1;
	for(WebElement ele:listBranchNumbers)
	{
		/*if(!ele.isDisplayed() && count!=1)
			continue;*/
		/*if(!ele.isDisplayed())
		{
			objUtils.scrollToTopIfNotAtTop();
		}*/
		if(!ele.isDisplayed())
		{
		Point coordinates=previousLoanOfficer.getLocation();
		Robot robot=new Robot();
		robot.mouseMove(coordinates.getX(), coordinates.getY()+120);
		objUtils.scrollToAnElement(ele);
		}
		temp=ele.getText();
		numBranch=temp.split("-")[1].trim();
		strBranchNumbers=strBranchNumbers+"'"+numBranch+"'"+",";
		count++;
	}
	return strBranchNumbers.substring(0, strBranchNumbers.length()-1);
}
private String getStrBranchNumbers(String BranchName) {	
		String numBranch=BranchName.split("-")[1].trim();
		return numBranch;
		
	
}

public void moveToCurrentColumnn(String uiColumnNameUnderProcess, int j) throws TwfException {
	String strXpathColumn="";
	WebElement elementColumn;
	if(uiColumnNameUnderProcess.equals("Revenue Funded"))
		uiColumnNameUnderProcess="Funded";
	if(j!=18)
		strXpathColumn="(//div[text()='"+uiColumnNameUnderProcess+"'])[1]";
	else
		strXpathColumn="(//div[text()='"+uiColumnNameUnderProcess+"'])[2]";
	elementColumn=objUtils.getObject(strXpathColumn);
	if(!elementColumn.isDisplayed())
		objUtils.scrollToAnElement(elementColumn);
}

public String getFccQueryFieldName(String uiColumnNameUnderProcess) 
{
	
	return uiColumnNameUnderProcess.replace(" ","_")+"_FccDB";
}


public int getProjetedLoanCountEntireMonth(String branch, String loanOfficerName, String branchNumber, String loanType) throws IOException, ParseException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
	/* 
	 * This method used to return projected score for the remaining days of Funded loan
	 */
	if(branchNumber.equals(""))
		branchNumber=getBranchNumbers(branch);
	int currentMonth=objUtils.getCurrentMonth()+1;
	String currentMonthName=objUtils.getEnglishMonthName(currentMonth);
	
	objSlap=new SLAP();
	String year=objSlap.getSelectedMonthYear(currentMonthName);
	ArrayList<Integer> locks=getLastFiveMonthLocksValue(currentMonth,branchNumber,loanOfficerName,loanType);
	ArrayList<Integer> fundings=getLastFiveMonthFundingsValue(currentMonth,branchNumber,loanOfficerName);	
	ArrayList<Float> fundLockRatio1=getFundLockRatio(locks,fundings);
	ArrayList<Float> fundLockRatio2=getFundLockRatio2(fundings);
	System.out.println(fundLockRatio2);
	ArrayList<Float> fundLockRatio3=getFundLockRatio3(fundLockRatio1,fundLockRatio2);
	float sumOfFundLockRatio3=objUtils.getSumOfListValues(fundLockRatio3,(a,b)->a+b);
	int locksImmediatePreviousMonth=getLocksImmediatePreviousMonth(currentMonth,branchNumber,loanOfficerName);
	int predictedMonthFunding=getPredictedMonthFunding(locksImmediatePreviousMonth,sumOfFundLockRatio3);
	return predictedMonthFunding;
}

private int getPredictedMonthFunding(int locksImmediatePreviousMonth, float sumOfFundLockRatio3) {
	
	return Math.round(locksImmediatePreviousMonth*sumOfFundLockRatio3);
}

private int getLocksImmediatePreviousMonth(int monthNumber, String branhNumbers, String loanOfficerName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, ParseException {
	// It will return immediate previous month lock value
	
	int prevMonthNumber;
	String prevMonthName;
	String prevMonthYear;
	String startDate_prevMonth,endDate_prevMonth;
	String query_Lock,lockValue;
	Connection con=dbUtils.init();
	objSlap=new SLAP();
		if(loanOfficerName.equals("")) {
		query_Lock=objFccQuery.getQueryByAttributeName("Locks_FccDB_BranchLevel");}
		else
		{
			query_Lock=objFccQuery.getQueryByAttributeName("Locks_FccDB_LoLevel");
			query_Lock=query_Lock.replace("#LoName", loanOfficerName);
		}
		
		prevMonthNumber=monthNumber-1;
		if(prevMonthNumber==0)
			prevMonthNumber=12;
		
		prevMonthName=objUtils.getEnglishMonthName(prevMonthNumber);
		
		prevMonthYear=objSlap.getSelectedMonthYear(prevMonthName);
		
		startDate_prevMonth=prevMonthNumber+"/"+"01"+"/"+prevMonthYear;
		
		endDate_prevMonth=prevMonthNumber+"/"+objUtils.getLatDayOfGivenMonthNumber(String.valueOf(prevMonthNumber),prevMonthYear)+"/"+prevMonthYear;
		
		query_Lock=query_Lock.replace("#StartDate", startDate_prevMonth).replace("#EndDate", endDate_prevMonth).replace("#branchList", branhNumbers);
		
		lockValue=dbUtils.getSingleDBValue(con, query_Lock);		
		
	
	con.close();
	
	
	return Integer.parseInt(lockValue);
}

private ArrayList<Float> getFundLockRatio3(ArrayList<Float> fundLockRatio1, ArrayList<Float> fundLockRatio2) {
	Iterator <Float>it=fundLockRatio2.iterator();
	ArrayList<Float>temp=(ArrayList<Float>)fundLockRatio1.stream().map(cost->cost*it.next()).collect(Collectors.toList());
	return temp;
}

private ArrayList<Float> getFundLockRatio2(ArrayList<Integer> fundings) {
	ArrayList<Float> temp=new <Float>ArrayList();
	int sumOfFundings=fundings.stream().mapToInt(i->i.intValue()).sum();
	for(int x:fundings)
		temp.add((float)x/sumOfFundings);
	return temp;
}

private ArrayList<Float> getFundLockRatio(ArrayList<Integer> locks, ArrayList<Integer> fundings) {
	ArrayList<Float> temp;
	int index=0;
	Iterator<Integer> it=fundings.iterator();
	temp=(ArrayList<Float>) locks.stream().map(cost->it.next()/(float)cost).collect(Collectors.toList());
	
	for(Float t:temp)
	{		
		if(t.isNaN() || t.isInfinite())
			temp.set(index, 0f);
	index++;
	}
	return temp;	
}

	private ArrayList<Integer> getLastFiveMonthLocksValue(int monthNumber, String branhNumbers, String loanOfficerName,
			String loanType)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException,
			ParseException, InstantiationException, ClassNotFoundException, SQLException, IOException {
		// It will give last five months locks value excluding the immediate previous
		// month.
		// Locks_FccDB_BranchLevel
		ArrayList<Integer> list = new ArrayList<Integer>();
		int prevMonthNumber;
		String prevMonthName;
		String prevMonthYear;
		String startDate_prevMonth, endDate_prevMonth;
		String query_Lock, lockValue;
		Connection con = dbUtils.init();
		objSlap = new SLAP();
		prevMonthNumber = monthNumber - 1;
		if (loanType.equalsIgnoreCase("Funded")) {
			if (prevMonthNumber == 0) {
				prevMonthNumber = 12;
			}
			prevMonthNumber = prevMonthNumber - 1;
		}
		for (int i = 1; i <= 5; i++) {
		
			if (loanOfficerName.equals("")) {
				query_Lock = objFccQuery.getQueryByAttributeName("Locks_FccDB_BranchLevel");
			} else {
				query_Lock = objFccQuery.getQueryByAttributeName("Locks_FccDB_LoLevel");
				query_Lock = query_Lock.replace("#LoName", loanOfficerName);
			}

			if (prevMonthNumber == 0) {
				prevMonthNumber = 12;
			}
			prevMonthName = objUtils.getEnglishMonthName(prevMonthNumber);
			prevMonthYear = objSlap.getSelectedMonthYear(prevMonthName);
			startDate_prevMonth = prevMonthYear + "-" + prevMonthNumber + "-01";
			endDate_prevMonth = prevMonthYear + "-" + prevMonthNumber + "-"
					+ objUtils.getLatDayOfGivenMonthNumber(String.valueOf(prevMonthNumber), prevMonthYear);
			query_Lock = query_Lock.replace("#StartDate", startDate_prevMonth).replace("#EndDate", endDate_prevMonth)
					.replace("#branchList", branhNumbers);
			lockValue = dbUtils.getSingleDBValue(con, query_Lock);
			list.add(Integer.parseInt(lockValue));
			System.out.println(query_Lock);
			prevMonthNumber--;
		}
		con.close();

		return list;
	}

private ArrayList<Integer> getLastFiveMonthFundingsValue(int monthNumber, String branhNumbers, String loanOfficerName) throws SQLException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException, IOException, ParseException {
	// It will give last five months Funding value including the immediate previous month.
	//Funded_FccDB_BranchLevel
	ArrayList<Integer> list=new ArrayList<Integer>();
	int prevMonthNumber;
	String prevMonthName;
	String prevMonthYear;
	String startDate_prevMonth,endDate_prevMonth;
	String query_Funded,fundedValue;
	Connection con=dbUtils.init();
	objSlap=new SLAP();
	prevMonthNumber=monthNumber-1;
	for(int i=0;i<5;i++)
	{
		if(loanOfficerName.equals("")) {
		query_Funded=objFccQuery.getQueryByAttributeName("Funded_FccDB_BranchLevel");}
		else {
			query_Funded=objFccQuery.getQueryByAttributeName("Funded_FccDB_LoLevel");
			query_Funded=query_Funded.replace("#LoName", loanOfficerName);
		}
		
		if(prevMonthNumber==0) {
			prevMonthNumber=12;
		}
		prevMonthName=objUtils.getEnglishMonthName(prevMonthNumber);
		prevMonthYear=objSlap.getSelectedMonthYear(prevMonthName);
		startDate_prevMonth=prevMonthNumber+"/"+"01"+"/"+prevMonthYear;
		endDate_prevMonth=prevMonthNumber+"/"+objUtils.getLatDayOfGivenMonthNumber(String.valueOf(prevMonthNumber),prevMonthYear)+"/"+prevMonthYear;
		query_Funded=query_Funded.replace("#StartDate", startDate_prevMonth).replace("#EndDate", endDate_prevMonth).replace("#branchList", branhNumbers);
		fundedValue=dbUtils.getSingleDBValue(con, query_Funded);
		list.add(Integer.parseInt(fundedValue));	
		prevMonthNumber--;
	}
	con.close();
	
	return list;
	
}


public Boolean verifyFundedRunRate(int completed_Loan_Count_UI, int actual_TargetLoan_Count_ForRemainingMonth_UI, int projectedFundedLoanCount_EntireMonth) {
	// Verification of Projected score against the projection shown in Ui stacked bar chart
	Boolean status;
	System.out.println("Completed Funded Loan:"+completed_Loan_Count_UI);
	System.out.println("Total loan count projection for entire month:"+projectedFundedLoanCount_EntireMonth);
	System.out.println("Total loan count projection for remaining month in Ui:"+actual_TargetLoan_Count_ForRemainingMonth_UI);
	System.out.println("Total loan count projection for remaining month expected in Ui:"+(projectedFundedLoanCount_EntireMonth-completed_Loan_Count_UI));
	
	status=projectedFundedLoanCount_EntireMonth-completed_Loan_Count_UI==actual_TargetLoan_Count_ForRemainingMonth_UI?true:false;
	if(status==true)
		System.out.println("Funded Projected Loan Count Matched Ui Value="+actual_TargetLoan_Count_ForRemainingMonth_UI+" and expected="+(projectedFundedLoanCount_EntireMonth-completed_Loan_Count_UI));
	return status;
}

public boolean verifyGrandTotalRow(String loanType, String string, String selectedBarchartMonth,
		String selectedBarchartYear, String branchNumbers) throws TwfException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, SecurityException, IllegalArgumentException, SQLException, IOException, ParseException {
	
	String startDate=objUtils.getStartDate(selectedBarchartMonth, selectedBarchartYear);
	String endDate=objUtils.getEndDate(selectedBarchartMonth, selectedBarchartYear);
	
	List<String> grandTotalList=Arrays.asList("Amount","Gbr","GbrBps","GbrTarget","GbrVariance");
	
	Map<String,Integer> mapGrandTotal=getGrandTotalMapFromUI(grandTotalList);
	Map<String,Integer> mapGrandTotal_DB=getGrandTotalMapFromDB(grandTotalList,startDate,endDate,branchNumbers,loanType);
	Boolean flag=objUtils.compareMap(mapGrandTotal,mapGrandTotal_DB,startDate,endDate,branchNumbers);
	
	return flag;
	
}



private Map<String, Integer> getGrandTotalMapFromDB(List<String> grandTotalList, String startDate, String endDate, String branchNumbers, String loanType) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException, NoSuchFieldException, SecurityException, IllegalArgumentException 
{
	int i=0;
	List<String> ls;
	LinkedHashMap<String,Integer> map=new LinkedHashMap<String,Integer>();
	String colName;
	int varianceValue=0;
	Iterator <String>it_grandTotalList=grandTotalList.iterator();
	
	Connection con=dbUtils.init();
	
	String query="";
	
	if(loanType.equals("Credit Pulled"))
		query=objFccQuery.getQueryByAttributeName("creditPulledTotal");
	else if(loanType.equals("LE Application"))
		query=objFccQuery.getQueryByAttributeName("leApplicationTotal");
	else if(loanType.equals("Locks"))
		query=objFccQuery.getQueryByAttributeName("locksTotal");
	else if(loanType.equals("Funded"))
		query=objFccQuery.getQueryByAttributeName("fundedTotal");
	
	query=query.replace("#StartDate", startDate).replace("#EndDate", endDate).replace("#BranchList", branchNumbers);
	
	int val;
	ls=dbUtils.getResultList(con,query );
	while(it_grandTotalList.hasNext())
	{
		colName=it_grandTotalList.next();		
		if(colName.equals("GbrVariance"))
		{
			varianceValue=map.get("GbrBps")-map.get("GbrTarget");
			map.put(colName,varianceValue);
			continue;
		}
		val=Integer.parseInt(ls.get(i++));
		map.put(colName,val);
	}
	con.close();
	return map;
}

private Map<String, Integer> getGrandTotalMapFromUI(List<String> grandTotalList) throws TwfException {
	String strGrandTotal="//div[text()='Grand Total']//..//following-sibling::td//child::div[2]";
	
	List <WebElement> list=objUtils.CreateObjectList(strGrandTotal);
	LinkedHashMap<String,Integer> map=new LinkedHashMap<String,Integer>();
	
	
	Iterator <String>it_grandTotalList=grandTotalList.iterator();
	
	String text;
	for(WebElement l:list)
	{
		if(!(text=l.getText()).equals(""))
		{
			if(text.contains(","))
				text=text.replace(",", "");
			if(text.contains("("))
			{
				text=text.replace("(", "").replace(")", "");
				text=String.valueOf(Integer.parseInt(text)*-1);
			}
			map.put(it_grandTotalList.next(),Integer.parseInt(text));
		}
			
	}
	return map;
}

	

	

}//end Of Class