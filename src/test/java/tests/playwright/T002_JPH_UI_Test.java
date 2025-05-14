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
    @Parameters({"browser","headless"})
    static void setupAll(String b,String h) {
        // read browser name from CLI else from the Test suite property
        String selectedBrowser = System.getProperty("browser",b);
        Boolean headless = System.getProperty("browser",h) == "true";
        System.out.println("Testing class T002_JPH_UI_Test");
        System.out.println(String.format("Using browser engine %s with headless=%s", selectedBrowser,headless));
        playwright = Playwright.create();
        if (selectedBrowser.contains("firefox")){
            browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(headless));
        }else if (selectedBrowser.contains("safari")){
            browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(headless));
        }else{ // for chromium, chrome & edge
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
        }
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
        Logger.log(
                page.title().contains("jsonplaceholder")
                        ? "Test T002_JPH_UI_Test.checkPageTitle passed"
                        : "Test T002_JPH_UI_Test.checkPageTitle failed"
                );
        assertEquals(page.title(), "JSOlaceholder");
    }

    @Test
    public void clickPostsLink() {
        Logger.log("Testing T002_JPH_UI_Test.getSinglePost");

        page.navigate("https://jsonplaceholder.typicode.com/");
        page.click("text=/posts");
        Logger.log(
                page.url().contains("/posts") && page.content().contains("\"userId\"")
                        ? "Test T002_JPH_UI_Test.checkPageTitle passed"
                        : "Test T002_JPH_UI_Test.checkPageTitle failed"
                );
        assertTrue(page.url().contains("/posts"));
        assertTrue(page.content().contains("\"userId\""));
    }

    @Test
    public void validateJsonListItems() {
        Logger.log("Testing T002_JPH_UI_Test.getSinglePost");

        page.navigate("https://jsonplaceholder.typicode.com/posts");
        String content = page.content();
        Logger.log(
                content.contains("{") && content.contains("userId") 
                        ? "Test T002_JPH_UI_Test.checkPageTitle passed"
                        : "Test T002_JPH_UI_Test.checkPageTitle failed"
                );
        assertTrue(content.contains("{") && content.contains("userId"), "Expected JSON data in content");
    }
}
