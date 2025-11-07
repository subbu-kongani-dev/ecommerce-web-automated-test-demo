package com.nopcommerce.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtil provides utility methods for capturing screenshots during test execution.
 * 
 * Key Features:
 * - Captures full page screenshots using Selenium WebDriver
 * - Automatically creates screenshot directory if it doesn't exist
 * - Adds timestamp to screenshot filenames for uniqueness
 * - Handles IOException gracefully and logs errors
 * - Returns screenshot path for use in reporting
 * 
 * Usage Scenarios:
 * - Capturing evidence of test failures
 * - Documenting test execution steps
 * - Debugging issues in CI/CD pipelines
 * - Attaching to test reports (Extent Reports, Allure, etc.)
 * 
 * @author NopCommerce Automation Team
 * @version 1.0
 */
public class ScreenshotUtil {
    private static final Logger logger = LogManager.getLogger(ScreenshotUtil.class);
    private static ConfigReader config = ConfigReader.getInstance();
    
    /**
     * Captures a screenshot and saves it with the specified test name.
     * The screenshot filename includes the test name and a timestamp for uniqueness.
     * 
     * @param driver WebDriver instance used to capture the screenshot.
     *               Must not be null and should be in an active state.
     * @param testName Name of the test for which screenshot is being captured.
     *                 This will be part of the screenshot filename.
     * @return String containing the full path to the saved screenshot file,
     *         or null if screenshot capture fails due to IOException.
     *         
     * File Naming Convention:
     * - Format: {testName}_{timestamp}.png
     * - Example: testSuccessfulRegistration_20251107_163045.png
     * 
     * Exception Handling:
     * - IOException is caught and logged (occurs during file operations)
     * - Returns null on failure instead of propagating exception
     * - Allows test execution to continue even if screenshot fails
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";
        String screenshotPath = config.getScreenshotPath() + fileName;
        
        try {
            File directory = new File(config.getScreenshotPath());
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(screenshotPath);
            FileUtils.copyFile(srcFile, destFile);
            logger.info("Screenshot captured: " + screenshotPath);
            return screenshotPath;
        } catch (IOException e) {
            logger.error("Failed to capture screenshot: " + testName, e);
            return null;
        }
    }
    
    /**
     * Captures a screenshot with a default filename.
     * This is a convenience method that calls captureScreenshot(driver, "Screenshot").
     * 
     * @param driver WebDriver instance used to capture the screenshot.
     *               Must not be null and should be in an active state.
     * @return String containing the full path to the saved screenshot file,
     *         or null if screenshot capture fails.
     *         
     * Use this method when:
     * - Test name is not available
     * - Generic screenshots are needed
     * - Quick debugging screenshots are required
     */
    public static String captureScreenshot(WebDriver driver) {
        return captureScreenshot(driver, "Screenshot");
    }
}
