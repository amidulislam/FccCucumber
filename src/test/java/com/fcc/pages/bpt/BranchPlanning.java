package com.fcc.pages.bpt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.fcc.test.CommonUtils;
import com.fcc.test.SoftAssert;
import com.tavant.base.DriverFactory;
import com.tavant.base.WebPage;
import com.tavant.utils.TwfException;

import jxl.read.biff.BiffException;

public class BranchPlanning extends WebPage {

	CommonUtils utils = new CommonUtils();

	public void uploadMasterData(String documentName) throws TwfException, BiffException, IOException {
		System.out.println("Input file Name :"+documentName);
		getElementByUsing("BrowseFiles")
				.sendKeys(System.getProperty("user.dir") + "\\src\\test\\resources\\" + documentName);
	}

	public void navigateToBPTforUser(String args)
			throws TwfException, BiffException, IOException, InterruptedException {
		WebDriver driver = DriverFactory.getDriver();
		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(0));
		driver.get("http://mailhog-dev.supremelending.com/");

		driver.findElement(By.xpath("//*[@ng-repeat=\"message in messages\"][1]")).click();

		String newURL = driver.findElement(By.xpath("//*[text()='BUDGETING TOOL']")).getAttribute("href").toString();
		driver.get("https://web-fcc-qa.supremelending.com/home/login");

		getElementByUsing("UserNameLoginPage").sendKeys("svcQaFCCAutomationUs");
		getElementByUsing("PasswordLoginPage").sendKeys("k0EHqCQ4CBCY45jQhBbnGFfvU66J3s|");
		getElementByUsing("LoginBtn").click();

		utils.waitUntillCondition("BPTCreatePlanBtn");
		driver.get(newURL);
		utils.waitUntillCondition("UserBranchDropDown");
	}

	public void verifyUploadData(String branchId) throws IOException, TwfException, BiffException {

		File file = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\NovBranchHistoricalData.xlsx");
		WebDriver driver = DriverFactory.getDriver();
		FileInputStream inputStream = new FileInputStream(file);
		XSSFWorkbook workbook = null;
		workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheetAt(0);

		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();

		for (int i = 1; i < rowCount + 1; i++) {

			Row row = sheet.getRow(i);
			if((int)row.getCell(0).getNumericCellValue()==Integer.parseInt(branchId)) {
				
				System.out.println("*****************Verifying Product Mix**************");
				verifyProductMix(row, 1);
				System.out.println("*****************Verifying Compensation**************");
				verifyCompensation();
				System.out.println("*****************Verifying SG&A ($$)**************");
				verifySGandA();
				System.out.println("*****************Verifying Direct Production Costs**************");
				verifyDirectProductionCosts(row);
				System.out.println("*****************Verifying Staffing Model **************");
				verifyStaffingModel(row);
			}
		}
	}

	public void verifyProductMix(Row row, int section) throws TwfException, BiffException, IOException {
		WebDriver driver = DriverFactory.getDriver();
		switch (section) {
		case 1:
			section = 1;
			break;
		case 2:
			section = 3;
			break;
		case 3:
			section = 5;
			break;
		case 4:
			section = 7;
			break;
		}
		SoftAssert.assertEqual(utils.roundOffto2Decimalpoint5up(driver.findElement(By.xpath("//td[text()=' Conventional ']/following::input["+section+"]"))
				.getAttribute("value")), utils.roundOffto2Decimalpoint5up(row.getCell(10).getNumericCellValue()), "Conventional Actual BPS mismatch from MasterData");
		SoftAssert.assertEqual(utils.roundOffto2Decimalpoint5up(driver.findElement(By.xpath("//td[text()=' Conventional ']/following::input["+(section+1)+"]"))
				.getAttribute("value")), utils.roundOffto2Decimalpoint5up(row.getCell(6).getNumericCellValue()*100), "Conventional PMIX mismatch from MasterData");
		
		SoftAssert.assertEqual(utils.roundOffto2Decimalpoint5up(driver.findElement(By.xpath("//td[text()=' Government ']/following::input["+section+"]"))
				.getAttribute("value")),utils.roundOffto2Decimalpoint5up( row.getCell(11).getNumericCellValue()), "Govt Actual BPS mismatch from MasterData");
		SoftAssert.assertEqual(utils.roundOffto2Decimalpoint5up(driver.findElement(By.xpath("//td[text()=' Government ']/following::input["+(section+1)+"]"))
				.getAttribute("value")), utils.roundOffto2Decimalpoint5up(row.getCell(7).getNumericCellValue()*100), "GOVT PMIX mismatch from MasterData");
		
		SoftAssert.assertEqual(utils.roundOffto2Decimalpoint5up(driver.findElement(By.xpath("//td[text()=' Jumbo ']/following::input["+section+"]"))
				.getAttribute("value")), utils.roundOffto2Decimalpoint5up(row.getCell(12).getNumericCellValue()), "Jumbo Actual BPS mismatch from MasterData");
		SoftAssert.assertEqual(utils.roundOffto2Decimalpoint5up(driver.findElement(By.xpath("//td[text()=' Jumbo ']/following::input["+(section+1)+"]"))
				.getAttribute("value")), utils.roundOffto2Decimalpoint5up(row.getCell(8).getNumericCellValue()*100), "JUMBO PMIX mismatch from MasterData");
		
		SoftAssert.assertEqual(utils.roundOffto2Decimalpoint5up(driver.findElement(By.xpath("//td[text()=' Bond ']/following::input["+section+"]"))
				.getAttribute("value")),utils.roundOffto2Decimalpoint5up( row.getCell(13).getNumericCellValue()), "Bond Actual BPS mismatch from MasterData");
		SoftAssert.assertEqual(utils.roundOffto2Decimalpoint5up(driver.findElement(By.xpath("//td[text()=' Bond ']/following::input["+(section+1)+"]"))
				.getAttribute("value")), utils.roundOffto2Decimalpoint5up(row.getCell(9).getNumericCellValue()*100), "BOND PMIX mismatch from MasterData");
		
		SoftAssert.assertEqual(utils.roundOffto2Decimalpoint5up(driver.findElement(By.xpath("//td[text()=' Fee Income ']/following::input["+section+"]"))
				.getAttribute("value")),utils.roundOffto2Decimalpoint5up(row.getCell(14).getNumericCellValue()), "Fee Income Actual BPS mismatch from MasterData");
		String total = utils.floatRoundOffTo0Places((Float.valueOf(utils.roundOffto2Decimalpoint5up(row.getCell(10).getNumericCellValue()))*
				Float.valueOf(utils.roundOffto2Decimalpoint5up(row.getCell(6).getNumericCellValue())))+
				(Float.valueOf(utils.roundOffto2Decimalpoint5up(row.getCell(11).getNumericCellValue()))*
						Float.valueOf(utils.roundOffto2Decimalpoint5up(row.getCell(7).getNumericCellValue())))+
				(Float.valueOf(utils.roundOffto2Decimalpoint5up(row.getCell(12).getNumericCellValue()))*
						Float.valueOf(utils.roundOffto2Decimalpoint5up(row.getCell(8).getNumericCellValue())))+
				(Float.valueOf(utils.roundOffto2Decimalpoint5up(row.getCell(13).getNumericCellValue()))*
						Float.valueOf(utils.roundOffto2Decimalpoint5up(row.getCell(9).getNumericCellValue())))+
				(Float.valueOf(utils.roundOffto2Decimalpoint5up(row.getCell(14).getNumericCellValue()))));
		SoftAssert.assertEqual(driver.findElement(By.xpath("//td[text()='Total']/following::td[1]")).getText(),
				total, "Total Blended Rev mismatch from MasterData");

		SoftAssert.assertEqual(driver.findElement(By.xpath("//td[text()='Total']/following::td[2]")).getText(),
				"100%", "Total Percentage mismatch from MasterData");
		Actions actions = new Actions(driver);
		WebElement input = driver.findElement(By.xpath("//td[text()=' Conventional ']/following::input[3]"));
		actions.moveToElement(input).perform();
		SoftAssert.assertEqual(getElementByUsing("ProductMixMarginToolTip").isDisplayed(), true, "ProductMixMarginToolTip ToolTip not shown");
		
		input = driver.findElement(By.xpath("//td[text()=' Conventional ']/following::input[4]"));
		actions.moveToElement(input).perform();
		SoftAssert.assertEqual(getElementByUsing("ProductMixPercentageToolTip").isDisplayed(), true, "ProductMixPercentageToolTip ToolTip not shown");
		
		
	}
	
	public void verifyCompensation() throws TwfException, BiffException, IOException {
		WebDriver driver = DriverFactory.getDriver();
		Actions actions = new Actions(driver);
		WebElement input = driver.findElement(By.xpath("//td[text()=' Loan Officer (LO) ']/following::input[1]"));
		actions.moveToElement(input).perform();
		SoftAssert.assertEqual(getElementByUsing("ProductionPercentageToolTip").isDisplayed(), true, "ProductionPercentageToolTip ToolTip not shown");
		input = driver.findElement(By.xpath("//td[text()=' Loan Officer (LO) ']/following::input[2]"));
		actions.moveToElement(input).perform();
		SoftAssert.assertEqual(getElementByUsing("AvgBPSLOToolTip").isDisplayed(), true, "AvgBPSLOToolTip ToolTip not shown");
		input = driver.findElement(By.xpath("//td[text()=' Loan Officer (LO) ']/following::input[3]"));
		actions.moveToElement(input).perform();
		SoftAssert.assertEqual(getElementByUsing("SalaryLOToolTip").isDisplayed(), true, "SalaryLOToolTip ToolTip not shown");
		
		input = driver.findElement(By.xpath("//td[text()=' LO Assistant (LOA) ']/following::input[1]"));
		actions.moveToElement(input).perform();
		SoftAssert.assertEqual(getElementByUsing("ProductionPercentageToolTip").isDisplayed(), true, "ProductionPercentageToolTip ToolTip not shown");
		input = driver.findElement(By.xpath("//td[text()=' LO Assistant (LOA) ']/following::input[2]"));
		actions.moveToElement(input).perform();
		SoftAssert.assertEqual(getElementByUsing("AvgBPSLOAToolTip").isDisplayed(), true, "AvgBPSLOAToolTip ToolTip not shown");
		input = driver.findElement(By.xpath("//td[text()=' LO Assistant (LOA) ']/following::input[3]"));
		actions.moveToElement(input).perform();
		SoftAssert.assertEqual(getElementByUsing("SalaryLOAToolTip").isDisplayed(), true, "SalaryLOAToolTip ToolTip not shown");
		
		input = driver.findElement(By.xpath("//td[text()=' Sales Manager ']/following::input[1]"));
		actions.moveToElement(input).perform();
		SoftAssert.assertEqual(getElementByUsing("ProductionPercentageToolTip").isDisplayed(), true, "ProductionPercentageToolTip ToolTip not shown");
		input = driver.findElement(By.xpath("//td[text()=' Sales Manager ']/following::input[2]"));
		actions.moveToElement(input).perform();
		SoftAssert.assertEqual(getElementByUsing("AvgBPSSalesMangerToolTip").isDisplayed(), true, "AvgBPSSalesMangerToolTip ToolTip not shown");
		input = driver.findElement(By.xpath("//td[text()=' Sales Manager ']/following::input[3]"));
		actions.moveToElement(input).perform();
		SoftAssert.assertEqual(getElementByUsing("SalarySalesManagerToolTip").isDisplayed(), true, "SalarySalesManagerToolTip ToolTip not shown");
	
		input = driver.findElement(By.xpath("//td[text()=' Producing BM ']/following::input[2]"));
		actions.moveToElement(input).perform();
		SoftAssert.assertEqual(getElementByUsing("AvgBpsProducingBMToolTip").isDisplayed(), true, "AvgBpsProducingBMToolTip ToolTip not shown");
		input = driver.findElement(By.xpath("//td[text()=' Producing BM ']/following::input[3]"));
		actions.moveToElement(input).perform();
		SoftAssert.assertEqual(getElementByUsing("SalaryProducingBMToolTip").isDisplayed(), true, "SalaryProducingBMToolTip ToolTip not shown");
		input = driver.findElement(By.xpath("//td[text()=' Producing BM ']/following::input[4]"));
		actions.moveToElement(input).perform();
		SoftAssert.assertEqual(getElementByUsing("OverRideBPSProducingBMToolTip").isDisplayed(), true, "OverRideBPSProducingBMToolTip ToolTip not shown");
	
		input = driver.findElement(By.xpath("//td[text()=' Non Producing BM ']/following::input[1]"));
		actions.moveToElement(input).perform();
		SoftAssert.assertEqual(getElementByUsing("SalaryNonProducingBMToolTip").isDisplayed(), true, "SalaryNonProducingBMToolTip ToolTip not shown");
		input = driver.findElement(By.xpath("//td[text()=' Non Producing BM ']/following::input[2]"));
		actions.moveToElement(input).perform();
		SoftAssert.assertEqual(getElementByUsing("OverRideBPSProducingBMToolTip").isDisplayed(), true, "OverRideBPSProducingBMToolTip ToolTip not shown");
	
		input = driver.findElement(By.xpath("//td[text()=' Processors ']/following::input[1]"));
		actions.moveToElement(input).perform();
		SoftAssert.assertEqual(getElementByUsing("ProcessorsToolTip").isDisplayed(), true, "ProcessorsToolTip ToolTip not shown");
		input = driver.findElement(By.xpath("//td[text()=' All Other ']/following::input[1]"));
		actions.moveToElement(input).perform();
		SoftAssert.assertEqual(getElementByUsing("AllOthersCompensationToolTip").isDisplayed(), true, "AllOthersCompensationToolTip ToolTip not shown");
		
		int total = Integer.parseInt(driver.findElement(By.xpath("//td[text()=' Loan Officer (LO) ']/following::input[1]")).getAttribute("value"))+
				Integer.parseInt(driver.findElement(By.xpath("//td[text()=' LO Assistant (LOA) ']/following::input[1]")).getAttribute("value"))+
				Integer.parseInt(driver.findElement(By.xpath("//td[text()=' Sales Manager ']/following::input[1]")).getAttribute("value"))+
				Integer.parseInt(driver.findElement(By.xpath("//td[text()=' Producing BM ']/following::input[1]")).getAttribute("value"));
		
		SoftAssert.assertEqual(String.valueOf(total), "100", "Compensation Total Mismatch");
		
		
		int operationsTotal = Integer.parseInt(driver.findElement(By.xpath("//td[text()=' Processors ']/following::input[1]")).getAttribute("value"))+
				Integer.parseInt(driver.findElement(By.xpath("//td[text()=' All Other ']/following::input[1]")).getAttribute("value"));

		SoftAssert.assertEqual(String.valueOf(operationsTotal),
				driver.findElement(By.xpath("//td[text()=' Total']/following::span[1]")).getText()
						.replaceAll("[^0-9?!\\.]", ""),
				"operations Total Mismatch");

		SoftAssert.assertEqual(String.valueOf(operationsTotal),
				driver.findElement(By.xpath("//*[text()='Total Monthly Branch Compensation']/following::span[1]"))
						.getText().replaceAll("[^0-9?!\\.]", ""),
				"Total Monthly Branch Compensation Mismatch");

	}
	public void verifySGandA() throws TwfException, BiffException, IOException {
		WebDriver driver = DriverFactory.getDriver();
		Actions actions = new Actions(driver);
		WebElement input;
		input = driver.findElement(By.xpath("//td[text()=' Rent ']/following::input[1]"));
		actions.moveToElement(input).perform();
		SoftAssert.assertEqual(getElementByUsing("RentToolTip").isDisplayed(), true, "RentToolTip ToolTip not shown");

		input = driver.findElement(By.xpath("//td[text()=' Marketing & Leads ']/following::input[1]"));
		actions.moveToElement(input).perform();
		SoftAssert.assertEqual(getElementByUsing("MarketingLeadsToolTip").isDisplayed(), true, "MarketingLeadsToolTip ToolTip not shown");

		
		input = driver.findElement(By.xpath("//td[text()=' Telephone & Internet ']/following::input[1]"));
		actions.moveToElement(input).perform();
		SoftAssert.assertEqual(getElementByUsing("TelephoneAndNetworkToolTip").isDisplayed(), true, "TelephoneAndNetworkToolTip ToolTip not shown");

		input = driver.findElement(By.xpath("/(//td[text()=' All Other '])[2]/following::input[1]"));
		actions.moveToElement(input).perform();
		SoftAssert.assertEqual(getElementByUsing("TelephoneAndNetworkToolTip").isDisplayed(), true, "TelephoneAndNetworkToolTip ToolTip not shown");

		int total = Integer.parseInt(driver.findElement(By.xpath("//td[text()=' Rent ']/following::input[1]")).getAttribute("value"))+
				Integer.parseInt(driver.findElement(By.xpath("//td[text()=' Marketing & Leads ']/following::input[1]")).getAttribute("value"))+
				Integer.parseInt(driver.findElement(By.xpath("//td[text()=' Telephone & Internet ']/following::input[1]")).getAttribute("value"))+
				Integer.parseInt(driver.findElement(By.xpath("/(//td[text()=' All Other '])[2]/following::input[1]")).getAttribute("value"));
		
		SoftAssert.assertEqual(String.valueOf(total), driver.findElement(By.xpath("//*[text()=' SG&A ($$) Total']/following::span[1]"))
				.getText().replaceAll("[^0-9?!\\.]", ""), "SG&A ($$) Total Mismatch");
	}
	
	public void verifyDirectProductionCosts(Row row) throws BiffException, IOException, TwfException {
		SoftAssert.assertEqual(
				utils.floatRoundOffTo0Places(getElementByUsing("DirectProductionCosts").getAttribute("value")),
				utils.floatRoundOffTo0Places(utils.roundOffto2Decimalpoint5up(row.getCell(15).getNumericCellValue())),
				"DirectProductionCosts mismatch from MasterData");

	}
	
	public void verifyStaffingModel(Row row) throws BiffException, IOException, TwfException {
		SoftAssert.assertEqual(utils.roundOffto2Decimalpoint5up(getElementByUsing("AvgLoanSize")
				.getAttribute("value").replaceAll("[^0-9?!\\.]", "")), utils.roundOffto2Decimalpoint5up(row.getCell(16).getNumericCellValue()), "AvgLoanSize mismatch from MasterData");
		SoftAssert.assertEqual(utils.roundOffto2Decimalpoint5up(getElementByUsing("CurrentLOProductivity")
				.getAttribute("value").replaceAll("[^0-9?!\\.]", "")), utils.roundOffto2Decimalpoint5up(row.getCell(29).getNumericCellValue()), "CurrentLOProductivity mismatch from MasterData");
		SoftAssert.assertEqual(utils.roundOffto2Decimalpoint5up(getElementByUsing("LoanOfficersActual")
				.getAttribute("value").replaceAll("[^0-9?!\\.]", "")), "0", "LoanOfficersActual mismatch from MasterData");
		SoftAssert.assertEqual(utils.roundOffto2Decimalpoint5up(getElementByUsing("LoanOfficersRequiredToHit")
				.getAttribute("value").replaceAll("[^0-9?!\\.]", "")), "0.00", "LoanOfficersRequiredToHit mismatch from MasterData");
		SoftAssert.assertEqual(utils.roundOffto2Decimalpoint5up(getElementByUsing("LOAActual")
				.getAttribute("value").replaceAll("[^0-9?!\\.]", "")), "0", "LOAActual mismatch from MasterData");
		SoftAssert.assertEqual(utils.roundOffto2Decimalpoint5up(getElementByUsing("LOAProductivity")
				.getAttribute("value").replaceAll("[^0-9?!\\.]", "")), "0.00", "LOAProductivity mismatch from MasterData");
		
		SoftAssert.assertEqual(utils.roundOffto2Decimalpoint5up(getElementByUsing("ProcessorsStaffingActual")
				.getAttribute("value").replaceAll("[^0-9?!\\.]", "")), "0", "ProcessorsStaffingActual mismatch from MasterData");
		SoftAssert.assertEqual(utils.roundOffto2Decimalpoint5up(getElementByUsing("ProcessorsStaffingProductivity")
				.getAttribute("value").replaceAll("[^0-9?!\\.]", "")), "0.00", "ProcessorsStaffingProductivity mismatch from MasterData");
	}
	
	
	
	public static void main(String[] strings) throws IOException, TwfException, BiffException {
		BranchPlanning objExcelFile = new BranchPlanning();

		objExcelFile.verifyUploadData("");

	}

	@Override
	public void checkPage() {
		// TODO Auto-generated method stub

	}

}
