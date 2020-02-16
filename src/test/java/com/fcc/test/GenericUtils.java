package com.fcc.test;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.tavant.utils.TwfException;

import jxl.read.biff.BiffException;

public class GenericUtils {

	private static final Logger logger = Logger.getLogger(GenericUtils.class) ;
	
	private static ThreadLocal<GenericUtils>  genericUtils=new ThreadLocal();
	public static GenericUtils getInstance() {
		if (genericUtils.get()==null)
			genericUtils.set(new GenericUtils()); 
		return (GenericUtils) genericUtils.get();
		
	}
	
	public GenericUtils() {
		// TODO Auto-generated constructor stub
	}

	public String getValueFromField(WebDriver driver, String tName, String eleIdentifier, ElementIdentificationType etype) throws BiffException, IOException, TwfException {
		String uiValue ="";
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			WebElement uiElement = getElement(driver, eleIdentifier,etype );
			String tagName = uiElement.getTagName();
			String uiType = uiElement.getAttribute("type");

			if (tagName.equalsIgnoreCase("input")) {
				if (uiType.equalsIgnoreCase("text")) {
					uiValue = uiElement.getAttribute("value");
				//} else if (uiType.equalsIgnoreCase("text") && !(uiElement.isEnabled())) {
				//	uiValue = uiElement.getAttribute("value");
				} else if (uiType.equalsIgnoreCase("radio")||uiType.equalsIgnoreCase("checkbox")) {
					uiValue = String.valueOf(uiElement.isSelected());
				}
			} else if (tagName.equalsIgnoreCase("select")) {
				try {
					uiValue =new Select(uiElement).getFirstSelectedOption().getText().trim();
				} catch (Exception e) {
					logger.error("No options selected for Element " + tName);
					uiValue = "";
				}
			}else if (tagName.equalsIgnoreCase("span")||tagName.equalsIgnoreCase("b")) {
						uiValue =uiElement.getText().trim();
					}
				
		} catch (Exception e) {
			logger.debug("Element " + tName+ " is not present or visible");
			logger.error(e);

		}
		logger.info("Value for element " + eleIdentifier +" is (" + uiValue + ")");
		driver.manage().timeouts().implicitlyWait(60L, TimeUnit.SECONDS);
		return uiValue;
		
	}
	
	public boolean isObjectEnabled(WebDriver driver, String tName, String eleIdentifier, ElementIdentificationType etype) throws BiffException, IOException, TwfException {
		
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		logger.debug("Check if"+tName+" enabled...");
		WebElement uiElement = getElement(driver, eleIdentifier,etype );
		boolean isEnabled = uiElement.isEnabled();
		logger.debug(tName+" enabled? == "+isEnabled);
		return isEnabled;
	}
	public void inputValueToField(WebDriver driver, String tName, String iValue, String eleIdentifier, ElementIdentificationType etype) throws BiffException, IOException, TwfException {
//		String uiValue ="";
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			WebElement uiElement = getElement(driver, eleIdentifier,etype );
			String tagName = uiElement.getTagName();
			String uiType = uiElement.getAttribute("type");

			if (tagName.equalsIgnoreCase("input")) {
				if (uiType.equalsIgnoreCase("text")) {
					uiElement.click();
					uiElement.clear();
					uiElement.click();
					uiElement.sendKeys(iValue);
				//} else if (uiType.equalsIgnoreCase("text") && !(uiElement.isEnabled())) {
				//	uiValue = uiElement.getAttribute("value");
				} else if (uiType.equalsIgnoreCase("radio")||uiType.equalsIgnoreCase("checkbox")) {
					if (uiElement.isSelected()!=Boolean.valueOf(iValue))
					uiElement.click();
				}
			} else if (tagName.equalsIgnoreCase("textarea")) {
				uiElement.click();
				uiElement.sendKeys(iValue);
			}else if (tagName.equalsIgnoreCase("select")) {
				try {
					new Select(uiElement).selectByVisibleText(iValue);;
				} catch (Exception e) {
					logger.error("No options selected for Element " + tName);
				}
			}/*else if (tagName.equalsIgnoreCase("span")) {
						uiValue =uiElement.getText();
					}*/
				
		} catch (Exception e) {
			logger.debug("Element " + tName+ " is not present or visible");
			logger.error(e);

		}
		logger.info("Value for element " + eleIdentifier +" is (complete )");
		driver.manage().timeouts().implicitlyWait(60L, TimeUnit.SECONDS);
		
	}
	
	public WebElement getElement(WebDriver driver, String eleIdentifier, ElementIdentificationType etype)
			throws BiffException, IOException, TwfException {
		try {
			logger.debug("Getting element " + eleIdentifier);
			String elementId = eleIdentifier;
			ElementIdentificationType elementIdentificationType = etype;
			WebElement element = null;
			switch (elementIdentificationType) {
			case ID:
				element = driver.findElement(By.id((String) elementId));
				break;
			case XPATH:
				element = driver.findElement(By.xpath((String) elementId));
				break;
			case NAME:
				element = driver.findElement(By.name((String) elementId));
				break;
			case CSS:
				element = driver.findElement(By.cssSelector((String) elementId));
				break;
			case LINKTEXT:
				element = driver.findElement(By.linkText((String) elementId));
				break;
			default:
				break;
			}
			return element;
		} catch (NullPointerException e) {
			throw new TwfException("Problem in finding Element " + eleIdentifier + " Check for Identification type");
		} catch (Exception e) {
			throw new TwfException("Problem in finding Element " + eleIdentifier);
		}
	}
}

