package com.nopcommerce.pages;

import java.time.Duration;

import org.openqa.selenium.By;
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
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	/**
	 * Navigate dynamically to a main menu and (optionally) a sub menu
	 * 
	 * @param mainMenu e.g. "Computers", "Electronics", "Books"
	 * @param subMenu  e.g. "Notebooks", "Desktops", or null if not applicable
	 */
	public void navigateTo(String mainMenu, String subMenu) {
		try {
			// 1️⃣ Locate main menu link dynamically
			WebElement mainMenuElement = wait.until(ExpectedConditions
					.visibilityOf(menuDiv.findElement(By.xpath(".//a[normalize-space()='" + mainMenu + "']"))));

			// 2️⃣ Hover over the main menu (to trigger submenu dropdown)
			WebElementActions.moveToElement(driver, mainMenuElement);
			logger.info("Hovered over main menu: " + mainMenu);

			if (subMenu != null && !subMenu.isEmpty()) {
				// 3️⃣ Wait for submenu list and locate the sub-item
				WebElement subMenuElement = wait.until(ExpectedConditions.elementToBeClickable(menuDiv.findElement(
						By.xpath(".//div[@aria-label='" + mainMenu + "']//a[normalize-space()='" + subMenu + "']"))));

				// 4️⃣ Click on the submenu item
				WebElementActions.click(driver, subMenuElement, "Submenu: " + subMenu);
			} else {
				// No submenu: click the main menu directly
				WebElementActions.click(driver, mainMenuElement, "Main menu: " + mainMenu);
			}

		} catch (NoSuchElementException e) {
			logger.error("Menu item not found: " + mainMenu + " → " + subMenu, e);
			throw new RuntimeException("Menu not found: " + mainMenu + " / " + subMenu);
		} catch (TimeoutException e) {
			logger.error("Timed out waiting for menu/submenu: " + mainMenu + " → " + subMenu, e);
			throw new RuntimeException("Timeout on menu navigation: " + mainMenu + " / " + subMenu);
		}
	}
}
