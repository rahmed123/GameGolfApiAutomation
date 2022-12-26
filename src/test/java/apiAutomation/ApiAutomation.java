package apiAutomation;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.google.gson.stream.JsonWriter;
import com.mongodb.diagnostics.logging.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utilities.ReportingListener;

@Listeners(ReportingListener.class)
public class ApiAutomation {

	// Global Variables
	public String baseURI = "http://stg-yiiportal-service.gameyourgame.com/";
	public String cookies;
	public String username = "briansexton";
	public String password = "1234ASdf";
	public String userId = "";
	public String roundId = "";
	public ExtentHtmlReporter htmlReporter;
	public ExtentReports extent;
	public ExtentTest test;
	public String test1 = "123";

	// Getting Session cookies and Creating Object for Extent Report
	@BeforeTest
	public void POSTLoginUser() {

		// Creating Objects for Extent Report
		htmlReporter = new ExtentHtmlReporter("TestReport.html");
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);

		RestAssured.baseURI = this.baseURI;
		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Payload for Post request
		JSONObject requestParams = new JSONObject();
		requestParams.put("username", this.username);
		requestParams.put("password", this.password);
		httpRequest.header("Content-Type", "application/json");
		httpRequest.body(requestParams.toJSONString());

		// Response Object
		Response response = httpRequest.request(Method.POST, "user/login");

		// Getting Response Code
		int code = response.getStatusCode();
		// System.out.println("Response Code is:" + code);
		Assert.assertEquals(200, code);

		String phpsession = response.getCookie("PHPSESSID");
		String gygUser = response.getCookie("gygUser");

		// Create cookies required to send in other requests

		phpsession = "PHPSESSID=" + phpsession + "; ";
		gygUser = "gygUser=" + gygUser;
		this.cookies = (phpsession + gygUser);
		System.out.println(cookies);

	}

	@Test(priority = 1)
	public void TC0001_GETUserStats() throws Exception {

		// Extent report Test initialization
		test = extent.createTest("Test Case1", "This request is to get user stats");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Sending request and getting response
		String relativeURL = "api/user?with=user_stats";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Set user id to global userid variable
		JsonPath jsonPath = response.jsonPath();
		this.userId = jsonPath.get("user.id").toString();

		// Getting and validating Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 1: " + code);
		System.out.println("User id for Testcase 1: " + this.userId);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 300) {
			test.pass("Successfully executed");
		} else {
			test.fail("Status code is " + code + "");
		}

	}

	@Test(priority = 2)
	public void TC0002_GETRoundStats() {
		// Specify base URI

		test = extent.createTest("Test Case 2", "This request is to get Round stats");
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Sending request and getting response
		String relativeURL = "api/round?with=round_stats,golfcourse";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asPrettyString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 2: " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code != 200 || response.body() == null) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is " + code);
		}

	}

	@Test(priority = 3)
	public void TC0003_GETUserAttribute() {

		// Extent Report
		test = extent.createTest("Test Case 3", "This request is to get User Attribute");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response
		String relativeURL = "api/user/attribute_library?locale=en-US";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 3: " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is " + code + "");
		}
	}

	@Test(priority = 4)
	public void TC0004_GETActivityFeed() throws InterruptedException {
		// Extent Report
		test = extent.createTest("Test Case 4", "This request is to get Activity Feed");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response
		String relativeURL = "api/activity_feed?id=" + this.userId + "&username=" + this.username
				+ "&limit=10&offset=0&locale=en-US&group_by_story=1";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Getting and Setting round id
		JsonPath jsonPath = response.jsonPath();

		this.roundId = jsonPath.get("activity[0].data.round_id").toString();

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 4: " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is " + code + "");
		}

	}

	@Test(priority = 5)
	public void TC0005_GETRecommendedUsers() {
		// Extent Report
		test = extent.createTest("Test Case 5", "This request is to get Recommended Users");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response
		String relativeURL = "api/user/recommended.json?limit=20&with=user_stats";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// JsonPath jsonPath = response.jsonPath();

		// System.out.println(jsonPath.get("resultset_info.limit"));
		// String resultInfo = response.jsonPath().get("1.body.resultset_info.limit");
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 5 " + code);
		Assert.assertEquals(200, code);
		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is " + code);
		}

	}

	@Test(priority = 6)
	public void TC0006_GETComments() {
		// Extent Report
		test = extent.createTest("Test Case 6", "This request is to get Comments for Activity");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response
		String relativeURL = "api/round/" + this.roundId + "/comment?limit=3";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 6 " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is " + code);
		}

	}

	@Test(priority = 7)
	public void TC0007_POSTComments() {

		// Extent Report
		test = extent.createTest("Test Case 7", "This request is to Post Comment for Activity");

		// Specify base URI

		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Payload for Post request
		JSONObject requestParams = new JSONObject();
		requestParams.put("body", "test comment from API Automation");

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Request Body
		httpRequest.body(requestParams.toJSONString());

		// Response Object
		String relativeURL = "api/round/" + this.roundId + "/comment";
		Response response = httpRequest.request(Method.POST, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 7 " + code);
		Assert.assertEquals(200, code);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is " + code);
		}
	}

	@Test(priority = 8)
	public void TC0008_GETFollowings() {

		// Extent Report
		test = extent.createTest("Test Case 8", "This request is to get Followings");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response
		String relativeURL = "api/follower?limit=30&offset=0&with=user_stats";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 8 " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is " + code);
		}
	}

	@Test(priority = 9)
	public void TC0009_GETFollowers() {

		// Extent Report
		test = extent.createTest("Test Case 9", "This request is to get User Followers");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response
		String relativeURL = "api/following?limit=0&with=user_stats";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 9 " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is " + code);
		}

	}

	/*
	 * @Test(priority = 9) // Unable to run due to query parameters issues public
	 * void TC0009_POSTSearchfollowers() { //Specify base URI
	 * 
	 * 
	 * RestAssured.baseURI = "https://www.gamegolf.com/api/" ;
	 * 
	 * 
	 * //Creating an Object for request RequestSpecification httpRequest =
	 * RestAssured.given();
	 * 
	 * //Request Payload for Post request JSONObject requestParams = new
	 * JSONObject(); requestParams.put("offset","0");
	 * requestParams.put("query_filter","fullname");
	 * requestParams.put("fullname","b"); requestParams.put("limit","2");
	 * 
	 * "{\"offset\":0,\"query_filter\":{\"fullname\":\"b\"},\"limit\":21}
	 * 
	 * 
	 * // Request Headers httpRequest.header("Content-Type","application/json");
	 * httpRequest.header("Cookie",this.cookies);
	 * 
	 * //Request Body httpRequest.body(requestParams.toJSONString());
	 * 
	 * //Response Object Response response = httpRequest.request(Method.POST,
	 * "user/search?default_operator=AND&with=you_follow");
	 * 
	 * //Print Response in Console Window String responseBody =
	 * response.getBody().asString(); //System.out.println("Response Body is :" +
	 * responseBody);
	 * 
	 * // Getting Response Code int code = response.getStatusCode();
	 * System.out.println("Response Code for Testcase 9 " + code);
	 * Assert.assertEquals(200, code); }
	 */

	@Test(priority = 10)
	public void TC0010_GETPro() {

		// Extent Report
		test = extent.createTest("Test Case 10", "This request is to get Pro");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response
		String relativeURL = "api/user/jmfloop/profile";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 10 " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is " + code);
		}

	}

	@Test(priority = 11)
	public void TC0011_GETPro() {

		// Extent Report
		test = extent.createTest("Test Case 11", "This request is to get Pro");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response
		String relativeURL = "api/user/gmac/profile";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 11 " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is " + code);
		}

	}

	@Test(priority = 12)
	public void TC0012_GETPro() {

		// Extent Report
		test = extent.createTest("Test Case 12", "This request is to get Pro");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response
		String relativeURL = "api/user/leew/profile";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 12 " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is " + code);
		}

	}

	@Test(priority = 13)
	public void TC00013_POSTSearchPlayer() {

		// Extent Report
		test = extent.createTest("Test Case 13", "This request is to Search Players");

		// Specify base URI

		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Payload for Post request
		JSONObject requestParams = new JSONObject();
		requestParams.put("fullname", "jane");
		requestParams.put("offset", "0");
		requestParams.put("limit", "21");

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : POST");

		// Request Body
		httpRequest.body(requestParams.toJSONString());

		// Response Object
		String relativeURL = "api/user/search?default_operator=AND&with=you_follow";
		Response response = httpRequest.request(Method.POST, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 13 " + code);
		Assert.assertEquals(200, code);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is " + code);
		}
	}

	@Test(priority = 14)
	public void TC0014_GETCompareAllRounds() {

		// Extent Report
		test = extent.createTest("Test Case 14", "This request is to get compare stats for All rounds");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response
		String relativeURL = "api/stats/series?user_id=218263";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 14 " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is " + code);
		}

	}

	@Test(priority = 15)
	public void TC0015_GETCompareLastTenRounds() {

		// Extent Report
		test = extent.createTest("Test Case 15", "This request is to get compare stats for Last 10 rounds");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response
		String relativeURL = "api/stats/user?user_id=218263&from_offset=0&to_offset=9";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 15 " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is " + code);
		}
	}

	@Test(priority = 16)
	public void TC0016_GETGolfCourse() {

		// Extent Report
		test = extent.createTest("Test Case 16", "This request is to get Golfcourse Data");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response
		String relativeURL = "api/golfcourse/605";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 16 " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is " + code);
		}

	}

	@Test(priority = 17)
	public void TC0017_GETGolfCoursePlayers() {

		// Extent Report
		test = extent.createTest("Test Case 17", "This request is to get Golfcourse Players");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response
		String relativeURL = "api/golfcourse/605/players?limit=25";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 17 " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is " + code);
		}

	}

	@Test(priority = 18)
	public void TC0018_GETGolfCourseRounds() {

		// Extent Report
		test = extent.createTest("Test Case 18", "This request is to get Golfcourse Players");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response
		String relativeURL = "api/golfcourse/605/rounds?limit=25&sort=stats.normalized_under_over:asc";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 18 " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is " + code);
		}

	}

	@Test(priority = 19)
	public void TC0019_GETGolfCourseNearby() {

		// Extent Report
		test = extent.createTest("Test Case 19", "This request is to get Golfcourse Nearby");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response
		String relativeURL = "api/golfcourse/605/nearby?limit=25&with=scorecard";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 19 " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is " + code);
		}

	}

	@Test(priority = 20)
	public void TC0020_GETGolfCourseActivities() {

		// Extent Report
		test = extent.createTest("Test Case 20", "This request is to get Golfcourse Activities");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response
		String relativeURL = "api/golfcourse/605/activity?courseid=605&limit=10&offset=0&group_by_story=1&locale=en-US";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 20 " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is " + code);
		}

	}

	@Test(priority = 21)
	public void TC0021_GETStrokeGainLastRound() {

		// Extent Report
		test = extent.createTest("Test Case 21", "This request is to get Stroke Gain for Last Round");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response
		String relativeURL = "api/stats/strokes_gained?v=2&user_id=" + this.userId + "&rounds_offset=0&rounds_limit=1";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 21 " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is " + code);
		}

	}

	@Test(priority = 22)
	public void TC0022_GETStrokeGainLastRound() {

		// Extent Report
		test = extent.createTest("Test Case 22", "This request is to get Stroke Gain for last three rounds");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response
		String relativeURL = "api/stats/strokes_gained?v=2&user_id=" + this.userId + "&rounds_offset=0&rounds_limit=3";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 22 " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is " + code);
		}

	}

	@Test(priority = 23)
	public void TC0023_GETApproachMissLastRound() {

		// Extent Report
		test = extent.createTest("Test Case 23", "Insights - Approach miss Last round max distance = 100 ");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response
		String relativeURL = "api/stats/approach_miss?v=2&user_id=" + this.userId
				+ "&rounds_offset=0&rounds_limit=1&max_distance=100";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 23 " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is " + code);
		}

	}

	@Test(priority = 24)
	public void TC0024_GETApproachMissLastRound() {

		// Extent Report
		test = extent.createTest("Test Case 24",
				"Insights - Approach miss Last round, min distance = 100 and max distance = 150 ");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response

		String relativeURL = "api/stats/approach_miss?v=2&user_id=" + this.userId
				+ "&rounds_offset=0&rounds_limit=1&max_distance=150&min_distance=100";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 24 " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is" + code);
		}
	}

	@Test(priority = 25)
	public void TC0025_GETApproachMissLastRound() {

		// Extent Report
		test = extent.createTest("Test Case 25",
				"Insights - Approach miss Last round, min distance = 100 and max distance = 150 ");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response

		String relativeURL = "api/stats/approach_miss?v=2&user_id=" + this.userId
				+ "&rounds_offset=0&rounds_limit=1&max_distance=150&min_distance=100";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 25 " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is" + code);
		}
	}

	@Test(priority = 26, description = "Insights - Approach miss Last round min distance = 150")
	public void TC0026_GETApproachMissLastRoundMinDistance150() {

		// Extent Report
		test = extent.createTest("Test Case 26", "Insights - Approach miss Last round min distance = 150");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response

		String relativeURL = "api/stats/approach_miss?v=2&user_id=" + this.userId
				+ "&rounds_offset=0&rounds_limit=1&min_distance=150";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 26 " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is" + code);
		}
	}

	@Test(priority = 27, description = "Insights - Approach miss Last three rounds max distance = 100")
	public void TC0027_GETApproachMissLastThreeRoundMaxDistance100() {

		// Extent Report
		test = extent.createTest("Test Case 27", "Insights - Approach miss Last three rounds max distance = 100");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response

		String relativeURL = "api/stats/approach_miss?v=2&user_id=" + this.userId
				+ "&rounds_offset=0&rounds_limit=3&max_distance=100";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 27 " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");

			ReportingListener.onSuccess("GET", relativeURL, responseBody);
		} else {
			test.fail("Status code is" + code);
		}
	}

	@Test(priority = 28, description = "Insights - Approach miss Last three rounds max distance = 150 and min distance = 100")
	public void TC0028_GETApproachMissLastThreeRoundMaxDistance150() {

		// Extent Report
		test = extent.createTest("Test Case 28",
				"Insights - Approach miss Last three rounds max distance = 150 and min distance = 100");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response

		String relativeURL = "api/stats/approach_miss?v=2&user_id=" + this.userId
				+ "&rounds_offset=0&rounds_limit=3&max_distance=150&min_distance=100";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 28 " + code);
		Assert.assertEquals(200, code);

		// Displaying Testcase Status in extend report on the basis of status code
		if (code == 200) {
			test.pass("Successfully executed");
			ReportingListener.onSuccess("GET", relativeURL, responseBody);

		} else {
			test.fail("Status code is" + code);
		}
	}

	@Test(priority = 29, description = "Insights - Approach miss Last three rounds min distance = 150")
	public void TC0029_GETApproachMissLastThreeRoundMinDistance150() {

		// Extent Report
		test = extent.createTest("Test Case 29", "Insights - Approach miss Last three rounds min distance = 150");

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Display Request Method in Extent Report
		test.log(Status.INFO, "Request Method : GET");

		// Send Request and Get response

		String relativeURL = "api/stats/approach_miss?v=2&user_id=" + this.userId
				+ "&rounds_offset=0&rounds_limit=3&min_distance=150";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Display URL in Extent Report
		test.log(Status.INFO, "Sending Request " + this.baseURI + relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Displaying Response Body in Extent Report
		test.log(Status.INFO, "Response Body");
		test.log(Status.INFO, responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 29 " + code);

		// Displaying Testcase Status in extend report on the basis of status code

		if (code == 200) {
			test.pass("Successfully executed");

			ReportingListener.onSuccess("GET", relativeURL, responseBody);

		} else {
			test.fail("Status code is" + code);

		}

		Assert.assertEquals(code, 200);
	}

	@AfterTest
	void GETLogoutUser() throws Exception {
		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Response Object
		Response response = httpRequest.request(Method.GET, "user/logout");

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		// System.out.println("Response Code is:" + code);

		extent.flush();

	}

}
