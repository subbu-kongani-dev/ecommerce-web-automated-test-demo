package com.nopcommerce.examples;

import com.nopcommerce.utils.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Example test class demonstrating the new YAML-based capability management.
 * 
 * This class shows various ways to use the enhanced framework:
 * - Local execution with different browsers
 * - LambdaTest cloud execution
 * - Runtime browser selection
 * - Parallel execution support
 * 
 * @author NopCommerce Automation Team
 */
public class CapabilityDemoTest {
    private static final Logger logger = LogManager.getLogger(CapabilityDemoTest.class);
    private WebDriver driver;
    
    /**
     * Example 1: Basic test using default configuration
     * Uses browser and platform from config.properties
     */
    @Test(priority = 1)
    public void testWithDefaultConfiguration() {
        logger.info("Running test with default configuration");
        driver = DriverManager.getDriver();
        
        driver.get("https://demo.nopcommerce.com/");
        
        String title = driver.getTitle();
        logger.info("Page title: " + title);
        Assert.assertTrue(title.contains("nopCommerce"), "Title should contain nopCommerce");
    }
    
    /**
     * Example 2: Test with specific browser (Chrome)
     * Override default browser configuration
     */
    @Test(priority = 2)
    public void testWithChromeBrowser() {
        logger.info("Running test with Chrome browser");
        driver = DriverManager.getDriver("chrome");
        
        driver.get("https://demo.nopcommerce.com/");
        
        String title = driver.getTitle();
        Assert.assertNotNull(title, "Title should not be null");
    }
    
    /**
     * Example 3: Test with Firefox browser
     * Demonstrates browser switching
     */
    @Test(priority = 3, enabled = false) // Disabled by default, enable when needed
    public void testWithFirefoxBrowser() {
        logger.info("Running test with Firefox browser");
        driver = DriverManager.getDriver("firefox");
        
        driver.get("https://demo.nopcommerce.com/");
        
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, "https://demo.nopcommerce.com/", "URL should match");
    }
    
    /**
     * Example 4: Parameterized test for multiple browsers
     * Can be used with TestNG XML for parallel execution
     */
    @Test(priority = 4, enabled = false)
    @Parameters({"browser"})
    public void testWithParameterizedBrowser(@Optional("chrome") String browser) {
        logger.info("Running test with parameterized browser: " + browser);
        driver = DriverManager.getDriver(browser);
        
        driver.get("https://demo.nopcommerce.com/");
        
        Assert.assertNotNull(driver.getTitle(), "Title should not be null");
    }
    
    /**
     * Example 5: LambdaTest execution test
     * To run on LambdaTest:
     * 1. Set execution.platform=LAMBDATEST in config.properties
     * 2. Ensure LT_USERNAME and LT_ACCESS_KEY are set
     * 3. Run the test
     */
    @Test(priority = 5, enabled = false, groups = {"cloud"})
    public void testOnLambdaTest() {
        logger.info("Running test on LambdaTest cloud");
        // System property will override config.properties
        System.setProperty("execution.platform", "LAMBDATEST");
        
        driver = DriverManager.getDriver();
        
        driver.get("https://demo.nopcommerce.com/");
        
        String title = driver.getTitle();
        logger.info("Test executed on LambdaTest. Page title: " + title);
        Assert.assertTrue(title.contains("nopCommerce"), "Title validation on LambdaTest");
    }
    
    /**
     * Clean up after each test
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        logger.info("Cleaning up driver");
        DriverManager.quitDriver();
    }
    
    /**
     * Setup method - can be used for pre-test configurations
     */
    @BeforeMethod
    public void setUp() {
        logger.info("Setting up test environment");
        // Any pre-test setup can go here
    }
}
