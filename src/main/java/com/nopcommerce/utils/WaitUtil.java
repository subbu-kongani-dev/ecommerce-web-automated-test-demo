package com.nopcommerce.utils;

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

    public static WebElement waitForElementToBeVisible(WebDriver driver, WebElement element) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
            wait.pollingEvery(Duration.ofMillis(500));
            wait.ignoring(org.openqa.selenium.StaleElementReferenceException.class);
            return wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            logger.error("Element not visible within {} seconds timeout", config.getExplicitWait(), e);
            throw e;
        }
    }

    public static WebElement waitForElementToBeClickable(WebDriver driver, WebElement element) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
            wait.pollingEvery(Duration.ofMillis(500));
            wait.ignoring(org.openqa.selenium.StaleElementReferenceException.class);
            wait.ignoring(org.openqa.selenium.ElementClickInterceptedException.class);
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            logger.error("Element not clickable within {} seconds timeout", config.getExplicitWait(), e);
            throw e;
        }
    }
    
    /**
     * Wait for page to be fully loaded and ready.
     * Useful for ensuring page stability before interacting with elements.
     * 
     * @param driver WebDriver instance
     */
    public static void waitForPageLoad(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(webDriver -> 
                ((org.openqa.selenium.JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));
            logger.debug("Page loaded successfully");
        } catch (Exception e) {
            logger.warn("Page load wait timed out, continuing anyway", e);
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
}

