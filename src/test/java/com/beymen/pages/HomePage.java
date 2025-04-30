package com.beymen.pages;

import com.beymen.utils.ElementLocator;
import com.beymen.utils.ElementActions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.apache.log4j.Logger;

public class HomePage extends BasePage {

    private ElementActions elementActions;

    public HomePage(WebDriver driver) {
        super(driver);
        elementActions = new ElementActions(driver);
    }

    public boolean isHomePageDisplayed(String value) {
        logger.info("Checking if home page title contains: " + value);
        return driver.getTitle().contains(value);
    }

    public void searchProduct(String productName,String key) {
        logger.info("Searching for product: " + productName + " using key: " + key);
        WebElement searchInput = driver.findElement(ElementLocator.get(key));
        searchInput.sendKeys(productName);
    }
}