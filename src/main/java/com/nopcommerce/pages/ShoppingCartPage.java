package com.nopcommerce.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.nopcommerce.base.BasePage;
import com.nopcommerce.utils.WebElementActions;

public class ShoppingCartPage extends BasePage {
    
    public ShoppingCartPage(WebDriver driver) {
        super(driver);
    }
    
    @FindBy(xpath = "//div[@class='page-title']//h1")
    private WebElement pageTitle;
    
    @FindBy(xpath = "//div[@class='no-data' or text()='Your Shopping Cart is empty!']")
    private WebElement emptyCartMessage;
    
    public boolean isShoppingCartPageDisplayed() {
        return WebElementActions.isDisplayed(driver, pageTitle, "Shopping Cart page title");
    }
    
    public boolean isCartEmpty() {
        return WebElementActions.isDisplayed(driver, emptyCartMessage, "Empty cart message");
    }
    
    public String getCartPageTitle() {
        return WebElementActions.getText(driver, pageTitle, "Cart page title");
    }
}
