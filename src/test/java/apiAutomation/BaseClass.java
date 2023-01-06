package apiAutomation;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BaseClass {

	// Global Variables for API automation
	public String baseURI = "http://stg-yiiportal-service.gameyourgame.com/";
	public String cookies;
	public String username = "briansexton";
	public String password = "1234ASdf";
	public String userId = "";
	public String roundId = "";
	public String test1 = "123";

	// Global declaration of Extent report objects
	public static ExtentReports extent;
	public ExtentSparkReporter spark;
	public static ExtentTest logger;

	@BeforeTest
	public void POSTLoginUser() {

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

	}

	//Get Request method
	
	
	  public int GetAPI(String relativeURL,String dataType){ // Specify base URI
	  RestAssured.baseURI = this.baseURI;
	  
	  // Creating an Object for request 
	  RequestSpecification httpRequest =
	  RestAssured.given();
	  
	  // Request Headers 
	  httpRequest.header("Content-Type", dataType);
	  httpRequest.header("Cookie", this.cookies);
	  
	  // Send Request and Get response
	  
	  //String 
	  relativeURL = "api/stats/tee_miss?v=2&user_id="+this.userId+
	  "&rounds_offset=0&rounds_limit=3";
	  Response response =
	  httpRequest.request(Method.GET, relativeURL);
	  
	  
	  // Print Response in Console Window 
	  String responseBody =
	  response.getBody().asString();
	  
	  
	  // System.out.println("Response Body is :" + responseBody);
	  
	  int code = response.getStatusCode();
	  
	  printLogs("GET", code, baseURI+relativeURL, responseBody); return code; }
	 
	
	// Method for prinitng logs in extent report
	public static void printLogs(String method, int code, String url, String response) {

	
		logger.log(Status.INFO, "<b>API Method : </b>" + method);
		logger.log(Status.INFO, "<b>API Response Code : </b>" + code);
		logger.log(Status.INFO, "<b>API URL : </b>" + url);
		logger.log(Status.INFO, "<b>API Response : </b>" + response);

	}

}
