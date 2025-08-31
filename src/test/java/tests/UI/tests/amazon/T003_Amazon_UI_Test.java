package tests.UI.tests.amazon;

import base.BaseUITest;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import utils.Logger;

public class T003_Amazon_UI_Test extends BaseUITest {

    @Test(description = "Verify Amazon homepage title")
    @Parameters({"url"})
    public void testAmazonHomePageTitle(String url) {
        page.navigate(url);
        String title = page.title();
        Logger.formattedLog("Amazon Title: " + title,this.logMode);
        Assert.assertTrue(title.toLowerCase().contains("amazon"));
    }

    @Test(description = "Search for a product and verify results")
    @Parameters({"url"})
    public void testAmazonSearch(String url) {
        page.navigate(url);
        page.fill("input#twotabsearchtextbox", "laptop");
        page.click("input#nav-search-submit-button");
        page.waitForSelector("span.a-size-medium");
        String resultsText = page.textContent("span.a-size-medium");
        Assert.assertNotNull(resultsText);
        Logger.formattedLog("First result: " + resultsText,this.logMode);
    }
}