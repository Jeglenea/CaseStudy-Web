package com.beymen.pages;

import com.beymen.utils.ElementActions;
import com.beymen.utils.ElementLocator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CartPage extends BasePage {

    private ElementActions elementActions;

    public CartPage(WebDriver driver) {
        super(driver);
        elementActions = new ElementActions(driver);
    }

    public String getCartPrice(String key) {
        logger.info("Getting cart price.");
        String rawPrice = driver.findElement(ElementLocator.get(key)).getText();
        logger.debug("Raw cart price text: " + rawPrice);
        return rawPrice.replaceAll("[^0-9,\\.]", "").replace(",00", "").trim();
    }

    public void increaseQuantity(String value) {
        logger.info("Increasing product quantity to: " + value);
        logger.debug("Attempting to find option with value: " + value);
        driver.findElement(ElementLocator.get("cartPageIncreaseQty")).click();

        // Check if the option exists
        try {
            WebElement option = driver.findElement(By.xpath("//option[@value='" + value + "']"));
            option.click();
            elementActions.elementTextContains("cartPageNotify", "Sepetiniz Güncellenmiştir");
        } catch (Exception e) {
            logger.warn("Option with value '" + value + "' not found. Skipping the click action.");
        }
    }

    public void removeProduct(String key) {
        logger.info("Removing product from cart with key: " + key);
        logger.debug("Clicking on the product element with key: " + key);
        driver.findElement(ElementLocator.get(key)).click();
    }
}