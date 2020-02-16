package com.fcc.pages.bpt;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.fcc.test.ElementIdentificationType;
import com.fcc.test.GenericUtils;

import org.apache.commons.lang3.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.tavant.base.DriverFactory;
import com.tavant.utils.TwfException;

import jxl.read.biff.BiffException;

public class ProductMix {

	public String ConventionalMargin;
	public String GovernmentMargin;
	public String JumboMargin;
	public String BondMargin;
	public String FeeIncomeMargin;

	public String ConventionalMix;
	public String GovernmentMix;
	public String BondMix;
	public String FeeIncomeMix;

	public enum ProductMixEnum {

		ConventionalMargin("//td[text()=' Conventional ']/following::input[3]", ElementIdentificationType.XPATH),
		GovernmentMargin("//td[text()=' Government ']/following::input[3]", ElementIdentificationType.XPATH),
		JumboMargin("//td[text()=' Government ']/following::input[3]", ElementIdentificationType.XPATH),
		BondMargin("//td[text()=' Bond ']/following::input[3]", ElementIdentificationType.XPATH),
		FeeIncomeMargin("//td[text()=' Fee Income ']/following::input[3]", ElementIdentificationType.XPATH),
		ConventionalMix("//td[text()=' Conventional ']/following::input[4]", ElementIdentificationType.XPATH),
		GovernmentMix("//td[text()=' Government ']/following::input[4]", ElementIdentificationType.XPATH),
		BondMix("//td[text()=' Government ']/following::input[4]", ElementIdentificationType.XPATH),
		FeeIncomeMix("//td[text()=' Bond ']/following::input[4]", ElementIdentificationType.XPATH);

		private String eleIdentifier;
		private ElementIdentificationType iType;

		private ProductMixEnum(String _eleIdentifier, ElementIdentificationType _type) {
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
			for (ProductMixEnum object : ProductMixEnum.values()) {
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
		ProductMix one = new ProductMix();
		ProductMix thisClass = new ProductMix();
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
