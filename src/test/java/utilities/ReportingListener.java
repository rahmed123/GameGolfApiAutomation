package utilities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;



public class ReportingListener implements ITestListener{

	    public static ExtentReports extent;
	    public static ExtentTest logger;
	    public ExtentHtmlReporter htmlReporter;
	 

	    //Create report on start
	    public void onStart(ITestContext testContext){
	    	
	    
	        String timeStamp=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	        String repName="Test-Report-"+timeStamp+".html";

			htmlReporter = new ExtentHtmlReporter(repName);
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);
	
	    }
	    
	    //Set test Name
	    public void onTestStart(ITestResult tr) {
	    	logger=extent.createTest(tr.getName());
	    }
	 
	  
	    public static void onSuccess(String method,String apiUrl, String response) { // create new entry in the report
	    	

			logger.log(Status.INFO,"Request Method: " + method );
			logger.log(Status.INFO,"Request URL: " + apiUrl );
			logger.log(Status.INFO,"Request response: " + response );
			logger.log(Status.PASS,"Test Successfully executed ");

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
		 
	    
	    //On test fail 
	    
	    public void onTestFailure(ITestResult result) {
	    	logger.log(Status.FAIL, result.getMethod().getDescription());
	    	logger.log(Status.FAIL, result.getThrowable().getMessage());
	    
	    }
	    
	    
	    
	    // On Test skip 
	    public void onTestSkip(ITestResult result)
	    {
	    	logger.log(Status.SKIP, result.getMethod().getDescription());
	    	logger.log(Status.SKIP, result.getThrowable().getMessage());
	    }
	   
	    //Finalize the report
	    public void onFinish(ITestContext context){
	        extent.flush();

	    }
	
	
	
	
	
	
	
	
	
	 //Testing
	
	
	

}
