package tests.UI;

// import com.microsoft.playwright.*;

import org.testng.annotations.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import utils.Logger;
// import utils.enums.BrowserEngine;
import utils.enums.LogMode;;

public class T002_JPH_UI_Test extends BaseUITest {
    // This class is used to test the JSON Placeholder UI
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
