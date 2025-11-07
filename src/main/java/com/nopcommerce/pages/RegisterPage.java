package com.nopcommerce.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.nopcommerce.base.BasePage;

public class RegisterPage extends BasePage {
    
    public RegisterPage(WebDriver driver) {
        super(driver);
    }
    
    @FindBy(id = "gender-male")
    private WebElement genderMaleRadio;
    
    @FindBy(id = "FirstName")
    private WebElement firstNameField;
    
    @FindBy(id = "LastName")
    private WebElement lastNameField;
    
    @FindBy(name = "DateOfBirthDay")
    private WebElement dayDropdown;
    
    @FindBy(name = "DateOfBirthMonth")
    private WebElement monthDropdown;
    
    @FindBy(name = "DateOfBirthYear")
    private WebElement yearDropdown;
    
    @FindBy(id = "Email")
    private WebElement emailField;
    
    @FindBy(id = "Company")
    private WebElement companyField;
    
    @FindBy(id = "Password")
    private WebElement passwordField;
    
    @FindBy(id = "ConfirmPassword")
    private WebElement confirmPasswordField;
    
    @FindBy(id = "register-button")
    private WebElement registerButton;
    
    @FindBy(className = "result")
    private WebElement registrationResult;
    
    @FindBy(xpath = "//a[text()='Continue']")
    private WebElement continueButton;
    
    public void selectGenderMale() {
        click(genderMaleRadio);
        logger.info("Selected gender: Male");
    }
    
    public void enterFirstName(String firstName) {
        type(firstNameField, firstName);
    }
    
    public void enterLastName(String lastName) {
        type(lastNameField, lastName);
    }
    
    public void selectDateOfBirth(String day, String month, String year) {
        try {
            // Scroll to the date of birth section to ensure visibility
            scrollToElement(dayDropdown);
            
            // Wait for elements to be in a stable state
            waitForElementStability(dayDropdown);
            
            // Select day, month, year with enhanced error handling
            selectByValue(dayDropdown, day);
            selectByValue(monthDropdown, month);
            selectByValue(yearDropdown, year);
            logger.info("Date of birth selected: " + day + "/" + month + "/" + year);
        } catch (Exception e) {
            logger.warn("Standard date selection failed, attempting JavaScript fallback: " + e.getMessage());
            // Firefox-specific fallback using JavaScript
            selectDateOfBirthWithJavaScript(day, month, year);
        }
    }
    
    private void selectDateOfBirthWithJavaScript(String day, String month, String year) {
        try {
            // Use JavaScript to directly set dropdown values
            executeJavaScript("document.getElementsByName('DateOfBirthDay')[0].value = '" + day + "';");
            executeJavaScript("document.getElementsByName('DateOfBirthMonth')[0].value = '" + month + "';");
            executeJavaScript("document.getElementsByName('DateOfBirthYear')[0].value = '" + year + "';");
            logger.info("Date of birth selected using JavaScript: " + day + "/" + month + "/" + year);
        } catch (Exception e) {
            logger.error("Failed to select date of birth even with JavaScript fallback", e);
            throw e;
        }
    }
    
    private void waitForElementStability(WebElement element) {
        try {
            // Wait a moment for any page animations or dynamic loading
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Wait interrupted: " + e.getMessage());
        }
    }
    
    public void enterEmail(String email) {
        type(emailField, email);
    }
    
    public void enterCompany(String company) {
        type(companyField, company);
    }
    
    public void enterPassword(String password) {
        type(passwordField, password);
    }
    
    public void enterConfirmPassword(String confirmPassword) {
        type(confirmPasswordField, confirmPassword);
    }
    
    public void clickRegisterButton() {
        click(registerButton);
        logger.info("Clicked on Register button");
    }
    
    public String getRegistrationResult() {
        return getText(registrationResult);
    }
    
    public boolean isRegistrationSuccessful() {
        String result = getRegistrationResult();
        return result.contains("Your registration completed");
    }
    
    public HomePage clickContinueButton() {
        click(continueButton);
        return new HomePage(driver);
    }
    
    public void registerUser(String firstName, String lastName, String email, String password) {
        selectGenderMale();
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterPassword(password);
        enterConfirmPassword(password);
        clickRegisterButton();
    }
}
