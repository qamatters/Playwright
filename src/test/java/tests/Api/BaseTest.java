package tests.Api;

import com.microsoft.playwright.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import utils.Logger;
import utils.enums.LogMode;

public abstract class BaseTest {
    protected static Playwright playwright;
    protected static APIRequestContext api;
    protected LogMode logMode;

    @BeforeClass(alwaysRun = true)
    @Parameters({"logMode","url"})
    public void baseSetup(String mode,String url) {
        this.logMode = LogMode.parse(mode);
        Logger.log("Testing class " + this.getClass().getSimpleName(),logMode);
        if (playwright == null) playwright = Playwright.create();
        if (api == null)  api = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(url));
        logMode = LogMode.parse(mode);
    }

    @AfterClass(alwaysRun = true)
    public void baseTeardown() {
        if (api != null) api.dispose();
        if (playwright != null) playwright.close();
        api = null;
        playwright = null;
        Logger.log("Executed tests class " + this.getClass().getSimpleName(),logMode);
    }

    @BeforeMethod(alwaysRun = true)
    public void setupLogger() {
        Logger.increment_test_number();
    }
}
