package com.fcc.pages.bpt;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;

import com.fcc.test.ElementIdentificationType;
import com.fcc.test.GenericUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.tavant.base.DriverFactory;
import com.tavant.utils.TwfException;

import jxl.read.biff.BiffException;

public class SGandA {
	public String Rent;
	public String MarketingLeads;
	public String TelephoneInternet;
	public String AllOther;
	

	public enum SGandAEnum {

		Rent("//td[text()=' Rent ']/following::input[1]", ElementIdentificationType.XPATH),
		MarketingLeads("//td[text()=' Marketing & Leads ']/following::input[1]", ElementIdentificationType.XPATH),
		TelephoneInternet("//td[text()=' Telephone & Internet ']/following::input[1]", ElementIdentificationType.XPATH),
		AllOther("/(//td[text()=' All Other '])[2]/following::input[1]", ElementIdentificationType.XPATH);
		

		private String eleIdentifier;
		private ElementIdentificationType iType;

		private SGandAEnum(String _eleIdentifier, ElementIdentificationType _type) {
			this.setEleIdentifier(_eleIdentifier);
			this.setiType(_type);
		}

		public String getEleIdentifier() {
			return eleIdentifier;
		}

		public void setEleIdentifier(String _eleIdentifier) {
			eleIdentifier = _eleIdentifier;
		}

		public ElementIdentificationType getiType() {
			return iType;
		}

		public void setiType(ElementIdentificationType _iType) {
			iType = _iType;
		}
	}

	public void setDataToUI() throws BiffException, IOException, TwfException, IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException, InterruptedException {

		System.out.println("Begin input data for Calculations from Json ");
		WebDriver driver = DriverFactory.getDriver();
		Thread.sleep(1000);
		Class thisClass = this.getClass();
		try {
			for (SGandAEnum object : SGandAEnum.values()) {
				Field fo = thisClass.getDeclaredField(object.toString());
				System.out.println("Input data for feild " + fo.getName());
				if (StringUtils.isNotEmpty((String) fo.get(this))) {
					GenericUtils.getInstance().inputValueToField(driver, object.name(), (String) fo.get(this),
							object.eleIdentifier, object.iType);
				} else {
					System.out.println("Input data for feild " + fo.getName() + " is not present");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, JsonIOException,
			IOException, IllegalAccessException {

		String root = System.getProperty("user.dir") + java.io.File.separator
				+ Thread.currentThread().getStackTrace()[1].getFileName().replaceAll(".java", ".json");
		SGandA one = new SGandA();
		SGandA thisClass = new SGandA();
		Field[] allFields = thisClass.getClass().getDeclaredFields();
		for (Field f : allFields) {

			f.set(one, "");
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FileWriter r = new FileWriter(root);
		r.write(gson.toJson(one));
		r.close();
		System.out.println(root);
	}
}
