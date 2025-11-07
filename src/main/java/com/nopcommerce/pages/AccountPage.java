package com.nopcommerce.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.nopcommerce.base.BasePage;
import com.nopcommerce.utils.WebElementActions;

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
		return WebElementActions.isDisplayed(driver, pageTitle, "Account page title");
	}

	public String getAccountPageTitle() {
		return WebElementActions.getText(driver, pageTitle, "Account page title");
	}

	public void clickCustomerInfo() {
		WebElementActions.click(driver, customerInfoLink, "Customer Info link");
	}

	public void clickAddresses() {
		WebElementActions.click(driver, addressesLink, "Addresses link");
	}

	public void clickOrders() {
		WebElementActions.click(driver, ordersLink, "Orders link");
	}
}
