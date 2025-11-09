package com.nopcommerce.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.nopcommerce.listeners.TestListener;
import com.nopcommerce.core.config.ConfigurationManager;
import com.nopcommerce.core.driver.DriverManager;
import com.nopcommerce.utils.ScreenshotUtil;

/**
 * BaseTest class serves as the foundation for all test classes in the
 * framework. It provides common setup and teardown functionality for test
 * execution.
 * 
 * Key Features: - Initializes WebDriver before each test method - Captures
 * screenshots on test failure - Manages browser lifecycle - Provides logging
 * capabilities - Integrates with TestListener for enhanced reporting
 * 
 * @author NopCommerce Automation Team
 * @version 2.0
 */
@Listeners(TestListener.class)
public class BaseTest {
	protected WebDriver driver;
	protected ConfigurationManager config;
	protected static final Logger logger = LogManager.getLogger(BaseTest.class);

	/**
	 * Setup method that runs before each test method. Initializes WebDriver, loads
	 * configuration, and navigates to the application URL.
	 * 
	 * @param browser Optional parameter to specify browser type (chrome, firefox,
	 *                edge). If not provided, uses the browser specified in
	 *                config.properties. Can be passed from TestNG XML file for
	 *                cross-browser testing.
	 */
	@BeforeMethod(alwaysRun = true)
	@Parameters({ "browser" })
	public void setUp(@Optional String browser) {
		// Add visual separation before test setup
		System.out.println("\n**********************************************************************\n");
		logger.info("=== Test Setup Started ===");
		config = ConfigurationManager.getInstance();
		driver = DriverManager.getDriver(browser);
		logger.info("WebDriver initialized successfully");
		String appUrl = config.getAppUrl();
		driver.get(appUrl);
		logger.info("Navigated to application URL: " + appUrl);
		logger.info("=== Test Setup Completed ===");
		logger.info("");
	}

	/**
	 * Teardown method that runs after each test method. Captures screenshots on
	 * test failure and closes the browser session.
	 * 
	 * @param result ITestResult object containing test execution details including:
	 *               - Test name - Test status (SUCCESS, FAILURE, SKIP) - Exception
	 *               details if test failed - Test execution time
	 * 
	 *               Screenshot Capture Logic: - Only captures screenshot if test
	 *               status is FAILURE - Screenshot filename includes test name and
	 *               timestamp - Screenshots are saved to the path specified in
	 *               config.properties
	 */
	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult result) {
		logger.info("=== Test Teardown Started ===");

		// Extract test information from ITestResult
		String testName = result.getMethod().getMethodName();
		int testStatus = result.getStatus();
		String statusText = getTestStatusText(testStatus);

		logger.info("Test Name: " + testName);
		logger.info("Test Status: " + statusText);

		// Capture screenshot on test failure
		if (testStatus == ITestResult.FAILURE) {
			logger.warn("Test FAILED - Capturing screenshot");
			try {
				String screenshotPath = ScreenshotUtil.captureScreenshot(driver, testName);
				if (screenshotPath != null) {
					logger.info("Screenshot saved at: " + screenshotPath);
				} else {
					logger.error("Failed to capture screenshot for test: " + testName);
				}
			} catch (Exception e) {
				logger.error("Exception occurred while capturing screenshot: " + e.getMessage(), e);
			}
		}

		// Close browser session
		DriverManager.quitDriver();
		logger.info("WebDriver closed successfully");
		logger.info("=== Test Teardown Completed ===");
		logger.info("");
		logger.info("");
	}

	/**
	 * Helper method to convert TestNG test status code to readable text.
	 * 
	 * @param status Integer status code from ITestResult 1 = SUCCESS 2 = FAILURE 3
	 *               = SKIP
	 * @return String representation of test status
	 */
	private String getTestStatusText(int status) {
		switch (status) {
			case ITestResult.SUCCESS:
				return "SUCCESS";
			case ITestResult.FAILURE:
				return "FAILURE";
			case ITestResult.SKIP:
				return "SKIP";
			default:
				return "UNKNOWN";
		}
	}
}
