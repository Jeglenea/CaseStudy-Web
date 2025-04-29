package com.beymen.test;

import com.beymen.pages.*;
import com.beymen.utils.ExcelReader;
import com.beymen.utils.FileWriterUtility;
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
        logger.info("----------------------------------------------");
        logger.info("Test Setup completed");
    }

    @Test
    @Order(1)
    public void openHomePage() {
        logger.info("Test: openHomePage started");
        logger.info("----------------------------------------------");
        homePage.navigateToHomePage();
        homePage.isHomePageDisplayed();
        homePage.isElementVisible("homePageCookies");
        homePage.clickElement("homePageCookiesReject");
        homePage.clickElement("homePageGenderSelect");
        homePage.isElementVisible("homePageLogo");
        homePage.isElementVisible("homePageSearchBox");
        homePage.isElementVisible("homePageNavbar");
        homePage.homePageFooterContainer("homePageFooterTop", "BEYMEN HAKKINDA", "MAĞAZADAN TESLİM", "KOLAY İADE");
        homePage.homePageFooterContainer("homePageFooterBottom", "ÜCRETSİZ KARGO", "HESABIM", "MAĞAZALAR");
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
        homePage.elementTextContains("elementTextContains","Şort");
        homePage.elementTextContains("elementTextContains","Şortu");
        homePage.elementTextContains("homePageSearchSuggestionItem","Şort");
        homePage.elementTextContains("homePageSearchSuggestionItem","Şortu");
        logger.info("----------------------------------------------");
        logger.info("Test: searchFirstTermFromExcel started");
    }

    @Test
    @Order(3)
    public void clearAndSearchSecondTermFromExcel() throws IOException {
        logger.info("Test: clearAndSearchSecondTermFromExcel started");
        logger.info("----------------------------------------------");
        homePage.clearSearchBox();
        String term2 = ExcelReader.readCell("src/test/resources/data.xlsx", 0, 1);
        homePage.searchProduct(term2,"searchPageSearchBox");
        homePage.elementTextContains("elementTextContains","Gömlek");
        homePage.elementTextContains("homePageSearchSuggestionItem","Gömlek");
        homePage.pressEnter();
        logger.info("----------------------------------------------");
        logger.info("Test: clearAndSearchSecondTermFromExcel started");
    }

    @Test
    @Order(4)
    public void selectRandomProduct() throws IOException {
        logger.info("Test: selectRandomProduct started");
        logger.info("----------------------------------------------");
        homePage.isElementVisible("searchPageProductList");
        String expectedText = ExcelReader.readCell("src/test/resources/data.xlsx", 0, 1);
        homePage.elementTextContains("searchPageSearchFind", expectedText);
        homePage.elementTextContains("searchPageSearchFilter", expectedText);
        searchResultsPage.clickRandomProduct("searchPageProductDesc");
        productName = productPage.getProductTitle();
        productPrice = productPage.getProductPrice();
        searchResultsPage.clickRandomProduct2("sizeSelect");

        String content = "Ürün: " + productName + "\nFiyat: " + productPrice;
        FileWriterUtility.writeToFile("src/test/resources/product-info.txt", content);
        logger.info("----------------------------------------------");
        logger.info("Test: selectRandomProduct started");
    }

    @Test
    @Order(5)
    public void addToCartAndValidatePrice() throws InterruptedException {
        logger.info("Test: addToCartAndValidatePrice started");
        logger.info("----------------------------------------------");
        productPage.addToCart();
        driver.navigate().to("https://www.beymen.com/tr/cart");
        String cartPrice = cartPage.getCartPrice();
        homePage.isElementVisible("cartPrice");
        Assertions.assertEquals(productPrice, cartPrice, "Fiyatlar uyuşmuyor!");
        logger.info("----------------------------------------------");
        logger.info("Test: addToCartAndValidatePrice started");
    }

    @Test
    @Order(6)
    public void increaseQuantityAndVerify() {
        logger.info("Test: increaseQuantityAndVerify started");
        logger.info("----------------------------------------------");
        cartPage.elementAttributeContains("cartPageIncreaseQty", "aria-label", "1 adet");
        cartPage.increaseQuantity("2");
        cartPage.elementAttributeContains("cartPageIncreaseQty", "aria-label", "2 adet");
        //Assertions.assertEquals("2", cartPage.getQuantity(), "Ürün adedi 2 değil.");
        logger.info("----------------------------------------------");
        logger.info("Test: increaseQuantityAndVerify started");
    }

    @Test
    @Order(7)
    public void removeProductAndVerifyEmptyCart() {
        logger.info("Test: removeProductAndVerifyEmptyCart started");
        logger.info("----------------------------------------------");
        cartPage.removeProduct();
        homePage.elementTextContains("cartPageEmpty", "Sepetinizde Ürün Bulunmamaktadır");
        homePage.elementTextContains("cartPageEmptyNotify", "Ürün Silindi");
        logger.info("----------------------------------------------");
        logger.info("Test: removeProductAndVerifyEmptyCart started");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}