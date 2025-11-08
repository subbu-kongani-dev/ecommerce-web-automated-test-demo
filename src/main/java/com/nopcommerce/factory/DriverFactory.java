package com.nopcommerce.factory;

import com.nopcommerce.models.BrowserCapability;
import com.nopcommerce.utils.CapabilityReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.MutableCapabilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * DriverFactory creates WebDriver instances based on YAML configuration files.
 * Implements Factory design pattern for flexible driver creation.
 * Supports both local and remote (LambdaTest) execution.
 * 
 * Key Features:
 * - Dynamic capability loading from YAML files
 * - Support for multiple browsers (Chrome, Firefox, Edge, Safari)
 * - Local and cloud (LambdaTest) execution support
 * - Centralized driver configuration management
 * - Thread-safe implementation
 * 
 * Design Patterns:
 * - Factory Pattern: Encapsulates driver creation logic
 * - Strategy Pattern: Different strategies for local vs remote execution
 * 
 * @author NopCommerce Automation Team
 * @version 2.0
 */
public class DriverFactory {
    private static final Logger logger = LogManager.getLogger(DriverFactory.class);
    
    /**
     * Creates WebDriver instance based on browser and platform.
     * 
     * @param browser Browser name (chrome, firefox, edge, safari)
     * @param platform Platform type (local, lambdatest)
     * @return Configured WebDriver instance
     * @throws IllegalArgumentException if unsupported browser or platform
     */
    public static WebDriver createDriver(String browser, String platform) {
        logger.info("Creating driver for browser: {} on platform: {}", browser, platform);
        
        BrowserCapability capability = CapabilityReader.loadCapabilities(browser, platform);
        WebDriver driver;
        
        if ("LAMBDATEST".equalsIgnoreCase(platform)) {
            driver = createRemoteDriver(capability);
        } else {
            driver = createLocalDriver(capability);
        }
        
        // Apply timeouts
        applyTimeouts(driver, capability);
        
        // Apply window configuration
        applyWindowConfiguration(driver, capability);
        
        logger.info("Driver created successfully");
        return driver;
    }
    
    /**
     * Creates local WebDriver instance.
     * 
     * @param capability Browser capability configuration
     * @return WebDriver instance
     */
    private static WebDriver createLocalDriver(BrowserCapability capability) {
        String browser = capability.getBrowser().toLowerCase();
        
        switch (browser) {
            case "chrome":
                return createChromeDriver(capability);
            case "firefox":
                return createFirefoxDriver(capability);
            case "edge":
                return createEdgeDriver(capability);
            case "safari":
                return createSafariDriver(capability);
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }
    
    /**
     * Creates Chrome driver with configuration from YAML.
     * 
     * @param capability Browser capability configuration
     * @return ChromeDriver instance
     */
    private static WebDriver createChromeDriver(BrowserCapability capability) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        // Apply headless mode
        if (capability.isHeadless()) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=" + 
                capability.getWindow().getWidth() + "," + 
                capability.getWindow().getHeight());
        }
        
        // Apply arguments from YAML
        if (capability.getOptions() != null && capability.getOptions().getArgs() != null) {
            capability.getOptions().getArgs().forEach(options::addArguments);
        }
        
        // Apply preferences from YAML
        if (capability.getOptions() != null && capability.getOptions().getPrefs() != null) {
            Map<String, Object> prefs = capability.getOptions().getPrefs();
            options.setExperimentalOption("prefs", prefs);
        }
        
        // Apply experimental options from YAML
        if (capability.getOptions() != null && 
            capability.getOptions().getExperimentalOptions() != null) {
            capability.getOptions().getExperimentalOptions().forEach(options::setExperimentalOption);
        }
        
        // Apply capabilities from YAML
        if (capability.getCapabilities() != null) {
            capability.getCapabilities().forEach((key, value) -> {
                if (value instanceof Boolean) {
                    options.setCapability(key, (Boolean) value);
                } else {
                    options.setCapability(key, value.toString());
                }
            });
        }
        
        logger.info("Chrome driver initialized with YAML configuration");
        return new ChromeDriver(options);
    }
    
    /**
     * Creates Firefox driver with configuration from YAML.
     * 
     * @param capability Browser capability configuration
     * @return FirefoxDriver instance
     */
    private static WebDriver createFirefoxDriver(BrowserCapability capability) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        
        // Apply headless mode
        if (capability.isHeadless()) {
            options.addArguments("-headless");
        }
        
        // Apply arguments from YAML
        if (capability.getOptions() != null && capability.getOptions().getArgs() != null) {
            capability.getOptions().getArgs().forEach(options::addArguments);
        }
        
        // Apply preferences from YAML
        if (capability.getOptions() != null && capability.getOptions().getPrefs() != null) {
            capability.getOptions().getPrefs().forEach((key, value) -> {
                if (value instanceof Boolean) {
                    options.addPreference(key, (Boolean) value);
                } else if (value instanceof Integer) {
                    options.addPreference(key, (Integer) value);
                } else {
                    options.addPreference(key, value.toString());
                }
            });
        }
        
        // Apply capabilities from YAML
        if (capability.getCapabilities() != null) {
            capability.getCapabilities().forEach((key, value) -> {
                if (value instanceof Boolean) {
                    options.setCapability(key, (Boolean) value);
                } else {
                    options.setCapability(key, value.toString());
                }
            });
        }
        
        logger.info("Firefox driver initialized with YAML configuration");
        return new FirefoxDriver(options);
    }
    
    /**
     * Creates Edge driver with configuration from YAML.
     * 
     * @param capability Browser capability configuration
     * @return EdgeDriver instance
     */
    private static WebDriver createEdgeDriver(BrowserCapability capability) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        
        // Apply headless mode
        if (capability.isHeadless()) {
            options.addArguments("--headless");
        }
        
        // Apply arguments from YAML
        if (capability.getOptions() != null && capability.getOptions().getArgs() != null) {
            capability.getOptions().getArgs().forEach(options::addArguments);
        }
        
        // Apply preferences from YAML
        if (capability.getOptions() != null && capability.getOptions().getPrefs() != null) {
            Map<String, Object> prefs = capability.getOptions().getPrefs();
            options.setExperimentalOption("prefs", prefs);
        }
        
        // Apply experimental options from YAML
        if (capability.getOptions() != null && 
            capability.getOptions().getExperimentalOptions() != null) {
            capability.getOptions().getExperimentalOptions().forEach(options::setExperimentalOption);
        }
        
        // Apply capabilities from YAML
        if (capability.getCapabilities() != null) {
            capability.getCapabilities().forEach((key, value) -> {
                if (value instanceof Boolean) {
                    options.setCapability(key, (Boolean) value);
                } else {
                    options.setCapability(key, value.toString());
                }
            });
        }
        
        logger.info("Edge driver initialized with YAML configuration");
        return new EdgeDriver(options);
    }
    
    /**
     * Creates Safari driver with configuration from YAML.
     * 
     * @param capability Browser capability configuration
     * @return SafariDriver instance
     */
    private static WebDriver createSafariDriver(BrowserCapability capability) {
        WebDriverManager.safaridriver().setup();
        SafariOptions options = new SafariOptions();
        
        // Apply capabilities from YAML
        if (capability.getCapabilities() != null) {
            capability.getCapabilities().forEach((key, value) -> {
                if (value instanceof Boolean) {
                    options.setCapability(key, (Boolean) value);
                } else {
                    options.setCapability(key, value.toString());
                }
            });
        }
        
        // Safari doesn't support headless mode
        if (capability.isHeadless()) {
            logger.warn("Safari does not support headless mode, ignoring headless configuration");
        }
        
        logger.info("Safari driver initialized with YAML configuration");
        return new SafariDriver(options);
    }
    
    /**
     * Creates remote WebDriver instance for LambdaTest execution.
     * 
     * @param capability Browser capability configuration
     * @return RemoteWebDriver instance
     */
    private static WebDriver createRemoteDriver(BrowserCapability capability) {
        try {
            BrowserCapability.LambdaTestConfig ltConfig = capability.getLambdatest();
            
            if (ltConfig == null) {
                throw new IllegalStateException("LambdaTest configuration not found in YAML");
            }
            
            String username = ltConfig.getUser();
            String accessKey = ltConfig.getAccessKey();
            String hubUrl = ltConfig.getHubUrl();
            
            if (username == null || accessKey == null) {
                throw new IllegalStateException(
                    "LambdaTest credentials not set. Please set LT_USERNAME and LT_ACCESS_KEY environment variables");
            }
            
            // Create capabilities for LambdaTest
            MutableCapabilities capabilities = new MutableCapabilities();
            
            // Add all capabilities from YAML
            if (capability.getCapabilities() != null) {
                capability.getCapabilities().forEach(capabilities::setCapability);
            }
            
            // Add browser-specific options
            capabilities = addBrowserOptionsForRemote(capabilities, capability);
            
            // Create remote driver
            URL remoteUrl = new URL(String.format("https://%s:%s@%s", 
                username, accessKey, hubUrl.replace("https://", "")));
            
            logger.info("Connecting to LambdaTest Grid: " + hubUrl);
            return new RemoteWebDriver(remoteUrl, capabilities);
            
        } catch (Exception e) {
            logger.error("Failed to create remote driver", e);
            throw new RuntimeException("Failed to create remote driver for LambdaTest", e);
        }
    }
    
    /**
     * Adds browser-specific options for remote execution.
     * 
     * @param capabilities Base capabilities
     * @param capability Browser capability configuration
     * @return Enhanced capabilities with browser options
     */
    private static MutableCapabilities addBrowserOptionsForRemote(
            MutableCapabilities capabilities, BrowserCapability capability) {
        
        String browser = capability.getBrowser().toLowerCase();
        
        if (capability.getOptions() != null) {
            switch (browser) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (capability.getOptions().getArgs() != null) {
                        capability.getOptions().getArgs().forEach(chromeOptions::addArguments);
                    }
                    if (capability.getOptions().getPrefs() != null) {
                        chromeOptions.setExperimentalOption("prefs", capability.getOptions().getPrefs());
                    }
                    capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                    break;
                    
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (capability.getOptions().getPrefs() != null) {
                        capability.getOptions().getPrefs().forEach((key, value) -> {
                            if (value instanceof Boolean) {
                                firefoxOptions.addPreference(key, (Boolean) value);
                            } else if (value instanceof Integer) {
                                firefoxOptions.addPreference(key, (Integer) value);
                            } else {
                                firefoxOptions.addPreference(key, value.toString());
                            }
                        });
                    }
                    capabilities.setCapability("moz:firefoxOptions", firefoxOptions);
                    break;
                    
                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    if (capability.getOptions().getArgs() != null) {
                        capability.getOptions().getArgs().forEach(edgeOptions::addArguments);
                    }
                    if (capability.getOptions().getPrefs() != null) {
                        edgeOptions.setExperimentalOption("prefs", capability.getOptions().getPrefs());
                    }
                    capabilities.setCapability("ms:edgeOptions", edgeOptions);
                    break;
            }
        }
        
        return capabilities;
    }
    
    /**
     * Applies timeout configuration to driver.
     * 
     * @param driver WebDriver instance
     * @param capability Browser capability configuration
     */
    private static void applyTimeouts(WebDriver driver, BrowserCapability capability) {
        if (capability.getTimeouts() != null) {
            BrowserCapability.TimeoutsConfig timeouts = capability.getTimeouts();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeouts.getImplicit()));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(timeouts.getPageLoad()));
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(timeouts.getScript()));
            logger.debug("Applied timeouts from YAML configuration");
        }
    }
    
    /**
     * Applies window configuration to driver.
     * 
     * @param driver WebDriver instance
     * @param capability Browser capability configuration
     */
    private static void applyWindowConfiguration(WebDriver driver, BrowserCapability capability) {
        if (capability.getWindow() != null) {
            BrowserCapability.WindowConfig window = capability.getWindow();
            if (window.isMaximize()) {
                driver.manage().window().maximize();
                logger.debug("Window maximized");
            } else if (window.getWidth() > 0 && window.getHeight() > 0) {
                org.openqa.selenium.Dimension dimension = 
                    new org.openqa.selenium.Dimension(window.getWidth(), window.getHeight());
                driver.manage().window().setSize(dimension);
                logger.debug("Window size set to: {}x{}", window.getWidth(), window.getHeight());
            }
        }
    }
}
