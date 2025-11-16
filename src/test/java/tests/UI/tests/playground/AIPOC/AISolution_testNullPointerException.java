package tests.UI.tests.playground.AIPOC;

import base.BaseUITest;
import com.microsoft.playwright.Locator;
import org.testng.annotations.Test;

public class AISolution_testNullPointerException extends BaseUITest {
    @Test(description = "Trigger NullPointerException by interacting with a null object")
    public void testNullPointerException() {
        Locator button = null;
        button.click();

    }

}
