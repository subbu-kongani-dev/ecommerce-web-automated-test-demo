package com.nopcommerce.tests;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.nopcommerce.base.BaseTest;
import com.nopcommerce.pages.HomePage;
import com.nopcommerce.pages.RegisterPage;

public class RegistrationTest extends BaseTest {
	private HomePage homePage;
	private RegisterPage registerPage;

	@Test(priority = 1, description = "Verify Successful User Registration")
	public void testSuccessfulRegistration() {
		logger.info("Starting test: testSuccessfulRegistration");
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String email = "testuser" + timestamp + "@nopcommerce.com";

		homePage = new HomePage(driver);
		registerPage = homePage.clickRegisterLink();
		registerPage.registerUser("Test", "User", email, "Test@123456");

		boolean isSuccessful = registerPage.isRegistrationSuccessful();
		logger.info("Registration successful: " + isSuccessful);
		Assert.assertTrue(isSuccessful, "Registration should be successful");

		String resultMessage = registerPage.getRegistrationResult();
		logger.info("Registration result: " + resultMessage);
		Assert.assertTrue(resultMessage.contains("Your registration completed"), "Success message should be displayed");
		logger.info("Test passed: User registered successfully with email: " + email);
	}

	@Test(priority = 2, description = "Verify Registration with All Fields")
	public void testRegistrationWithAllFields() {
		logger.info("Starting test: testRegistrationWithAllFields");
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String email = "fulluser" + timestamp + "@nopcommerce.com";

		homePage = new HomePage(driver);
		registerPage = homePage.clickRegisterLink();
		registerPage.selectGenderMale();
		registerPage.enterFirstName("John");
		registerPage.enterLastName("Doe");
//        registerPage.selectDateOfBirth("15", "5", "1990");
		registerPage.enterEmail(email);
		registerPage.enterCompany("Test Company");
		registerPage.enterPassword("Test@123456");
		registerPage.enterConfirmPassword("Test@123456");
		registerPage.clickRegisterButton();

		Assert.assertTrue(registerPage.isRegistrationSuccessful(), "Registration with all fields should be successful");
		logger.info("Test passed: User registered with all fields");
	}

	@Test(priority = 3, description = "Verify Registration Page Navigation")
	public void testRegistrationPageNavigation() {
		logger.info("Starting test: testRegistrationPageNavigation");
		homePage = new HomePage(driver);
		registerPage = homePage.clickRegisterLink();
		String currentUrl = driver.getCurrentUrl();
		Assert.assertTrue(currentUrl.contains("/register"), "Should navigate to registration page");
		logger.info("Test passed: Successfully navigated to registration page");
	}
}
