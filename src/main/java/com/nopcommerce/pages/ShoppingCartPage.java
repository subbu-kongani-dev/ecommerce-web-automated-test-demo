package com.nopcommerce.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.nopcommerce.base.BasePage;

public class ShoppingCartPage extends BasePage {
    
    public ShoppingCartPage(WebDriver driver) {
        super(driver);
    }
    
    @FindBy(xpath = "//div[@class='page-title']//h1")
    private WebElement pageTitle;
    
    @FindBy(xpath = "//div[@class='no-data' or text()='Your Shopping Cart is empty!']")
    private WebElement emptyCartMessage;
    
    public boolean isShoppingCartPageDisplayed() {
        return isDisplayed(pageTitle);
    }
    
    public boolean isCartEmpty() {
        return isDisplayed(emptyCartMessage);
    }
    
    public String getCartPageTitle() {
        return getText(pageTitle);
    }
}
