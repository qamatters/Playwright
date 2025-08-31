package tests.UI.pages.AutomationPlayground.BankingWorkFlow;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import listeners.ReportUtil;

public class LoginPage extends BasePage {
    private final Locator username;
    private final Locator password;
    private final Locator loginButton;

    public LoginPage(Page page) {
        super(page);
        this.username = page.locator("#username");
        this.password = page.locator("#password");
        this.loginButton = page.locator("#loginForm > button");
    }

    public void loginToBankApplication() {
        username.fill("testuser");
        password.fill("password123");
        loginButton.click();
        ReportUtil.logInfo("User information is entered and login button is clicked");
    }

}
