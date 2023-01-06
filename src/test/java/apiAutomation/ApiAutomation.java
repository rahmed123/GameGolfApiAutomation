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
import com.aventstack.extentreports.model.Media;
import com.google.gson.stream.JsonWriter;

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
public class ApiAutomation extends BaseClass {

	// Getting Session cookies and Creating Object for Extent Report

	@Test(priority = 1)
	public void TC0001_GETUserStats() throws Exception {

		// Extent report Test initialization

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Sending request and getting response
		String relativeURL = "api/user?with=user_stats";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Set user id to global userid variable
		JsonPath jsonPath = response.jsonPath();
		this.userId = jsonPath.get("user.id").toString();

		// Getting and validating Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 1: " + code);

		System.out.println("User id for Testcase 1: " + this.userId);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);

		Assert.assertEquals(code, 200);

	}

	@Test(priority = 2)
	public void TC0002_GETRoundStats() {
		// Specify base URI

		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Sending request and getting response
		String relativeURL = "api/round?with=round_stats,golfcourse";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asPrettyString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 2: " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);

		Assert.assertEquals(code, 200);

	}

	@Test(priority = 3)
	public void TC0003_GETUserAttribute() {

		// Extent Report

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response
		String relativeURL = "api/user/attribute_library?locale=en-US";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 3: " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);

		Assert.assertEquals(200, code);
	}

	@Test(priority = 4)
	public void TC0004_GETActivityFeed() {
		// Extent Report

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response
		String relativeURL = "api/activity_feed?id="+this.userId+"&username="+this.username+"&limit=10&offset=0&locale=en-US&group_by_story=1";
		Response response = httpRequest.request(Method.GET, relativeURL);

	
		// Getting and Setting round id
		JsonPath jsonPath = response.jsonPath();

		this.roundId = jsonPath.get("activity[5].data.round_id").toString();

		System.out.println(roundId);
		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 4: " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

	}

	@Test(priority = 5)
	public void TC0005_GETRecommendedUsers() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response
		String relativeURL = "api/user/recommended.json?limit=20&with=user_stats";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();

		// JsonPath jsonPath = response.jsonPath();

		// System.out.println(jsonPath.get("resultset_info.limit"));
		// String resultInfo = response.jsonPath().get("1.body.resultset_info.limit");
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 5 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

	}

	@Test(priority = 6)
	public void TC0006_GETComments() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response
		String relativeURL = "api/round/" + this.roundId + "/comment?limit=3";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 6 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

	}

	@Test(priority = 7)
	public void TC0007_POSTComments() {

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

		// Request Body
		httpRequest.body(requestParams.toJSONString());

		// Response Object
		String relativeURL = "api/round/" + this.roundId + "/comment";
		Response response = httpRequest.request(Method.POST, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 7 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("POST", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

	}

	@Test(priority = 8)
	public void TC0008_GETFollowings() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response
		String relativeURL = "api/follower?limit=30&offset=0&with=user_stats";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 8 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);
	}

	@Test(priority = 9)
	public void TC0009_GETFollowers() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response
		String relativeURL = "api/following?limit=0&with=user_stats";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 9 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

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

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response
		String relativeURL = "api/user/jmfloop/profile";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 10 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);

		Assert.assertEquals(200, code);

	}

	@Test(priority = 11)
	public void TC0011_GETPro() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response
		String relativeURL = "api/user/gmac/profile";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 11 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

	}

	@Test(priority = 12)
	public void TC0012_GETPro() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response
		String relativeURL = "api/user/leew/profile";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 12 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

	}

	@Test(priority = 13)
	public void TC00013_POSTSearchPlayer() {

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

		// Request Body
		httpRequest.body(requestParams.toJSONString());

		// Response Object
		String relativeURL = "api/user/search?default_operator=AND&with=you_follow";
		Response response = httpRequest.request(Method.POST, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 13 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("POST", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

	}

	@Test(priority = 14)
	public void TC0014_GETCompareAllRounds() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response
		String relativeURL = "api/stats/series?user_id=218263";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 14 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

	}

	@Test(priority = 15)
	public void TC0015_GETCompareLastTenRounds() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response
		String relativeURL = "api/stats/user?user_id=218263&from_offset=0&to_offset=9";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 15 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

	}

	@Test(priority = 16)
	public void TC0016_GETGolfCourse() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response
		String relativeURL = "api/golfcourse/605";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 16 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

	}

	@Test(priority = 17)
	public void TC0017_GETGolfCoursePlayers() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response
		String relativeURL = "api/golfcourse/605/players?limit=25";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 17 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

	}

	@Test(priority = 18)
	public void TC0018_GETGolfCourseRounds() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response
		String relativeURL = "api/golfcourse/605/rounds?limit=25&sort=stats.normalized_under_over:asc";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 18 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

	}

	@Test(priority = 19)
	public void TC0019_GETGolfCourseNearby() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response
		String relativeURL = "api/golfcourse/605/nearby?limit=25&with=scorecard";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 19 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

	}

	@Test(priority = 20)
	public void TC0020_GETGolfCourseActivities() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response
		String relativeURL = "api/golfcourse/605/activity?courseid=605&limit=10&offset=0&group_by_story=1&locale=en-US";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 20 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

	}

	@Test(priority = 21)
	public void TC0021_GETStrokeGainLastRound() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response
		String relativeURL = "api/stats/strokes_gained?v=2&user_id=" + this.userId + "&rounds_offset=0&rounds_limit=1";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 21 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

	}

	@Test(priority = 22)
	public void TC0022_GETStrokeGainLastRound() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response
		String relativeURL = "api/stats/strokes_gained?v=2&user_id=" + this.userId + "&rounds_offset=0&rounds_limit=3";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 22 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

	}

	@Test(priority = 23)
	public void TC0023_GETApproachMissLastRound() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response
		String relativeURL = "api/stats/approach_miss?v=2&user_id=" + this.userId
				+ "&rounds_offset=0&rounds_limit=1&max_distance=100";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 23 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

	}

	@Test(priority = 24)
	public void TC0024_GETApproachMissLastRound() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response

		String relativeURL = "api/stats/approach_miss?v=2&user_id=" + this.userId
				+ "&rounds_offset=0&rounds_limit=1&max_distance=150&min_distance=100";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 24 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

	}

	@Test(priority = 25)
	public void TC0025_GETApproachMissLastRound() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response

		String relativeURL = "api/stats/approach_miss?v=2&user_id=" + this.userId
				+ "&rounds_offset=0&rounds_limit=1&max_distance=150&min_distance=100";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 25 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

	}

	@Test(priority = 26, description = "Insights - Approach miss Last round min distance = 150")
	public void TC0026_GETApproachMissLastRoundMinDistance150() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response

		String relativeURL = "api/stats/approach_miss?v=2&user_id=" + this.userId
				+ "&rounds_offset=0&rounds_limit=1&min_distance=150";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 26 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

	}

	@Test(priority = 27, description = "Insights - Approach miss Last three rounds max distance = 100")
	public void TC0027_GETApproachMissLastThreeRoundMaxDistance100() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response

		String relativeURL = "api/stats/approach_miss?v=2&user_id=" + this.userId
				+ "&rounds_offset=0&rounds_limit=3&max_distance=100";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 27 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

	}

	@Test(priority = 28, description = "Insights - Approach miss Last three rounds max distance = 150 and min distance = 100")
	public void TC0028_GETApproachMissLastThreeRoundMaxDistance150() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response

		String relativeURL = "api/stats/approach_miss?v=2&user_id=" + this.userId
				+ "&rounds_offset=0&rounds_limit=3&max_distance=150&min_distance=100";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 28 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(200, code);

	}

	@Test(priority = 29, description = "Insights - Approach miss Last three rounds min distance = 150")
	public void TC0029_GETApproachMissLastThreeRoundMinDistance150() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response

		String relativeURL = "api/stats/approach_miss?v=2&user_id=" + this.userId
				+ "&rounds_offset=0&rounds_limit=3&min_distance=150";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 29 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(code, 200);
	}

	@Test(priority = 30, description = "Insights - Tee miss for last round ")
	public void TC0030_GETApproachTeeMissLastRound() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response

		String relativeURL = "api/stats/tee_miss?v=2&user_id=" + this.userId + "&rounds_offset=0&rounds_limit=1";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 30 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(code, 200);
	}

	@Test(priority = 31, description = "Insights - Tee miss for last three rounds ")
	public void TC0031_GETApproachTeeMissLastThreeRound() {

		// Specify base URI
		RestAssured.baseURI = this.baseURI;

		// Creating an Object for request
		RequestSpecification httpRequest = RestAssured.given();

		// Request Headers
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Cookie", this.cookies);

		// Send Request and Get response

		String relativeURL = "api/stats/tee_miss?v=2&user_id=" + this.userId + "&rounds_offset=0&rounds_limit=3";
		Response response = httpRequest.request(Method.GET, relativeURL);

		// Print Response in Console Window
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is :" + responseBody);

		// Getting Response Code
		int code = response.getStatusCode();
		System.out.println("Response Code for Testcase 31 " + code);

		// for printing logs in extent report- using below method which one available in
		// base class
		printLogs("GET", code, baseURI + relativeURL, responseBody);
		Assert.assertEquals(code, 200);
	}

	@Test(priority = 32, description =
	  "Insights - Tee miss for last three rounds")
	
	public void TC0032_GETApproachTeeMissLastThreeRound() {
	  
	  // Relative URL. 
		  String relativeURL ="api/stats/tee_miss?v=2&user_id="+this.userId+"&rounds_offset=0&rounds_limit=3";
	  
	  //Call method of GET API and pass relative url and datatype
	  
	  int code=GetAPI(relativeURL, "application/json");
			  
	  System.out.println("Response Code for Testcase 32 " + code);
	  
	  // Apply assertion on response code 
	  Assert.assertEquals(code, 200);
	  }
	 
}
