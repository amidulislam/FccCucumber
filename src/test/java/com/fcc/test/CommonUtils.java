package com.fcc.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.lang.Class;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.function.BinaryOperator;
import java.util.regex.Pattern;
import com.fcc.test.SoftAssert;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import jxl.read.biff.BiffException;

import com.tavant.base.DriverFactory;
import com.tavant.base.WebPage;
import com.tavant.kwutils.CustomStep;
import com.tavant.kwutils.KWVariables;
import com.tavant.utils.TwfException;

public class CommonUtils extends WebPage {

	static String warrantyNumber;

	static WebDriver driver;

	public void doubleClick(String element) throws TwfException {
		try {
			driver = DriverFactory.getDriver();
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			Actions action = new Actions(driver).doubleClick(getElementByUsing(element));
			action.build().perform();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			addExceptionToReport("Problem in double clicking th element", "double is not working ",
					"Double click should work");
		}
	}

	public static void gotoMainWindow() throws TwfException {
		driver = DriverFactory.getDriver();
		System.out.println(driver);
		driver.switchTo().defaultContent();
	}

	public static void gotoFrame(String frame) throws TwfException {
		driver = DriverFactory.getDriver();
		System.out.println(driver);
		driver.switchTo().frame(frame);
	}

	public void sendDateTo(String str) throws TwfException, BiffException, IOException {
		String[] arrOfStr = str.split("_");
		String element = arrOfStr[0];
		String[] list = arrOfStr[1].toString().split("-");
		String noOfDays = list[1];
		int noOfDaysPastCurrentDate = Integer.parseInt(noOfDays);
		driver = DriverFactory.getDriver();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate ldate = LocalDate.now();
		LocalDate yesterday = ldate.minusDays(noOfDaysPastCurrentDate);
		getElementByUsing(element).clear();
		getElementByUsing(element).sendKeys(dtf.format(yesterday));
	}

	public void scrollToAnElement(String values)
			throws TwfException, BiffException, IOException, InterruptedException, InvalidFormatException {
		driver = DriverFactory.getDriver();
		WebElement element = getElementByUsing(values);
		Thread.sleep(2000);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

	}

	public void scrollToAnElement(WebElement element) throws TwfException {
		driver = DriverFactory.getDriver();
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public String getTextFromWebElement(WebElement element) {
		// String text=element.getText().trim();
		String text = element.getAttribute("innerText").trim();
		if (text.contains(","))
			text = text.replace(",", "");
		return text;
	}

	public void selectFromDropDown(String str) throws BiffException, IOException, TwfException {
		String[] arrOfStr = str.split("_");
		driver = DriverFactory.getDriver();
		WebElement DrDnMenuelement = getElementByUsing(arrOfStr[0]);
		waitForElement(DrDnMenuelement, 10000);
		DrDnMenuelement.click();
		WebElement ListOptionelement = getElementByUsing(arrOfStr[1]);
		waitForElement(ListOptionelement, 10000);
		ListOptionelement.click();
	}

	public void highlightMe(String val) throws InterruptedException, TwfException, BiffException, IOException {

		driver = DriverFactory.getDriver();
		JavascriptExecutor js = (JavascriptExecutor) driver;

		WebElement ele = getElementByUsing(val);

		js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", ele);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {

			System.out.println(e.getMessage());
		}

		js.executeScript("arguments[0].setAttribute('style','border: solid 2px white');", ele);

	}

	public static boolean isClickable(WebElement element) {

		try {
			driver = DriverFactory.getDriver();
			WebDriverWait wait = new WebDriverWait(driver, 5);

			wait.until(ExpectedConditions.elementToBeClickable(element));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void isEnable(String element) throws BiffException, IOException, TwfException {
		boolean status = getElementByUsing(element).isEnabled();
		System.out.println("Element enabled " + status);

	}

	public void pageLoadTimeOut() throws InterruptedException {
		Thread.sleep(3000);
	}

	/*
	 * Common Utility Methods for FCC Author: Syed Amidul Islam Design Date:
	 * 11-Mar-2019
	 */
	public int getElementCount(String strElement) {
		List<WebElement> elem = driver.findElements(By.xpath(strElement));
		return elem.size();
	}

	public void scrollToTopIfNotAtTop() throws TwfException {
		driver = DriverFactory.getDriver();
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		Long yCoOrdinate = (Long) executor.executeScript("return window.pageYOffset;");
		if (yCoOrdinate != 0)
			executor.executeScript("window.scrollTo(0, 0);");
	}

	public void killAllChromeProcess(String str) throws IOException // throws Exception,IOException,TwfException
	{
		String command;
		System.out.println("Hello supreme lending I am going to kill all chrome process");
		command = "taskkill /F /IM chrome.exe /T";
		Runtime.getRuntime().exec(command);
		System.out.println("All chrome process killed");
	}

	Sheet getSheetForRead(String fileName, String sheetName) throws IOException {
		String strfilePath;

		strfilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\" + fileName + ".xls";
		System.out.println(strfilePath);
		File tcFile = new File(strfilePath);
		FileInputStream fis = new FileInputStream(tcFile);
		Workbook wb = new HSSFWorkbook(fis);
		Sheet sheet = wb.getSheet(sheetName);
		return sheet;
	}

	public Workbook getWorkbook(String fileName) throws IOException {
		String strfilePath;

		strfilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\" + fileName + ".xls";
		System.out.println(strfilePath);
		File tcFile = new File(strfilePath);
		FileInputStream fis = new FileInputStream(tcFile);
		Workbook wb = new HSSFWorkbook(fis);
		return wb;
	}

	void writeCellValue(Workbook wb, String strSheetName, String loanID, String testDataID) throws IOException {
		int rowNum;
		Sheet sh = wb.getSheet(strSheetName);
		// sh.getRow(0).createCell(2).setCellValue("2.41.0");
		rowNum = getCurrentRow(sh, testDataID);
		sh.getRow(rowNum).getCell(2).setCellValue(loanID);
		FileOutputStream fout = new FileOutputStream(
				new File(System.getProperty("user.dir") + "\\src\\test\\resources\\Testcases.xls"));
		wb.write(fout);
		fout.close();
	}

	int getCurrentRow(Sheet sh, String testDataID) {
		String dataID;
		int rowCount, colCount, i = 0;
		rowCount = sh.getLastRowNum() + 1;
		HSSFRow row = null;
		colCount = sh.getRow(0).getLastCellNum();
		for (i = 1; i < rowCount; i++) {
			dataID = sh.getRow(i).getCell(2).getStringCellValue();
			if (dataID.equals(testDataID))
				break;
		}
		return i;

	}

	public WebElement getObject(String strXpath) throws TwfException {

		driver = DriverFactory.getDriver();
		return driver.findElement(By.xpath(strXpath));
	}

	public void clickObject(String strXpath) {
		try {
			driver = DriverFactory.getDriver();
			WebElement ele = driver.findElement(By.xpath(strXpath));
			Actions act = new Actions(driver);
			act.click(ele).build().perform();
			// ele.click();
		} catch (Exception e) {
			System.out.println("Element not clickable or incorrect xpath");
			e.printStackTrace();
		}
	}

	public void clickObjectByActions(String strXpath) throws TwfException, InterruptedException {
			driver = DriverFactory.getDriver();
			WebElement ele = driver.findElement(By.xpath(strXpath));
			Actions act = new Actions(driver);
			 WebDriver driver = DriverFactory.getDriver();
	         JavascriptExecutor scroll = (JavascriptExecutor) driver;
	         scroll.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	         
	         act.click(ele).build().perform();
			Thread.sleep(2000);
			
	}

	public void clickObjectByActions(WebElement element) {
		try {
			driver = DriverFactory.getDriver();
			Actions act = new Actions(driver);
			act.click(element).build().perform();
			// ele.click();
		} catch (Exception e) {
			System.out.println("Element not clickable or element is not found");
			e.printStackTrace();
		}
	}

	public void JSClick(WebElement element) throws TwfException {
		driver = DriverFactory.getDriver();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", element);
	}

	public void waitForVisibility(String element_xpath, int amountOfSecond) {
		try {
			driver = DriverFactory.getDriver();
			WebDriverWait wt = new WebDriverWait(driver, amountOfSecond);
			wt.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element_xpath)));

		} catch (TwfException e) {
			System.out.println("Element is not visible");

			// e.printStackTrace();
		}

	}
	

	public void waitUntillCondition(String element)
			throws TwfException, BiffException, IOException, InterruptedException {
		WebDriver driver = DriverFactory.getDriver();
		Thread.currentThread().sleep(1000);
		WebDriverWait wait = new WebDriverWait(driver, 15);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(getElementByUsing(element)));
			Thread.currentThread().sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isElementExist(String element_xpath, int amountOfSecond) throws TwfException {
		Boolean flag = true;
		try {
			driver = DriverFactory.getDriver();
			WebDriverWait wt = new WebDriverWait(driver, amountOfSecond);
			// wt.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element_xpath)));
			wt.until(ExpectedConditions.elementToBeClickable(By.xpath(element_xpath)));

		} catch (TimeoutException e) {
			System.out.println("Element is not visible");
			flag = false;
			// e.printStackTrace();
		}
		return flag;

	}

	public void waitTillClickable(String element_xpath, int amountOfSecond) throws TwfException {

		driver = DriverFactory.getDriver();
		WebDriverWait wt = new WebDriverWait(driver, amountOfSecond);
		wt.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element_xpath)));
		wt.until(ExpectedConditions.elementToBeClickable(By.xpath(element_xpath)));
	}

	public void waitforProperty(WebElement ele, String propertyName, String propertyValue) {
		while (!ele.getAttribute(propertyName).equals(propertyValue)) {
			try {
				Thread.sleep(500);
				System.out.println("Waiting for region dropdown to open");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("List of Regions-Branch successfully populated");
	}

	public void scrollToBottom() throws TwfException {
		driver = DriverFactory.getDriver();
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

	public void scrollToTop() throws TwfException {
		driver = DriverFactory.getDriver();
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0)");
	}

	public void clearChromeCache(String str) throws TwfException, InterruptedException {
		driver = DriverFactory.getDriver();
		Actions actions = new Actions(driver);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.open();");
		String fccParent = driver.getWindowHandle();
		ArrayList<String> newtab = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(newtab.get(1));
		driver.get("chrome://settings/clearBrowserData");
		Thread.sleep(3000);
		actions.sendKeys(Keys.ENTER);
		actions.perform();
		Thread.sleep(12000);
		driver.close();
		driver.switchTo().window(fccParent);

	}

	public void waitTillLoadSymbol() throws TwfException, InterruptedException {
		driver = DriverFactory.getDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		while (driver.findElements(By.xpath("//div[@class='loading-indicator'][2]")).size() > 0) {
			System.out.println("Waiting due to Hand Symbol");
			Thread.sleep(1000);
		}
		System.out.println("Hand Symbol disappeared");
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	public void waitTillLoadSymbolBlast(String str) throws TwfException, InterruptedException {
		driver = DriverFactory.getDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		while (driver.findElements(By.xpath("//div[@id='loading-indicator-div-level-detail']")).size() > 0) {
			System.out.println("Waiting due to Home Symbol...");
			System.out.println("Wait Page is loading...");
			Thread.sleep(1000);
		}
		while (driver.findElements(By.xpath("//div[@id='loading-indicator-undefined']")).size() > 0) {
			System.out.println("Waiting due to Home Symbol...");
			System.out.println("Wait Page is loading...");
			Thread.sleep(1000);
		}
		System.out.println("Home Symbol disappeared");
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	public void selectRegionsFromDropdown(String regions)
			throws BiffException, IOException, TwfException, InterruptedException, InvalidFormatException {
		WebElement ddRegion = getElementByUsing("ddl_region");
		// ddRegion.click(); //This click sometimes not working
		this.JSClick(ddRegion);
		// objUtils.clickObject(xpath_ddl_region);
		// getElementByUsing("txtSearchRegion").click();
		// WebElement eleSearchRegion=getElementByUsing("txtSearchRegion");
		// eleSearchRegion.click();
		// this.JSClick(eleSearchRegion);
		while (!ddRegion.getAttribute("isopentreeview").equals("open")) {
			System.out.println("Waiting for region drop down to expand");
			Thread.sleep(1000);
			getElementByUsing("txtSearchRegion").click();
			System.out.println("Element clicked inside While loop in second try");

		}
		By element = By.xpath("//a[contains(text(),'Regions')]");
		while (!this.isElementPresent(element)) {
			System.out.println("Waiting for region drop down to expand");
			Thread.sleep(1000);
		}
		this.selectRegions(regions);
		ddRegion.click();
	}
	
	
	public void selectRegionsFromDropdownBPT(String regions)
			throws BiffException, IOException, TwfException, InterruptedException, InvalidFormatException {
		WebElement ddRegion = getElementByUsing("BranchesDropDown");
		// ddRegion.click();
		// This click sometimes not working
		this.JSClick(ddRegion);
		// objUtils.clickObject(xpath_ddl_region);
		// getElementByUsing("txtSearchRegion").click();
		// WebElement eleSearchRegion=getElementByUsing("txtSearchRegion");
		// eleSearchRegion.click();
		// this.JSClick(eleSearchRegion);
		while (!ddRegion.getAttribute("isopentreeview").equals("open")) {
			System.out.println("Waiting for region drop down to expand");
			Thread.sleep(1000);
			getElementByUsing("txtSearchRegion").click();
			System.out.println("Element clicked inside While loop in second try");

		}
		By element = By.xpath("//a[contains(text(),'Regions')]");
		while (!this.isElementPresent(element)) {
			System.out.println("Waiting for region drop down to expand");
			Thread.sleep(1000);
		}
		this.selectRegions(regions);
		ddRegion.click();
	}

	void selectRegions(String region) throws TwfException, InterruptedException {
		String xpathRegionToBeSelected;
		String checkboxRegion;
		int scrollValue = 250;
		if (!region.contains(",")) {
			if (region.contains("~")) {

				String parent_Region = region.split("~")[0];
				String child_Region = region.split("~")[1];
				String strExpandRegion = "(//a[contains(text(),'" + parent_Region
						+ "')]/../child::span[2])[1]/../span[1]";
				checkboxRegion = "(//a[contains(text(),'" + child_Region + "')])[1]/../span";
				this.waitForVisibility(strExpandRegion, 60);
				this.clickObjectByActions(strExpandRegion);
				this.waitForVisibility(checkboxRegion, 100);
				this.clickObject(checkboxRegion);
			}

			else {
				xpathRegionToBeSelected = "//a[contains(text(),'" + region + "')]";
				checkboxRegion = "//a[contains(text(),'" + region + "')]/../child::span[2]";
				// objUtils.waitForElement(By.xpath(checkboxRegion),10);
				driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
				if (!this.isElementExist(checkboxRegion, 1))
					checkboxRegion = "//a[contains(text(),'" + region + "')]/preceding-sibling::span";
				this.waitForVisibility(checkboxRegion, 100);
				this.clickObject(checkboxRegion);
			}
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		} else {
			String regions[] = region.split(",");
			for (String s : regions) {
				if (s.contains("~")) {

					String parent_Region = s.split("~")[0];
					String child_Region = s.split("~")[1];
					String strExpandRegion = "(//a[contains(text(),'" + parent_Region
							+ "')]/../child::span[2])[1]/../span[1]";
					checkboxRegion = "(//a[contains(text(),'" + child_Region + "')])[1]/../span";
					this.waitForVisibility(strExpandRegion, 60);
					this.clickObjectByActions(strExpandRegion);
					this.waitForVisibility(checkboxRegion, 100);
					this.clickObject(checkboxRegion);
				} else {
					xpathRegionToBeSelected = "//a[contains(text(),'" + s + "')]";
					checkboxRegion = "//a[contains(text(),'" + s + "')]/../child::span[2]";
					driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
					if (!this.isElementExist(checkboxRegion, 1))
						checkboxRegion = "//a[contains(text(),'" + s + "')]/preceding-sibling::span";
					this.waitForVisibility(checkboxRegion, 10);
					this.clickObject(checkboxRegion);
				}
			}
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		}
		System.out.println("End of Select Regions Mehtod");
	}

	public void clickFCCModule(String module) throws BiffException, InvalidFormatException, IOException, TwfException {
		System.out.println("...start..clickFCCModule..");
		String strFCCModule = KWVariables.getVariables().get("fccModule");
		strFCCModule = strFCCModule.replace("moduleName", module);
		this.clickObject(strFCCModule);
		System.out.println("Module " + module + "Clicked");
	}
	
	public void clickFCCModule_New(String module) throws BiffException, InvalidFormatException, IOException, TwfException {
		System.out.println("SLAP Dashboard Execution started");
		driver.manage().window().maximize();
		//Fire_waitUntilTime
		String strFCCModule = "//div[text()='moduleName']";
		strFCCModule = strFCCModule.replace("moduleName", module);
		this.clickObject(strFCCModule);	
		System.out.println("Module " + module + "Clicked");
		//Fire_waitUntilTime
		System.out.println("Navigate to SLAP Dashboard");
	}

	public void switchToCurrentWindow(String str) throws TwfException {
		System.out.println("Control trying to Switch to Current window");
		driver = DriverFactory.getDriver();
		Set winList = driver.getWindowHandles();
		Iterator itr = winList.iterator();
		String parent = (String) itr.next();
		System.out.println("parent:" + parent);
		String win = (String) itr.next();
		driver.switchTo().window(win);
		System.out.println("*******************Windows : " + winList.size());
		System.out.println(driver.getTitle());
//	driver=DriverFactory.getDriver();
//	String fccParent=driver.getWindowHandle();
//	ArrayList<String> newtab=new ArrayList<String>(driver.getWindowHandles());
//	driver.switchTo().window(newtab.get(1));
		System.out.println("Control Switched to Current window");
	}

	public String getPropertyValue(String PropertiesFileName, String propertyName) throws IOException {
		String propVal = null;
		Properties props = new Properties();
		String propFilePath = System.getProperty("user.dir") + "\\" + "src\\test\\resources\\" + PropertiesFileName;
		if (!PropertiesFileName.contains("."))
			propFilePath = propFilePath + ".properties";
		FileInputStream fileInput = new FileInputStream(propFilePath);
		props.load(fileInput);
		propVal = props.getProperty(propertyName).trim();
		return propVal;
	}

	public String getBranchNumbers(String PropertiesFileBranchList, String branchName) throws IOException {
		String branchNumbers = "";
		Properties props = new Properties();
		String propFilePath = System.getProperty("user.dir") + "\\" + "src\\test\\resources\\"
				+ PropertiesFileBranchList;

		FileInputStream fileInput = new FileInputStream(propFilePath);
		props.load(fileInput);
		if (branchName.contains(",")) {
			String arrBranchName[] = branchName.split(",");
			for (String branch : arrBranchName) {
				if (branch.contains("~"))
					branch = branch.split("~")[1];
				branchNumbers = branchNumbers + "," + props.getProperty(branch).trim();
			}
			branchNumbers = branchNumbers.substring(1);

		} else {
			branchNumbers = props.getProperty(branchName).trim();
		}
		return branchNumbers;
	}

	public List<WebElement> CreateObjectList(String strXpath) throws TwfException {
		driver = DriverFactory.getDriver();
		return driver.findElements(By.xpath(strXpath));
	}

	int getCurrentYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	int getCurrentMonth() {
		// It returns current month number
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH);
	}

	String getEnglishMonthName(int month) {
		// It returns current month Name----jan=1---dec=12
		return Month.of(month).toString().substring(0, 3); // It returns first three letters of a month
	}

	int getLastDayOfCurrentMonth()

	{
		int monthMaxDays;
		Calendar cal = Calendar.getInstance();
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	int getLatDayOfMonth(String strMonthNumber) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, Integer.parseInt(strMonthNumber));
		int lastDate = calendar.getActualMaximum(Calendar.DATE);
		return lastDate;
	}

	int getLatDayOfGivenMonthNumber(String strMonthNumber, String selectedYear) {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		dateFormat.setTimeZone(TimeZone.getTimeZone("CST"));
		String strDate = dateFormat.format(date);
		int year = Integer.parseInt(selectedYear);
		int month =(Integer.parseInt(strMonthNumber) - 1);
		int currentDate= 1;
		Calendar calendar = Calendar.getInstance();
		 calendar.set(year, month, currentDate);
		int lastDate = calendar.getActualMaximum(Calendar.DATE);
		return lastDate;
	}

	public String getCurrentTimeStamp() {
		String currentTimeStamp = String.valueOf(new Timestamp(new Date().getTime()));
		currentTimeStamp = currentTimeStamp.replaceAll("[^0-9]", "");
		return currentTimeStamp;
	}

//Description: This Method will select specified month from the sheet 
	public void selectMonth(Map<String, String> data) throws BiffException, IOException, TwfException {
		CommonUtils objUtils = new CommonUtils();

		String month = data.get("Month");
		String ddlMonth = "//ul[@class='ui-igcombo-listitemholder'][1]//li[text()='";
		ddlMonth = ddlMonth + month + "']";
		getElementByUsing("ddlMonthDropdown").click();
		objUtils.waitForVisibility(ddlMonth, 120);
		objUtils.clickObject(ddlMonth);
	}

//------------------
//-------------------------------------------------------------------------------------------
	/* Date and Calendar related Methods */
	String backMonth(String receivedMonth, int backNumber) throws ParseException {
		/*
		 * This method used for getting back month of a given month with specified
		 * difference
		 */
		int finalMonth_Number;
		String strFinalMonth;

		finalMonth_Number = getMonthNumber(receivedMonth) - backNumber;
		strFinalMonth = formatMonth(String.valueOf(finalMonth_Number));
		return strFinalMonth;
	}

	int getMonthNumber(String receivedMonth) throws ParseException {
		// It return equivalent month number of a given month. Eg. Jun 5
		Date date = new SimpleDateFormat("MMMM").parse(receivedMonth);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int receivedMonth_Number = cal.get(Calendar.MONTH) + 1;
		return receivedMonth_Number;

	}

	public String formatMonth(String month) throws ParseException {

		SimpleDateFormat monthParse = new SimpleDateFormat("MM");
		SimpleDateFormat monthDisplay = new SimpleDateFormat("MMM");
		return monthDisplay.format(monthParse.parse(month));
	}

	/* This method returns start date of given month for a given year */
	String getStartDate(String givenMonth, String selectedMonth_Year) throws ParseException {
		String startDate, endDate;
		String selectedMonth_Number = String.valueOf(getMonthNumber(givenMonth));
		startDate = selectedMonth_Number + "/" + "01/" + selectedMonth_Year;
		return startDate;
	}

	/* This method returns end date of given month for a given year */
	public String getEndDate(String givenMonth, String selectedMonth_Year) throws ParseException {
		String startDate, endDate;
		String selectedMonth_Number = String.valueOf(getMonthNumber(givenMonth));
		endDate = selectedMonth_Number + "/" + getLatDayOfGivenMonthNumber(selectedMonth_Number,selectedMonth_Year) + "/"
				+ selectedMonth_Year;
		return endDate;
	}

	public boolean stringContainsNumber(String s) {
		return Pattern.compile("[0-9]").matcher(s).find();
	}

	/* Date and Calendar related Methods End here */
	public Boolean compareMap(Map<String, Integer> m1, Map<String, Integer> m2, String startDate, String endDate,
			String branchNumbers) throws TwfException, NumberFormatException, IOException {
		System.out.println("----------------Inside compareMap--------------");
		Boolean flag = true;
		String colName;
		int val1, val2, diff;
		for (String key : m1.keySet()) {
			colName = key;
			val1 = m1.get(key).intValue();
			val2 = m2.get(key).intValue();
			if (val1 != val2) {
				System.out.println("Ui Value=" + val1 + "\tDb Value=" + val2 + "\tFor " + colName
						+ " ,Value Mismatch where start date=" + startDate + " end Date=" + endDate
						+ " and branch Number=" + branchNumbers);
				diff = val1 - val2; /// It calculates the variance
				if (diff > Integer.parseInt(getPropertyValue("FccCommon", "SlapGridDrillDownVariance"))) {
					SoftAssert.assertEqual(val1, val2, colName + " Value Mismatch where start date=" + startDate
							+ " end Date=" + endDate + " and branch Number=" + branchNumbers);
					flag = false;
				}
			}
		}
		System.out.println("----------------end compareMap--------------");
		return flag;
	}

	String getBackDateOfCurrentDate(int differnce) {
		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);

		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");

		calendar.add(Calendar.DATE, differnce);

		Date previousDate = calendar.getTime();
		format1.format(previousDate);

		return format1.format(previousDate);
	}

	public <T> T getSumOfListValues(List<T> list, BinaryOperator<T> adder) {

		return list.stream().reduce(adder).get();

	}

	public Boolean verifyLinkedHashMap(LinkedHashMap<String, String> map,
			LinkedHashMap<String, String> variance_vs_DB_Value, String startDate, String endDate,
			String region_numbers) throws NumberFormatException, IOException {
		System.out.println("----------------inside verifyLinkedHashMap--------------");
		Boolean flag = true;
		String colName;
		String Uival, dbVal;
		CommonUtils obj = new CommonUtils();
		for (String key : map.keySet()) {
			colName = key;
			Uival = map.get(key);
			dbVal = variance_vs_DB_Value.get(key);
			if(dbVal ==(null))
				dbVal="0";
			int varience =Integer.parseInt(Uival) - Integer.parseInt(dbVal);
			int variencecutoff= Integer
					.valueOf(obj.getPropertyValue("FccCommon", "BlastAmountVariance"));
			System.out.println("variencecutoff  :"+variencecutoff);
			System.out.println("key   :   "+key +"     varience   :" +varience);
			if (Math.abs(varience) >= variencecutoff) {
				System.out.println("Ui Value=" + Uival + "\tDb Value=" + dbVal + "\tFor " + colName
						+ " ,Value Mismatch where start date=" + startDate + " end Date=" + endDate
						+ " and branch Number=" + region_numbers);
				SoftAssert.assertEqual(Uival, dbVal, colName + " Value Mismatch where start date=" + startDate
						+ " end Date=" + endDate + " and branch Number=" + region_numbers);
				flag = false;
				
			}
		}
		System.out.println("----------------end verifyLinkedHashMap--------------");
		return flag;

	}

	public void verifyElementDisplayed(String element) throws BiffException, IOException, TwfException {
		Assert.assertEquals(getElementByUsing(element).isDisplayed(), true, element + " is Not Displayed");
	}

	public void navigateToWhatIF(String element) throws BiffException, IOException, TwfException, InterruptedException {
		System.out.println("***Navigating to What If Tab***");
		getElementByUsing("linkWhatIF").click();
		waitTillLoadSymbolBlast("");
		verifyElementDisplayed("Level2BodyFixedContainer");
		verifyElementDisplayed("Level2BodyData");
	}

	public void navigateToDetails(String element)
			throws BiffException, IOException, TwfException, InterruptedException {
		System.out.println("***Navigating to Detail Tab***");
		getElementByUsing("linkDetail").click();
		waitTillLoadSymbolBlast("");
		verifyElementDisplayed("Level3BodyFixedContainer");
		verifyElementDisplayed("Level3BodyData");
	}

	public void navigateToVariance(String element)
			throws BiffException, IOException, TwfException, InterruptedException {
		System.out.println("***Navigating to Variance Tab***");
		getElementByUsing("linkVariance").click();
		waitTillLoadSymbolBlast("");
		verifyElementDisplayed("Level3VarianceBody");
		verifyElementDisplayed("Level3VarianceData");
	}

	public void navigateToOverview(String element)
			throws BiffException, IOException, TwfException, InterruptedException {
		System.out.println("***Navigating to Overview Tab***");
		getElementByUsing("linkOverview").click();
		waitTillLoadSymbolBlast("");
		verifyElementDisplayed("Level1BodyFixedContainer");
		verifyElementDisplayed("Level1BodyData");
	}
	public  String getBackDateOfCurrentYear(int differnce) {
		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
		int month = calendar.get(Calendar.MONTH);
		int diff=2;
		int date = calendar.get(Calendar.DATE);
		if(date>14)
			diff=1;
		calendar.add(Calendar.MONTH, -month);
		calendar.add(Calendar.YEAR, differnce);
		Date previousDate = calendar.getTime();
		format1.format(previousDate);
		
		calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -diff);
		calendar.add(Calendar.YEAR, differnce);
		Date currentDate1 = calendar.getTime();
		format1.format(currentDate1);
		
		String oldDate=format1.format(previousDate)+" - "+format1.format(currentDate1);
		
		LocalDate convertedDate = LocalDate.parse(format1.format(currentDate1), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
		convertedDate = convertedDate.withDayOfMonth(convertedDate.getMonth().length(convertedDate.isLeapYear()));
		Date date1 = java.sql.Date.valueOf(convertedDate);
		oldDate=format1.format(date1);
		System.out.println(oldDate);
		return oldDate;
	}
	
	public  String getBackDateOfCurrentMonth(int differnce) throws ParseException {
		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
		int diff=0;
		int date = calendar.get(Calendar.DATE);
		if(date>14)
			diff=1;
		calendar.add(Calendar.MONTH, differnce+diff);

		Date previousDate = calendar.getTime();
		format1.format(previousDate);

		String oldDate=format1.format(previousDate);
		LocalDate convertedDate = LocalDate.parse(oldDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
		convertedDate = convertedDate.withDayOfMonth(convertedDate.getMonth().length(convertedDate.isLeapYear()));
		Date date1 = java.sql.Date.valueOf(convertedDate);
		oldDate=format1.format(date1);
		System.out.println(oldDate);
		return oldDate;
	}
	
	public String roundOffto2Decimalpoint5up(double number) {
		number = number *100;
		number= Math.round(number);
		number=number/100;
		DecimalFormat format = new DecimalFormat("0.00");
		String expectedDec = format.format(number);
		return expectedDec;
	}
	
	public String roundOffto2Decimalpoint5up(String num) {
		double number = Double.valueOf(num);
		number = number *100;
		number= Math.round(number);
		number=number/100;
		DecimalFormat format = new DecimalFormat("0.00");
		String expectedDec = format.format(number);
		return expectedDec;
	}
	public String floatRoundOffTo0Places(float number) {

		DecimalFormat format = new DecimalFormat("0");
		String expectedDec = format.format(number);
		return expectedDec;

	}
	public String floatRoundOffTo0Places(String number) {
		DecimalFormat format = new DecimalFormat("0");
		String expectedDec = format.format(Float.parseFloat(number));
		return expectedDec;
	}
	
	@Override
	public void checkPage() {

	}
}