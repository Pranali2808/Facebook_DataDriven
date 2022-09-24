package com.qa.tests;

import java.io.IOException;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.util.DataUtil;
import com.qa.util.MyXLSReader;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

public class Login {
	WebDriver driver;

	public MyXLSReader excelReader;
	DataUtil util = new DataUtil();

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
	
	@Severity(SeverityLevel.CRITICAL)
	@Description("Verify login button is clicked or not??")
	@Story("Story name : To enter given credentials in login page")
	@Test(dataProvider = "dataSupplier",priority = 1, description="Verifying login page test")
	public void testlogin(HashMap<String, String> hMap) throws IOException {
		excelReader = new MyXLSReader("src\\test\\resources\\FBDataDriven.xlsx");
		if (!util.isRunnable(excelReader, "LoginTest", "Testcases") || hMap.get("Runmode").equals("N")) {

			throw new SkipException("Run mode is set to N,hence not executed");
		}

		System.setProperty("webdriver.gecko.driver", "C:\\Selenium\\geckodriver.exe");
		driver = new FirefoxDriver();
		driver.get("https://www.facebook.com/");
		driver.findElement(By.xpath("//input[@name='email']")).sendKeys(hMap.get("Username"));
		driver.findElement(By.xpath("//input[@name='pass']")).sendKeys(hMap.get("Password"));
		driver.findElement(By.xpath("//button[@name='login']")).click();

		String expectedResult = hMap.get("ExpectedResult");

		boolean expectedConvertedResult = false;

		if (expectedResult.equalsIgnoreCase("Success")) {
			expectedConvertedResult = true;
		} else if (expectedResult.equalsIgnoreCase("Failure")) {
			expectedConvertedResult = false;
		}

	}

	@DataProvider
	public Object[][] dataSupplier() {

		Object[][] data = null;
		try {
			excelReader = new MyXLSReader("src\\test\\resources\\FBDataDriven.xlsx");
			data = util.getTestData(excelReader, "LoginTest", "Data");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return data;
	}
}
