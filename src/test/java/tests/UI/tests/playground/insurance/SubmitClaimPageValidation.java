package tests.UI.tests.playground.insurance;

import base.BaseUITest;
import listeners.ReportUtil;
import org.testng.annotations.Test;
import tests.UI.pages.AutomationPlayground.insurance.Claims;
import tests.UI.pages.AutomationPlayground.insurance.InsuranceLoginPage;
import tests.utils.DateTimeUtility;
import utils.Logger;

public class SubmitClaimPageValidation extends BaseUITest {
    String claim = "P1005" + DateTimeUtility.getCurrentDateTimeInSpecificFormat();


    @Test
    public void validateClaimSubmissionFromUser() {
        InsuranceLoginPage loginPage = new InsuranceLoginPage(page);
        Claims claims = new Claims(page);
        String claimAmount = "10000";
        Logger.formattedLog("Testing JIRA_302_Login Page Validation", logMode);
        page.navigate("https://qamatters.github.io/demoautomationWebSite/WorkFlows/insurance/login.html");
        Logger.formattedLog("Demo app title: " + page.title(), this.logMode);
        ReportUtil.logInfo("Title of the App is :" + page.title());
        ReportUtil.verifyTitle("Login - Insurance Simulation", "Validate title");
        loginPage.launchUserLoginPage();
        loginPage.loginToInsurancePortal("user1", "user123");
        loginPage.validateUserLogin();
        claims.submitNewClaim(claim, claimAmount);
        loginPage.logOut();

    }

    @Test(dependsOnMethods = "validateClaimSubmissionFromUser")
    public void validateClaimsFromAdmin() {
        InsuranceLoginPage loginPage = new InsuranceLoginPage(page);
        Claims claims = new Claims(page);
        loginPage.launchAdminLoginPage();
        loginPage.loginToInsurancePortal("admin", "admin123");
        loginPage.validateAdminLogin();
        claims.approveClaim(claim);
    }
}
