package com.nopcommerce.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.nopcommerce.base.BasePage;
import com.nopcommerce.utils.WebElementActions;

public class LoginPage extends BasePage {
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    @FindBy(id = "Email")
    private WebElement emailField;
    
    @FindBy(id = "Password")
    private WebElement passwordField;
    
    @FindBy(xpath = "//button[text()='Log in']")
    private WebElement loginButton;
    
    @FindBy(xpath = "//div[contains(@class,'message-error')]")
    private WebElement errorMessage;
    
    public void enterEmail(String email) {
        WebElementActions.type(driver, emailField, email, "Email field");
    }
    
    public void enterPassword(String password) {
        WebElementActions.type(driver, passwordField, password, "Password field");
    }
    
    public HomePage clickLoginButton() {
        WebElementActions.click(driver, loginButton, "Login button");
        return new HomePage(driver);
    }
    
    public HomePage login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        return clickLoginButton();
    }
    
    public boolean isErrorMessageDisplayed() {
        return WebElementActions.isDisplayed(driver, errorMessage, "Error message");
    }
    
    public String getErrorMessage() {
        return WebElementActions.getText(driver, errorMessage, "Error message");
    }
}
