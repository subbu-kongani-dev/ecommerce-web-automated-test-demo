package com.nopcommerce.utils;

import com.nopcommerce.models.BrowserCapability;
import org.yaml.snakeyaml.Yaml;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

/**
 * CapabilityReader loads and parses browser capability configurations from YAML files.
 * Supports both local and remote (LambdaTest) execution configurations.
 * 
 * Key Features:
 * - Parses YAML configuration files for browser capabilities
 * - Supports environment variable substitution (${ENV_VAR})
 * - Loads configurations from classpath or file system
 * - Thread-safe implementation
 * 
 * @author NopCommerce Automation Team
 * @version 1.0
 */
public class CapabilityReader {
    private static final Logger logger = LogManager.getLogger(CapabilityReader.class);
    private static final String CAPABILITIES_PATH = "capabilities/";
    
    /**
     * Loads browser capability configuration from YAML file.
     * 
     * @param browser Browser name (chrome, firefox, edge, safari)
     * @param platform Platform type (local, lambdatest)
     * @return BrowserCapability object containing all configuration
     * @throws RuntimeException if configuration file is not found or invalid
     */
    public static BrowserCapability loadCapabilities(String browser, String platform) {
        String fileName = String.format("%s_%s.yaml", browser.toLowerCase(), platform.toLowerCase());
        logger.info("Loading capabilities from: " + fileName);
        
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = CapabilityReader.class.getClassLoader()
                .getResourceAsStream(CAPABILITIES_PATH + fileName);
            
            if (inputStream == null) {
                // Try loading from file system
                inputStream = new FileInputStream("src/main/resources/" + CAPABILITIES_PATH + fileName);
            }
            
            // Load YAML into a Map first, then convert to BrowserCapability
            Map<String, Object> yamlMap = yaml.load(inputStream);
            BrowserCapability capability = mapToBrowserCapability(yamlMap);
            
            // Substitute environment variables
            substituteEnvironmentVariables(capability);
            
            logger.info("Successfully loaded capabilities for " + browser + " on " + platform);
            return capability;
            
        } catch (Exception e) {
            logger.error("Failed to load capabilities from " + fileName, e);
            throw new RuntimeException("Failed to load capability configuration: " + fileName, e);
        }
    }
    
    /**
     * Converts YAML Map to BrowserCapability object.
     * 
     * @param yamlMap Map loaded from YAML file
     * @return BrowserCapability object
     */
    @SuppressWarnings("unchecked")
    private static BrowserCapability mapToBrowserCapability(Map<String, Object> yamlMap) {
        BrowserCapability capability = new BrowserCapability();
        
        capability.setBrowser((String) yamlMap.get("browser"));
        capability.setPlatform((String) yamlMap.get("platform"));
        capability.setHeadless(yamlMap.get("headless") != null ? (Boolean) yamlMap.get("headless") : false);
        capability.setCapabilities((Map<String, Object>) yamlMap.get("capabilities"));
        
        // Map options
        if (yamlMap.get("options") != null) {
            Map<String, Object> optionsMap = (Map<String, Object>) yamlMap.get("options");
            BrowserCapability.OptionsConfig options = new BrowserCapability.OptionsConfig();
            options.setArgs((List<String>) optionsMap.get("args"));
            options.setPrefs((Map<String, Object>) optionsMap.get("prefs"));
            options.setExperimentalOptions((Map<String, Object>) optionsMap.get("experimentalOptions"));
            
            if (optionsMap.get("useTechnologyPreview") != null) {
                options.setUseTechnologyPreview((Boolean) optionsMap.get("useTechnologyPreview"));
            }
            if (optionsMap.get("automaticInspection") != null) {
                options.setAutomaticInspection((Boolean) optionsMap.get("automaticInspection"));
            }
            if (optionsMap.get("automaticProfiling") != null) {
                options.setAutomaticProfiling((Boolean) optionsMap.get("automaticProfiling"));
            }
            capability.setOptions(options);
        }
        
        // Map timeouts
        if (yamlMap.get("timeouts") != null) {
            Map<String, Object> timeoutsMap = (Map<String, Object>) yamlMap.get("timeouts");
            BrowserCapability.TimeoutsConfig timeouts = new BrowserCapability.TimeoutsConfig();
            timeouts.setImplicit((Integer) timeoutsMap.get("implicit"));
            timeouts.setPageLoad((Integer) timeoutsMap.get("pageLoad"));
            timeouts.setScript((Integer) timeoutsMap.get("script"));
            capability.setTimeouts(timeouts);
        }
        
        // Map window
        if (yamlMap.get("window") != null) {
            Map<String, Object> windowMap = (Map<String, Object>) yamlMap.get("window");
            BrowserCapability.WindowConfig window = new BrowserCapability.WindowConfig();
            window.setMaximize(windowMap.get("maximize") != null ? (Boolean) windowMap.get("maximize") : true);
            window.setWidth(windowMap.get("width") != null ? (Integer) windowMap.get("width") : 1920);
            window.setHeight(windowMap.get("height") != null ? (Integer) windowMap.get("height") : 1080);
            capability.setWindow(window);
        }
        
        // Map LambdaTest config
        if (yamlMap.get("lambdatest") != null) {
            Map<String, Object> ltMap = (Map<String, Object>) yamlMap.get("lambdatest");
            BrowserCapability.LambdaTestConfig ltConfig = new BrowserCapability.LambdaTestConfig();
            ltConfig.setHubUrl((String) ltMap.get("hubUrl"));
            ltConfig.setUser((String) ltMap.get("user"));
            ltConfig.setAccessKey((String) ltMap.get("accessKey"));
            capability.setLambdatest(ltConfig);
        }
        
        return capability;
    }
    
    /**
     * Substitutes environment variables in configuration.
     * Supports ${VAR_NAME} format.
     * First checks environment variables, then falls back to config.properties.
     * 
     * @param capability BrowserCapability object to process
     */
    private static void substituteEnvironmentVariables(BrowserCapability capability) {
        if (capability.getLambdatest() != null) {
            BrowserCapability.LambdaTestConfig ltConfig = capability.getLambdatest();
            ConfigReader config = ConfigReader.getInstance();
            
            // Substitute LT_USERNAME
            if (ltConfig.getUser() != null && ltConfig.getUser().contains("${")) {
                String envVar = extractEnvVarName(ltConfig.getUser());
                String value = System.getenv(envVar);
                
                if (value != null && !value.isEmpty()) {
                    ltConfig.setUser(value);
                    logger.debug("Substituted " + envVar + " from environment variable");
                } else {
                    logger.warn("Environment variable not found: " + envVar);
                    // Fallback to config.properties
                    String configValue = config.getLambdaTestUsername();
                    if (configValue != null && !configValue.isEmpty()) {
                        ltConfig.setUser(configValue);
                        logger.info("Using LambdaTest username from config.properties");
                    } else {
                        logger.error("LambdaTest username not found in environment or config.properties");
                    }
                }
            }
            
            // Substitute LT_ACCESS_KEY
            if (ltConfig.getAccessKey() != null && ltConfig.getAccessKey().contains("${")) {
                String envVar = extractEnvVarName(ltConfig.getAccessKey());
                String value = System.getenv(envVar);
                
                if (value != null && !value.isEmpty()) {
                    ltConfig.setAccessKey(value);
                    logger.debug("Substituted " + envVar + " from environment variable");
                } else {
                    logger.warn("Environment variable not found: " + envVar);
                    // Fallback to config.properties
                    String configValue = config.getLambdaTestAccessKey();
                    if (configValue != null && !configValue.isEmpty()) {
                        ltConfig.setAccessKey(configValue);
                        logger.info("Using LambdaTest access key from config.properties");
                    } else {
                        logger.error("LambdaTest access key not found in environment or config.properties");
                    }
                }
            }
        }
    }
    
    /**
     * Extracts environment variable name from ${VAR_NAME} format.
     * 
     * @param value String containing environment variable placeholder
     * @return Environment variable name
     */
    private static String extractEnvVarName(String value) {
        return value.substring(value.indexOf("{") + 1, value.indexOf("}"));
    }
    
    /**
     * Checks if configuration exists for given browser and platform.
     * 
     * @param browser Browser name
     * @param platform Platform type
     * @return true if configuration file exists, false otherwise
     */
    public static boolean hasCapabilities(String browser, String platform) {
        String fileName = String.format("%s_%s.yaml", browser.toLowerCase(), platform.toLowerCase());
        InputStream inputStream = CapabilityReader.class.getClassLoader()
            .getResourceAsStream(CAPABILITIES_PATH + fileName);
        return inputStream != null;
    }
}
