package com.nopcommerce.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.nopcommerce.base.BasePage;
import com.nopcommerce.utils.WebElementActions;

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
		WebElementActions.click(driver, genderMaleRadio, "Gender Male radio button");
	}

	public void enterFirstName(String firstName) {
		WebElementActions.type(driver, firstNameField, firstName, "First Name field");
	}

	public void enterLastName(String lastName) {
		WebElementActions.type(driver, lastNameField, lastName, "Last Name field");
	}

	public void selectDateOfBirth(String day, String month, String year) {
		try {
			// Scroll to the date of birth section to ensure visibility
			WebElementActions.scrollToElement(driver, dayDropdown, "Day dropdown");

			// Wait for elements to be in a stable state
			WebElementActions.waitForElementStability(driver, dayDropdown);

			// Select day, month, year with enhanced error handling
			WebElementActions.selectByValue(driver, dayDropdown, day);
			WebElementActions.selectByValue(driver, monthDropdown, month);
			WebElementActions.selectByValue(driver, yearDropdown, year);
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
			WebElementActions.executeJavaScript(driver, "document.getElementsByName('DateOfBirthDay')[0].value = '" + day + "';");
			WebElementActions.executeJavaScript(driver, "document.getElementsByName('DateOfBirthMonth')[0].value = '" + month + "';");
			WebElementActions.executeJavaScript(driver, "document.getElementsByName('DateOfBirthYear')[0].value = '" + year + "';");
			logger.info("Date of birth selected using JavaScript: " + day + "/" + month + "/" + year);
		} catch (Exception e) {
			logger.error("Failed to select date of birth even with JavaScript fallback", e);
			throw e;
		}
	}

	public void enterEmail(String email) {
		WebElementActions.type(driver, emailField, email, "Email field");
	}

	public void enterCompany(String company) {
		WebElementActions.type(driver, companyField, company, "Company field");
	}

	public void enterPassword(String password) {
		WebElementActions.type(driver, passwordField, password, "Password field");
	}

	public void enterConfirmPassword(String confirmPassword) {
		WebElementActions.type(driver, confirmPasswordField, confirmPassword, "Confirm Password field");
	}

	public void clickRegisterButton() {
		WebElementActions.click(driver, registerButton, "Register button");
	}

	public String getRegistrationResult() {
		return WebElementActions.getText(driver, registrationResult, "Registration result");
	}

	public boolean isRegistrationSuccessful() {
		logger.info("Verifying registration success");
		String result = getRegistrationResult();
		return result.contains("Your registration completed");
	}

	public HomePage clickContinueButton() {
		logger.info("Clicking on Continue button to navigate to Home Page");
		WebElementActions.click(driver, continueButton, "Continue button");
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
