/**
 * 
 */
package com.tavant;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author mamatha.hadarageri
 *
 */
public class Value {
	
	@JsonProperty("testPlan")
    private String testPlan;

 

    public String getTestPlan() {
        return testPlan;
    }

 

    public void setTestPlan(String testPlan) {
        this.testPlan = testPlan;
    }
	

}
