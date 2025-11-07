package com.nopcommerce.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.nopcommerce.base.BasePage;

public class AccountPage extends BasePage {
    
    public AccountPage(WebDriver driver) {
        super(driver);
    }
    
    @FindBy(xpath = "//div[@class='page-title']//h1")
    private WebElement pageTitle;
    
    @FindBy(linkText = "Customer info")
    private WebElement customerInfoLink;
    
    @FindBy(linkText = "Addresses")
    private WebElement addressesLink;
    
    @FindBy(linkText = "Orders")
    private WebElement ordersLink;
    
    public boolean isAccountPageDisplayed() {
        return isDisplayed(pageTitle);
    }
    
    public String getAccountPageTitle() {
        return getText(pageTitle);
    }
    
    public void clickCustomerInfo() {
        click(customerInfoLink);
        logger.info("Clicked on Customer Info link");
    }
    
    public void clickAddresses() {
        click(addressesLink);
        logger.info("Clicked on Addresses link");
    }
    
    public void clickOrders() {
        click(ordersLink);
        logger.info("Clicked on Orders link");
    }
}
