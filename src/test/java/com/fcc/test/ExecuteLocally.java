package com.fcc.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class ExecuteLocally {

	public ExecuteLocally() {
		// TODO Auto-generated constructor stub
	}
	
	public static void runTestNGTest() {
		System.setProperty("env", "custom");
		System.setProperty("xml", "custom");
		 TestNG myTestNG = new TestNG();
		 XmlSuite mySuite = new XmlSuite();
		 mySuite.setName("Fire Test Suite");
		 XmlTest myTest = new XmlTest(mySuite);
		 myTest.setName("FIRE WEB Executor");
		 
		 List<XmlClass> myClasses = new ArrayList<XmlClass> ();
		 myClasses.add(new XmlClass("com.tavant.kwutils.MyTestCaseExecuter"));
		 myTest.setXmlClasses(myClasses);
		 List<XmlTest> myTests = new ArrayList<XmlTest>();
		 myTests.add(myTest);
		
		 mySuite.addListener("com.tavant.utils.TestReporter");
		 mySuite.addListener("com.tavant.utils.seqController");
		 List<XmlSuite> mySuites = new ArrayList<XmlSuite>();
		 mySuites.add(mySuite);
		 System.out.println(mySuite.toXml());
		 myTestNG.setXmlSuites(mySuites);
		 myTestNG.run();
	}
	
	public static void main(String[] args)  {
		
	
		
		
		runTestNGTest();
	}
}

