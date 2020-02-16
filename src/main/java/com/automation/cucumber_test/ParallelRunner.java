package com.automation.cucumber_test;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;



@CucumberOptions(strict = false, features = "features", glue = { "com.tavant.automationFramework" }, tags = { "~@ignore" }, monochrome = false, plugin = { "pretty", "json:target/cucumber.json", "html:target/cucumber-reports", "rerun:target/rerun.txt" })
//@Listeners({ com.DashBoardListener.DashBoardListener.ListenerDetails.class })
public class ParallelRunner extends AbstractTestNGCucumberTests {

    @BeforeClass
    public static void setUpSuite() {
        System.setProperty("jagacy.properties.dir", "features");
        System.setProperty("test.window", "true");
    }
    
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
