package com.nopcommerce.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.nopcommerce.base.BasePage;

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
        type(emailField, email);
    }
    
    public void enterPassword(String password) {
        type(passwordField, password);
    }
    
    public HomePage clickLoginButton() {
        click(loginButton);
        logger.info("Clicked on Login button");
        return new HomePage(driver);
    }
    
    public HomePage login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        return clickLoginButton();
    }
    
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }
    
    public String getErrorMessage() {
        return getText(errorMessage);
    }
}
