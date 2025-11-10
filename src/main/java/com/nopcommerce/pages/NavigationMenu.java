package com.nopcommerce.pages;

import static com.nopcommerce.utils.WaitUtil.*;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.nopcommerce.base.BasePage;
import com.nopcommerce.utils.WebElementActions;

public class NavigationMenu extends BasePage {

	@FindBy(css = "div.menu[role='menu']")
	private WebElement menuDiv;

	private WebDriverWait wait;

	public NavigationMenu(WebDriver driver) {
		super(driver);
		// Increased timeout for CI/CD environments
		int timeoutSeconds = isCI() ? 30 : 15;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
		this.wait.pollingEvery(Duration.ofMillis(300));
	}
	
	/**
	 * Detect if running in CI/CD environment
	 */
	private boolean isCI() {
		return System.getenv("CI") != null || 
		       System.getenv("GITHUB_ACTIONS") != null;
	}

	/**
	 * Navigate dynamically to a main menu and (optionally) a sub menu
	 * Enhanced with retry logic and stability checks for CI/CD environments.
	 * 
	 * @param mainMenu e.g. "Computers", "Electronics", "Books"
	 * @param subMenu  e.g. "Notebooks", "Desktops", or null if not applicable
	 */
	public void navigateTo(String mainMenu, String subMenu) {
		int maxRetries = 3;
		int attempt = 0;
		Exception lastException = null;
		
		while (attempt < maxRetries) {
			try {
				attempt++;
				logger.info("Navigation attempt {}/{}: {} → {}", attempt, maxRetries, mainMenu, subMenu);
				
				// Wait for page and DOM stability
				waitForPageLoad(driver);
				waitForDomStability(driver);
				
				// Small pause for menu rendering in headless mode
				if (isCI()) {
					shortPause(1000);
				}
				
				// 1️⃣ Wait for menu container to be ready
				wait.until(ExpectedConditions.visibilityOf(menuDiv));
				
				// 2️⃣ Locate main menu link dynamically with retry
				WebElement mainMenuElement = findMenuElement(mainMenu);
				
				// 3️⃣ Scroll to ensure visibility
				scrollToElement(mainMenuElement);
				shortPause(300);

				if (subMenu != null && !subMenu.isEmpty()) {
					// 4️⃣ Hover over the main menu to trigger submenu dropdown
					WebElementActions.moveToElement(driver, mainMenuElement);
					logger.info("Hovered over main menu: " + mainMenu);
					
					// Wait for submenu to appear
					shortPause(800);
					
					// 5️⃣ Wait for submenu and locate the sub-item
					WebElement subMenuElement = findSubmenuElement(mainMenu, subMenu);
					
					// 6️⃣ Click on the submenu item
					WebElementActions.click(driver, subMenuElement, "Submenu: " + subMenu);
				} else {
					// No submenu: click the main menu directly
					WebElementActions.click(driver, mainMenuElement, "Main menu: " + mainMenu);
				}
				
				// Wait for navigation to complete
				waitForPageLoad(driver);
				logger.info("✅ Successfully navigated to {} → {}", mainMenu, subMenu);
				return; // Success!
				
			} catch (org.openqa.selenium.StaleElementReferenceException e) {
				lastException = e;
				logger.warn("StaleElementReferenceException on attempt {}, retrying...", attempt);
				shortPause(1000);
			} catch (NoSuchElementException e) {
				lastException = e;
				logger.error("Menu item not found on attempt {}: {} → {}", attempt, mainMenu, subMenu);
				if (attempt >= maxRetries) {
					throw new RuntimeException("Menu not found after " + maxRetries + " attempts: " + mainMenu + " / " + subMenu, e);
				}
				shortPause(1000);
			} catch (TimeoutException e) {
				lastException = e;
				logger.error("Timeout on attempt {} waiting for menu/submenu: {} → {}", attempt, mainMenu, subMenu);
				if (attempt >= maxRetries) {
					throw new RuntimeException("Timeout on menu navigation after " + maxRetries + " attempts: " + mainMenu + " / " + subMenu, e);
				}
				shortPause(1500);
			} catch (Exception e) {
				lastException = e;
				logger.error("Unexpected error on attempt {} for navigation: {} → {}", attempt, mainMenu, subMenu, e);
				if (attempt >= maxRetries) {
					throw new RuntimeException("Navigation failed after " + maxRetries + " attempts: " + mainMenu + " / " + subMenu, e);
				}
				shortPause(1000);
			}
		}
		
		// If we get here, all retries failed
		throw new RuntimeException("Timeout on menu navigation: " + mainMenu + " / " + subMenu, lastException);
	}
	
	/**
	 * Find main menu element with explicit wait and retry
	 */
	private WebElement findMenuElement(String mainMenu) {
		By locator = By.xpath(".//a[normalize-space()='" + mainMenu + "']");
		return wait.until(ExpectedConditions.elementToBeClickable(
			menuDiv.findElement(locator)));
	}
	
	/**
	 * Find submenu element with explicit wait and retry
	 */
	private WebElement findSubmenuElement(String mainMenu, String subMenu) {
		By locator = By.xpath(".//div[@aria-label='" + mainMenu + "']//a[normalize-space()='" + subMenu + "']");
		return wait.until(ExpectedConditions.elementToBeClickable(
			menuDiv.findElement(locator)));
	}
	
	/**
	 * Scroll element into view using JavaScript
	 */
	private void scrollToElement(WebElement element) {
		try {
			((JavascriptExecutor) driver).executeScript(
				"arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", element);
		} catch (Exception e) {
			logger.warn("Could not scroll to element", e);
		}
	}
}
