package tests.UI.tests.playground.specificUseCases;

import base.BrowserHelper;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.testng.annotations.Test;

public class Test_LaunchBrowserInIncognito  {
    @Test
    public void TestAlertBehaviourInPlaywright() {
        BrowserHelper.launchBrowser("chromium", false);  // launch fresh browser
        BrowserContext incognitoContext = BrowserHelper.launchFreshIncognitoContext();

        Page incognitoPage = BrowserHelper.openNewPage(incognitoContext);

        // Navigate and perform your test
        incognitoPage.navigate("https://qamatters.github.io/demoautomationWebSite/SpecialFields/dropdown.html");
        System.out.println("Title: " + incognitoPage.title());



        // Cleanup manually since BaseUITest teardown won't handle this browser
        incognitoContext.close();
        BrowserHelper.closeBrowser();
    }
}
