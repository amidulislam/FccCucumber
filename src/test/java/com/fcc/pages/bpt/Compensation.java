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

public class Compensation {

	public String LoanOfficerProduction;
	public String LoanOfficerAssistantProduction;
	public String SalesManagerProduction;
	
	public String LoanOfficerAVGBPS;
	public String LoanOfficerAssistantAVGBPS;
	public String SalesManagerAVGBPS;
	public String ProducingBMAVGBPS;
	
	public String LoanOfficerSalary;
	public String LoanOfficerAssistantSalary;
	public String SalesManagerSalary;
	public String ProducingBMSalary;
	public String NonProducingBMSalary;
	
	public String ProducingBMOverrideBPS;
	public String NonProducingBMOverrideBPS;
	
	public String Processors;
	public String AllOthers;

	public enum CompensationEnum {

		LoanOfficerProduction("//td[text()=' Loan Officer (LO) ']/following::input[1]", ElementIdentificationType.XPATH),
		LoanOfficerAssistantProduction("//td[text()=' LO Assistant (LOA) ']/following::input[1]", ElementIdentificationType.XPATH),
		SalesManagerProduction("//td[text()=' Sales Manager ']/following::input[1]", ElementIdentificationType.XPATH),
		
		LoanOfficerAVGBPS("//td[text()=' Loan Officer (LO) ']/following::input[2]", ElementIdentificationType.XPATH),
		LoanOfficerAssistantAVGBPS("//td[text()=' LO Assistant (LOA) ']/following::input[2]", ElementIdentificationType.XPATH),
		SalesManagerAVGBPS("//td[text()=' Sales Manager ']/following::input[2]", ElementIdentificationType.XPATH),
		ProducingBMAVGBPS("//td[text()=' Producing BM ']/following::input[2]", ElementIdentificationType.XPATH),
		
		
		LoanOfficerSalary("//td[text()=' Loan Officer (LO) ']/following::input[3]", ElementIdentificationType.XPATH),
		LoanOfficerAssistantSalary("//td[text()=' LO Assistant (LOA) ']/following::input[3]", ElementIdentificationType.XPATH),
		SalesManagerSalary("//td[text()=' Sales Manager ']/following::input[3]", ElementIdentificationType.XPATH),
		ProducingBMSalary("//td[text()=' Producing BM ']/following::input[3]", ElementIdentificationType.XPATH),
		NonProducingBMSalary("//td[text()=' Non Producing BM ']/following::input[1]", ElementIdentificationType.XPATH),
		
		ProducingBMOverrideBPS("//td[text()=' Producing BM ']/following::input[4]", ElementIdentificationType.XPATH),
		NonProducingBMOverrideBPS("//td[text()=' Non Producing BM ']/following::input[1]", ElementIdentificationType.XPATH),
		
		Processors("//td[text()=' Processors ']/following::input[1]", ElementIdentificationType.XPATH),
		AllOthers("//td[text()=' All Other ']/following::input[1]", ElementIdentificationType.XPATH);
		
		
		

		private String eleIdentifier;
		private ElementIdentificationType iType;

		private CompensationEnum(String _eleIdentifier, ElementIdentificationType _type) {
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
			for (CompensationEnum object : CompensationEnum.values()) {
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
		Compensation one = new Compensation();
		Compensation thisClass = new Compensation();
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
