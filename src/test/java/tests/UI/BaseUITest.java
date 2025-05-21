package tests.UI;

import com.microsoft.playwright.*;
import org.testng.annotations.*;
import utils.Logger;
import utils.enums.BrowserEngine;
import utils.enums.LogMode;

public abstract class BaseUITest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext context;
    protected static Page page;
    protected LogMode logMode;
    protected BrowserEngine browserType;
    protected String url;
    protected boolean headless;

    @BeforeClass(alwaysRun = true)
    @Parameters({"logMode", "url", "browser", "headless"})
    public void baseSetup(String mode, String url, String browserType, String headless) {
        playwright = Playwright.create();
        this.logMode = LogMode.parse(System.getProperty("logMode",mode));
        this.url = url;
        this.browserType = BrowserEngine.parse(System.getProperty("browser",browserType));
        this.headless = Boolean.parseBoolean(System.getProperty("headless",headless));
        Logger.log("Testing class " + this.getClass().getSimpleName(),logMode);

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(this.headless);
        switch (this.browserType) {
            case Firefox:
                browser = playwright.firefox().launch(options);
                break;
            case Webkit:
                browser = playwright.webkit().launch(options);
                break;
            default:
                browser = playwright.chromium().launch(options);
        }
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterClass(alwaysRun = true)
    public void baseTeardown() {
        if (page != null) page.close();
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
        context = null;
        page = null;
        browser = null;
        playwright = null;
        Logger.log("Executed tests class " + this.getClass().getSimpleName(),logMode);
    }

    @BeforeMethod(alwaysRun = true)
    public void setupLogger() {
        Logger.increment_test_number();
    }
}