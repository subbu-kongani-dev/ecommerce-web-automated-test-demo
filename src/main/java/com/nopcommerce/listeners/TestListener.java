package com.nopcommerce.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.nopcommerce.core.driver.DriverManager;
import com.nopcommerce.utils.ScreenshotUtil;

/**
 * TestListener implements ITestListener to provide custom actions during test
 * execution lifecycle. Integrates with Extent Reports for enhanced test
 * reporting and screenshot capture on failures.
 * 
 * Key Features: - Listens to TestNG test execution events - Captures
 * screenshots on test failures - Integrates with Extent Reports for HTML
 * reporting - Provides detailed logging for test execution flow
 * 
 * Lifecycle Methods: - onStart: Called before test suite starts - onFinish:
 * Called after test suite completes - onTestStart: Called before each test
 * method starts - onTestSuccess: Called when a test passes - onTestFailure:
 * Called when a test fails (captures screenshot) - onTestSkipped: Called when a
 * test is skipped
 * 
 * Usage: Add @Listeners(TestListener.class) annotation to test classes or
 * configure in testng.xml file
 * 
 * @author NopCommerce Automation Team
 * @version 1.0
 */
public class TestListener implements ITestListener {
	private static final Logger logger = LogManager.getLogger(TestListener.class);

	/**
	 * Invoked before the test suite starts execution. Initializes Extent Reports
	 * for the test suite.
	 * 
	 * @param context ITestContext containing suite-level information: - Suite name
	 *                - Test configuration - Start time
	 */
	@Override
	public void onStart(ITestContext context) {
		logger.info("Test Suite Started: " + context.getName());
		ExtentReportManager.getExtentReports();
	}

	/**
	 * Invoked after the test suite completes execution. Flushes Extent Reports to
	 * generate the final HTML report.
	 * 
	 * @param context ITestContext containing suite-level results: - Passed tests
	 *                count - Failed tests count - Skipped tests count - End time
	 */
	@Override
	public void onFinish(ITestContext context) {
		logger.info("Test Suite Finished: " + context.getName());
		ExtentReportManager.flushReports();
	}

	/**
	 * Invoked before each test method starts execution. Creates a test entry in
	 * Extent Report with test name and description.
	 * 
	 * @param result ITestResult containing test method information: - Method name -
	 *               Test description - Test parameters
	 */
	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		String testDescription = result.getMethod().getDescription();
		logger.info("Test Started: " + testName);
		ExtentReportManager.createTest(testName, testDescription != null ? testDescription : testName);
		ExtentReportManager.logInfo("Test execution started: " + testName);
	}

	/**
	 * Invoked when a test method passes successfully. Logs the test success in
	 * Extent Report.
	 * 
	 * @param result ITestResult containing test execution details: - Test name -
	 *               Execution time - Test status
	 */
	@Override
	public void onTestSuccess(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		logger.info("Test Passed: " + testName);
		logger.info("");
		ExtentReportManager.logPass("Test passed successfully: " + testName);
	}

	/**
	 * Invoked when a test method fails. Captures screenshot and attaches it to
	 * Extent Report with failure details.
	 * 
	 * @param result ITestResult containing test failure information: - Test name -
	 *               Failure exception/throwable - Stack trace - Failure message
	 * 
	 *               Actions Performed: 1. Logs test failure with test name 2.
	 *               Captures screenshot of the failure state 3. Logs failure
	 *               message and error details to Extent Report 4. Attaches
	 *               screenshot to Extent Report for visual evidence 5. Handles
	 *               exceptions gracefully if screenshot capture fails
	 */
	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		logger.error("Test Failed: " + testName);

		try {
			String screenshotPath = ScreenshotUtil.captureScreenshot(DriverManager.getDriver(), testName);
			ExtentReportManager.logFail("Test failed: " + testName);
			ExtentReportManager.logFail("Error: " + result.getThrowable().getMessage());
			if (screenshotPath != null) {
				ExtentReportManager.attachScreenshot(screenshotPath);
			}
		} catch (Exception e) {
			logger.error("Failed to capture screenshot", e);
		}
	}

	/**
	 * Invoked when a test method is skipped. Logs the skip event (occurs when
	 * dependencies fail or @Test(enabled=false)).
	 * 
	 * @param result ITestResult containing test skip information: - Test name -
	 *               Skip reason
	 */
	@Override
	public void onTestSkipped(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		logger.warn("Test Skipped: " + testName);
		logger.info("");
	}
}
