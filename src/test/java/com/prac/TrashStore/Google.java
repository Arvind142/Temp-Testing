package com.prac.TrashStore;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import com.prac.util.Constants;
import com.prac.util.TestNGBase;

public class Google extends TestNGBase {

	WebDriver driver = null;

	@DataProvider(name = "dataProvider")
	public Object[][] dataProvider() {
		return businessFunction.getDataFromExcel("Google");
	}

	@Test(dataProvider = "dataProvider")
	public void withExcelPram(Object[] testData) {
		// few reporting must
		String testName = className + "." + (new Object() {
		}.getClass().getEnclosingMethod().getName()) + "." + testData[0];
		try {
			log(testName, "VAL1", "Browser started");
			Thread.sleep(15000);
			log(testName, "Navigation", "Successs Expected", "Success found", Constants.Reporting.PASS);
		} catch (Exception e) {
			log(testName, "Error found", "No exception expected", "exception found: " + e.getMessage(),
					Constants.Reporting.FAIL);
		}
	}

	@DataProvider(name = "dataProvider1")
	public Object[][] dataProvider1() {
		Object[][] data = { { "TCX1", "Wow" }, { "TCX2", "Wowx" } };
		return data;
	}

	@Test(dataProvider = "dataProvider1")
	public void withDataProviderPram(Object... args) {
		// few reporting must
		String testName = className + "." + (new Object() {
		}.getClass().getEnclosingMethod().getName()) + "." + args[0];
		try {
			log(testName, "VAL1", "INFO-LOG");
			Thread.sleep(5000);
			log(testName, "VAL2", "B", "B");
			log(testName, "VAL3", "A", "A", Constants.Reporting.PASS);
			log(testName, "VAL4", "C", "D", Constants.Reporting.PASS, "https://www.google.com");
		} catch (SkipException e) {
			log(testName, "Skip Exception", "", "", Constants.Reporting.SKIP);
		} catch (Exception e) {
			log(testName, "Error found", "No exception expected", "exception found: " + e.getMessage(),
					Constants.Reporting.FAIL);
		}
	}

	@Test
	public void invalidCaseWithSkip() {
		String testName = className + "." + (new Object() {
		}.getClass().getEnclosingMethod().getName());
		// few reporting must
		throw new SkipException("Test Case to be skipped");
	}

	@Test
	public void testWithoutPram() {
		// few reporting must
		String testName = className + "." + (new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			throw new Exception("Some Error");
		} catch (SkipException e) {
			log(testName, "Skip Exception", "", "", Constants.Reporting.SKIP);
		} catch (Exception e) {
			log(testName, "Error found", "No exception expected", "exception found: " + e.getMessage(),
					Constants.Reporting.FAIL);
		}
	}
}