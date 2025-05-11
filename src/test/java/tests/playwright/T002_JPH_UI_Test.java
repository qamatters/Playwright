package tests.playwright;

import com.microsoft.playwright.*;

import org.testng.annotations.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class T002_JPH_UI_Test {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeClass
    static void setupAll() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @BeforeMethod
    void setup() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterMethod
    void teardown() {
        context.close();
    }

    @AfterClass
    static void teardownAll() {
        browser.close();
        playwright.close();
    }

    @Test
    public void checkPageTitle() {
        page.navigate("https://jsonplaceholder.typicode.com/");
        assertEquals(page.title(), "JSONPlaceholder - Free Fake REST API");
    }

    @Test
    public void clickPostsLink() {
        page.navigate("https://jsonplaceholder.typicode.com/");
        page.click("text=/posts");
        assertTrue(page.url().contains("/posts"));
        assertTrue(page.content().contains("\"userId\""));
    }

    @Test
    public void validateJsonListItems() {
        page.navigate("https://jsonplaceholder.typicode.com/posts");
        String content = page.content();
        assertTrue(content.contains("{") && content.contains("userId"), "Expected JSON data in content");
    }
}
