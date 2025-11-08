package com.nopcommerce.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ConfigReader provides centralized access to configuration properties.
 * Implements Singleton pattern to ensure single instance throughout application.
 * 
 * Key Features:
 * - Loads configuration from config.properties file
 * - Singleton pattern ensures consistent configuration across framework
 * - Provides type-safe getter methods for all configuration properties
 * - Handles file loading errors with appropriate logging
 * 
 * Supported Configuration Properties:
 * - app.url: Application URL to test
 * - browser: Default browser (chrome, firefox, edge)
 * - headless: Run browser in headless mode (true/false)
 * - implicit.wait: Default implicit wait timeout in seconds
 * - explicit.wait: Default explicit wait timeout in seconds
 * - page.load.timeout: Maximum page load timeout in seconds
 * - screenshot.path: Directory path for saving screenshots
 * - report.path: Directory path for saving test reports
 * - thread.count: Number of parallel threads for test execution
 * 
 * @author NopCommerce Automation Team
 * @version 1.0
 */
public class ConfigReader {
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static ConfigReader instance;
    private Properties properties;
    private Properties localProperties;
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    private static final String LOCAL_CONFIG_FILE_PATH = "src/main/resources/config.local.properties";
    
    /**
     * Private constructor to prevent direct instantiation.
     * Loads properties file during initialization.
     */
    private ConfigReader() {
        loadProperties();
        loadLocalProperties();
    }
    
    /**
     * Returns the singleton instance of ConfigReader.
     * Creates new instance on first call, returns existing instance on subsequent calls.
     * Thread-safe implementation using synchronized keyword.
     * 
     * @return ConfigReader singleton instance
     */
    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }
    
    /**
     * Loads configuration properties from config.properties file.
     * Called automatically during ConfigReader initialization.
     * 
     * @throws RuntimeException if config.properties file is not found
     */
    private void loadProperties() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fis);
            logger.info("Configuration properties loaded successfully");
        } catch (IOException e) {
            logger.error("Failed to load configuration properties", e);
            throw new RuntimeException("Configuration file not found: " + CONFIG_FILE_PATH);
        }
    }
    
    /**
     * Loads local configuration properties from config.local.properties file.
     * This file contains sensitive data and is excluded from Git.
     * Method fails silently if file doesn't exist (for CI/CD environments).
     */
    private void loadLocalProperties() {
        localProperties = new Properties();
        try (FileInputStream fis = new FileInputStream(LOCAL_CONFIG_FILE_PATH)) {
            localProperties.load(fis);
            logger.info("Local configuration properties loaded successfully from " + LOCAL_CONFIG_FILE_PATH);
        } catch (IOException e) {
            logger.debug("Local config file not found (this is normal for CI/CD): " + LOCAL_CONFIG_FILE_PATH);
            // Don't throw exception - local config is optional
        }
    }
    
    /**
     * Generic method to get any property value by key.
     * 
     * @param key Property key from config.properties
     * @return String value of the property, or null if key doesn't exist
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Gets the application URL to test.
     * 
     * @return Application URL from config.properties (app.url)
     */
    public String getAppUrl() {
        return getProperty("app.url");
    }
    
    /**
     * Gets the default browser for test execution.
     * Checks system property first (from Maven -Dbrowser=chrome), then falls back to config.properties.
     * 
     * @return Browser name (chrome, firefox, edge) from system property or config.properties
     */
    public String getBrowser() {
        String browser = System.getProperty("browser");
        if (browser != null && !browser.isEmpty()) {
            logger.info("Using browser from system property: " + browser);
            return browser;
        }
        return getProperty("browser");
    }
    
    /**
     * Determines if tests should run in headless mode.
     * Checks system property first (from Maven -Dheadless=true), then falls back to config.properties.
     * 
     * @return true if headless mode is enabled, false otherwise
     */
    public boolean isHeadless() {
        String headless = System.getProperty("headless");
        if (headless != null && !headless.isEmpty()) {
            logger.info("Using headless mode from system property: " + headless);
            return Boolean.parseBoolean(headless);
        }
        return Boolean.parseBoolean(getProperty("headless"));
    }
    
    /**
     * Gets the implicit wait timeout in seconds.
     * Applied globally to all element location operations.
     * 
     * @return Implicit wait timeout in seconds
     */
    public int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait"));
    }
    
    /**
     * Gets the explicit wait timeout in seconds.
     * Used for waiting on specific conditions using WebDriverWait.
     * 
     * @return Explicit wait timeout in seconds
     */
    public int getExplicitWait() {
        return Integer.parseInt(getProperty("explicit.wait"));
    }
    
    /**
     * Gets the page load timeout in seconds.
     * Maximum time to wait for a page to fully load.
     * 
     * @return Page load timeout in seconds
     */
    public int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("page.load.timeout"));
    }
    
    /**
     * Gets the directory path where screenshots will be saved.
     * 
     * @return Screenshot directory path (should end with /)
     */
    public String getScreenshotPath() {
        return getProperty("screenshot.path");
    }
    
    /**
     * Gets the directory path where test reports will be saved.
     * 
     * @return Report directory path (should end with /)
     */
    public String getReportPath() {
        return getProperty("report.path");
    }
    
    /**
     * Gets the number of parallel threads for test execution.
     * Used in TestNG XML configuration for parallel execution.
     * 
     * @return Number of threads for parallel execution
     */
    public int getThreadCount() {
        return Integer.parseInt(getProperty("thread.count"));
    }
    
    /**
     * Gets the LambdaTest username.
     * Priority order:
     * 1. Environment variable LT_USERNAME (for CI/CD)
     * 2. config.local.properties file (for local development)
     * 3. config.properties file (fallback, not recommended)
     * 
     * @return LambdaTest username or null if not configured
     */
    public String getLambdaTestUsername() {
        // Priority 1: Environment variable (for CI/CD)
        String username = System.getenv("LT_USERNAME");
        if (username != null && !username.isEmpty()) {
            logger.debug("Using LT_USERNAME from environment variable");
            return username;
        }
        
        // Priority 2: Local config file (for Eclipse/local development)
        if (localProperties != null) {
            String localUsername = localProperties.getProperty("lt.username");
            if (localUsername != null && !localUsername.isEmpty()) {
                logger.debug("Using LT username from config.local.properties");
                return localUsername;
            }
        }
        
        // Priority 3: Main config file (fallback, not recommended)
        String configUsername = getProperty("lt.username");
        if (configUsername != null && !configUsername.isEmpty()) {
            logger.warn("Using LT username from config.properties (not secure - use config.local.properties instead)");
            return configUsername;
        }
        
        return null;
    }
    
    /**
     * Gets the LambdaTest access key.
     * Priority order:
     * 1. Environment variable LT_ACCESS_KEY (for CI/CD)
     * 2. config.local.properties file (for local development)
     * 3. config.properties file (fallback, not recommended)
     * 
     * @return LambdaTest access key or null if not configured
     */
    public String getLambdaTestAccessKey() {
        // Priority 1: Environment variable (for CI/CD)
        String accessKey = System.getenv("LT_ACCESS_KEY");
        if (accessKey != null && !accessKey.isEmpty()) {
            logger.debug("Using LT_ACCESS_KEY from environment variable");
            return accessKey;
        }
        
        // Priority 2: Local config file (for Eclipse/local development)
        if (localProperties != null) {
            String localAccessKey = localProperties.getProperty("lt.accesskey");
            if (localAccessKey != null && !localAccessKey.isEmpty()) {
                logger.debug("Using LT access key from config.local.properties");
                return localAccessKey;
            }
        }
        
        // Priority 3: Main config file (fallback, not recommended)
        String configAccessKey = getProperty("lt.accesskey");
        if (configAccessKey != null && !configAccessKey.isEmpty()) {
            logger.warn("Using LT access key from config.properties (not secure - use config.local.properties instead)");
            return configAccessKey;
        }
        
        return null;
    }
}
