package com.nopcommerce.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.nopcommerce.base.BaseTest;
import com.nopcommerce.pages.HomePage;
import com.nopcommerce.pages.LoginPage;
import com.nopcommerce.pages.RegisterPage;
import com.nopcommerce.pages.SearchResultsPage;

public class HomePageTest extends BaseTest {
	private HomePage homePage;

	@Test(priority = 1, description = "Verify Home Page Title")
	public void testHomePageTitle() {
		logger.info("Starting test: testHomePageTitle");
		homePage = new HomePage(driver);
		String actualTitle = homePage.getHomePageTitle();
		logger.info("Actual page title: " + actualTitle);
		Assert.assertTrue(actualTitle.contains("nopCommerce"),
				"Home page title should contain 'nopCommerce'" + " but found: " + actualTitle);
		logger.info("Test passed: Home page title verified successfully");
	}

	@Test(priority = 2, description = "Verify Home Page Logo Display")
	public void testHomePageLogo() {
		logger.info("Starting test: testHomePageLogo");
		homePage = new HomePage(driver);
		boolean isLogoDisplayed = homePage.isLogoDisplayed();
		Assert.assertTrue(isLogoDisplayed, "Home page logo should be displayed");
		logger.info("Test passed: Home page logo is displayed");
	}

	@Test(priority = 3, description = "Verify Navigation to Register Page")
	public void testNavigationToRegisterPage() {
		logger.info("Starting test: testNavigationToRegisterPage");
		homePage = new HomePage(driver);
		RegisterPage registerPage = homePage.clickRegisterLink();
		String currentUrl = driver.getCurrentUrl();
		logger.info("Current URL: " + currentUrl);
		Assert.assertTrue(currentUrl.contains("/register"),
				"URL should contain '/register' after clicking Register link");
		logger.info("Test passed: Successfully navigated to Register page");
	}

	@Test(priority = 4, description = "Verify Navigation to Login Page")
	public void testNavigationToLoginPage() {
		logger.info("Starting test: testNavigationToLoginPage");
		homePage = new HomePage(driver);
		LoginPage loginPage = homePage.clickLoginLink();
		String currentUrl = driver.getCurrentUrl();
		logger.info("Current URL: " + currentUrl);
		Assert.assertTrue(currentUrl.contains("/login"), "URL should contain '/login' after clicking Login link");
		logger.info("Test passed: Successfully navigated to Login page");
	}

	@Test(priority = 5, description = "Verify Search Functionality with Valid Product")
	public void testSearchWithValidProduct() {
		logger.info("Starting test: testSearchWithValidProduct");
		homePage = new HomePage(driver);
		String searchTerm = "laptop";
		SearchResultsPage searchPage = homePage.searchProduct(searchTerm);
		Assert.assertTrue(searchPage.isSearchPageHeaderDisplayed(), "Search page header should be displayed");
		boolean hasResults = searchPage.areSearchResultsDisplayed();
		logger.info("Search results found: " + hasResults);
		Assert.assertTrue(hasResults, "Search should return results for product: " + searchTerm);
		logger.info("Test passed: Search functionality working correctly");
	}
}
