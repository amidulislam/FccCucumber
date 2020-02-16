package com.cucumbar.sample;
import io.cucumber.java.en.Given;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.fcc.test.*;
import com.fcc.test.BLAST.*;
import com.tavant.base.DriverFactory;

public class sampleSteps {

	@Given("I launch the {string} URL in browser")
	public void i_launch_the_URL_in_browser(String string) {
		System.setProperty("webdriver.chrome.driver", "D:/jan/pocccc_bddd/FccAutomation/ext/chromedriver.exe");
		WebDriver driver=new ChromeDriver();
		driver.get("https://svcQaFCCAutomationAd:r50RLTVyRnFLSmbvWmJqhTbLAmacZj@qa-fcc.supremelending.com/");
		driver.manage().window().maximize();
	    throw new cucumber.api.PendingException();
	}
	
	 @Given("I click on button$")
	    public void i_click_on_button() throws Throwable {

	/*	 System.out.println("...Slap......");
		 WebDriver driver = DriverFactory.getDriver();
		 driver.manage().window().maximize();
		 CommonUtils commonUtils_obj = new CommonUtils();
		 BLAST blast_obj = new BLAST();
		 commonUtils_obj.clickFCCModule("SLAP");
		 commonUtils_obj.switchToCurrentWindow("");
		// commonUtils_obj.selectMonth();
		// blast_obj.selectRselectRselectRegion("");
		// blast_obj.verifyDrilldownPageGrandTotal("");*/
	 }
	 
	/* @Given("^I click on button$")
	    public void i_click_on_button1() throws Throwable {

		 System.out.println("...blast......");
	 
	 }*/
	 
	 @Given("I click on button{int}")
	 public void i_click_on_button(Integer int1) {

	 }
	 
}
