package com.nopcommerce.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.nopcommerce.base.BaseTest;
import com.nopcommerce.pages.HomePage;
import com.nopcommerce.pages.SearchResultsPage;

public class SearchTest extends BaseTest {
    private HomePage homePage;
    private SearchResultsPage searchPage;
    
    @Test(priority = 1, description = "Verify Search with Valid Product Name")
    public void testSearchWithValidProduct() {
        logger.info("Starting test: testSearchWithValidProduct");
        homePage = new HomePage(driver);
        searchPage = homePage.searchProduct("computer");
        
        Assert.assertTrue(searchPage.isSearchPageHeaderDisplayed(), "Search page should be displayed");
        int resultCount = searchPage.getSearchResultsCount();
        logger.info("Number of search results: " + resultCount);
        Assert.assertTrue(resultCount > 0, "Search should return results for valid product");
        logger.info("Test passed: Search with valid product successful");
    }
    
    @Test(priority = 2, description = "Verify Search with Invalid Product Name")
    public void testSearchWithInvalidProduct() {
        logger.info("Starting test: testSearchWithInvalidProduct");
        homePage = new HomePage(driver);
        searchPage = homePage.searchProduct("xyznonexistentproduct123");
        
        Assert.assertTrue(searchPage.isSearchPageHeaderDisplayed(), "Search page should be displayed");
        boolean hasResults = searchPage.areSearchResultsDisplayed();
        logger.info("Search results found: " + hasResults);
        logger.info("Test passed: Search with invalid product handled correctly");
    }
    
    @Test(priority = 3, description = "Verify Search with Partial Product Name")
    public void testSearchWithPartialName() {
        logger.info("Starting test: testSearchWithPartialName");
        homePage = new HomePage(driver);
        searchPage = homePage.searchProduct("note");
        
        Assert.assertTrue(searchPage.isSearchPageHeaderDisplayed(), "Search page should be displayed");
        boolean hasResults = searchPage.areSearchResultsDisplayed();
        logger.info("Search results found for partial search: " + hasResults);
        logger.info("Test passed: Partial search functionality verified");
    }
}
