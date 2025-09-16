package tests.UI.tests.playground.bankingWorkFlow;

import base.BaseUITest;
import listeners.ReportUtil;
import org.testng.annotations.Test;
import tests.UI.pages.AutomationPlayground.BankingWorkFlow.Dashboard;
import tests.UI.pages.AutomationPlayground.BankingWorkFlow.FundTransferPage;
import tests.UI.pages.AutomationPlayground.BankingWorkFlow.LoginPage;
import tests.UI.pages.AutomationPlayground.BankingWorkFlow.PayeePage;
import utils.Logger;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import static tests.UI.pages.AutomationPlayground.BankingWorkFlow.constants.PayeeConstants.PAYEE_NAME;

public class PayeePageValidation extends BaseUITest {

    @Test
    public void validatePayeeTable() {
        LoginPage loginToBankApplication = new LoginPage(page);
        Dashboard dashboard = new Dashboard(page);
        PayeePage payeePage = new PayeePage(page);
        Logger.formattedLog("Testing T004_PayeePageValidation", logMode);
        page.navigate("https://qamatters.github.io/demoautomationWebSite/WorkFlows/Banking/index.html");
        Logger.formattedLog("Bank Demo app title: " + page.title(),this.logMode);
        ReportUtil.logInfo("Title of the App is :" + page.title());
        ReportUtil.verifyTitle("Bank Login", "Validate title");
        loginToBankApplication.loginToBankApplication();
        dashboard.navigateToPayeePage();
        payeePage.validatePayeeTableData();

    }
    @Test
    public void validateAddPayeePage() {
        LoginPage loginToBankApplication = new LoginPage(page);
        PayeePage payeePage = new PayeePage(page);
        FundTransferPage fundTransferPage = new FundTransferPage(page);
        Dashboard dashboard = new Dashboard(page);
        Logger.formattedLog("Testing T004_PayeePageValidation", logMode);
        page.navigate("https://qamatters.github.io/demoautomationWebSite/WorkFlows/Banking/index.html");
        Logger.formattedLog("Bank Demo app title: " + page.title(),this.logMode);
        ReportUtil.logInfo("Title of the App is :" + page.title());
        ReportUtil.verifyTitle("Bank Login", "Validate title");
        loginToBankApplication.loginToBankApplication();
        dashboard.navigateToPayeePage();
        payeePage.addPayee();
        payeePage.clickFundTransfer();
        fundTransferPage.validateAddedPayee(PAYEE_NAME);
    }
    @Test (dependsOnMethods = "validateAddPayeePage")
    public void validateRemovePayee() {
        LoginPage loginToBankApplication = new LoginPage(page);
        PayeePage payeePage = new PayeePage(page);
        FundTransferPage fundTransferPage = new FundTransferPage(page);
        Dashboard dashboard = new Dashboard(page);
        Logger.formattedLog("Testing T004_PayeePageValidation", logMode);
        page.navigate("https://qamatters.github.io/demoautomationWebSite/WorkFlows/Banking/index.html");
        Logger.formattedLog("Bank Demo app title: " + page.title(),this.logMode);
        ReportUtil.logInfo("Title of the App is :" + page.title());
        ReportUtil.verifyTitle("Bank Login", "Validate title");
        loginToBankApplication.loginToBankApplication();
        dashboard.navigateToPayeePage();
        payeePage.removePayee();
    }


}
