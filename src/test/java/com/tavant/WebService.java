package com.tavant;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.String;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;

//import javax.ws.rs.core.HttpHeaders;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebService {

	public static void main(String[] args) throws Exception {
		WebService objWebService=new WebService();
		objWebService.performProductSearch();
	}

	/**
	 * @param displayedTotalProducts
	 * @return
	 * @throws Exception
	 */
	public void performProductSearch() throws Exception {
		
		try {
			System.out.println("Initiating OB Call........");
		
			//To read API URL from properties
			String fccApiGetUrlPath= System.getProperty("user.dir") + "\\fcc.properties";
			System.out.println("----------fccApiGetUrlPath=="+fccApiGetUrlPath);
			
			File file = new File(fccApiGetUrlPath);
            FileInputStream fileInput = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fileInput);
            String azureTestCaseApiUrl;
            String azureSourceUrl = properties.getProperty("TC1_TestcaseApiGETurl");  
            System.out.println(".-------azureTestCaseApiUrl.."+azureSourceUrl);
            
            String TestCaseDetails = properties.getProperty("TestCaseDetails");
            System.out.println("------TestCaseDetails----" +TestCaseDetails);
            String[] arrTestCaseDetails=null;
            if(TestCaseDetails.contains(","))
            {
            	arrTestCaseDetails = TestCaseDetails.split(",");
            }
                       
            String[] testCaseInfo=null;
            String testPlanId,testSuiteId,testCaseId;
            for(String testCase:arrTestCaseDetails)
            {
            	testCaseInfo=testCase.split("#");
            	testPlanId=testCaseInfo[0];
            	testSuiteId=testCaseInfo[1];
            	testCaseId=testCaseInfo[2];
            	azureTestCaseApiUrl=azureSourceUrl.replace("PLAND_ID", testPlanId).replace("SUITES_ID", testSuiteId).replace("TESTCASE_ID", testCaseId);
            	System.out.println("...azureTestCaseApiUrl.------."+azureTestCaseApiUrl);
            	this.CreateFeatureFileFromAzureTestCase(azureTestCaseApiUrl, testCaseId);            	   
           }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
		
          			
    //To send APRI URL and get Json Response
	public void CreateFeatureFileFromAzureTestCase(String azureTestCaseApiUrl,String testCaseId) throws Exception
	{
		
		System.out.println(".....testCaseId----.."+testCaseId);
		HttpHeaders he = new HttpHeaders();		
		String encodedUserCreds = Base64.getEncoder()
				.encodeToString("username:idat5qy7bk5rqj6gh76ykwiunmli43rtorlma3gmnurh62f2s4aq".getBytes());
		
		he = getHeaders(he, "ContentType%APPLICATION_FORM_URLENCODED&&Accept%application/json&&Authorization%Basic"
				+ encodedUserCreds);

		HttpEntity<String> entity = new HttpEntity<String>(he);

		ResponseEntity<?> re = returnResponse(azureTestCaseApiUrl,HttpMethod.GET, entity, String.class);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		System.out.println("Json--Response>>>>------"+re.getBody().toString());			
		Map<String, Object> map = JSONUtils.JsonToMap((String) re.getBody());
		System.out.println("Value>>>------" + map.get("value").toString().replace("   ", " "));
		List<Map<String, Object>> map1 = (List<Map<String, Object>>) map.get("value");
		for (Map<String, Object> mapentry : map1) 
		{
			Map<String, Object> workitem_Obj = ((List<Map<String, Object>>) ((Map) mapentry.get("workItem"))
					.get("workItemFields")).get(0);
			System.out.println("workItem>>>>>>>" + workitem_Obj.get("Microsoft.VSTS.TCM.Steps"));
			String str = String.valueOf(workitem_Obj.get("Microsoft.VSTS.TCM.Steps"));
			String arrTempTestCases[] = str.split("P&gt;");
			ArrayList<String> list = new ArrayList<String>();
		
			String tempStr;
			for (int j = 0; j < arrTempTestCases.length; j++) {
				tempStr = arrTempTestCases[j];
				if (j % 2 != 0 & !tempStr.contains("true\">")) {
					//tempStr = tempStr.replace("&lt;/", "").trim();
					list.add(tempStr);
				}
			}
			for (String s : list) {
				System.out.println("...workItem---..."+s);
			}
			WebService objWebService = new WebService();
			objWebService.createFeatureFile(list,testCaseId);
		}
        
        }
		/**
	 * Get Header Values
	 * 
	 * @param he
	 * @param headerValues
	 * @return
	 */

	private static HttpHeaders getHeaders(HttpHeaders he, String headerValues) {
		MediaType contentType = null;
		for (String s : headerValues.split("&&")) {

			contentType = MediaType.APPLICATION_FORM_URLENCODED;

			if (s.trim().contains("ContentType")) {
				he.setContentType(contentType);
			} else if (s.trim().contains("Accept")) {
				he.set("Accept", "application/json");

			} else {
				he.add(s.split("%")[0], s.split("%")[1]);
			}
		}
		return he;
	}

	private static ResponseEntity<?> returnResponse(String url, HttpMethod action, HttpEntity<?> entity,
			Class<?> class1) throws Exception {
		RestTemplate rt = new RestTemplate();
		ResponseEntity<?> re = null;
		try {
			re = rt.exchange(url, action, entity, class1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return re;
	}

	void createFeatureFile(List<String> strList,String testCaseId) throws IOException {
		
		System.out.println(".createFeatureFile--.testCaseId--------."+testCaseId);
		
		String path_DestinationFeatureFile = System.getProperty("user.dir") + "\\features\\fcc_"+testCaseId+".feature";
		System.out.println("------path_DestinationFeatureFile--"+path_DestinationFeatureFile);

		FileWriter myWriter = new FileWriter(path_DestinationFeatureFile);

		// String output;
		System.out.println("Output from Server .... \n");

		String prefix = "@web\nFeature: Test scenarios and commands \n\n@TC001 \nScenario: To test the following commands with Cucumber framework\n\n";

		myWriter.write(prefix);

		for (String s : strList) {
			myWriter.write(s);
			myWriter.write("\n");
		}
		myWriter.close();

	}

}
