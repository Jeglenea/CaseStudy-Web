package com.beymen.pages;

import com.beymen.utils.ElementLocator;
import org.openqa.selenium.WebDriver;
import org.apache.log4j.Logger;

public class ProductPage extends BasePage {

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public String getProductTitle() {
        logger.info("Getting product title.");
        return driver.findElement(ElementLocator.get("productTitle")).getText();
    }

    public String getProductPrice() {
        if (!driver.findElements(ElementLocator.get("productDiscountDesc")).isEmpty()) {
            String discountText = driver.findElement(ElementLocator.get("productDiscountDesc")).getText().toLowerCase();
            if (discountText.contains("sepette")) {
                logger.info("Detected 'sepette' discount. Returning discounted price.");
                return driver.findElement(ElementLocator.get("productPriceDiscounted")).getText();
            } else if (discountText.contains("üzeri") || discountText.contains("the one ve garanti'ye")) {
                logger.info("Discount text matches 'üzeri' or 'The One ve Garanti'ye'. Returning regular price.");
                return driver.findElement(ElementLocator.get("productPrice")).getText();
            }
        }
        logger.info("No discount description found or matched. Returning regular price.");
        return driver.findElement(ElementLocator.get("productPrice")).getText();
    }

    public void addToCart() {
        logger.info("Adding product to cart.");
        driver.findElement(ElementLocator.get("addToCartButton")).click();
    }
}