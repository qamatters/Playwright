package tests.UI.tests.playground.AIPOC;

import base.BaseUITest;
import org.testng.annotations.Test;

import static listeners.ReportUtil.assertTrue;

public class AISolution_testLoginFailure extends BaseUITest {
    @Test(description = "Fail login scenario")
    public void testLoginFailure() {
        page.navigate("https://the-internet.herokuapp.com/login");
        page.fill("#username", "wrongUser");
        page.fill("#password", "wrongPass");
        page.click("button[type='submit']");
        String errorMessage = page.textContent("#flash");
        System.out.println("error message is :" + errorMessage);
        assertTrue(errorMessage.contains("You enetered an invalid username"), "Expected login failure message");
    }
}
