package tests.UI.tests.playground.bankingWorkFlow;

import base.BaseUITest;
import org.testng.annotations.Test;
import tests.UI.pages.AutomationPlayground.BankingWorkFlow.Dashboard;
import tests.UI.pages.AutomationPlayground.BankingWorkFlow.LoginPage;
import utils.Logger;

public class DashboardValidation extends BaseUITest {
    @Test
    public void validateDashbOardpage() {
        LoginPage loginToBankApplication = new LoginPage(page);
        Dashboard dashboard= new Dashboard(page);
        Logger.formattedLog("Testing T002_JPH_UI_Test.checkPageTitle", logMode);
        page.navigate("https://qamatters.github.io/demoautomationWebSite/WorkFlows/Banking/index.html");
        loginToBankApplication.loginToBankApplication();
        dashboard.validateOperations();
        dashboard.dashboardPageValidations();
    }
}
