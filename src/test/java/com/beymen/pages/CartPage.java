package com.beymen.pages;

import com.beymen.utils.ElementLocator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CartPage extends BasePage {

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public String getCartPrice() {
        logger.info("Getting cart price.");
        String rawPrice = driver.findElement(ElementLocator.get("cartPrice")).getText();
        return rawPrice.replace(",00", "").trim();
    }

    public void increaseQuantity(String value) {
        logger.info("Increasing product quantity to: " + value);
        driver.findElement(ElementLocator.get("cartPageIncreaseQty")).click();

        // Check if the option exists
        try {
            WebElement option = driver.findElement(By.xpath("//option[@value='" + value + "']"));
            option.click();
        } catch (Exception e) {
            logger.warn("Option with value '" + value + "' not found. Skipping the click action.");
        }
    }

    public String getQuantity() {
        logger.info("Retrieving product quantity from cart.");
        return driver.findElement(ElementLocator.get("cartPageQuantityValue")).getAttribute("value");
    }

    public void removeProduct() {
        logger.info("Removing product from cart.");
        driver.findElement(ElementLocator.get("removeButton")).click();
    }

    public boolean isCartEmpty() {
        logger.info("Checking if cart is empty.");
        return driver.getPageSource().contains("Sepetinizde ürün bulunmamaktadır");
    }

    public boolean elementAttributeContains(String key, String attribute, String expectedValue) {
        logger.info("Checking if attribute '" + attribute + "' of element '" + key + "' contains: " + expectedValue);
        try {
            WebElement element = driver.findElement(ElementLocator.get(key));
            String actualValue = element.getAttribute(attribute);
            if (actualValue == null) {
                logger.warn("Attribute '" + attribute + "' not found for element '" + key + "'. Skipping the action.");
                return false;
            }
            return actualValue.contains(expectedValue);
        } catch (Exception e) {
            logger.warn("Error occurred while checking the attribute: " + e.getMessage());
            return false;
        }
    }
}