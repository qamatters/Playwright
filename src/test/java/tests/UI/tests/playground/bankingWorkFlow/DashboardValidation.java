package tests.UI.tests.playground.bankingWorkFlow;

import base.BaseUITest;
import org.testng.annotations.Test;
import tests.UI.pages.AutomationPlayground.BankingWorkFlow.Dashboard;
import tests.UI.pages.AutomationPlayground.BankingWorkFlow.FundTransferPage;
import tests.UI.pages.AutomationPlayground.BankingWorkFlow.LoginPage;
import tests.UI.pages.AutomationPlayground.BankingWorkFlow.PayeePage;
import utils.Logger;

import static tests.UI.pages.AutomationPlayground.BankingWorkFlow.constants.PayeeConstants.PAYEE_NAME;

public class DashboardValidation extends BaseUITest {
    @Test
    public void validateDashbOardpage() {
        LoginPage loginToBankApplication = new LoginPage(page);
        Dashboard dashboard= new Dashboard(page);
        PayeePage payeePage = new PayeePage(page);
        FundTransferPage fundTransferPage = new FundTransferPage(page);
        Logger.formattedLog("Testing T002_JPH_UI_Test.checkPageTitle", logMode);
        page.navigate("https://qamatters.github.io/demoautomationWebSite/WorkFlows/Banking/index.html");
        loginToBankApplication.loginToBankApplication();
        dashboard.validateOperations();
        dashboard.dashboardPageValidations();
        dashboard.navigateToPayeePage();
        payeePage.validatePayeeTableData();
        payeePage.addPayee();
        payeePage.clickFundTransfer();
        fundTransferPage.validateAddedPayee(PAYEE_NAME);
        page.waitForTimeout(2000*60);
        dashboard.navigateToPayeePage();
        payeePage.removePayee();
    }
}