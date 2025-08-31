package tests.UI.tests.jph;

import base.BaseUITest;
import org.testng.annotations.Test;
import utils.Logger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class T002_JPH_UI_Test extends BaseUITest {
    // This class is used to test the JSON Placeholder UI
    @Test
    public void checkPageTitle() {
        Logger.formattedLog("Testing T002_JPH_UI_Test.checkPageTitle",logMode);
        page.navigate("https://jsonplaceholder.typicode.com/");
        Logger.formattedLog(
                page.title().contains("jsonplaceholder")
                        ? "Test T002_JPH_UI_Test.checkPageTitle passed"
                        : "Test T002_JPH_UI_Test.checkPageTitle failed",
                this.logMode
                );
        assertEquals(page.title(), "JSOlaceholder");
    }

    @Test
    public void clickPostsLink() {
        Logger.formattedLog("Testing T002_JPH_UI_Test.getSinglePost",logMode);

        page.navigate("https://jsonplaceholder.typicode.com/");
        page.click("text=/posts");
        Logger.formattedLog(
                page.url().contains("/posts") && page.content().contains("\"userId\"")
                        ? "Test T002_JPH_UI_Test.checkPageTitle passed"
                        : "Test T002_JPH_UI_Test.checkPageTitle failed",
                logMode
                );
        assertTrue(page.url().contains("/posts"));
        assertTrue(page.content().contains("\"userId\""));
    }

    @Test
    public void validateJsonListItems() {
        Logger.formattedLog("Testing T002_JPH_UI_Test.getSinglePost",logMode);

        page.navigate("https://jsonplaceholder.typicode.com/posts");
        String content = page.content();
        Logger.formattedLog(
                content.contains("{") && content.contains("userId") 
                        ? "Test T002_JPH_UI_Test.checkPageTitle passed"
                        : "Test T002_JPH_UI_Test.checkPageTitle failed",
                logMode
                );
        assertTrue(content.contains("{") && content.contains("userId"), "Expected JSON data in content");
    }
}
