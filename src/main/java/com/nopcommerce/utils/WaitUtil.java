package com.nopcommerce.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nopcommerce.core.config.ConfigurationManager;

import java.time.Duration;

/**
 * WaitUtil provides reusable wait strategies for Selenium WebDriver.
 * Enhanced with better error handling and retry logic for CI/CD environments.
 * 
 * @author NopCommerce Automation Team
 * @version 2.0
 */
public class WaitUtil {
    private static final Logger logger = LogManager.getLogger(WaitUtil.class);
    private static final ConfigurationManager config = ConfigurationManager.getInstance();
    
    // Increased timeout for CI/CD environments (GHA, headless browsers)
    private static final int CI_TIMEOUT_MULTIPLIER = isCI() ? 2 : 1;
    
    /**
     * Detect if running in CI/CD environment
     */
    private static boolean isCI() {
        return System.getenv("CI") != null || 
               System.getenv("GITHUB_ACTIONS") != null ||
               System.getenv("JENKINS_HOME") != null;
    }

    public static WebElement waitForElementToBeVisible(WebDriver driver, WebElement element) {
        try {
            int timeout = config.getExplicitWait() * CI_TIMEOUT_MULTIPLIER;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            wait.pollingEvery(Duration.ofMillis(300));
            wait.ignoring(org.openqa.selenium.StaleElementReferenceException.class);
            wait.ignoring(org.openqa.selenium.NoSuchElementException.class);
            return wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            logger.error("Element not visible within {} seconds timeout (CI mode: {})", 
                config.getExplicitWait() * CI_TIMEOUT_MULTIPLIER, isCI(), e);
            throw e;
        }
    }

    public static WebElement waitForElementToBeClickable(WebDriver driver, WebElement element) {
        try {
            int timeout = config.getExplicitWait() * CI_TIMEOUT_MULTIPLIER;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            wait.pollingEvery(Duration.ofMillis(300));
            wait.ignoring(org.openqa.selenium.StaleElementReferenceException.class);
            wait.ignoring(org.openqa.selenium.ElementClickInterceptedException.class);
            wait.ignoring(org.openqa.selenium.NoSuchElementException.class);
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            logger.error("Element not clickable within {} seconds timeout (CI mode: {})", 
                config.getExplicitWait() * CI_TIMEOUT_MULTIPLIER, isCI(), e);
            throw e;
        }
    }
    
    /**
     * Wait for page to be fully loaded and ready.
     * Enhanced with multiple stability checks for CI/CD environments.
     * 
     * @param driver WebDriver instance
     */
    public static void waitForPageLoad(WebDriver driver) {
        try {
            int timeout = isCI() ? 60 : 30; // Longer timeout in CI
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            
            // Wait for document ready state
            wait.until(webDriver -> 
                ((org.openqa.selenium.JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));
            
            // Wait for jQuery if present
            try {
                wait.until(webDriver -> 
                    (Boolean) ((org.openqa.selenium.JavascriptExecutor) webDriver)
                        .executeScript("return typeof jQuery === 'undefined' || jQuery.active === 0"));
            } catch (Exception e) {
                // jQuery not present, continue
            }
            
            // Additional stability pause in CI environments
            if (isCI()) {
                shortPause(2000);
            }
            
            logger.debug("Page loaded successfully (CI mode: {})", isCI());
        } catch (Exception e) {
            logger.warn("Page load wait timed out after {} seconds, continuing anyway (CI mode: {})", 
                isCI() ? 60 : 30, isCI(), e);
        }
    }
    
    /**
     * Add a brief pause for element stability.
     * Useful in CI/CD environments where page rendering may be slower.
     * 
     * @param milliseconds Duration to wait
     */
    public static void shortPause(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Short pause interrupted", e);
        }
    }
    
    /**
     * Wait for DOM to stabilize (no changes for a period).
     * Critical for dynamic pages with animations/transitions in headless mode.
     * 
     * @param driver WebDriver instance
     */
    public static void waitForDomStability(WebDriver driver) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            int maxAttempts = 5;
            int stableCount = 0;
            String lastDomState = "";
            
            for (int i = 0; i < maxAttempts; i++) {
                String currentDomState = (String) js.executeScript(
                    "return document.body.innerHTML.length.toString()");
                
                if (currentDomState.equals(lastDomState)) {
                    stableCount++;
                    if (stableCount >= 2) {
                        logger.debug("DOM is stable after {} checks", i + 1);
                        return;
                    }
                } else {
                    stableCount = 0;
                }
                
                lastDomState = currentDomState;
                shortPause(500);
            }
            
            logger.debug("DOM stability check completed");
        } catch (Exception e) {
            logger.warn("DOM stability check failed, continuing", e);
        }
    }
    
    /**
     * Wait for element to be present in DOM (but not necessarily visible).
     * Useful for checking if element exists before visibility check.
     * 
     * @param driver WebDriver instance
     * @param locator By locator to find element
     * @return WebElement if found
     */
    public static WebElement waitForElementPresence(WebDriver driver, org.openqa.selenium.By locator) {
        try {
            int timeout = config.getExplicitWait() * CI_TIMEOUT_MULTIPLIER;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            wait.pollingEvery(Duration.ofMillis(300));
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            logger.error("Element not present within {} seconds: {}", 
                config.getExplicitWait() * CI_TIMEOUT_MULTIPLIER, locator, e);
            throw e;
        }
    }
    
    /**
     * Stale-element-safe wait for clickable element using By locator.
     * This is the RECOMMENDED approach over waitForElementToBeClickable(WebElement).
     * Automatically handles StaleElementReferenceException by re-locating the element.
     * 
     * @param driver WebDriver instance
     * @param locator By locator to find element
     * @return Fresh WebElement that is clickable
     */
    public static WebElement waitForClickableElement(WebDriver driver, org.openqa.selenium.By locator) {
        try {
            int timeout = config.getExplicitWait() * CI_TIMEOUT_MULTIPLIER;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            wait.pollingEvery(Duration.ofMillis(300));
            wait.ignoring(org.openqa.selenium.StaleElementReferenceException.class);
            wait.ignoring(org.openqa.selenium.ElementClickInterceptedException.class);
            wait.ignoring(org.openqa.selenium.NoSuchElementException.class);
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (Exception e) {
            logger.error("Element not clickable within {} seconds: {}", 
                config.getExplicitWait() * CI_TIMEOUT_MULTIPLIER, locator, e);
            throw e;
        }
    }
    
    /**
     * Stale-element-safe wait for visible element using By locator.
     * This is the RECOMMENDED approach over waitForElementToBeVisible(WebElement).
     * Automatically handles StaleElementReferenceException by re-locating the element.
     * 
     * @param driver WebDriver instance
     * @param locator By locator to find element
     * @return Fresh WebElement that is visible
     */
    public static WebElement waitForVisibleElement(WebDriver driver, org.openqa.selenium.By locator) {
        try {
            int timeout = config.getExplicitWait() * CI_TIMEOUT_MULTIPLIER;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            wait.pollingEvery(Duration.ofMillis(300));
            wait.ignoring(org.openqa.selenium.StaleElementReferenceException.class);
            wait.ignoring(org.openqa.selenium.NoSuchElementException.class);
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            logger.error("Element not visible within {} seconds: {}", 
                config.getExplicitWait() * CI_TIMEOUT_MULTIPLIER, locator, e);
            throw e;
        }
    }
}

