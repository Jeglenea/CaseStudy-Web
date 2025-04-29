package com.beymen.pages;

import com.beymen.utils.ElementLocator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Random;

public class SearchResultsPage extends BasePage {

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public void clickRandomProduct(String key) {
        logger.info("Clicking a random product using key: " + key);
        By productListLocator = ElementLocator.get(key);
        List<WebElement> products = driver.findElements(productListLocator);
        logger.info("Total product count: " + products.size());
        if (!products.isEmpty()) {
            WebElement randomProduct = products.get(new Random().nextInt(products.size()));

            // Log the product XPath
            String productXPath = randomProduct.getAttribute("xpath");
            logger.info("Selected product XPath: " + productXPath);

            // Scroll to the selected product before clicking, centering in viewport
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", randomProduct);
            new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(randomProduct));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", randomProduct);
        } else {
            throw new RuntimeException("Ürün listesi boş, seçim yapılamadı.");
        }
    }

    public void clickRandomProduct2(String key) {
        logger.info("Clicking a random product using locator key: " + key);
        By productListLocator = ElementLocator.get(key);
        List<WebElement> products = driver.findElements(productListLocator);
        logger.info("Found " + products.size() + " products.");
        if (!products.isEmpty()) {
            WebElement randomProduct = products.get(new Random().nextInt(products.size()));

            // Log the product XPath
            String productXPath = randomProduct.getAttribute("xpath");
            logger.info("Chosen product XPath: " + productXPath);

            // Scroll to the selected product before clicking
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", randomProduct);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", randomProduct);
        } else {
            throw new RuntimeException("Ürün listesi boş, seçim yapılamadı.");
        }
    }
}