package com.nopcommerce.pages;

import static com.nopcommerce.utils.WaitUtil.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.nopcommerce.base.BasePage;
import com.nopcommerce.utils.WebElementActions;

public class HomePage extends BasePage {

	public HomePage(WebDriver driver) {
		super(driver);
	}

	@FindBy(linkText = "Register")
	private WebElement registerLink;

	@FindBy(linkText = "Log in")
	private WebElement loginLink;

	@FindBy(linkText = "Log out")
	private WebElement logoutLink;

	@FindBy(className = "ico-account")
	private WebElement myAccountLink;

	@FindBy(id = "small-searchterms")
	private WebElement searchBox;

	@FindBy(xpath = "//button[@type='submit' and text()='Search']")
	private WebElement searchButton;

	@FindBy(className = "ico-cart")
	private WebElement shoppingCartLink;

	@FindBy(xpath = "//div[@class='header-logo']//a")
	private WebElement logo;
	
	// By locators for stale-element-safe operations
	private static final By REGISTER_LINK = By.linkText("Register");
	private static final By LOGIN_LINK = By.linkText("Log in");
	private static final By SEARCH_BOX = By.id("small-searchterms");
	private static final By SEARCH_BUTTON = By.xpath("//button[@type='submit' and text()='Search']");

	public RegisterPage clickRegisterLink() {
		// RECOMMENDED: Use By locator to avoid StaleElementReferenceException
		WebElement element = waitForClickableElement(driver, REGISTER_LINK);
		shortPause(300); // Stability pause
		element.click();
		logger.info("Clicked on Register link");
		waitForPageLoad(driver);
		return new RegisterPage(driver);
	}

	public LoginPage clickLoginLink() {
		// RECOMMENDED: Use By locator to avoid StaleElementReferenceException
		WebElement element = waitForClickableElement(driver, LOGIN_LINK);
		shortPause(300); // Stability pause
		element.click();
		logger.info("Clicked on Login link");
		waitForPageLoad(driver);
		return new LoginPage(driver);
	}

	public void clickLogoutLink() {
		WebElementActions.click(driver, logoutLink, "Logout link");
	}

	public boolean isUserLoggedIn() {
		try {
			return WebElementActions.isDisplayed(driver, logoutLink, "Logout link");
		} catch (Exception e) {
			return false;
		}
	}

	public SearchResultsPage searchProduct(String searchTerm) {
		// Use By locators for stale-element-safe operations
		WebElement searchBoxElement = waitForVisibleElement(driver, SEARCH_BOX);
		searchBoxElement.clear();
		searchBoxElement.sendKeys(searchTerm);
		logger.info("Entered search term: " + searchTerm);
		
		WebElement searchButtonElement = waitForClickableElement(driver, SEARCH_BUTTON);
		shortPause(300); // Stability pause
		searchButtonElement.click();
		logger.info("Clicked on Search button");
		
		waitForPageLoad(driver);
		return new SearchResultsPage(driver);
	}

	public ShoppingCartPage clickShoppingCart() {
		WebElementActions.click(driver, shoppingCartLink, "Shopping Cart link");
		return new ShoppingCartPage(driver);
	}

	public AccountPage clickMyAccount() {
		WebElementActions.click(driver, myAccountLink, "My Account link");
		return new AccountPage(driver);
	}

	public boolean isLogoDisplayed() {
		return WebElementActions.isDisplayed(driver, logo, "Logo");
	}

	public String getHomePageTitle() {
		return getPageTitle();
	}
}
