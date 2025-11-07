package com.nopcommerce.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.Duration;

/**
 * DriverManager handles WebDriver initialization and lifecycle management.
 * Uses ThreadLocal pattern to support parallel test execution.
 * 
 * Key Features:
 * - Manages WebDriver instances using ThreadLocal for thread-safety
 * - Supports multiple browsers (Chrome, Firefox, Edge)
 * - Configures browser options (headless mode, notifications, popups)
 * - Automatically sets up driver binaries using WebDriverManager
 * - Configures timeouts (implicit, page load) from config properties
 * - Maximizes browser window by default
 * 
 * ThreadLocal Pattern:
 * - Each thread gets its own WebDriver instance
 * - Enables parallel test execution without interference
 * - Prevents thread-safety issues in multi-threaded scenarios
 * 
 * @author NopCommerce Automation Team
 * @version 1.0
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
     * 
     * @param browserName Name of browser to initialize (chrome, firefox, edge).
     *                    If null or empty, uses browser from config.properties.
     *                    Case-insensitive.
     * @return WebDriver instance for the current thread.
     * @throws IllegalArgumentException if unsupported browser is specified.
     * 
     * Browser Configuration:
     * - Chrome: Supports headless mode, disables notifications and popups
     * - Firefox: Supports headless mode
     * - Edge: Supports headless mode
     * 
     * Timeout Configuration:
     * - Implicit Wait: Applied to element location
     * - Page Load Timeout: Maximum time to wait for page load
     * Both values read from config.properties
     */
    public static WebDriver getDriver(String browserName) {
        if (driver.get() == null) {
            // Use parameter browser if provided, otherwise use config browser
            String browser = (browserName != null && !browserName.isEmpty()) 
                ? browserName.toLowerCase() 
                : config.getBrowser().toLowerCase();
            boolean headless = config.isHeadless();
            
            logger.info("Initializing " + browser + " browser (Headless: " + headless + ")");
            
            switch (browser) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (headless) {
                        chromeOptions.addArguments("--headless=new");
                        chromeOptions.addArguments("--window-size=1920,1080");
                    }
                    chromeOptions.addArguments("--disable-notifications");
                    chromeOptions.addArguments("--disable-popup-blocking");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--remote-allow-origins=*");
                    chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                    driver.set(new ChromeDriver(chromeOptions));
                    break;
                    
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (headless) {
                        firefoxOptions.addArguments("-headless");
                    }
                    firefoxOptions.addArguments("--width=1920");
                    firefoxOptions.addArguments("--height=1080");
                    firefoxOptions.addPreference("dom.webnotifications.enabled", false);
                    firefoxOptions.addPreference("dom.push.enabled", false);
                    firefoxOptions.addPreference("media.navigator.enabled", false);
                    firefoxOptions.addPreference("media.peerconnection.enabled", false);
                    driver.set(new FirefoxDriver(firefoxOptions));
                    break;
                    
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    if (headless) {
                        edgeOptions.addArguments("--headless");
                    }
                    driver.set(new EdgeDriver(edgeOptions));
                    break;
                    
                default:
                    logger.error("Invalid browser: " + browser);
                    throw new IllegalArgumentException("Browser not supported: " + browser);
            }
            
            driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
            driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
            driver.get().manage().window().maximize();
            logger.info("Browser initialized successfully");
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
