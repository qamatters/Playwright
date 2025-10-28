package tests.UI.pages.AutomationPlayground.insurance;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import listeners.ReportUtil;

public class InsuranceLoginPage extends BasePage {
    private final Locator userLoginButton;
    private final Locator adminLoginButton;
    private final Locator username;
    private final Locator password;
    private final Locator loginButtonForUser;
    private final Locator loginButtonForAdmin;
    private final Locator submitYourClaimTitle;

    private final Locator logOutButton;
    private final Locator adminUserName;
    private final Locator adminPassword;
    private final Locator adminLoginHeading;
    private final Locator loggedInAsAdmin;

    public InsuranceLoginPage(Page page) {
        super(page);
        this.userLoginButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("User Login"));
        this.adminLoginButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Admin Login"));
        this.username = page.locator("#username");
        this.password = page.locator("#password");
        this.adminUserName = page.locator("#adminUsername");
        this.adminPassword = page.locator("#adminPassword");
        this.loginButtonForUser = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login as User"));
        this.loginButtonForAdmin = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login as Admin"));
        this.submitYourClaimTitle = page.locator("//h1[contains(text(),'Submit Your Claim')]");
        this.logOutButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Logout"));
        this.adminLoginHeading =  page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Admin Dashboard"));
        this.loggedInAsAdmin = page.getByText("Logged in as: Admin");
    }

    public void launchUserLoginPage() {
        ReportUtil.assertTrue(userLoginButton.isVisible(), "User Login button is displayed");
        userLoginButton.click();
        ReportUtil.assertTrue(username.isVisible(), "Username field is visible");
    }

    public void launchAdminLoginPage() {
        ReportUtil.assertTrue(adminLoginButton.isVisible(), "Admin Login button is displayed");
        adminLoginButton.click();
        ReportUtil.assertTrue(adminUserName.isVisible(), "Username field is visible for Admin Login");
    }

    public void loginToInsurancePortal(String testUserName, String testUserPassword) {
        if (testUserName.contains("admin")) {
            adminUserName.fill(testUserName);
            adminPassword.fill(testUserPassword);
            loginButtonForAdmin.click();
        } else {
            username.fill(testUserName);
            password.fill(testUserPassword);
            loginButtonForUser.click();
        }
        ReportUtil.logInfo("User information is entered and login button is clicked");
    }

    public void validateUserLogin() {
        waitForVisible(submitYourClaimTitle, 10000);
        ReportUtil.verifyTrue(submitYourClaimTitle.isVisible(), "Submit your claim title is available");
    }

    public void validateAdminLogin() {
        waitForVisible(adminLoginHeading, 10000);
        ReportUtil.verifyTrue(adminLoginHeading.isVisible(), adminLoginHeading.textContent() + " is visible");
        ReportUtil.verifyTrue(loggedInAsAdmin.isVisible(), loggedInAsAdmin.textContent() + " is visible");
    }


    public void logOut() {
        waitForVisible(logOutButton, 1000);
        logOutButton.click();
        ReportUtil.assertTrue(userLoginButton.isVisible(), "User Login button is displayed");
    }


}
