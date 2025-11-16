package tests.UI.tests.playground.insurance;

import base.BaseUITest;
import listeners.ReportUtil;
import org.testng.annotations.Test;
import tests.UI.pages.AutomationPlayground.insurance.Claims;
import tests.UI.pages.AutomationPlayground.insurance.InsuranceLoginPage;
import utils.Logger;

public class InsuranceLoginPageTest extends BaseUITest {
    @Test
    public void validateLoginPage() {
        InsuranceLoginPage loginPage = new InsuranceLoginPage(page);
        Claims claims = new Claims(page);
        Logger.formattedLog("Testing JIRA_301_Login Page Validation", logMode);
        page.navigate("https://qamatters.github.io/demoautomationWebSite/WorkFlows/insurance/login.html");
        Logger.formattedLog("Demo app title: " + page.title(),this.logMode);
        ReportUtil.logInfo("Title of the App is :" + page.title());
        ReportUtil.verifyTitle("Login - Insurance Simulation", "Validate title");
        loginPage.launchUserLoginPage();
        loginPage.loginToInsurancePortal("user1", "user123");
        loginPage.validateUserLogin();
        claims.getExistingClaims();
    }
}
