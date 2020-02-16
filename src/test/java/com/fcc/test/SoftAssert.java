package com.fcc.test;
import org.apache.log4j.Logger;

import com.tavant.utils.TwfException;


public class SoftAssert {

	private static final Logger logger = Logger.getLogger(SoftAssert.class) ;
	private static ThreadLocal<SoftAssert>  softAssert=new ThreadLocal();
	
	private org.testng.asserts.SoftAssert sAssert = null;
	
	SoftAssert() {
		sAssert =new org.testng.asserts.SoftAssert();
	}
	
	private static SoftAssert getInstance() {
		if (softAssert.get()==null)
			softAssert.set(new SoftAssert()); 
		return (SoftAssert) softAssert.get();
		
	}
	
	public static void assertEqual(Object actual, Object expected, String msg) {
		System.out.println("Asserting : " +msg + " Expected [" + expected.toString() + "] actual is [" + actual.toString() + "]\n");
		getInstance().sAssert.assertEquals(actual, expected, msg + "Expected [" + expected + "] actual is [" + actual + "]\n");
	}

	public static void assertUnEqual(Object actual, Object expected, String msg) {
		//logger.debug("Using thread id " + Thread.currentThread().getId() + " : Asserting : " +msg + " Expected [" + expected.toString() + "] actual is [" + actual.toString() + "]\n");
		getInstance().sAssert.assertEquals(actual, expected, msg+"Expected [" + expected + "] actual is [" + actual + "]\n\n\n");
	}
	
	public static void assertTrue(boolean condition, String msg) {
		logger.debug("Using thread id " + Thread.currentThread().getId() + " : Asserting : " +msg +" Expected [true] actual is ["+ condition+ "]\n");
		getInstance().sAssert.assertTrue(condition, msg);
	}
	
	public static void assertEquals(Object actual, Object expected, String msg) {
	//	logger.debug("Using thread id " + Thread.currentThread().getId() + " : Asserting : " +msg + " Expected [" + expected.toString() + "] actual is [" + actual.toString() + "]\n");
		System.out.println(" Asserting : Expected [" + expected.toString() + "] actual is [" + actual.toString() + "]\n");
		getInstance().sAssert.assertEquals(actual, expected, msg );
	}
	

	public static void assertFalse(boolean condition, String msg) {
		logger.debug("Using thread id " + Thread.currentThread().getId() + " : Asserting : " +msg +" Expected [false] actual is ["+ condition+ "]\n");
		getInstance().sAssert.assertFalse(condition, msg);
	}
	
	public static void assertAll() throws TwfException {
		try {
			getInstance().sAssert.assertAll();
			getInstance().sAssert =new org.testng.asserts.SoftAssert();
		} catch (AssertionError e) {
			e.printStackTrace();
			getInstance().sAssert =new org.testng.asserts.SoftAssert();
			softAssert.remove();
			throw new TwfException(e.getMessage());
		}
	}
}

