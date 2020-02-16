package com.fcc.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.tavant.base.DriverFactory;
import com.tavant.base.WebPage;
import com.tavant.utils.TwfException;

import jxl.read.biff.BiffException;

public class FccUtility extends WebPage {
	static WebDriver driver;
	DataBaseUtils objDB;
	CommonUtils objUtils=new CommonUtils();
	int noOfWorkingDaysElapsed=0;	
	String getXptahTargetLoan(String barChartType,String strStackedBarchartTarget) throws NumberFormatException, TwfException
	{
		switch(barChartType)
		{
		case "Credit Pulled":			
			strStackedBarchartTarget=strStackedBarchartTarget.replace("BarType","1");			
			break;
		case "Locks":
			
			strStackedBarchartTarget=strStackedBarchartTarget.replace("BarType","2");
			break;
		case "LE Application":
			
			strStackedBarchartTarget=strStackedBarchartTarget.replace("BarType","3");
			break;
		case "Funded":
			
			strStackedBarchartTarget=strStackedBarchartTarget.replace("BarType","4");
			break;
		}
		return strStackedBarchartTarget;
	}
	int getTargetLoan(String barChartType,String strStackedBarchartTarget) throws NumberFormatException, TwfException
	{
		int targetLoan_count_forRemainingMonth;		
		strStackedBarchartTarget=this.getXptahTargetLoan(barChartType,strStackedBarchartTarget);
		if(!objUtils.isElementExist(strStackedBarchartTarget,2))
			return 0;
		else
		{
		WebElement ele=objUtils.getObject(strStackedBarchartTarget);
		targetLoan_count_forRemainingMonth=Integer.parseInt(ele.getText());
		return targetLoan_count_forRemainingMonth;
		}		
	}
	int getRemainingWorkingDayDB(String loanType) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException
	{
		int noOfHolidayElapsed;
		int remainigHolidays,remainingWorkingDays=0;		
		LocalDate today=LocalDate.now();
		String startDate=String.valueOf(today.withDayOfMonth(1));
		String endDate=String.valueOf(today.withDayOfMonth(today.lengthOfMonth()));		
		Calendar cal=Calendar.getInstance();
		int currentDateNumber=cal.get(Calendar.DAY_OF_MONTH);
		String currentDate_previous=String.valueOf(today.withDayOfMonth(currentDateNumber-1));
		String currentDate=String.valueOf(today.withDayOfMonth(currentDateNumber));
        System.out.println("current date="+cal.get(Calendar.DAY_OF_MONTH));
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
          System.out.println("LastDate of Current Month: " + daysInMonth);
         noOfHolidayElapsed=noOfHoliday(startDate,currentDate_previous);
		noOfWorkingDaysElapsed=(currentDateNumber-1)-noOfHolidayElapsed;
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date dateobj = new Date();
		String strDateToday=df.format(dateobj);
		boolean flagHoliday=isTheDateIsSupremeHoliday(strDateToday);
		if(flagHoliday)
			noOfWorkingDaysElapsed=noOfWorkingDaysElapsed-1;
		remainigHolidays=noOfHoliday(currentDate,endDate);
		remainingWorkingDays=((daysInMonth-currentDateNumber)+1)-remainigHolidays;		
		return remainingWorkingDays;
	}
	int getRemainingWorkingDays_CurrentMonth()
	{
		Calendar cal=Calendar.getInstance();
        int currentDate=cal.get(Calendar.DAY_OF_MONTH);
        System.out.println("current date="+cal.get(Calendar.DAY_OF_MONTH));
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
          System.out.println("LastDate of Current Month: " + daysInMonth);
          
          Calendar now = Calendar.getInstance();
            System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1) + "-"
                + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));

             int daysToIncrement =daysInMonth-currentDate+1;
             int i=1;
             int remainingWorkingDays=0;
            do
            {
            System.out.println("Date after increment: " + cal.getTime());
            if(cal.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY)
            remainingWorkingDays++;
            cal.add(Calendar.DATE, 1);            
            i++;
            }while(i<=daysToIncrement);
        System.out.println("Remaining Working Days are="+remainingWorkingDays);
		return remainingWorkingDays;
	}
	int noOfHoliday(String startDate,String endDate) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException
	{
		/*Stardate =mm/dd/yyyy
		endDate=mm/dd/yyyy*/
		int holidays_count = 0;
		objDB=new DataBaseUtils();		
		Connection con=objDB.init();
		String sqlLeaves_currentMonth="select count(*) as NoOfDays from HolidayCalendar where date between '"+startDate+"' and '"+endDate+"' and Holiday is not null";
		System.out.println(sqlLeaves_currentMonth);
		if(con!=null)
		{
			Statement st=con.createStatement();
			ResultSet rs=st.executeQuery(sqlLeaves_currentMonth);
			rs.next();
			holidays_count= rs.getInt(1);
		}				
		System.out.println("Number of Holiday Present between "+startDate+" and "+endDate+"="+holidays_count);
		con.close();
		return holidays_count;
		
	}
	
	boolean isTheDateIsSupremeHoliday(String strDate) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException
	{
		/*StrDate =mm/dd/yyyy
		 */
		
		boolean flagHoliday=false;
		int holidays_count = 0;
		objDB=new DataBaseUtils();		
		Connection con=objDB.init();
		String sqlLeaves_currentMonth="select count(*) as NoOfDays from HolidayCalendar where date between '"+strDate+"' and '"+strDate+"' and Holiday is not null";
		System.out.println(sqlLeaves_currentMonth);
		if(con!=null)
		{
			Statement st=con.createStatement();
			ResultSet rs=st.executeQuery(sqlLeaves_currentMonth);
			rs.next();
			holidays_count= rs.getInt(1);
		}			
		if(holidays_count==1) {
			System.out.println(strDate+" Is a Holiday");
			flagHoliday=true;}
		con.close();
		return flagHoliday;
		
	}
	
	boolean verifyTargetLoanForRemainingDays(int completedLoanCount,int actualTargetLoanCount,int remainingWorkingDays,String loanType) throws TwfException
	{
		boolean flag=true;
		float targetForEachDay;
		int expectedTargetForRemainingDays;
		System.out.println(" ****** completedLoanCount  :"+completedLoanCount);
		System.out.println(" ****** noOfWorkingDaysElapsed  :"+noOfWorkingDaysElapsed);
		System.out.println(" ****** remainingWorkingDays  :"+remainingWorkingDays);
		targetForEachDay=(float)completedLoanCount/noOfWorkingDaysElapsed;
		System.out.println(" ****** targetForEachDay  :"+targetForEachDay);
		expectedTargetForRemainingDays=Math.round(targetForEachDay*remainingWorkingDays);
		System.out.println(" ****** expectedTargetForRemainingDays  :"+expectedTargetForRemainingDays);
		SLAP.calculatedRunRate=expectedTargetForRemainingDays;
		if(expectedTargetForRemainingDays==actualTargetLoanCount)			
			System.out.println("Ui Projected score:"+actualTargetLoanCount+" is Matched for "+loanType+" with the expected Calculated Value: "+expectedTargetForRemainingDays);
		else {
			System.out.println("Ui Projected score:"+actualTargetLoanCount+" is not matching for "+loanType+" with the expected Calculated Value: "+expectedTargetForRemainingDays);
			//addExceptionToReport("Loan count not matching for "+loanType, String.valueOf(actualTargetLoanCount), String.valueOf(expectedTargetForRemainingDays));

			flag=false;}		
		return flag;
	}	
	@Override	
	public void checkPage() {
		// TODO Auto-generated method stub
		
	}

}
