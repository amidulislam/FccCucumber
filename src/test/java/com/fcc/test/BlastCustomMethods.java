package com.fcc.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.tavant.base.DriverFactory;
import com.tavant.base.WebPage;
import com.tavant.kwutils.KWVariables;
import com.tavant.utils.TwfException;

import jxl.read.biff.BiffException;

public class BlastCustomMethods extends WebPage {
	CommonUtils objUtils = new CommonUtils();
	// static LinkedHashMap<String, String> level3AttributeName_vs_DB_Value = new
	// LinkedHashMap<String, String>();

	@Override
	public void checkPage() {
		// TODO Auto-generated method stub

	}

	public void selectVarianceType(String varianceType)
			throws BiffException, IOException, TwfException, InterruptedException {
		// TODO Auto-generated method stub
		getElementByUsing("btnVariance").click();
		String ddVariance = "//li[contains(text(),'" + varianceType + "')]";
		objUtils.getObject(ddVariance).click();
		objUtils.waitTillLoadSymbolBlast("");
	}

	public List<String> getDisplayedGlListAtVarianceTab() throws TwfException {
		// This method will return all the displayed Gl present in the Variance Tab;
		String glUiName, glAttributeList = null;
		List<String> displayedGlList = null;

		String strGlList = "//table[@id='tblLevel3Variance_fixed']//child::tr/td[1]/div";
		List<WebElement> glDisplayedInVarianceTab = objUtils.CreateObjectList(strGlList);

		for (WebElement ele : glDisplayedInVarianceTab) {
			if (!ele.isDisplayed()) {

				objUtils.scrollToAnElement(ele);
			}
			glUiName = ele.getText();
			/*
			 * if (glUiName.contains("Cumulative Balance")) break;
			 */
			if (glAttributeList == null)
				glAttributeList = glUiName;
			else
				glAttributeList = glAttributeList + ":" + glUiName;
			System.out.println(glUiName);
		}
		String arrAttribute[] = glAttributeList.split(":");
		displayedGlList = Arrays.asList(arrAttribute);
		return displayedGlList;
	}

	public ArrayList<LinkedHashMap<String, String>> getFirstOrSecondColumnAmount(List<String> lsDisplyedGl,
			String locator_FirstColumn, String locator_SecondColumn, String locator_ThirdColumn) throws TwfException {

		String amount1, amount2, amount3;
		ArrayList<LinkedHashMap<String, String>> destMap = new ArrayList<LinkedHashMap<String, String>>();

		LinkedHashMap<String, String> map1 = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> map2 = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> map3 = new LinkedHashMap<String, String>();

		String strLocator1, strLocator2, strLocator3;
		for (String glName : lsDisplyedGl) {
			strLocator1 = locator_FirstColumn;
			strLocator2 = locator_SecondColumn;
			strLocator3 = locator_ThirdColumn;

			strLocator1 = strLocator1.replace("GlName", glName);
			strLocator2 = strLocator2.replace("GlName", glName);
			strLocator3 = strLocator3.replace("GlName", glName);

			amount1 = getAmount(strLocator1);
			amount2 = getAmount(strLocator2);
			amount3 = getAmount(strLocator3);

			map1.put(glName, amount1);
			map2.put(glName, amount2);
			map3.put(glName, amount3);
		}

		destMap.add(map1);
		destMap.add(map2);
		destMap.add(map3);

		return destMap;
	}

	@SuppressWarnings("static-access")
	private String getAmount(String strAmount) throws TwfException {

		String value;
		boolean flagParenthesis = false;

		WebElement element = objUtils.getObject(strAmount);

		if (!element.isDisplayed()) {
			objUtils.scrollToAnElement(element);
		}
		value = element.getText();

		if (value.contains("("))
			flagParenthesis = true;
		value = value.replaceAll("[^0-9]", "");

		if (flagParenthesis)
			value = String.valueOf(Long.parseLong(value) * -1);

		return value;
	}

	public String getEndDatesForVarianceCalculation(String varianceType)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException, ParseException {
		String dates = null;

		if (varianceType.equals("Day over Day"))
			dates = getEndDatesForDoD();
		else if (varianceType.equals("Month over Month"))
			dates = getEndDatesForMoM();
		else if (varianceType.equals("Year over Year"))
			dates = getEndDatesForYoY();
		return dates;

	}

	public String getEndDatesForYoY() {
		// TODO Auto-generated method stub
		String dates;
		String endDate1 = null, endDate2 = null;
		int diff = 0;
		int dateFound = 0;
		while (true) {
			String backDate = objUtils.getBackDateOfCurrentYear((diff++) * -1);
			if (endDate2 == null) {
				endDate2 = backDate;
				dateFound++;
			} else if (endDate1 == null) {
				endDate1 = backDate;
				dateFound++;
			}
			if (dateFound == 2)
				break;

		}
		dates = endDate1 + ":" + endDate2;
		return dates;
	}

	public String getEndDatesForMoM() throws ParseException {
		// TODO Auto-generated method stub
		String dates;
		String endDate1 = null, endDate2 = null;
		int diff = 2;
		int dateFound = 0;
		while (true) {
			String backDate = objUtils.getBackDateOfCurrentMonth((diff++) * -1);
			if (endDate2 == null) {
				endDate2 = backDate;
				dateFound++;
			} else if (endDate1 == null) {
				endDate1 = backDate;
				dateFound++;
			}
			if (dateFound == 2)
				break;

		}
		dates = endDate1 + ":" + endDate2;
		return dates;
	}

	private String getEndDatesForDoD()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException {
		// TODO Auto-generated method stub
		String dates;
		String endDate1 = null, endDate2 = null;
		int diff = 1;
		int dateFound = 0;
		while (true) {
			String backDate = objUtils.getBackDateOfCurrentDate((diff++) * -1);
			if (isTheDateAHoliday(backDate))
				continue;
			if (endDate2 == null) {
				endDate2 = backDate;
				dateFound++;
			} else if (endDate1 == null) {
				endDate1 = backDate;
				dateFound++;
			}
			if (dateFound == 2)
				break;

		}
		dates = endDate1 + ":" + endDate2;
		return dates;
	}

	private boolean isTheDateAHoliday(String backDate)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException {

		// It checks whether inputed backDate is a holiday or not

		FccUtility fcc = new FccUtility();

		if (fcc.noOfHoliday(backDate, backDate) == 1)
			return true;
		else
			return false;
	}

	public String getStartDate(String endDate1) {

		String month = endDate1.split("/")[0];
		String year = endDate1.split("/")[2];

		return month + "/01/" + year;
	}
	
		public String getStartDateYear(String endDate1) {

		String month = "01";
		String year = endDate1.split("/")[2];

		return month + "/01/" + year;
	}

	public LinkedHashMap<String, String> getDailyVarianceFromUi(LinkedHashMap<String, String> mapAmount_FirstColumn,
			LinkedHashMap<String, String> mapAmount_SecondColumn) {

		LinkedHashMap<String, String> dailyVarianceFromUi = new LinkedHashMap<String, String>();

		Iterator<Map.Entry<String, String>> itr1 = mapAmount_FirstColumn.entrySet().iterator();
		Iterator<Map.Entry<String, String>> itr2 = mapAmount_SecondColumn.entrySet().iterator();
		int val1, val2, diff;
		String key;
		Map.Entry<String, String> entry;
		while (itr1.hasNext()) {
			entry = itr1.next();
			key = entry.getKey();
			val1 = Integer.parseInt(entry.getValue());
			val2 = Integer.parseInt(itr2.next().getValue());
			diff = val1 - val2;
			dailyVarianceFromUi.put(key, String.valueOf(diff));
		}
		return dailyVarianceFromUi;
	}

	String getBlastAmount(Connection con, String startDate, String endDate, String attributeName, String accountNumber,
			String region_numbers) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, BiffException, InvalidFormatException, IOException {
		Statement st = null;
		ResultSet rs = null;
		String dbAttributeValue = null;
		System.out.println(con);
		if(accountNumber.contains(","))
			accountNumber=accountNumber.replaceAll(",", "','");
		String strSql = "select sum(Amount) from Temp_GeneralLedger with(nolock) where GLAccount in('" + accountNumber
				+ "') and JournalPostDate between '" + startDate + "' and '" + endDate + "' and  GLBranch in("
				+ region_numbers + ") and (DebitGLAccount >= 30000 or DebitGLAccount < 10000 "
				+ "or creditglaccount >= 30000 OR creditglaccount < 10000)";
		System.out.println(strSql);
		Boolean flagSign = false;

		if (con != null) {
			st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = st.executeQuery(strSql);
			if (rs.next()) {
				try {
					dbAttributeValue = rs.getString(1).trim();
					if(dbAttributeValue == null || dbAttributeValue.length() == 0) {
						System.out.println("******** Null ***********");
						dbAttributeValue="0";
					}	
					dbAttributeValue = String.valueOf(Math.round(Double.parseDouble(dbAttributeValue)));
					flagSign = getSignForGl(attributeName);
					if (flagSign) {
						dbAttributeValue = String.valueOf(Long.parseLong(dbAttributeValue) * (-1));
					}
				} catch (Exception e) {
					System.out.println("Database Value is Null");
					dbAttributeValue="0";
					dbAttributeValue = String.valueOf(Math.round(Double.parseDouble(dbAttributeValue)));
					flagSign = getSignForGl(attributeName);
					if (flagSign) {
						dbAttributeValue = String.valueOf(Long.parseLong(dbAttributeValue) * (-1));
					}
					// e.printStackTrace();
				}
			}
		}
		System.out.println("query done, dbAttributeValue"+dbAttributeValue);
		return dbAttributeValue;
	}

	public Boolean getSignForGl(String attributeName) throws InstantiationException, IllegalAccessException,
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
		Map<String, String> mapNonSum = getMapNonSumNotFound();
		if ((strNonSumMappingAttribute = mapNonSum.get(attributeName)) != null)
			attributeName = strNonSumMappingAttribute;
		String strSql = "select  NonSumOperator from v_dim_AccountTreeAccount( nolock) where  GLName='" + attributeName
				+ "' and NonSumOperator is NULL";

		Connection conSL_ACCT_DW = objDB.init();

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
			conSL_ACCT_DW.close();
		}
// ************************
		return flag;

	}

	HashMap<String, String> getMapNonSumNotFound() throws BiffException, InvalidFormatException, IOException {
		// It will return those list of GL,whose Name mapping is not found in Non sum
		// operator list.
		HashMap<String, String> mapNonSum = new HashMap<String, String>();
		String strNonSumNotFoundList = KWVariables.getVariables().get("NonSumList");
		String arrNonSumNotFoundList[] = strNonSumNotFoundList.split(",");
		for (String str : arrNonSumNotFoundList) {
			mapNonSum.put(str.split("#")[0], str.split("#")[1]);
		}
		return mapNonSum;
	}

	LinkedHashMap<String, String> getGlGroupFormula() throws BiffException, InvalidFormatException, IOException {
		String groupName, formula;
		LinkedHashMap<String, String> glGroupFormula = new LinkedHashMap<String, String>();
		String strGroupFormula = KWVariables.getVariables().get("GlGroupFormula");
		String[] arrGroupFormula = strGroupFormula.split("#");
		for (String str : arrGroupFormula) {
			groupName = str.split("=")[0];
			formula = str.split("=")[1];
			glGroupFormula.put(groupName, formula);
		}
		return glGroupFormula;

	}

	public void verifyVarianceAgainstEncompassDB(Map<String, String> data,
			LinkedHashMap<String, String> mapAmount_FirstColumn, LinkedHashMap<String, String> mapAmount_SecondColumn,
			LinkedHashMap<String, String> mapAmount_ThirdColumn,
			LinkedHashMap<String, String> mapAmount_DailyVarianceUi, String startDate1, String endDate1,
			String startDate2, String endDate2) throws BiffException, InvalidFormatException, IOException,
			InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, TwfException {
		System.out.println("********************Inside verifyVarianceAgainstEncompassDB**********************");
		WebDriver driver = DriverFactory.getDriver();
		String GlGroupList = KWVariables.getVariables().get("GlGroupList");
		String arrGlGroupList[] = GlGroupList.split(":");

		ArrayList<String> listGlGroupList = new ArrayList<String>();
		DataBaseUtils objDB = new DataBaseUtils();

		Connection con_Encompass = objDB.initEncompassDatabase();
		// Connection conSL_ACCT_DW = objDB.init();

		Boolean flag = true, tempFlag;
		objUtils = new CommonUtils();
		String file_Level3ReportName = System.getProperty("user.dir") + "\\test-output\\"
		+ objUtils.getCurrentTimeStamp() + "VarianceVerification.xlsx";
		if (con_Encompass != null) {
			System.out.println("Database connection is successful");
			for (int i = 0; i < arrGlGroupList.length; i++) {
				System.out.println(arrGlGroupList[i].trim());
				listGlGroupList.add(arrGlGroupList[i].trim());
			}

			HashMap<String, String> glKeyPair = new HashMap<String, String>();
			glKeyPair = getGlKeyPairMap();

			String key;
			
			XSSFRow row = null;
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

			tDInformation.setHeightInPoints((3 * sheet.getDefaultRowHeightInPoints()));
			tDInformation.createCell(0).setCellValue("Test Case :Blast-Verification of " +data.get("VarianceType"));
			tDInformation.createCell(1).setCellValue(" ");
			tDInformation.createCell(2).setCellValue(" ");
			tDInformation.createCell(3).setCellValue("Selected Branch(s)=" + data.get("Branches"));
			tDInformation.createCell(4).setCellValue(" ");
			tDInformation.createCell(5).setCellValue(" ");
			tDInformation.createCell(6).setCellValue("Variance Type = "+data.get("VarianceType"));
			tDInformation.createCell(7).setCellValue("");
			tDInformation.createCell(8).setCellValue("");
			tDInformation.createCell(9).setCellValue("");
			
			tDInformation.getCell(0).setCellStyle(styleTestDataInfo);
			tDInformation.getCell(1).setCellStyle(styleTestDataInfo);
			tDInformation.getCell(2).setCellStyle(styleTestDataInfo);
			tDInformation.getCell(3).setCellStyle(styleTestDataInfo);
			tDInformation.getCell(4).setCellStyle(styleTestDataInfo);
			tDInformation.getCell(5).setCellStyle(styleTestDataInfo);
			tDInformation.getCell(6).setCellStyle(styleTestDataInfo);
			tDInformation.getCell(7).setCellStyle(styleTestDataInfo);
			tDInformation.getCell(8).setCellStyle(styleTestDataInfo);
			tDInformation.getCell(9).setCellStyle(styleTestDataInfo);

			XSSFRow rowhead = sheet.createRow((short) sheet.getLastRowNum() + 1);
			rowhead.createCell(0).setCellValue("GL Name");
			String StartDateUI = driver.findElement(By.xpath("//*[@id=\"tblLevel3Variance_headers\"]/thead//th[5]//div")).getText().trim();
			rowhead.createCell(1).setCellValue(StartDateUI +" UI Value");
			rowhead.createCell(2).setCellValue(StartDateUI +"Database Value");
			rowhead.createCell(3).setCellValue("Status Comparing UI AND DB ("+StartDateUI+")");
			String StartDate2UI = driver.findElement(By.xpath("//*[@id=\"tblLevel3Variance_headers\"]/thead//th[6]//div")).getText().trim();
			rowhead.createCell(4).setCellValue(StartDate2UI +" UI Value");
			rowhead.createCell(5).setCellValue(StartDate2UI +" Database Value");
			rowhead.createCell(6).setCellValue("Status Comparing UI AND DB ("+StartDate2UI+")");
			String VarianceType = driver.findElement(By.xpath("//*[@id=\"tblLevel3Variance_headers\"]/thead//th[7]//div")).getText().trim();
			rowhead.createCell(7).setCellValue(VarianceType+" UI Value");
			rowhead.createCell(8).setCellValue(VarianceType+" Calculated Value");
			rowhead.createCell(9).setCellValue("Status Comparing Variance");
			for (int j = 0; j <= 9; j++)
				rowhead.getCell(j).setCellStyle(style);

			XSSFFont fontRows = workbook.createFont();
			font.setBold(true);
			styleAccountName.setFont(font);
			styleAccountName.setWrapText(true);
			
			
			
			verifyMapValues(mapAmount_FirstColumn, listGlGroupList, glKeyPair, con_Encompass, data, startDate1,
					endDate1,sheet,row,"First");
			verifyMapValues(mapAmount_SecondColumn, listGlGroupList, glKeyPair, con_Encompass, data, startDate2,
					endDate2,sheet,row,"Second");
			compareVariance(mapAmount_FirstColumn, mapAmount_SecondColumn, mapAmount_ThirdColumn,sheet,row);

			
			for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
				sheet.autoSizeColumn(i, true);
			}
			FileOutputStream out = new FileOutputStream(file_Level3ReportName);
			workbook.write(out);
			out.close();
			System.out.println("Excel Report of Comparision has been generated at " + file_Level3ReportName);
	
		}
		System.out.println("********************End verifyVarianceAgainstEncompassDB**********************");
		
	}

	private void compareVariance(LinkedHashMap<String, String> mapAmount_FirstColumn,
			LinkedHashMap<String, String> mapAmount_SecondColumn, LinkedHashMap<String, String> mapAmount_ThirdColumn,XSSFSheet sheet,XSSFRow row)
			throws NumberFormatException, IOException {
		System.out.println("Inside compareVariance Method");
		int uiVal1, uiVal2, uiVal3, dailyVarianceValue = 0;
		CommonUtils obj = new CommonUtils();
		XSSFCellStyle styleStatus = sheet.getWorkbook().createCellStyle();
		XSSFCellStyle styleStatus2 = sheet.getWorkbook().createCellStyle();
		XSSFFont f = sheet.getWorkbook().createFont();
		XSSFFont f2 = sheet.getWorkbook().createFont();
		
		f.setBold(true);
		int j =1;
		for (String key : mapAmount_ThirdColumn.keySet()) {
			row= sheet.getRow(j+ 1);
			j++;
			System.out.println(key+"    :     "+mapAmount_FirstColumn.get(key));
			uiVal1 = Integer.parseInt(mapAmount_FirstColumn.get(key));
			System.out.println(key+"    :     "+mapAmount_SecondColumn.get(key));
			uiVal2 = Integer.parseInt(mapAmount_SecondColumn.get(key));
			System.out.println(key+"    :     "+mapAmount_ThirdColumn.get(key));
			if(mapAmount_ThirdColumn.get(key).equals(""))
				uiVal3 = Integer.parseInt("0");
			else
			uiVal3 = Integer.parseInt(mapAmount_ThirdColumn.get(key));
		//	dailyVarianceValue = uiVal2- uiVal1;
			dailyVarianceValue= Integer.parseInt(SecondColumnData.get(key))-Integer.parseInt(FirstColumnData.get(key));
			int varience =dailyVarianceValue - uiVal3;
			int variencecutoff= Integer
					.valueOf(obj.getPropertyValue("FccCommon", "BlastAmountVariance"));
			System.out.println("variencecutoff  :"+variencecutoff);
			System.out.println("key   :   "+key +"     varience   :" +varience);
			
			row.createCell(7).setCellValue(uiVal3);
			row.createCell(8).setCellValue(dailyVarianceValue);
			if (Math.abs(varience) >= variencecutoff) {
				f2.setColor(IndexedColors.DARK_RED.getIndex());
				styleStatus2.setFont(f2);
				row.createCell(9).setCellValue("Fail ("+varience+")");
				row.getCell(9).setCellStyle(styleStatus2);
				SoftAssert.assertEqual(dailyVarianceValue, uiVal3, "Gl Name=" + key + " ,Variance Mismatch");
			
			}else{
				f.setColor(IndexedColors.GREEN.getIndex());
				styleStatus.setFont(f);
				row.createCell(9).setCellValue("Pass");
				row.getCell(9).setCellStyle(styleStatus);
			}
		}
		System.out.println("End compareVariance Method");
	}
	public static LinkedHashMap<String,String> FirstColumnData =new LinkedHashMap<String, String>();;
	public static  LinkedHashMap<String,String>  SecondColumnData= new LinkedHashMap<String, String>(); ;
	void verifyMapValues(LinkedHashMap<String, String> map, ArrayList<String> listGlGroupList,
			HashMap<String, String> glKeyPair, Connection con, Map<String, String> data, String startDate,
			String endDate,XSSFSheet sheet,XSSFRow row, String position)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException, BiffException, InvalidFormatException {
		System.out.println("********************Inside verifyMapValues**********************");
		String uiAttributeName;
		String uiAttributeValue;

		String dbAttributeValue;
		BLAST blast = new BLAST();
		CommonUtils obj = new CommonUtils();
		BLAST.level3AttributeName_vs_DB_Value.clear();
	
		String branchName = data.get("Branches");
		branchName = branchName.replace(" ", "_");
		String region_numbers = objUtils.getBranchNumbers("regionList.properties", branchName);
		String keyNumber;
		XSSFCellStyle styleStatus = sheet.getWorkbook().createCellStyle();
		XSSFCellStyle styleStatus2 = sheet.getWorkbook().createCellStyle();
		XSSFFont f = sheet.getWorkbook().createFont();
		XSSFFont f2 = sheet.getWorkbook().createFont();
		
		f.setBold(true);
		int i =position.equals("First")?1:4;
		int j =0;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			j++;
			if(i==1)
				row= sheet.createRow((short) sheet.getLastRowNum() + 1);
			else
				row= sheet.getRow(j+ 1);
			uiAttributeName = entry.getKey();
			keyNumber = blast.getAccountNumber(glKeyPair,uiAttributeName);

			if (uiAttributeName.contains("Brokered Loan Income"))
				System.out.println("Brokered Loan Income");

			uiAttributeValue = map.get(uiAttributeName);
			if (listGlGroupList.contains(uiAttributeName)) // Gl Group calculation logic
			{
				dbAttributeValue = blast.calculateFromUI(map, uiAttributeName);
				BLAST.level3AttributeName_vs_DB_Value.put(uiAttributeName, dbAttributeValue); // It contain DB value
																								// against
				// each GL and GL Group
				// continue;
				
				row.createCell(0).setCellValue(uiAttributeName);
				row.createCell(i).setCellValue(uiAttributeValue);
				row.createCell(i + 1).setCellValue(dbAttributeValue);
				int varience = Integer.parseInt(uiAttributeValue) - Integer.parseInt(dbAttributeValue);
				int variencecutoff = Integer.valueOf(obj.getPropertyValue("FccCommon", "BlastAmountVariance"));
				if (Math.abs(varience) >= variencecutoff) {
					f2.setColor(IndexedColors.DARK_RED.getIndex());
					styleStatus2.setFont(f2);
					row.createCell(i+2).setCellValue("Fail ("+varience+")");
					row.getCell(i+2).setCellStyle(styleStatus2);

				} else {
					f.setColor(IndexedColors.GREEN.getIndex());
					styleStatus.setFont(f);
					row.createCell(i+2).setCellValue("Pass");
					row.getCell(i+2).setCellStyle(styleStatus);
				}
				if(i==1)
				{
					System.out.println("**************FirstColumnData***************");
					System.out.println(uiAttributeName+":"+dbAttributeValue);
					FirstColumnData.put(uiAttributeName, dbAttributeValue);
					System.out.println("*****************************");
					
				}
				else {
					System.out.println("**************SecondColumnData***************");
					System.out.println(uiAttributeName+":"+dbAttributeValue);
					SecondColumnData.put(uiAttributeName, dbAttributeValue);
					System.out.println("*****************************");
					
				}
			} else { // Gl value extraction from dB logic

				dbAttributeValue = getBlastAmount(con, startDate, endDate, uiAttributeName, keyNumber, region_numbers); // DB using the  mapping key for a Gl
				BLAST.level3AttributeName_vs_DB_Value.put(uiAttributeName, dbAttributeValue);
				row.createCell(0).setCellValue(uiAttributeName);
				row.createCell(i).setCellValue(uiAttributeValue);
				row.createCell(i+1).setCellValue(dbAttributeValue);
				int varience = Integer.parseInt(uiAttributeValue) - Integer.parseInt(dbAttributeValue);
				int variencecutoff = Integer.valueOf(obj.getPropertyValue("FccCommon", "BlastAmountVariance"));
				if (Math.abs(varience) >= variencecutoff) {
					f2.setColor(IndexedColors.DARK_RED.getIndex());
					styleStatus2.setFont(f2);
					row.createCell(i+2).setCellValue("Fail ("+varience+")");
					row.getCell(i+2).setCellStyle(styleStatus2);

				} else {
					f.setColor(IndexedColors.GREEN.getIndex());
					styleStatus.setFont(f);
					row.createCell(i+2).setCellValue("Pass");
					row.getCell(i+2).setCellStyle(styleStatus);
				}
				if(i==1) {
					System.out.println("**************FirstColumnData***************");
					System.out.println(uiAttributeName+":"+dbAttributeValue);
					FirstColumnData.put(uiAttributeName, dbAttributeValue);
					System.out.println("*****************************");
					
				}
				else {
					System.out.println("**************SecondColumnData***************");
					System.out.println(uiAttributeName+":"+dbAttributeValue);
					SecondColumnData.put(uiAttributeName, dbAttributeValue);
					System.out.println("*****************************");
					
				}
			}
			if (dbAttributeValue == null) {
				System.out.println(
						uiAttributeName + " \nhaving UI Value=" + uiAttributeValue + " Mapping key is not found!!!");
				
				row.createCell(0).setCellValue(uiAttributeName);
				// row.getCell(0).setCellStyle(styleAccountName);
				row.createCell(i).setCellValue(uiAttributeValue);
				row.createCell(i + 1).setCellValue("Key Not Found in the Mapping sheet");
				f2.setColor(IndexedColors.RED.getIndex());
				styleStatus2.setFont(f2);
				row.createCell(i+2).setCellValue("Fail");
				row.getCell(i+2).setCellStyle(styleStatus2);
				if(i==1)
					FirstColumnData.put(uiAttributeName, "");
				else
					SecondColumnData.put(uiAttributeName, "");
			}
		}

		objUtils.verifyLinkedHashMap(map, BLAST.level3AttributeName_vs_DB_Value, startDate, endDate, region_numbers);
		System.out.println("********************End verifyMapValues**********************");
		
	}

	public HashMap<String, String> getGlKeyPairMap() throws BiffException, InvalidFormatException, IOException {
		System.out.println("********************Inside getGlKeyPairMap**********************");
		
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
		System.out.println("********************End getGlKeyPairMap**********************");
		return mapGlKeyPair;
	
		
	}
	
}
