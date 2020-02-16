$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("file:features/fcc_16392.feature");
formatter.feature({
  "name": "Test scenarios and commands",
  "description": "",
  "keyword": "Feature",
  "tags": [
    {
      "name": "@web"
    }
  ]
});
formatter.scenario({
  "name": "To test the following commands with Cucumber framework",
  "description": "",
  "keyword": "Scenario",
  "tags": [
    {
      "name": "@web"
    },
    {
      "name": "@TC001"
    }
  ]
});
formatter.before({
  "status": "passed"
});
formatter.before({
  "status": "passed"
});
formatter.step({
  "name": "Launch Fcc URL",
  "keyword": "Given "
});
formatter.match({
  "location": "fcc.launch_Fcc_URL()"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "Page gets launched",
  "keyword": "Then "
});
formatter.match({
  "location": "fcc.page_gets_launched()"
});
formatter.result({
  "error_message": "cucumber.api.PendingException: TODO: implement me\r\n\tat com.cucumbar.sample.fcc.page_gets_launched(fcc.java:55)\r\n\tat ✽.Page gets launched(file:features/fcc_16392.feature:8)\r\n",
  "status": "pending"
});
formatter.step({
  "name": "Click On Slap Module",
  "keyword": "Then "
});
formatter.match({
  "location": "fcc.click_On_Slap_Module()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "Slap home page should appear",
  "keyword": "Then "
});
formatter.match({
  "location": "fcc.slap_home_page_should_appear()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "Select Desired Region from the drop down",
  "keyword": "Then "
});
formatter.match({
  "location": "fcc.select_Desired_Region_from_the_drop_down()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "Region must be selected",
  "keyword": "Then "
});
formatter.match({
  "location": "fcc.region_must_be_selected()"
});
formatter.result({
  "status": "skipped"
});
formatter.after({
  "status": "passed"
});
formatter.after({
  "status": "passed"
});
});