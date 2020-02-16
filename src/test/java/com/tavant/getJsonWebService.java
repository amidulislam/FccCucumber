package com.tavant;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class getJsonWebService {
	
	 public static void main(String[] args) {
         try {


           URL url = new URL("https://dev.azure.com/supremelending/FCC/_apis/testplan/Plans/13733/Suites/13741/TestCase");
       	 // URL url = new URL("https://cnbcdigital.testrail.net/index.php?/cases/view/35423330");
          // https://dev.azure.com/supremelending/FCC/_testPlans/define?planId=11801&suiteId=11806
           //Test Case ID:12211
           //Test Case Name:BLAST Update formatting for Variance Feature-DOD Variance -Consolidated 
           //URL url = new URL("https://{instance}/{collection}/{project}/_apis/test/Plans/{planId}/suites/{suiteId}/testcases?api-version=5.0");
           //"https://{instance}/{collection}/{FCC}/_apis/test/Plans/{11801}/suites/{11806}/testcases?api-version=5.0");
           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
           conn.setRequestMethod("GET");
           conn.setRequestProperty("Accept", "application/json");


           if (conn.getResponseCode() != 200) {
               throw new RuntimeException("Failed : HTTP error code : "
                       + conn.getResponseCode());
           }


           BufferedReader br = new BufferedReader(new InputStreamReader(
               (conn.getInputStream())));

           
           String path_DestinationFeatureFile=System.getProperty("user.dir")+"\\FeatureFiles\\temp.feature";
           System.out.println(path_DestinationFeatureFile);
           
           FileWriter myWriter = new FileWriter(path_DestinationFeatureFile);
           
           String output;
           System.out.println("Output from Server .... \n");
           
           while ((output = br.readLine()) != null) 
           
              {
           	myWriter.write(output);
               System.out.println(output);
           }
           myWriter.close();
           conn.disconnect();

         } catch (MalformedURLException e) {

           e.printStackTrace();

         } catch (IOException e) {

           e.printStackTrace();

         }

       }

}
