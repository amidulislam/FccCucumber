package com.automation.cucumber_test;

import cucumber.api.testng.AbstractTestNGCucumberTests;
import cucumber.api.CucumberOptions;

@CucumberOptions(strict = false, features = "features", glue = { "com.tavant.automationFramework.stepDef","com.cucumbar.sample" }, tags = { "@web" }, monochrome = false, plugin = { "pretty", "json:target/cucumber.json", "html:report/cucumber-reports" })
public class Runner extends AbstractTestNGCucumberTests {

}
