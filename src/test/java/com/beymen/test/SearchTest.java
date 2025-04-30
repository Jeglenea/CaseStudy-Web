package com.beymen.test;

import com.beymen.pages.*;
import com.beymen.utils.ExcelReader;
import com.beymen.utils.FileWriterUtility;
import com.beymen.utils.ElementActions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.apache.log4j.Logger;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SearchTest {

    private static final Logger logger = Logger.getLogger(SearchTest.class);

    private static WebDriver driver;
    private static HomePage homePage;
    private static SearchResultsPage searchResultsPage;
    private static ProductPage productPage;
    private static CartPage cartPage;

    private static String productName;
    private static String productPrice;
    private static ElementActions elementActions;

    @BeforeAll
    public static void setUp() {
        logger.info("Test Setup started");
        logger.info("----------------------------------------------");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        homePage = new HomePage(driver);
        searchResultsPage = new SearchResultsPage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
        elementActions = new ElementActions(driver);
        logger.info("----------------------------------------------");
        logger.info("Test Setup completed");
    }

    @Test
    @Order(1)
    public void openHomePage() {
        logger.info("Test: openHomePage started");
        logger.info("----------------------------------------------");
        elementActions.navigateToUrl("https://www.beymen.com/");
        elementActions.waitForSeconds(2);
        homePage.isHomePageDisplayed("Beymen");
        elementActions.isElementVisible("homePageCookies");
        elementActions.clickElement("homePageCookiesReject");
        elementActions.clickElementJS("homePageGenderSelect");
        elementActions.isElementVisible("homePageLogo");
        elementActions.isElementVisible("homePageSearchBox");
        elementActions.isElementVisible("homePageNavbar");
        logger.info("----------------------------------------------");
        logger.info("Test: openHomePage completed");
    }

    @Test
    @Order(2)
    public void searchFirstTermFromExcel() throws IOException {
        logger.info("Test: searchFirstTermFromExcel started");
        logger.info("----------------------------------------------");
        String term1 = ExcelReader.readCell("src/test/resources/data.xlsx", 0, 0);
        homePage.searchProduct(term1, "homePageSearchBox");
        elementActions.elementTextContains("homePageSearchSuggestion","Şort","Şortu");
        elementActions.elementTextContains("homePageSearchSuggestionItem","Şort", "Şortu");
        logger.info("----------------------------------------------");
        logger.info("Test: searchFirstTermFromExcel completed");
    }

    @Test
    @Order(3)
    public void clearAndSearchSecondTermFromExcel() throws IOException {
        logger.info("Test: clearAndSearchSecondTermFromExcel started");
        logger.info("----------------------------------------------");
        elementActions.clearSearchBox("searchPageSearchBox");
        String term2 = ExcelReader.readCell("src/test/resources/data.xlsx", 0, 1);
        homePage.searchProduct(term2,"searchPageSearchBox");
        elementActions.elementTextContains("homePageSearchSuggestion","Gömlek");
        elementActions.elementTextContains("homePageSearchSuggestionItem","Gömlek");
        elementActions.pressEnter("searchPageSearchBox");
        logger.info("----------------------------------------------");
        logger.info("Test: clearAndSearchSecondTermFromExcel completed");
    }

    @Test
    @Order(4)
    public void selectRandomProduct() throws IOException {
        logger.info("Test: selectRandomProduct started");
        logger.info("----------------------------------------------");
        elementActions.isElementVisible("searchPageProductList");
        String expectedText = ExcelReader.readCell("src/test/resources/data.xlsx", 0, 1);
        elementActions.elementTextContains("searchPageSearchFind", expectedText);
        searchResultsPage.clickRandomProduct("searchPageProductDesc");
        productName = productPage.getProductTitle("productTitle");

        productPrice = productPage.getProductPrice("productPriceOld", "productPriceNew");

        String content = "Ürün: " + productName + "\nFiyat: " + productPrice;
        FileWriterUtility.writeToFile("src/test/resources/product-info.txt", content);
        logger.info("----------------------------------------------");
        logger.info("Test: selectRandomProduct completed");
    }

    @Test
    @Order(5)
    public void addToCartAndValidatePrice() throws InterruptedException {
        logger.info("Test: addToCartAndValidatePrice started");
        logger.info("----------------------------------------------");
        logger.info("Adding product to cart.");
        productPage.sizeCompareChoose();
        elementActions.waitUntilVisibleAndClick("addToCartButton");
        elementActions.isElementVisible("productPageAddSuccess");
        elementActions.elementTextContains("productPageAddSuccessText", "Sepete Eklendi");
        elementActions.navigateToUrl("https://www.beymen.com/tr/cart");
        elementActions.waitUntilInvisible("cartPageGhostLoad");
        String cartPrice = cartPage.getCartPrice("cartPrice");
        elementActions.isElementVisible("cartPrice");
        Assertions.assertEquals(productPrice, cartPrice, "Fiyatlar uyuşmuyor!");
        logger.info("Fiyatlar karşılaştırıldı ve eşleşti: " + productPrice + " == " + cartPrice);
        logger.info("----------------------------------------------");
        logger.info("Test: addToCartAndValidatePrice completed");
    }

    @Test
    @Order(6)
    public void increaseQuantityAndVerify() {
        logger.info("Test: increaseQuantityAndVerify started");
        logger.info("----------------------------------------------");
        elementActions.elementAttributeContains("cartPageIncreaseQty", "aria-label", "1 adet");
        cartPage.increaseQuantity("2");
        elementActions.elementAttributeContains("cartPageIncreaseQty", "aria-label", "2 adet");
        logger.info("----------------------------------------------");
        logger.info("Test: increaseQuantityAndVerify completed");
    }

    @Test
    @Order(7)
    public void removeProductAndVerifyEmptyCart() {
        logger.info("Test: removeProductAndVerifyEmptyCart started");
        logger.info("----------------------------------------------");
        elementActions.waitForSeconds(5); // There is a bug when you update the amount of product and suddenly remove it, the cart gets bugged and show product with updated amount
        elementActions.clickElement("removeButton");
        elementActions.waitUntilInvisible("cartPageGhostLoad");
        elementActions.elementTextContains("cartPageNotify", "Ürün Silindi");
        elementActions.waitUntilVisible("cartPageEmpty");
        elementActions.elementTextContains("cartPageEmpty", "SEPETINIZDE ÜRÜN BULUNMAMAKTADIR");
        logger.info("----------------------------------------------");
        logger.info("Test: removeProductAndVerifyEmptyCart completed");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}