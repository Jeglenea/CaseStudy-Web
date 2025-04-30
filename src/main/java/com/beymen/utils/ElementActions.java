package com.beymen.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import org.apache.log4j.Logger;

public class ElementActions {

    private WebDriver driver;
    protected final Logger logger = Logger.getLogger(getClass());

    public ElementActions(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateToUrl(String url) {
        logger.info("Navigating to homepage...");
        driver.get(url);
    }

    public boolean isElementVisible(String key) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10); // 10 seconds
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(ElementLocator.get(key)));
            logger.info("Element with key '" + key + "' is visible.");
            return element.isDisplayed();
        } catch (Exception e) {
            logger.warn("Element with key '" + key + "' is not visible.", e);
            return false;
        }
    }

    public void waitUntilVisibleAndClick(String key) {
        WebDriverWait wait = new WebDriverWait(driver, 10); // 10 seconds
        logger.info("Waiting until element with key '" + key + "' is visible and then clicking.");
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(ElementLocator.get(key)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        element.click();
        logger.info("Clicked on element with key '" + key + "'.");
    }

    public WebElement waitUntilVisible(String key) {
        logger.info("Waiting until element with key '" + key + "' is visible.");
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(ElementLocator.get(key)));
            logger.info("Element with key '" + key + "' is visible.");
            return element;
        } catch (Exception e) {
            logger.warn("Element with key '" + key + "' did not become visible.", e);
            return null;
        }
    }

    public void clickElement(String key) {
        logger.info("Clicking on element with key '" + key + "'.");
        WebElement element = driver.findElement(ElementLocator.get(key));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        element.click();
    }

    public void clickElementJS(String key) {
        logger.info("Clicking on element with key '" + key + "'.");
        WebElement element = driver.findElement(ElementLocator.get(key));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public boolean elementTextContains(String key, String... expectedTexts) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(ElementLocator.get(key)));
            String elementText = element.getText();
            for (String expectedText : expectedTexts) {
                if (elementText.toLowerCase().contains(expectedText.toLowerCase())) {
                    logger.info("Element with key '" + key + "' contains expected text: '" + expectedText + "'.");
                    return true;
                }
            }
            logger.info("Element with key '" + key + "' does not contain any of the expected texts.");
            return false;
        } catch (Exception e) {
            logger.warn("Could not get text from element with key '" + key + "' to check for expected values.", e);
            return false;
        }
    }

    public void pressEnter(String key) {
        logger.info("Pressing ENTER key in search box.");
        driver.findElement(ElementLocator.get(key)).sendKeys(Keys.ENTER);
    }

    public boolean elementAttributeContains(String key, String attribute, String expectedValue) {
        logger.info("Checking if attribute '" + attribute + "' of element '" + key + "' contains: " + expectedValue);
        try {
            WebElement element = driver.findElement(ElementLocator.get(key));
            String actualValue = element.getAttribute(attribute);
            logger.info("Actual value of attribute '" + attribute + "' for element '" + key + "': " + actualValue);
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

    public void clearSearchBox(String key) {
        logger.info("Clearing search box.");
        WebElement searchInput = driver.findElement(ElementLocator.get(key));
        searchInput.clear();
    }

    public boolean waitUntilInvisible(String key) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, (long) 0.5);
            boolean invisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(ElementLocator.get(key)));
            if (invisible) {
                logger.info("Element with key '" + key + "' is now invisible.");
            } else {
                logger.info("Element with key '" + key + "' is still visible.");
            }
            return invisible;
        } catch (Exception e) {
            logger.warn("Exception while waiting for element with key '" + key + "' to become invisible.", e);
            return false;
        }
    }

    public void waitForSeconds(long seconds) {
        logger.info("Waiting for " + seconds + " seconds.");
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Interrupted while waiting.", e);
        }
    }

    public boolean clickIfExists(String key) {
        logger.info("Attempting to click element with key '" + key + "' if it exists.");
        try {
            if (!driver.findElements(ElementLocator.get(key)).isEmpty()) {
                WebElement element = driver.findElement(ElementLocator.get(key));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
                element.click();
                logger.info("Clicked on element with key '" + key + "'.");
                return true;
            } else {
                logger.info("Element with key '" + key + "' does not exist. No action taken.");
                return false;
            }
        } catch (Exception e) {
            logger.warn("Error while attempting to click element with key '" + key + "'.", e);
            return false;
        }
    }

}