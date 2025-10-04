package tests.UI.pages.demoPlayground.ui;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import listeners.ReportUtil;

public class LoginPage extends BasePage {
    private final Locator username;
    private final Locator password;
    private final Locator loginButton;


    public LoginPage(Page page) {
        super(page);
        this.username = page.getByPlaceholder("Username");
        this.password = page.getByPlaceholder("Password");
        this.loginButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login"));
    }

    public void loginToDemoWebAndServicesApplication() {
        username.fill("deepak");
        password.fill("deepak");
        loginButton.click();
        ReportUtil.logInfo("User information is entered and login button is clicked");
    }



}
