package tests.playwright;

import com.microsoft.playwright.*;

import org.testng.annotations.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import utils.Logger;
import utils.enums.BrowserEngine;
import utils.enums.LogMode;;

public class T002_JPH_UI_Test {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeClass
    @Parameters({"browser","headless","logMode"})
    static void setupAll(String b,String h,String mode) {
        // read browser name from CLI else from the Test suite property
        BrowserEngine selectedBrowser = BrowserEngine.parse(System.getProperty("browser",b));
        Boolean headless = Boolean.parseBoolean(System.getProperty("headless",h));
        LogMode l = LogMode.parse(mode);
        Logger.log("Testing class T002_JPH_UI_Test",l);
        Logger.log(String.format("Using browser engine %s with headless=%s", selectedBrowser,headless),l);
        playwright = Playwright.create();
        if (selectedBrowser == BrowserEngine.Firefox){
            browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(headless));
        }else if (selectedBrowser == BrowserEngine.Webkit){
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
    @Parameters({"logMode"})
    public void checkPageTitle(String mode) {
        LogMode l = LogMode.parse(mode); 
        Logger.formattedLog("Testing T002_JPH_UI_Test.checkPageTitle",l);
        page.navigate("https://jsonplaceholder.typicode.com/");
        Logger.formattedLog(
                page.title().contains("jsonplaceholder")
                        ? "Test T002_JPH_UI_Test.checkPageTitle passed"
                        : "Test T002_JPH_UI_Test.checkPageTitle failed",
                l
                );
        assertEquals(page.title(), "JSOlaceholder");
    }

    @Test
    @Parameters({"logMode"})
    public void clickPostsLink(String mode) {
        LogMode l = LogMode.parse(mode); 
        Logger.formattedLog("Testing T002_JPH_UI_Test.getSinglePost",l);

        page.navigate("https://jsonplaceholder.typicode.com/");
        page.click("text=/posts");
        Logger.formattedLog(
                page.url().contains("/posts") && page.content().contains("\"userId\"")
                        ? "Test T002_JPH_UI_Test.checkPageTitle passed"
                        : "Test T002_JPH_UI_Test.checkPageTitle failed",
                l
                );
        assertTrue(page.url().contains("/posts"));
        assertTrue(page.content().contains("\"userId\""));
    }

    @Test
    @Parameters({"logMode"})
    public void validateJsonListItems(String mode) {
        LogMode l = LogMode.parse(mode); 
        Logger.formattedLog("Testing T002_JPH_UI_Test.getSinglePost",l);

        page.navigate("https://jsonplaceholder.typicode.com/posts");
        String content = page.content();
        Logger.formattedLog(
                content.contains("{") && content.contains("userId") 
                        ? "Test T002_JPH_UI_Test.checkPageTitle passed"
                        : "Test T002_JPH_UI_Test.checkPageTitle failed",
                l
                );
        assertTrue(content.contains("{") && content.contains("userId"), "Expected JSON data in content");
    }
}
