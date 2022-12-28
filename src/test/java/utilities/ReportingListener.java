package utilities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import apiAutomation.BaseClass;

public class ReportingListener extends BaseClass implements ITestListener {

	// Create report on start
	public void onStart(ITestContext testContext) {

		// setting file name with date format 
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		String repName = "Test-Report-" + timeStamp + ".html";

		// Creating object of Spark reporter 
		spark = new ExtentSparkReporter(System.getProperty("user.dir") + "\\test-output\\" + repName);
		try {

			spark.loadXMLConfig(System.getProperty("user.dir") + "/extent-config.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Creating object of extent report
		extent = new ExtentReports();
		extent.attachReporter(spark);
		extent.setSystemInfo("Host name", "Stag");
		extent.setSystemInfo("Env", "Stag");
		extent.setSystemInfo("user", "Rashid");

		//Modify report title and report name -> displayed in report header
		spark.config().setDocumentTitle("API Automation Report");
		spark.config().setReportName("Functional API Test Cases");

	}

	// Set test Name
	public void onTestStart(ITestResult tr) {
		logger = extent.createTest(tr.getName());
	}

	public void onTestSuccess(ITestResult result) {

		logger.log(Status.PASS, result.getMethod().getDescription());
	}

	public static void onSuccess(String method, String apiUrl, String response) { // create new entry in the report

		logger.log(Status.INFO, "Request Method: " + method);
		logger.log(Status.INFO, "Request URL: " + apiUrl);
		logger.log(Status.INFO, "Request response: " + response);
		logger.log(Status.PASS, "Test Successfully executed ");

	}

	/*
	 * public static void onTestFailure(String method,String apiUrl, String
	 * response) throws IOException { // create new entry in the report
	 * 
	 * logger.log(Status.INFO,"Request Method: " + method );
	 * logger.log(Status.INFO,"Request URL: " + apiUrl );
	 * logger.log(Status.INFO,"Request response: " + response );
	 * logger.log(Status.FAIL,"Test Failed");
	 * 
	 * }
	 */

	// On test fail

	public void onTestFailure(ITestResult result) {
		logger.log(Status.FAIL, result.getMethod().getDescription());
		logger.log(Status.FAIL, result.getThrowable().getMessage());

	}

	// On Test skip
	public void onTestSkip(ITestResult result) {
		logger.log(Status.SKIP, result.getMethod().getDescription());
		logger.log(Status.SKIP, result.getThrowable().getMessage());
	}

	// Finalize the report
	public void onFinish(ITestContext context) {
		extent.flush();

	}

}
