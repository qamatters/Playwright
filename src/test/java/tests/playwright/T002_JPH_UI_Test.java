package tests.playwright;

import com.microsoft.playwright.*;

import org.testng.annotations.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import utils.Logger;

public class T002_JPH_UI_Test {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeClass
    static void setupAll() {
        System.out.println("Testing class T002_JPH_UI_Test");
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @BeforeMethod
    void setup() {
        context = browser.newContext();
        page = context.newPage();
        Logger.increment_test_number();
    }

    @AfterMethod
    void teardown() {
        context.close();
    }

    @AfterClass
    static void teardownAll() {
        browser.close();
        playwright.close();
        System.out.println("Testing class T002_JPH_UI_Test finished");

    }

    @Test
    public void checkPageTitle() {
        Logger.log("Testing T002_JPH_UI_Test.checkPageTitle");
        page.navigate("https://jsonplaceholder.typicode.com/");
        assertEquals(page.title(), "JSONPlaceholder - Fake REST API");
        Logger.log(
                page.title().contains("JSONPlaceholder - Fake REST API")
                        ? "Test T002_JPH_UI_Test.checkPageTitle passed"
                        : "Test T002_JPH_UI_Test.checkPageTitle failed"
                );
    }

    @Test
    public void clickPostsLink() {
        Logger.log("Testing T002_JPH_UI_Test.getSinglePost");

        page.navigate("https://jsonplaceholder.typicode.com/");
        page.click("text=/posts");
        assertTrue(page.url().contains("/posts"));
        assertTrue(page.content().contains("\"userId\""));
        Logger.log(
                page.url().contains("/posts") && page.content().contains("\"userId\"")
                        ? "Test T002_JPH_UI_Test.checkPageTitle passed"
                        : "Test T002_JPH_UI_Test.checkPageTitle failed"
                );
    }

    @Test
    public void validateJsonListItems() {
        Logger.log("Testing T002_JPH_UI_Test.getSinglePost");

        page.navigate("https://jsonplaceholder.typicode.com/posts");
        String content = page.content();
        assertTrue(content.contains("{") && content.contains("userId"), "Expected JSON data in content");
        Logger.log(
                content.contains("{") && content.contains("userId") 
                        ? "Test T002_JPH_UI_Test.checkPageTitle passed"
                        : "Test T002_JPH_UI_Test.checkPageTitle failed"
                );
    }
}
