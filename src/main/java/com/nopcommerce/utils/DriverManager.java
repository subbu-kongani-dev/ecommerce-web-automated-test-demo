package com.nopcommerce.utils;

import com.nopcommerce.factory.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * DriverManager handles WebDriver initialization and lifecycle management.
 * Uses ThreadLocal pattern to support parallel test execution.
 * Enhanced with YAML-based capability management for flexible configuration.
 * 
 * Key Features:
 * - Manages WebDriver instances using ThreadLocal for thread-safety
 * - Supports multiple browsers (Chrome, Firefox, Edge, Safari)
 * - YAML-based dynamic capability configuration
 * - Support for both local and cloud (LambdaTest) execution
 * - Configures browser options from YAML files
 * - Automatically sets up driver binaries using WebDriverManager
 * - Configures timeouts, window settings from YAML configuration
 * 
 * Architecture:
 * - Uses DriverFactory for driver creation (Factory Pattern)
 * - Capabilities loaded from YAML files (Strategy Pattern)
 * - ThreadLocal for thread-safety in parallel execution
 * 
 * Execution Modes:
 * - LOCAL: Runs tests on local machine using WebDriverManager
 * - LAMBDATEST: Runs tests on LambdaTest cloud grid
 * 
 * @author NopCommerce Automation Team
 * @version 2.0
 */
public class DriverManager {
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ConfigReader config = ConfigReader.getInstance();
    
    // Static block to suppress Selenium process warnings
    static {
        try {
            java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(java.util.logging.Level.SEVERE);
            java.util.logging.Logger.getLogger("").setLevel(java.util.logging.Level.SEVERE);
        } catch (Exception e) {
            // Ignore if logging configuration fails
        }
    }
    
    /**
     * Gets the WebDriver instance for the current thread using default browser from config.
     * 
     * @return WebDriver instance for the current thread. Creates new instance if none exists.
     */
    public static WebDriver getDriver() {
        return getDriver(null);
    }
    
    /**
     * Gets or creates a WebDriver instance for the current thread with specified browser.
     * This method implements lazy initialization - creates driver only when needed.
     * Uses DriverFactory with YAML-based capability configuration.
     * 
     * @param browserName Name of browser to initialize (chrome, firefox, edge, safari).
     *                    If null or empty, uses browser from config.properties.
     *                    Case-insensitive.
     * @return WebDriver instance for the current thread.
     * @throws IllegalArgumentException if unsupported browser is specified.
     * 
     * Configuration:
     * - Browser capabilities loaded from YAML files in src/main/resources/capabilities/
     * - Supports LOCAL and LAMBDATEST execution platforms
     * - All browser options, timeouts, and window settings configured via YAML
     * 
     * YAML Files:
     * - Local: chrome_local.yaml, firefox_local.yaml, edge_local.yaml, safari_local.yaml
     * - LambdaTest: chrome_lambdatest.yaml, firefox_lambdatest.yaml, etc.
     * 
     * Platform Selection:
     * - Set via config.properties (execution.platform=LOCAL or LAMBDATEST)
     * - Or via system property: -Dexecution.platform=LAMBDATEST
     */
    public static WebDriver getDriver(String browserName) {
        if (driver.get() == null) {
            // Use parameter browser if provided, otherwise use config browser
            String browser = (browserName != null && !browserName.isEmpty()) 
                ? browserName.toLowerCase() 
                : config.getBrowser().toLowerCase();
            
            // Get execution platform from system property or config
            String platform = System.getProperty("execution.platform");
            if (platform == null || platform.isEmpty()) {
                platform = config.getProperty("execution.platform");
            }
            if (platform == null || platform.isEmpty()) {
                platform = "LOCAL"; // Default to local execution
            }
            
            logger.info("Initializing {} browser on {} platform", browser, platform);
            
            try {
                // Use DriverFactory to create driver with YAML configuration
                WebDriver webDriver = DriverFactory.createDriver(browser, platform);
                driver.set(webDriver);
                logger.info("Browser initialized successfully using YAML configuration");
            } catch (Exception e) {
                logger.error("Failed to initialize browser: " + browser, e);
                throw new RuntimeException("Failed to create WebDriver instance", e);
            }
        }
        return driver.get();
    }
    
    /**
     * Quits the WebDriver instance for the current thread and removes it from ThreadLocal.
     * This method should be called in @AfterMethod or @AfterClass to clean up resources.
     * 
     * Actions Performed:
     * 1. Closes all browser windows
     * 2. Ends the WebDriver session
     * 3. Removes the driver reference from ThreadLocal
     * 
     * Thread-Safety:
     * - Only affects the WebDriver instance for the calling thread
     * - Safe to call from multiple threads simultaneously
     * - Does nothing if driver is already null (idempotent operation)
     */
    public static void quitDriver() {
        if (driver.get() != null) {
            logger.info("Closing browser session");
            try {
                driver.get().quit();
                // Small delay to ensure process streams are closed properly
                Thread.sleep(500);
            } catch (Exception e) {
                logger.warn("Exception during driver quit: " + e.getMessage());
            } finally {
                driver.remove();
            }
        }
    }
}
