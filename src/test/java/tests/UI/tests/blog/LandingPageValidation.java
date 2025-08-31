package tests.UI.tests.blog;


import base.BaseUITest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import tests.UI.pages.blog.HomePage;
import utils.Logger;

public class LandingPageValidation extends BaseUITest {
    @Test
    @Parameters({"url"})
    public void checkPageTitle(String url) {
        HomePage homePage = new HomePage(page);
        Logger.formattedLog("Testing T002_JPH_UI_Test.checkPageTitle",logMode);
        page.navigate(url);
        Logger.formattedLog(
                page.title().contains("jsonplaceholder")
                        ? "Test T002_JPH_UI_Test.checkPageTitle passed"
                        : "Test T002_JPH_UI_Test.checkPageTitle failed",
                this.logMode
        );
        homePage.searchBlog("Jenkins");
    }

}
