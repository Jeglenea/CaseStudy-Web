package com.beymen.pages;

import com.beymen.utils.ElementLocator;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.apache.log4j.Logger;

public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void navigateToHomePage() {
        logger.info("Navigating to homepage...");
        driver.get("https://www.beymen.com/");
    }

    public boolean isHomePageDisplayed() {
        return driver.getTitle().contains("Beymen");
    }

    public void searchProduct(String productName,String key) {
        logger.info("Searching for product: " + productName + " using key: " + key);
        WebElement searchInput = driver.findElement(ElementLocator.get(key));
        searchInput.sendKeys(productName);
    }

    public void clearSearchBox() {
        logger.info("Clearing search box.");
        WebElement searchInput = driver.findElement(ElementLocator.get("searchPageSearchBox"));
        searchInput.clear();
    }

    public void pressEnter() {
        logger.info("Pressing ENTER key in search box.");
        driver.findElement(ElementLocator.get("searchPageSearchBox")).sendKeys(Keys.ENTER);
    }

    public boolean isElementVisible(String key) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10); // 10 seconds
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(ElementLocator.get(key)));
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void waitUntilVisibleAndClick(String key) {
        logger.info("Waiting for element and clicking key: " + key);
        WebDriverWait wait = new WebDriverWait(driver, 10); // 10 seconds
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(ElementLocator.get(key)));
        element.click();
    }

    public void clickElement(String key) {
        logger.info("Clicking element with key: " + key);
        WebElement element = driver.findElement(ElementLocator.get(key));
        element.click();
    }

    public boolean elementTextContains(String key, String expectedText) {
        logger.info("Checking if element with key: " + key + " contains text: " + expectedText);
        try {
            WebElement element = driver.findElement(ElementLocator.get(key));
            boolean result = element.getText().contains(expectedText);
            logger.info("Element text contains expected value: " + result);
            return result;
        } catch (Exception e) {
            logger.warn("Could not verify text presence due to exception: " + e.getMessage());
            return false;
        }
    }

    public boolean homePageFooterContainer(String key, String text1, String text2, String text3) {
        boolean result = true;
        logger.info("Checking visibility of footer section.");
        if (!isElementVisible("homePageFooter")) {
            logger.warn("Footer not visible.");
            result = false;
        }
        String[] expectedTexts = {text1, text2, text3};
        for (String text : expectedTexts) {
            boolean contains = elementTextContains(key, text);
            logger.info("Checking if footer contains '" + text + "': " + contains);
            if (!contains) {
                result = false;
            }
        }
        return result;
    }
}