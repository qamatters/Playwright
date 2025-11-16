package tests.UI.tests.playground.insurance;

import base.BaseUITest;
import listeners.ReportUtil;
import org.testng.annotations.Test;
import tests.UI.pages.AutomationPlayground.insurance.Claims;
import tests.UI.pages.AutomationPlayground.insurance.InsuranceLoginPage;
import utils.Logger;

public class SettleClaimPageValidation extends BaseUITest {
    String claim = "P1004";
    @Test
    public void settleClaimFromAdmin() {
        Logger.formattedLog("Testing JIRA_303_Reject Claim Validation", logMode);
        page.navigate("https://qamatters.github.io/demoautomationWebSite/WorkFlows/insurance/login.html");
        ReportUtil.verifyTitle("Login - Insurance Simulation", "Validate title");
        InsuranceLoginPage loginPage = new InsuranceLoginPage(page);
        Claims claims = new Claims(page);
        loginPage.launchAdminLoginPage();
        loginPage.loginToInsurancePortal("admin", "admin123");
        loginPage.validateAdminLogin();
        claims.settleClaim(claim);
    }
}
