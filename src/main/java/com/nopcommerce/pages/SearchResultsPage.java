package com.nopcommerce.pages;

import static com.nopcommerce.utils.WaitUtil.*;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.nopcommerce.base.BasePage;
import com.nopcommerce.utils.WebElementActions;
import java.util.List;

public class SearchResultsPage extends BasePage {
    
    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }
    
    @FindBy(xpath = "//div[contains(@class,'item-box')]")
    private List<WebElement> searchResults;
    
    @FindBy(xpath = "//div[@class='no-result']")
    private WebElement noResultMessage;
    
    @FindBy(xpath = "//div[@class='page-title']//h1")
    private WebElement searchPageHeader;
    
    public int getSearchResultsCount() {
        // Wait for search results to load with proper explicit wait
        waitForPageLoad(driver);
        waitForDomStability(driver);
        
        // Additional wait in CI/CD environments
        boolean isCI = System.getenv("CI") != null || System.getenv("GITHUB_ACTIONS") != null;
        shortPause(isCI ? 2000 : 1000);
        
        int count = searchResults.size();
        logger.info("Search results count: " + count);
        return count;
    }
    
    public boolean areSearchResultsDisplayed() {
        // Wait for search results to load
        waitForPageLoad(driver);
        waitForDomStability(driver);
        
        // Additional wait in CI/CD environments
        boolean isCI = System.getenv("CI") != null || System.getenv("GITHUB_ACTIONS") != null;
        shortPause(isCI ? 2000 : 1000);
        
        boolean hasResults = searchResults.size() > 0;
        logger.info("Search results displayed: " + hasResults);
        return hasResults;
    }
    
    public boolean isNoResultMessageDisplayed() {
        try {
            waitForElementToBeVisible(driver, noResultMessage);
            return WebElementActions.isDisplayed(driver, noResultMessage, "No result message");
        } catch (Exception e) {
            logger.debug("No result message not displayed");
            return false;
        }
    }
    
    public boolean isSearchPageHeaderDisplayed() {
        try {
            waitForElementToBeVisible(driver, searchPageHeader);
            return WebElementActions.isDisplayed(driver, searchPageHeader, "Search page header");
        } catch (Exception e) {
            logger.warn("Search page header not displayed", e);
            return false;
        }
    }
}
