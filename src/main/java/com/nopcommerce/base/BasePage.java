package com.nopcommerce.base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import com.nopcommerce.utils.WaitUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * BasePage serves as the parent class for all Page Object classes.
 * Implements common web element interactions and utilities using Page Object Model pattern.
 * 
 * Key Features:
 * - Provides reusable methods for common web element interactions
 * - Implements Page Factory pattern for element initialization
 * - Includes explicit waits for element stability
 * - Provides logging for all interactions
 * - Handles exceptions with proper error logging
 * - Supports JavaScript execution for advanced scenarios
 * 
 * Design Pattern:
 * - Page Object Model (POM): Encapsulates page elements and interactions
 * - DRY Principle: Reusable methods prevent code duplication
 * - Single Responsibility: Each method performs one specific action
 * 
 * All page classes (HomePage, LoginPage, etc.) extend this class
 * to inherit common functionality.
 * 
 * @author NopCommerce Automation Team
 * @version 1.0
 */
public class BasePage {
    protected WebDriver driver;
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    
    /**
     * Constructor to initialize BasePage with WebDriver.
     * Automatically initializes all @FindBy annotated elements using PageFactory.
     * 
     * @param driver WebDriver instance to interact with the browser
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Clicks on a web element with explicit wait for clickability.
     * 
     * @param element WebElement to click
     * @throws Exception if element is not clickable or click fails
     */
    protected void click(WebElement element) {
        try {
            WaitUtil.waitForElementToBeClickable(driver, element);
            element.click();
            logger.info("Clicked on element");
        } catch (Exception e) {
            logger.error("Failed to click on element", e);
            throw e;
        }
    }
    
    /**
     * Types text into a web element after clearing existing content.
     * Waits for element visibility before typing.
     * 
     * @param element WebElement to type into (input field, textarea)
     * @param text Text to enter into the element
     * @throws Exception if element is not visible or typing fails
     */
    protected void type(WebElement element, String text) {
        try {
            WaitUtil.waitForElementToBeVisible(driver, element);
            element.clear();
            element.sendKeys(text);
            logger.info("Entered text: " + text);
        } catch (Exception e) {
            logger.error("Failed to enter text", e);
            throw e;
        }
    }
    
    /**
     * Retrieves text from a web element.
     * Waits for element visibility before getting text.
     * 
     * @param element WebElement to get text from
     * @return Text content of the element
     * @throws Exception if element is not visible or getText fails
     */
    protected String getText(WebElement element) {
        try {
            WaitUtil.waitForElementToBeVisible(driver, element);
            String text = element.getText();
            logger.info("Retrieved text: " + text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text", e);
            throw e;
        }
    }
    
    /**
     * Checks if a web element is displayed on the page.
     * Returns false instead of throwing exception if element is not found.
     * 
     * @param element WebElement to check visibility
     * @return true if element is displayed, false otherwise
     */
    protected boolean isDisplayed(WebElement element) {
        try {
            WaitUtil.waitForElementToBeVisible(driver, element);
            return element.isDisplayed();
        } catch (Exception e) {
            logger.warn("Element not displayed");
            return false;
        }
    }
    
    /**
     * Selects an option from a dropdown by visible text.
     * 
     * @param element Dropdown WebElement (select element)
     * @param visibleText Text of the option to select (as displayed in UI)
     * @throws Exception if dropdown is not found or option doesn't exist
     */
    protected void selectByVisibleText(WebElement element, String visibleText) {
        try {
            WaitUtil.waitForElementToBeVisible(driver, element);
            Select select = new Select(element);
            select.selectByVisibleText(visibleText);
            logger.info("Selected dropdown option: " + visibleText);
        } catch (Exception e) {
            logger.error("Failed to select dropdown option", e);
            throw e;
        }
    }
    
    /**
     * Selects an option from a dropdown by value attribute.
     * 
     * @param element Dropdown WebElement (select element)
     * @param value Value attribute of the option to select (HTML value)
     * @throws Exception if dropdown is not found or value doesn't exist
     */
    protected void selectByValue(WebElement element, String value) {
        try {
            WaitUtil.waitForElementToBeVisible(driver, element);
            Select select = new Select(element);
            select.selectByValue(value);
            logger.info("Selected dropdown value: " + value);
        } catch (Exception e) {
            logger.error("Failed to select dropdown value", e);
            throw e;
        }
    }
    
    /**
     * Scrolls the page to bring the element into view.
     * Useful when elements are not visible in the viewport.
     * 
     * @param element WebElement to scroll to
     * @throws Exception if scrolling fails
     */
    protected void scrollToElement(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            logger.info("Scrolled to element");
        } catch (Exception e) {
            logger.error("Failed to scroll to element", e);
            throw e;
        }
    }
    
    /**
     * Executes custom JavaScript code in the browser.
     * Used for advanced scenarios that Selenium API doesn't support directly.
     * 
     * @param script JavaScript code to execute
     * @return Result returned by the JavaScript execution (can be cast to appropriate type)
     * @throws Exception if JavaScript execution fails
     * 
     * Common use cases:
     * - Clicking hidden elements
     * - Setting dropdown values
     * - Scrolling to specific positions
     * - Modifying element attributes
     */
    protected Object executeJavaScript(String script) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            return js.executeScript(script);
        } catch (Exception e) {
            logger.error("Failed to execute JavaScript: " + script, e);
            throw e;
        }
    }
    
    /**
     * Gets the title of the current page.
     * 
     * @return Page title as displayed in browser tab
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Gets the current URL of the page.
     * 
     * @return Current page URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
