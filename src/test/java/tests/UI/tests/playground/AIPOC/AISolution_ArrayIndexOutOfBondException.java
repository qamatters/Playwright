package tests.UI.tests.playground.AIPOC;

import base.BaseUITest;
import com.microsoft.playwright.Page;
import org.testng.annotations.Test;

public class AISolution_ArrayIndexOutOfBondException extends BaseUITest {
    @Test(description = "Trigger TimeoutException by waiting for missing element")
    public void testTimeoutException() {
        page.navigate("https://the-internet.herokuapp.com/dynamic_loading/1");
        page.waitForSelector("#nonExistentElement", new Page.WaitForSelectorOptions().setTimeout(2000));

    }
}
