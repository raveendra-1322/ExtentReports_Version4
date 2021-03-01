package com.extentreports4;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.utils.FileUtil;


public class NopCommerceTest {

	public WebDriver driver;
	public ExtentHtmlReporter htmlReporter;
	public ExtentReports extentReport;
	public ExtentTest test;

	@BeforeTest
	public void setUp() {
		htmlReporter=new ExtentHtmlReporter(System.getProperty("user.dir")+"/test-output/myreport.html"); 
		htmlReporter.config().setDocumentTitle("NopCommerceTest Automation");  //Title of the report
		htmlReporter.config().setReportName("Functional testing");  //name of the report
		htmlReporter.config().setTheme(Theme.DARK);  //theme of the report

		extentReport=new ExtentReports();

		extentReport.attachReporter(htmlReporter);
		extentReport.setSystemInfo("Hostname", "localhost");
		extentReport.setSystemInfo("os", "windows10");
		extentReport.setSystemInfo("testername", "ravi");
		extentReport.setSystemInfo("browser", "chrome");
	}
	@AfterTest
	public void tearDown() {

		extentReport.flush();
	}

	@BeforeMethod
	public void configuration() {
		System.setProperty("webdriver.chrome.driver", "F:\\chromedriver.exe");
		driver=new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.get("https://demo.nopcommerce.com/");

	}
	@Test
	public void getNopCommerceTitle() {

		test=extentReport.createTest("getNopCommerceTitle");
		test.createNode("test the title with valid input");
		String title=driver.getTitle();
		Assert.assertEquals(title, "nopCommerce demo store");

	}

	@Test
	public void getLogo() {

		test=extentReport.createTest("getLogo");
		test.createNode("check the logo");
		//Assert.assertTrue(driver.findElement(By.xpath("//img[@alt='nopCommerce demo store']")).isDisplayed());
		Assert.assertFalse(driver.findElement(By.xpath("//img[@alt='nopCommerce demo store']")).isDisplayed());

	}
	@AfterMethod	
	public void reportingResults(ITestResult result) throws IOException {

		if (result.getStatus() == ITestResult.FAILURE) {

			String screenshotpath=NopCommerceTest.getScreenshot(driver, result.getName());
			test.addScreenCaptureFromPath(screenshotpath);
			//test.addScreencastFromPath(screenshotpath);

			test.log(Status.FAIL, "Test case fail is"+result.getName());
			test.log(Status.FAIL, "Test case fail is"+result.getThrowable());
		}

		if (result.getStatus() == ITestResult.SKIP) {
			test.log(Status.SKIP, "Test case skip is"+result.getName());

		}
		if (result.getStatus() == ITestResult.SUCCESS) {
			test.log(Status.PASS, "Test case skip is"+result.getName());

		}
		driver.quit();
	}
	public static String getScreenshot(WebDriver driver, String screenshotpath) throws IOException {

		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());//time stamp
		String repName="Test-Report-"+timeStamp+".html";

		TakesScreenshot ts	=(TakesScreenshot)driver;
		File source=ts.getScreenshotAs(OutputType.FILE);

		String destination=System.getProperty("user.dir")+"/Screenshots/"+screenshotpath+timeStamp+".png";
		File finaldestination=new File(destination); 
		FileUtils.copyFile(source, finaldestination);
		return destination;
	}

}
