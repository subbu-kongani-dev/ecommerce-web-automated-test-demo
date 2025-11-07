package com.nopcommerce.pages;

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

	public RegisterPage clickRegisterLink() {
		WebElementActions.click(driver, registerLink, "Register link");
		return new RegisterPage(driver);
	}

	public LoginPage clickLoginLink() {
		WebElementActions.click(driver, loginLink, "Login link");
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
		WebElementActions.type(driver, searchBox, searchTerm, "Search box");
		WebElementActions.click(driver, searchButton, "Search button");
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
