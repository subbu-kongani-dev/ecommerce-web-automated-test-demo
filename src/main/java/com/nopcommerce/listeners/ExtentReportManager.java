package com.nopcommerce.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.nopcommerce.utils.ConfigReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ExtentReportManager manages Extent Reports for generating HTML test reports.
 * Uses ThreadLocal pattern to support parallel test execution.
 * 
 * Key Features:
 * - Creates beautiful HTML reports with test execution details
 * - Supports parallel test execution using ThreadLocal
 * - Attaches screenshots to test reports
 * - Provides methods for logging test steps and results
 * - Includes system information (OS, Browser, Application)
 * - Generates timestamped report files
 * 
 * Report Information Includes:
 * - Test execution summary (passed, failed, skipped)
 * - Detailed test steps with timestamps
 * - Screenshots for failed tests
 * - Exception stack traces
 * - Execution time for each test
 * - System and environment information
 * 
 * ThreadLocal Pattern:
 * - Each thread maintains its own ExtentTest instance
 * - Prevents data corruption in parallel execution
 * - Ensures thread-safe reporting
 * 
 * @author NopCommerce Automation Team
 * @version 1.0
 */
public class ExtentReportManager {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static ConfigReader config = ConfigReader.getInstance();
    
    /**
     * Creates and configures a new ExtentReports instance.
     * Sets up report configuration including theme, report name, and system info.
     * 
     * @return Configured ExtentReports instance
     * 
     * Report Configuration:
     * - Theme: Standard (clean, professional look)
     * - Report Name: NopCommerce Automation Test Report
     * - Document Title: Test Execution Report
     * - Filename: TestReport_{timestamp}.html
     * 
     * System Information Added:
     * - Application under test
     * - Browser used for testing
     * - Operating system
     */
    public static ExtentReports createInstance() {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String reportPath = config.getReportPath() + "TestReport_" + timestamp + ".html";
        
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setReportName("NopCommerce Automation Test Report");
        sparkReporter.config().setDocumentTitle("Test Execution Report");
        
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Application", "NopCommerce Demo");
        extent.setSystemInfo("Browser", config.getBrowser());
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        
        return extent;
    }
    
    /**
     * Gets the singleton ExtentReports instance.
     * Creates new instance if none exists (lazy initialization).
     * 
     * @return ExtentReports instance for the test suite
     */
    public static ExtentReports getExtentReports() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }
    
    /**
     * Creates a new test entry in the Extent Report.
     * Stores the ExtentTest instance in ThreadLocal for the current thread.
     * 
     * @param testName Name of the test method being executed
     * @param description Description of the test (from @Test annotation)
     * @return ExtentTest instance for logging test steps
     * 
     * Usage: Called automatically by TestListener.onTestStart()
     */
    public static ExtentTest createTest(String testName, String description) {
        ExtentTest extentTest = extent.createTest(testName, description);
        test.set(extentTest);
        return extentTest;
    }
    
    /**
     * Gets the ExtentTest instance for the current thread.
     * 
     * @return ExtentTest instance associated with the current thread
     */
    public static ExtentTest getTest() {
        return test.get();
    }
    
    /**
     * Logs an informational message to the test report.
     * Used for documenting test execution steps.
     * 
     * @param message Information message to log
     */
    public static void logInfo(String message) {
        getTest().log(Status.INFO, message);
    }
    
    /**
     * Logs a pass message to the test report.
     * Used when a test assertion passes or test completes successfully.
     * 
     * @param message Success message to log
     */
    public static void logPass(String message) {
        getTest().log(Status.PASS, message);
    }
    
    /**
     * Logs a failure message to the test report.
     * Used when a test assertion fails or an error occurs.
     * 
     * @param message Failure message to log (typically includes error details)
     */
    public static void logFail(String message) {
        getTest().log(Status.FAIL, message);
    }
    
    /**
     * Attaches a screenshot to the test report.
     * Screenshot appears inline in the HTML report for visual verification.
     * 
     * @param screenshotPath Full path to the screenshot file.
     *                       If null, no screenshot is attached.
     *                       
     * Usage: Typically called after test failures to provide visual evidence
     */
    public static void attachScreenshot(String screenshotPath) {
        if (screenshotPath != null) {
            getTest().addScreenCaptureFromPath(screenshotPath);
        }
    }
    
    /**
     * Flushes the Extent Report to write all logged information to the HTML file.
     * Must be called after all tests complete to generate the final report.
     * 
     * Usage: Called automatically by TestListener.onFinish()
     */
    public static void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }
}
