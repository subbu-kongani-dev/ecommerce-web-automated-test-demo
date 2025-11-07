package com.nopcommerce.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.nopcommerce.base.BasePage;

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
        click(registerLink);
        logger.info("Clicked on Register link");
        return new RegisterPage(driver);
    }
    
    public LoginPage clickLoginLink() {
        click(loginLink);
        logger.info("Clicked on Login link");
        return new LoginPage(driver);
    }
    
    public void clickLogoutLink() {
        click(logoutLink);
        logger.info("Clicked on Logout link");
    }
    
    public boolean isUserLoggedIn() {
        try {
            return isDisplayed(logoutLink);
        } catch (Exception e) {
            return false;
        }
    }
    
    public SearchResultsPage searchProduct(String searchTerm) {
        type(searchBox, searchTerm);
        click(searchButton);
        logger.info("Searched for product: " + searchTerm);
        return new SearchResultsPage(driver);
    }
    
    public ShoppingCartPage clickShoppingCart() {
        click(shoppingCartLink);
        logger.info("Clicked on Shopping Cart link");
        return new ShoppingCartPage(driver);
    }
    
    public AccountPage clickMyAccount() {
        click(myAccountLink);
        logger.info("Clicked on My Account link");
        return new AccountPage(driver);
    }
    
    public boolean isLogoDisplayed() {
        return isDisplayed(logo);
    }
    
    public String getHomePageTitle() {
        return getPageTitle();
    }
}
