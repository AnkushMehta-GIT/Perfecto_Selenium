 package com.perfecto.testing.selenium;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.Select;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

public class GeicoCarInsuranceJavaMain {

	private static RemoteWebDriver driver;

	//Old School Credentials
	//public static String USER_NAME = "MY_USER";
	//public static String PASSWORD = "MY_PASSWORD";
    public static ReportiumClient reportiumClient;


	public static String PERFECTO_TOKEN = "Security Token";
	public static String PERFECTO_HOST = "perfectomobile.com";
	
	private static String TARGET_EXECUTION = "mobile"; // If "Desktop" create Web Machine, else run on Mobile browser

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws MalformedURLException, IOException {
		System.out.println("Run started");

		// Create Remote WebDriver based on target execution platform
		if (TARGET_EXECUTION == "Desktop") {
			DesiredCapabilities capabilities = new DesiredCapabilities();

			//Security Token
			capabilities.setCapability("securityToken", PERFECTO_TOKEN);

			// Old school credentials:
			//capabilities.setCapability("user", USER_NAME);
			//capabilities.setCapability("password", PASSWORD);

			// Target Web Machine configuration
			capabilities.setCapability("platformName", "Windows");
			capabilities.setCapability("platformVersion", "8.1");
			capabilities.setCapability("browserName", "Firefox");
			capabilities.setCapability("browserVersion", "45");

			// Additional capabilities
			capabilities.setCapability("resolution", "1440x900");
			capabilities.setCapability("location", "NA-US-BOS");
			capabilities.setCapability("takesScreenshot", true);
			
			// Name of script
			capabilities.setCapability("scriptName", "GeicoCarInsuranceDesktopWeb");
			
			// Create Remote WebDriver
			System.out.println("Creating Web Machine per specified capabilities");
			driver = new RemoteWebDriver(new URL("https://" + PERFECTO_HOST + "/nexperience/perfectomobile/wd/hub"), capabilities);
		} else {

			// Define target mobile device
			String browserName = "mobileOS";
			DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);

			//Security Token
//			capabilities.setCapability("securityToken", PERFECTO_TOKEN);

			// Old school credentials:
			capabilities.setCapability("user", "");
			capabilities.setCapability("password", "");

			
//			capabilities.setCapability("deviceName", "3E3082519B1C5B7D659B8827321241C0DDB0F036"); // Set your target Device ID
			capabilities.setCapability("platformName", "iOS");
			capabilities.setCapability("platformVersion", "10.3");
			capabilities.setCapability("location", "NA-US-BOS");
			capabilities.setCapability("resolution", "2048x1536");
			capabilities.setCapability("manufacturer", "Apple");
			capabilities.setCapability("model", "iPad 9.7");
			// Define device allocation timeout, in minutes
			capabilities.setCapability("openDeviceTimeout", 5);
			
			// Name of script
			capabilities.setCapability("scriptName", "GOOGLEMobileWeb");
			
			// Create Remote WebDriver
			System.out.println("Allocating Mobile device per specified capabilities");
			driver = new RemoteWebDriver(new URL("https://" + PERFECTO_HOST + "/nexperience/perfectomobile/wd/hub"), capabilities);
			
			reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(
                    new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
                    .withProject(new Project("Sample Selenium-Reportium" , "1.0"))
                    .withContextTags("Regression") //Optional
                    .withWebDriver(driver) //Optional
                    .build());

		}
		
		// Define execution timeouts
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		reportiumClient.testStart("ESPN navigation" , new TestContext("Some tag" , "ESPN"));
		try {			
			// Navigate to geico.com
			
			driver.get("http://google.com");

			// Maximize browser window on desktop
			if(TARGET_EXECUTION == "Desktop")
                driver.manage().window().maximize();

			// Enter form data
//			Select type = new Select(driver.findElement(By.id("insurancetype")));
//
//			type.selectByVisibleText("Motorcycle");
//			driver.findElement(By.id("zip")).sendKeys("01434");
//			driver.findElement(By.id("submitButton")).click();
//
//			driver.findElement(By.id("firstName")).sendKeys("MyFirstName");
//			driver.findElement(By.id("lastName")).sendKeys("MyFamilyName");
//			driver.findElement(By.id("street")).sendKeys("My Address");
//
//			driver.findElement(By.id("date-monthdob")).sendKeys("8");
//			driver.findElement(By.id("date-daydob")).sendKeys("3");
//			driver.findElement(By.id("date-yeardob")).sendKeys("1981");
//
//			// Set Radio buttons, 1-Yes; 2-No
//			driver.findElement(By.xpath("//*[@class='radio'][2]")).click();
//			driver.findElement(By.id("btnSubmit")).click();
//
//			driver.findElement(By.id("hasCycle-error")).isDisplayed();
//
//			// select yes
//			Select hasCycle = new Select(driver.findElement(By.id("hasCycle")));
//			hasCycle.selectByIndex(1);
//
//			// new select added (which current company)
//			Select current = new Select(driver.findElement(By.id("currentInsurance")));
//			current.selectByVisibleText("Other");
            reportiumClient.testStep("Search for a team"); //TEST STEP - Search team.

			
			driver.findElement(By.xpath("//input[@name='q']")).click();
			Thread.sleep(3000);
			driver.findElement(By.xpath("//input[@name='q']")).sendKeys("Amazon.com");
			Thread.sleep(3000);
			driver.findElement(By.xpath("//input[@name='q']")).sendKeys(Keys.ENTER);
			System.out.println("Done google.com\n");
			
            reportiumClient.testStop(TestResultFactory.createSuccess());

			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				driver.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			driver.quit();
		}

		System.out.println("Run ended");
	}
}
