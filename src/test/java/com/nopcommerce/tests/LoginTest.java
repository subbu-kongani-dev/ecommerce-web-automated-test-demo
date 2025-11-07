package com.nopcommerce.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.nopcommerce.base.BaseTest;
import com.nopcommerce.pages.HomePage;
import com.nopcommerce.pages.LoginPage;

public class LoginTest extends BaseTest {
    private HomePage homePage;
    private LoginPage loginPage;
    
    @Test(priority = 1, description = "Verify Login with Invalid Credentials")
    public void testLoginWithInvalidCredentials() {
        logger.info("Starting test: testLoginWithInvalidCredentials");
        homePage = new HomePage(driver);
        loginPage = homePage.clickLoginLink();
        loginPage.enterEmail("invalid@test.com");
        loginPage.enterPassword("WrongPassword123");
        loginPage.clickLoginButton();
        boolean isErrorDisplayed = loginPage.isErrorMessageDisplayed();
        logger.info("Error message displayed: " + isErrorDisplayed);
        Assert.assertTrue(isErrorDisplayed, "Error message should be displayed for invalid credentials");
        logger.info("Test passed: Error message displayed for invalid credentials");
    }
    
    @Test(priority = 2, description = "Verify Login Page Navigation")
    public void testLoginPageNavigation() {
        logger.info("Starting test: testLoginPageNavigation");
        homePage = new HomePage(driver);
        loginPage = homePage.clickLoginLink();
        String currentUrl = driver.getCurrentUrl();
        logger.info("Current URL: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("/login"), "Should navigate to login page");
        logger.info("Test passed: Successfully navigated to login page");
    }
    
    @Test(priority = 3, description = "Verify Login Page Elements")
    public void testLoginPageElements() {
        logger.info("Starting test: testLoginPageElements");
        homePage = new HomePage(driver);
        loginPage = homePage.clickLoginLink();
        String pageTitle = driver.getTitle();
        logger.info("Login page title: " + pageTitle);
        Assert.assertTrue(pageTitle.contains("nopCommerce"), "Page title should contain 'nopCommerce'");
        logger.info("Test passed: Login page elements verified");
    }
}
