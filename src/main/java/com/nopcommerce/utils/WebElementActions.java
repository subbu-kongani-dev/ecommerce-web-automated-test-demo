package com.nopcommerce.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

/**
 * WebElementActions provides reusable methods for interacting with web
 * elements. This class follows the Utility/Helper pattern and promotes code
 * reusability.
 * 
 * Key Features: - Centralized element interaction logic - Built-in explicit
 * waits for element stability - Comprehensive error handling and logging -
 * JavaScript execution support for advanced scenarios - Stateless design for
 * thread-safety
 * 
 * Design Benefits: - Single Responsibility: Only handles element interactions -
 * Reusability: Can be used across Page Objects, Tests, and Utilities -
 * Maintainability: One place to update interaction logic - Testability: Easy to
 * unit test independently
 * 
 * Usage: - Page Objects can use this class instead of inheriting from BasePage
 * - Test classes can use this directly for quick interactions - Other utility
 * classes can leverage these methods
 * 
 * @author NopCommerce Automation Team
 * @version 1.0
 */
public class WebElementActions {
	private static final Logger logger = LogManager.getLogger(WebElementActions.class);

	/**
	 * Clicks on a web element with explicit wait for clickability.
	 * Includes retry logic for StaleElementReferenceException.
	 * 
	 * @param driver      WebDriver instance
	 * @param element     WebElement to click
	 * @param elementName Optional name for logging (can be null)
	 * @throws Exception if element is not clickable or click fails after retries
	 */
	public static void click(WebDriver driver, WebElement element, String elementName) {
		int maxRetries = 3;
		int attempt = 0;
		
		while (attempt < maxRetries) {
			try {
				WaitUtil.waitForElementToBeClickable(driver, element);
				element.click();
				String logMessage = elementName != null ? "Clicked on element: " + elementName : "Clicked on element";
				logger.info(logMessage);
				return; // Success, exit method
			} catch (org.openqa.selenium.StaleElementReferenceException e) {
				attempt++;
				if (attempt >= maxRetries) {
					String errorMessage = elementName != null 
						? "Failed to click on element after " + maxRetries + " retries (StaleElementReferenceException): " + elementName
						: "Failed to click on element after " + maxRetries + " retries (StaleElementReferenceException)";
					logger.error(errorMessage, e);
					throw e;
				}
				logger.warn("StaleElementReferenceException on attempt {}. Retrying...", attempt);
				try {
					Thread.sleep(500); // Brief pause before retry
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
				}
			} catch (Exception e) {
				String errorMessage = elementName != null ? "Failed to click on element: " + elementName
						: "Failed to click on element";
				logger.error(errorMessage, e);
				throw e;
			}
		}
	}

	/**
	 * Clicks on a web element without element name for logging.
	 * 
	 * @param driver  WebDriver instance
	 * @param element WebElement to click
	 * @throws Exception if element is not clickable or click fails
	 */
	public static void click(WebDriver driver, WebElement element) {
		click(driver, element, null);
	}

	/**
	 * Types text into a web element after clearing existing content. Waits for
	 * element visibility before typing.
	 * 
	 * @param driver    WebDriver instance
	 * @param element   WebElement to type into (input field, textarea)
	 * @param text      Text to enter into the element
	 * @param fieldName Optional field name for logging (can be null)
	 * @throws Exception if element is not visible or typing fails
	 */
	public static void type(WebDriver driver, WebElement element, String text, String fieldName) {
		try {
			WaitUtil.waitForElementToBeVisible(driver, element);
			element.clear();
			element.sendKeys(text);
			String logMessage = fieldName != null ? "Entered text in " + fieldName + ": " + text
					: "Entered text: " + text;
			logger.info(logMessage);
		} catch (Exception e) {
			String errorMessage = fieldName != null ? "Failed to enter text in " + fieldName : "Failed to enter text";
			logger.error(errorMessage, e);
			throw e;
		}
	}

	/**
	 * Types text into a web element without field name for logging.
	 * 
	 * @param driver  WebDriver instance
	 * @param element WebElement to type into
	 * @param text    Text to enter
	 * @throws Exception if typing fails
	 */
	public static void type(WebDriver driver, WebElement element, String text) {
		type(driver, element, text, null);
	}

	/**
	 * Retrieves text from a web element. Waits for element visibility before
	 * getting text.
	 * 
	 * @param driver  WebDriver instance
	 * @param element WebElement to get text from
	 * @return Text content of the element
	 * @throws Exception if element is not visible or getText fails
	 */
	public static String getText(WebDriver driver, WebElement element) {
		try {
			WaitUtil.waitForElementToBeVisible(driver, element);
			String text = element.getText();
			logger.info("Retrieved text: " + text);
			return text;
		} catch (Exception e) {
			logger.error("Failed to get text from element", e);
			throw e;
		}
	}

	/**
	 * Retrieves text from a web element with element name for better logging.
	 * 
	 * @param driver      WebDriver instance
	 * @param element     WebElement to get text from
	 * @param elementName Name of the element for logging
	 * @return Text content of the element
	 * @throws Exception if element is not visible or getText fails
	 */
	public static String getText(WebDriver driver, WebElement element, String elementName) {
		try {
			WaitUtil.waitForElementToBeVisible(driver, element);
			String text = element.getText();
			logger.info("Retrieved text from " + elementName + ": " + text);
			return text;
		} catch (Exception e) {
			logger.error("Failed to get text from element: " + elementName, e);
			throw e;
		}
	}

	/**
	 * Checks if a web element is displayed on the page. Returns false instead of
	 * throwing exception if element is not found.
	 * 
	 * @param driver  WebDriver instance
	 * @param element WebElement to check visibility
	 * @return true if element is displayed, false otherwise
	 */
	public static boolean isDisplayed(WebDriver driver, WebElement element) {
		try {
			WaitUtil.waitForElementToBeVisible(driver, element);
			return element.isDisplayed();
		} catch (Exception e) {
			logger.warn("Element not displayed");
			return false;
		}
	}

	/**
	 * Checks if a web element is displayed with element name for logging.
	 * 
	 * @param driver      WebDriver instance
	 * @param element     WebElement to check visibility
	 * @param elementName Name of the element for logging
	 * @return true if element is displayed, false otherwise
	 */
	public static boolean isDisplayed(WebDriver driver, WebElement element, String elementName) {
		try {
			WaitUtil.waitForElementToBeVisible(driver, element);
			boolean displayed = element.isDisplayed();
			logger.info("Element " + elementName + " displayed: " + displayed);
			return displayed;
		} catch (Exception e) {
			logger.warn("Element " + elementName + " not displayed");
			return false;
		}
	}

	/**
	 * Checks if a web element is enabled.
	 * 
	 * @param driver  WebDriver instance
	 * @param element WebElement to check
	 * @return true if element is enabled, false otherwise
	 */
	public static boolean isEnabled(WebDriver driver, WebElement element) {
		try {
			WaitUtil.waitForElementToBeVisible(driver, element);
			boolean enabled = element.isEnabled();
			logger.info("Element enabled status: " + enabled);
			return enabled;
		} catch (Exception e) {
			logger.warn("Element not enabled or not found");
			return false;
		}
	}

	/**
	 * Selects an option from a dropdown by visible text.
	 * 
	 * @param driver      WebDriver instance
	 * @param element     Dropdown WebElement (select element)
	 * @param visibleText Text of the option to select (as displayed in UI)
	 * @throws Exception if dropdown is not found or option doesn't exist
	 */
	public static void selectByVisibleText(WebDriver driver, WebElement element, String visibleText) {
		try {
			WaitUtil.waitForElementToBeVisible(driver, element);
			Select select = new Select(element);
			select.selectByVisibleText(visibleText);
			logger.info("Selected dropdown option by visible text: " + visibleText);
		} catch (Exception e) {
			logger.error("Failed to select dropdown option: " + visibleText, e);
			throw e;
		}
	}

	/**
	 * Selects an option from a dropdown by value attribute.
	 * 
	 * @param driver  WebDriver instance
	 * @param element Dropdown WebElement (select element)
	 * @param value   Value attribute of the option to select (HTML value)
	 * @throws Exception if dropdown is not found or value doesn't exist
	 */
	public static void selectByValue(WebDriver driver, WebElement element, String value) {
		try {
			WaitUtil.waitForElementToBeVisible(driver, element);
			Select select = new Select(element);
			select.selectByValue(value);
			logger.info("Selected dropdown value: " + value);
		} catch (Exception e) {
			logger.error("Failed to select dropdown value: " + value, e);
			throw e;
		}
	}

	/**
	 * Selects an option from a dropdown by index.
	 * 
	 * @param driver  WebDriver instance
	 * @param element Dropdown WebElement (select element)
	 * @param index   Index of the option to select (0-based)
	 * @throws Exception if dropdown is not found or index is invalid
	 */
	public static void selectByIndex(WebDriver driver, WebElement element, int index) {
		try {
			WaitUtil.waitForElementToBeVisible(driver, element);
			Select select = new Select(element);
			select.selectByIndex(index);
			logger.info("Selected dropdown option by index: " + index);
		} catch (Exception e) {
			logger.error("Failed to select dropdown option by index: " + index, e);
			throw e;
		}
	}

	/**
	 * Gets the selected option text from a dropdown.
	 * 
	 * @param driver  WebDriver instance
	 * @param element Dropdown WebElement (select element)
	 * @return Text of the selected option
	 * @throws Exception if dropdown is not found
	 */
	public static String getSelectedOptionText(WebDriver driver, WebElement element) {
		try {
			WaitUtil.waitForElementToBeVisible(driver, element);
			Select select = new Select(element);
			String selectedText = select.getFirstSelectedOption().getText();
			logger.info("Selected option text: " + selectedText);
			return selectedText;
		} catch (Exception e) {
			logger.error("Failed to get selected option text", e);
			throw e;
		}
	}

	/**
	 * Scrolls the page to bring the element into view. Useful when elements are not
	 * visible in the viewport.
	 * 
	 * @param driver  WebDriver instance
	 * @param element WebElement to scroll to
	 * @throws Exception if scrolling fails
	 */
	public static void scrollToElement(WebDriver driver, WebElement element) {
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
	 * Scrolls to element with element name for logging.
	 * 
	 * @param driver      WebDriver instance
	 * @param element     WebElement to scroll to
	 * @param elementName Name of the element for logging
	 * @throws Exception if scrolling fails
	 */
	public static void scrollToElement(WebDriver driver, WebElement element, String elementName) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(true);", element);
			logger.info("Scrolled to element: " + elementName);
		} catch (Exception e) {
			logger.error("Failed to scroll to element: " + elementName, e);
			throw e;
		}
	}

	/**
	 * Clicks an element using JavaScript. Useful for hidden elements or when
	 * standard click fails.
	 * 
	 * @param driver  WebDriver instance
	 * @param element WebElement to click
	 * @throws Exception if JavaScript click fails
	 */
	public static void clickUsingJavaScript(WebDriver driver, WebElement element) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", element);
			logger.info("Clicked element using JavaScript");
		} catch (Exception e) {
			logger.error("Failed to click element using JavaScript", e);
			throw e;
		}
	}

	/**
	 * 
	 ******************* Actions *****************************
	 ********************************************************* 
	 * 
	 */

	public static void moveToElement(WebDriver driver, WebElement element) {
		try {
			WaitUtil.waitForElementToBeVisible(driver, element);
			Actions actions = new Actions(driver);
			actions.moveToElement(element).perform();
			logger.info("Moved to element successfully");
		} catch (Exception e) {
			logger.error("Failed to move to element", e);
			throw e;
		}
	}

	/**
	 * Highlights an element (useful for debugging and screenshots).
	 * 
	 * @param driver  WebDriver instance
	 * @param element WebElement to highlight
	 */
	public static void highlightElement(WebDriver driver, WebElement element) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].style.border='3px solid red'", element);
			logger.debug("Highlighted element");
		} catch (Exception e) {
			logger.warn("Failed to highlight element", e);
		}
	}

	/**
	 * Gets an attribute value from an element.
	 * 
	 * @param driver        WebDriver instance
	 * @param element       WebElement to get attribute from
	 * @param attributeName Name of the attribute
	 * @return Value of the attribute
	 * @throws Exception if element is not found
	 */
	public static String getAttribute(WebDriver driver, WebElement element, String attributeName) {
		try {
			WaitUtil.waitForElementToBeVisible(driver, element);
			String value = element.getAttribute(attributeName);
			logger.info("Retrieved attribute '" + attributeName + "': " + value);
			return value;
		} catch (Exception e) {
			logger.error("Failed to get attribute: " + attributeName, e);
			throw e;
		}
	}

	/**
	 * Executes custom JavaScript code in the browser. Used for advanced scenarios
	 * that Selenium API doesn't support directly.
	 * 
	 * @param driver WebDriver instance
	 * @param script JavaScript code to execute
	 * @return Result returned by the JavaScript execution (can be cast to
	 *         appropriate type)
	 * @throws Exception if JavaScript execution fails
	 * 
	 *                   Common use cases: - Clicking hidden elements - Setting
	 *                   dropdown values - Scrolling to specific positions -
	 *                   Modifying element attributes
	 */
	public static Object executeJavaScript(WebDriver driver, String script) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			Object result = js.executeScript(script);
			logger.info("Executed JavaScript: " + script);
			return result;
		} catch (Exception e) {
			logger.error("Failed to execute JavaScript: " + script, e);
			throw e;
		}
	}

	/**
	 * Executes JavaScript with element as argument.
	 * 
	 * @param driver  WebDriver instance
	 * @param script  JavaScript code to execute
	 * @param element WebElement to pass as argument to the script
	 * @return Result returned by the JavaScript execution
	 * @throws Exception if JavaScript execution fails
	 */
	public static Object executeJavaScript(WebDriver driver, String script, WebElement element) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			Object result = js.executeScript(script, element);
			logger.info("Executed JavaScript with element: " + script);
			return result;
		} catch (Exception e) {
			logger.error("Failed to execute JavaScript: " + script, e);
			throw e;
		}
	}

	/**
	 * Waits for an element to become stable (no stale element issues). Useful
	 * before interacting with dynamically loaded elements.
	 * 
	 * @param driver  WebDriver instance
	 * @param element WebElement to wait for
	 */
	public static void waitForElementStability(WebDriver driver, WebElement element) {
		try {
			WaitUtil.waitForElementToBeVisible(driver, element);
			WaitUtil.waitForElementToBeClickable(driver, element);
			logger.info("Element is stable and ready for interaction");
		} catch (Exception e) {
			logger.error("Element did not become stable", e);
			throw e;
		}
	}
}
