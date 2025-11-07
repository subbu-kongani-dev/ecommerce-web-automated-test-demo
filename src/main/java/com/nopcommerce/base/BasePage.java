package com.nopcommerce.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * BasePage serves as the foundation class for all Page Object classes. Follows
 * the Page Object Model (POM) pattern with Separation of Concerns.
 * 
 * Key Features: - Provides WebDriver instance to all page objects - Implements
 * Page Factory pattern for element initialization - Delegates element
 * interactions to WebElementActions utility - Provides page-level utility
 * methods (getTitle, getURL)
 * 
 * Design Pattern: - Page Object Model (POM): Encapsulates page elements and
 * page-specific logic - Delegation Pattern: Delegates element interactions to
 * WebElementActions - Single Responsibility: Only manages page initialization
 * and page-level operations
 * 
 * Refactoring Benefits: - Reduced code duplication (element interactions
 * centralized in WebElementActions) - Better separation of concerns (page
 * infrastructure vs. element interactions) - Improved testability
 * (WebElementActions can be unit tested independently) - Enhanced reusability
 * (WebElementActions can be used in non-page classes) - Easier maintenance
 * (changes to interaction logic in one place)
 * 
 * All page classes (HomePage, LoginPage, etc.) extend this class to inherit
 * common page infrastructure.
 * 
 * @author NopCommerce Automation Team
 * @version 2.0 - Refactored to use WebElementActions utility
 */
public class BasePage {
	protected WebDriver driver;
	protected static final Logger logger = LogManager.getLogger(BasePage.class);

	/**
	 * Constructor to initialize BasePage with WebDriver. Automatically initializes
	 * all @FindBy annotated elements using PageFactory.
	 * 
	 * @param driver WebDriver instance to interact with the browser
	 */
	public BasePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	// ==================== Page-Level Utility Methods ====================
	// These methods are specific to page-level operations

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
