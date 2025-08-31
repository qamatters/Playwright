package base;

import com.aventstack.extentreports.Status;
import com.microsoft.playwright.*;
import listeners.ExtentTestNGReporter;
import listeners.ReportUtil;
import listeners.ScreenshotUtil;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.Logger;
import utils.enums.BrowserEngine;
import utils.enums.LogMode;

public abstract class BaseUITest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext context;
    public static Page page;
    protected LogMode logMode;
    protected BrowserEngine browserType;
    protected String url;
    protected boolean headless;

    @BeforeClass(alwaysRun = true)
    @Parameters({"logMode", "url", "browser", "headless"})
    public void baseSetup(String mode, String url, String browserType, String headless) {
        playwright = Playwright.create();
        this.logMode = LogMode.parse(System.getProperty("logMode", mode));
        this.url = url;
        this.browserType = BrowserEngine.parse(System.getProperty("browser", browserType));
        this.headless = Boolean.parseBoolean(System.getProperty("headless", headless));
        Logger.log("Testing class " + this.getClass().getSimpleName(), logMode);

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(this.headless);
        switch (this.browserType) {
            case Firefox -> browser = playwright.firefox().launch(options);
            case Webkit -> browser = playwright.webkit().launch(options);
            default -> browser = playwright.chromium().launch(options);
        }

        context = browser.newContext();
        page = context.newPage();

        // Set page in utilities
        ScreenshotUtil.setPage(page);
        ReportUtil.setPage(page);
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

        Logger.log("Executed tests class " + this.getClass().getSimpleName(), logMode);
    }

    @BeforeMethod(alwaysRun = true)
    public void setupLogger() {
        Logger.increment_test_number();
        ReportUtil.initSoftAssert();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            // Handle soft assertions (if any failed)
            ReportUtil.assertAll();
        } catch (AssertionError e) {
            System.out.println("Assertion failure details: " + e.getMessage());

            // Only log if not already logged by listener (runtime failure case)
            if (result.getAttribute("extentLogged") == null) {
                if (result.getStatus() == ITestResult.SUCCESS) {
                    result.setStatus(ITestResult.FAILURE);
                    result.setThrowable(e);
                }
                ExtentTestNGReporter.getTest().log(
                        Status.FAIL,
                        "Test failed due to assertion error(s). See step-level details above."
                );
            }

            // Re-throw so console shows FAIL
            throw e;
        } finally {
            // Always mark test finished
            ReportUtil.logInfo("Test Case Finished");
        }
    }
}
