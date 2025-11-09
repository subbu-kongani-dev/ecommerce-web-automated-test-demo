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
            return wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            logger.error("Element not visible within timeout", e);
            throw e;
        }
    }

    public static WebElement waitForElementToBeClickable(WebDriver driver, WebElement element) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            logger.error("Element not clickable within timeout", e);
            throw e;
        }
    }
}
