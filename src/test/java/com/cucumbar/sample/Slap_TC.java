package com.cucumbar.sample;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.fcc.test.CommonUtils;
import com.tavant.utils.TwfException;

//import com.tavant.utils.CommonUtils;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class Slap_TC {

	@Given("Launch Fcc URL")
	public void launch_Fcc_URL() throws InterruptedException, TwfException {
		
		System.out.println("....start of slap.........");
		
		System.setProperty("webdriver.chrome.driver", "D:/jan/pocccc_bddd/FccAutomation/ext/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
		driver.get("https://svcQaFCCAutomationAd:r50RLTVyRnFLSmbvWmJqhTbLAmacZj@qa-fcc.supremelending.com/");
		driver.manage().window().maximize();

		driver.findElement(By.xpath("//div[text()='SLAP']")).click();
		Thread.sleep(4000);
		ArrayList<String> allWindow = new ArrayList<String>(driver.getWindowHandles());
		
		driver.switchTo().window(allWindow.get(0));
		Thread.sleep(3000);
		driver.switchTo().window(allWindow.get(1));
		
		driver.findElement(By.xpath("//*[contains(@id,'img-tree-view')]")).click();
		driver.findElement(By.xpath("(//*[contains(@class,'ui-icon')])[9]")).click();
		driver.findElement(By.xpath("(//*[contains(@type,'submit')])[2]")).click();
		driver.quit();

		System.out.println("....End of slap.........");
	}

	@Then("Page gets launched")
	public void page_gets_launched() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@Then("Click On Slap Module")
	public void click_On_Slap_Module() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@Then("Slap home page should appear")
	public void slap_home_page_should_appear() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@Then("Select Desired Region from the drop down")
	public void select_Desired_Region_from_the_drop_down() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	@Then("Region must be selected")
	public void region_must_be_selected() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

}
