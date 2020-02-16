


	package com.fcc.test;

	import java.util.Arrays;

	import org.openqa.selenium.By;
	import org.openqa.selenium.JavascriptExecutor;
	import org.openqa.selenium.Keys;
	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.WebElement;
	import org.openqa.selenium.chrome.ChromeDriver;
	import org.openqa.selenium.chrome.ChromeOptions;
	import org.openqa.selenium.interactions.Action;
	import org.openqa.selenium.interactions.Actions;
	import org.openqa.selenium.remote.DesiredCapabilities;
	import org.openqa.selenium.support.ui.ExpectedConditions;
	import org.openqa.selenium.support.ui.WebDriverWait;
	import org.testng.annotations.Test;

	import com.tavant.base.DriverFactory;
	import com.tavant.utils.TwfException;

	import org.testng.annotations.Test;
	public class ChromeSetting{
	WebDriver driver;
	String strChromeDriverPath="D:\\FireKWDemo\\driver\\chromedriver.exe";
	//@Test
	    public void clearCache() throws InterruptedException {
	        System.setProperty("webdriver.chrome.driver",strChromeDriverPath);
	        ChromeOptions chromeOptions = new ChromeOptions();
	        chromeOptions.addArguments("disable-infobars");
	        chromeOptions.addArguments("start-maximized");
	        driver = new ChromeDriver(chromeOptions);
	        //driver.SendKeys($"{Keys.Control + "h"}")
	        driver.get("chrome://settings/clearBrowserData");
	        Thread.sleep(5000);
	        driver.switchTo().activeElement();
	        driver.findElement(By.cssSelector("* /deep/ #clearBrowsingDataConfirm")).click();
	        Thread.sleep(5000);
	    }
	 //@SuppressWarnings("deprecation")
	//@Test
	    public void launchWithoutCache() throws InterruptedException {
	        System.setProperty("webdriver.chrome.driver","C://WebDrivers/chromedriver.exe");
	        DesiredCapabilities cap = DesiredCapabilities.chrome();
	        cap.setCapability("applicationCacheEnabled", false);
	        driver = new ChromeDriver(cap);
	    }
	  @Test
	  public void f() throws InterruptedException {  System.setProperty("webdriver.chrome.driver","D:\\FireKWDemo\\driver\\chromedriver.exe");
	  driver=new ChromeDriver();
	  driver.get("chrome://settings/clearBrowserData");
	  Thread.sleep(3000);
	  //By strClear=By.cssSelector("* /deep/ #clearBrowsingDataConfirm");
	    By strClear=By.cssSelector("* /deep/ #clearBrowsingDataConfirm");
	    Actions actions = new Actions(driver);
	    actions.sendKeys(Keys.ENTER);
	    actions.perform();
	  //this.clickWhenReady(strClear, 30);

	 /* ChromeOptions options = new ChromeOptions(); options.addArguments("start-maximized");
	options.addArguments("--disable-extensions");
	options.addArguments("--user-data-dir=C:\\users\\" + System.getProperty("user.name") + "\\AppData\\Local\\Google\\Chrome\\User Data\\Profile 1"); options.addArguments("--no-sandbox");
	options.setExperimentalOption("detach", true); System.setProperty("webdriver.chrome.driver", strChromeDriverPath); //WebDriver driver = null;
	      System.setProperty("webdriver.chrome.driver", strChromeDriverPath);
	      System.setProperty("webdriver.chrome.logfile", "d:\\chromedriver.log");
	      ChromeOptions cOptions = new ChromeOptions();
	      cOptions.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
	      cOptions.addArguments("start-maximized");
	      driver = new ChromeDriver(cOptions);

	driver.manage().deleteAllCookies();*/
	}



	  public void clickWhenReady(By locator,int timeout)
	  {
	  WebElement element =null;
	  WebDriverWait wait=new WebDriverWait(driver,timeout);
	  element=wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	  element=wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	  element=wait.until(ExpectedConditions.elementToBeClickable(locator));
	  element.click();
	  }
	}
	
