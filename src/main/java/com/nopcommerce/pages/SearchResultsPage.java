package com.nopcommerce.pages;

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
        try {
            Thread.sleep(2000); // Wait for search results to load
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        int count = searchResults.size();
        logger.info("Search results count: " + count);
        return count;
    }
    
    public boolean areSearchResultsDisplayed() {
        try {
            Thread.sleep(2000); // Wait for search results to load
            boolean hasResults = searchResults.size() > 0;
            logger.info("Search results displayed: " + hasResults);
            return hasResults;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
    
    public boolean isNoResultMessageDisplayed() {
        return WebElementActions.isDisplayed(driver, noResultMessage, "No result message");
    }
    
    public boolean isSearchPageHeaderDisplayed() {
        return WebElementActions.isDisplayed(driver, searchPageHeader, "Search page header");
    }
}
