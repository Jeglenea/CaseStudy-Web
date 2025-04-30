package com.beymen.pages;

import com.beymen.utils.ElementLocator;
import com.beymen.utils.ElementActions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

public class ProductPage extends BasePage {

    private ElementActions elementActions;

    public ProductPage(WebDriver driver) {
        super(driver);
        elementActions = new ElementActions(driver);
    }

    public String getProductTitle(String key) {
        logger.info("Retrieving product title using key: " + key);
        logger.info("Getting product title.");
        return driver.findElement(ElementLocator.get(key)).getText();
    }

    public String sizeCompareChoose() {
        logger.info("Selecting size compare option.");

        String primaryKey = "productPageSizeSelect";
        String fallbackKey = "productPageSizeSelectEmergency";

        logger.info("Checking for primary size option: " + primaryKey);
        if (!driver.findElements(ElementLocator.get(primaryKey)).isEmpty()) {
            logger.info("Primary size option clicked: " + primaryKey);
            WebElement element = driver.findElement(ElementLocator.get(primaryKey));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            return primaryKey;
        } else {
            logger.info("Checking for fallback size option: " + fallbackKey);
            if (!driver.findElements(ElementLocator.get(fallbackKey)).isEmpty()) {
                logger.info("Fallback size option clicked: " + fallbackKey);
                WebElement element = driver.findElement(ElementLocator.get(fallbackKey));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                return fallbackKey;
            } else {
                logger.warn("No size option found.");
                return null;
            }
        }
    }

    public String getProductPrice(String keyOld, String keyNew) {
        String priceText;
        if (!driver.findElements(ElementLocator.get(keyOld)).isEmpty()) {
            WebElement oldPriceElement = driver.findElement(ElementLocator.get(keyOld));
            priceText = oldPriceElement.getText().replaceAll("[^0-9,\\.]", "").toLowerCase();
            logger.info("Old price found: " + priceText);
        } else {
            WebElement newPriceElement = driver.findElement(ElementLocator.get(keyNew));
            priceText = newPriceElement.getText().replaceAll("[^0-9,\\.]", "").toLowerCase();
            logger.info("New price found: " + priceText);
        }
        return priceText;
    }
}