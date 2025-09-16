package tests.UI.tests.playground.bankingWorkFlow;

import base.BaseUITest;
import listeners.ReportUtil;
import org.testng.annotations.Test;
import tests.UI.pages.AutomationPlayground.BankingWorkFlow.Dashboard;
import tests.UI.pages.AutomationPlayground.BankingWorkFlow.LoginPage;
import utils.Logger;

public class LoginValidation extends BaseUITest {
    @Test
    public void validateLoginPage() {
        LoginPage loginToBankApplication = new LoginPage(page);
        Dashboard dashboard= new Dashboard(page);
        Logger.formattedLog("Testing JIRA_300_Login Page Validation", logMode);
        page.navigate("https://qamatters.github.io/demoautomationWebSite/WorkFlows/Banking/index.html");
        Logger.formattedLog("Bank Demo app title: " + page.title(),this.logMode);
        ReportUtil.logInfo("Title of the App is :" + page.title());
        ReportUtil.verifyTitle("Bank Login", "Validate title");
        loginToBankApplication.loginToBankApplication();
        String userName = dashboard.getUserName();
        ReportUtil.logInfo("Logged in User Name is " + userName);
        ReportUtil.verifyText(userName, "Deepak Mathpal","Validate logged in User Name");
        Logger.formattedLog("Bank Demo app title: " + userName,this.logMode);
        dashboard.validateOperations();
        dashboard.dashboardPageValidations();
    }
}
