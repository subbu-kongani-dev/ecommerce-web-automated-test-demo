package com.nopcommerce.models;

import java.util.List;
import java.util.Map;

/**
 * Model class representing browser capability configuration loaded from YAML files.
 * Uses builder pattern for flexible object creation.
 * 
 * @author NopCommerce Automation Team
 * @version 1.0
 */
public class BrowserCapability {
    private String browser;
    private String platform;
    private OptionsConfig options;
    private Map<String, Object> capabilities;
    private TimeoutsConfig timeouts;
    private WindowConfig window;
    private boolean headless;
    private LambdaTestConfig lambdatest;
    
    // Getters and Setters
    public String getBrowser() {
        return browser;
    }
    
    public void setBrowser(String browser) {
        this.browser = browser;
    }
    
    public String getPlatform() {
        return platform;
    }
    
    public void setPlatform(String platform) {
        this.platform = platform;
    }
    
    public OptionsConfig getOptions() {
        return options;
    }
    
    public void setOptions(OptionsConfig options) {
        this.options = options;
    }
    
    public Map<String, Object> getCapabilities() {
        return capabilities;
    }
    
    public void setCapabilities(Map<String, Object> capabilities) {
        this.capabilities = capabilities;
    }
    
    public TimeoutsConfig getTimeouts() {
        return timeouts;
    }
    
    public void setTimeouts(TimeoutsConfig timeouts) {
        this.timeouts = timeouts;
    }
    
    public WindowConfig getWindow() {
        return window;
    }
    
    public void setWindow(WindowConfig window) {
        this.window = window;
    }
    
    public boolean isHeadless() {
        return headless;
    }
    
    public void setHeadless(boolean headless) {
        this.headless = headless;
    }
    
    public LambdaTestConfig getLambdatest() {
        return lambdatest;
    }
    
    public void setLambdatest(LambdaTestConfig lambdatest) {
        this.lambdatest = lambdatest;
    }
    
    /**
     * Options configuration for browser
     */
    public static class OptionsConfig {
        private List<String> args;
        private Map<String, Object> prefs;
        private Map<String, Object> experimentalOptions;
        private boolean useTechnologyPreview;
        private boolean automaticInspection;
        private boolean automaticProfiling;
        
        public List<String> getArgs() {
            return args;
        }
        
        public void setArgs(List<String> args) {
            this.args = args;
        }
        
        public Map<String, Object> getPrefs() {
            return prefs;
        }
        
        public void setPrefs(Map<String, Object> prefs) {
            this.prefs = prefs;
        }
        
        public Map<String, Object> getExperimentalOptions() {
            return experimentalOptions;
        }
        
        public void setExperimentalOptions(Map<String, Object> experimentalOptions) {
            this.experimentalOptions = experimentalOptions;
        }
        
        public boolean isUseTechnologyPreview() {
            return useTechnologyPreview;
        }
        
        public void setUseTechnologyPreview(boolean useTechnologyPreview) {
            this.useTechnologyPreview = useTechnologyPreview;
        }
        
        public boolean isAutomaticInspection() {
            return automaticInspection;
        }
        
        public void setAutomaticInspection(boolean automaticInspection) {
            this.automaticInspection = automaticInspection;
        }
        
        public boolean isAutomaticProfiling() {
            return automaticProfiling;
        }
        
        public void setAutomaticProfiling(boolean automaticProfiling) {
            this.automaticProfiling = automaticProfiling;
        }
    }
    
    /**
     * Timeouts configuration
     */
    public static class TimeoutsConfig {
        private int implicit;
        private int pageLoad;
        private int script;
        
        public int getImplicit() {
            return implicit;
        }
        
        public void setImplicit(int implicit) {
            this.implicit = implicit;
        }
        
        public int getPageLoad() {
            return pageLoad;
        }
        
        public void setPageLoad(int pageLoad) {
            this.pageLoad = pageLoad;
        }
        
        public int getScript() {
            return script;
        }
        
        public void setScript(int script) {
            this.script = script;
        }
    }
    
    /**
     * Window configuration
     */
    public static class WindowConfig {
        private boolean maximize;
        private int width;
        private int height;
        
        public boolean isMaximize() {
            return maximize;
        }
        
        public void setMaximize(boolean maximize) {
            this.maximize = maximize;
        }
        
        public int getWidth() {
            return width;
        }
        
        public void setWidth(int width) {
            this.width = width;
        }
        
        public int getHeight() {
            return height;
        }
        
        public void setHeight(int height) {
            this.height = height;
        }
    }
    
    /**
     * LambdaTest configuration
     */
    public static class LambdaTestConfig {
        private String hubUrl;
        private String user;
        private String accessKey;
        
        public String getHubUrl() {
            return hubUrl;
        }
        
        public void setHubUrl(String hubUrl) {
            this.hubUrl = hubUrl;
        }
        
        public String getUser() {
            return user;
        }
        
        public void setUser(String user) {
            this.user = user;
        }
        
        public String getAccessKey() {
            return accessKey;
        }
        
        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }
    }
}
