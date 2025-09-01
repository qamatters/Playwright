package tests.UI.pages.AutomationPlayground.BankingWorkFlow;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import listeners.ReportUtil;
import tests.utils.PageElementTextUtility;

public class Dashboard extends BasePage {
    private final Locator userName;
    private final Locator fundTransferButton;
    private final Locator viewTransactionHistory;
    private final Locator currentBalanceText;
    private final Locator balanceAmount;
    private final Locator balanceChart;

    public Dashboard(Page page) {
        super(page);
        this.userName = page.locator("#user");
        this.fundTransferButton = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Transfer Funds"));
        this.viewTransactionHistory = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View Transaction History"));
        this.currentBalanceText = page.getByText("Your Current Balance");
        this.balanceAmount = page.locator("#balance");
        this.balanceChart = page.locator("#balanceChart");
    }

    public String getUserName() {
        return userName.textContent();
    }

    public void validateOperations() {
        ReportUtil.verifyText(fundTransferButton.textContent().trim(), "Transfer Funds", "Validate Transfer Funds button text on page");
        ReportUtil.verifyText(viewTransactionHistory.textContent().trim(), "View Transaction History", "Validate view transaction history Button Text");
    }
    
    public void dashboardPageValidations() {
        // Regex for values like "$5680", "$100", "$99999"
        String regex = "^\\$\\d+$";
        ReportUtil.verifyTrue(currentBalanceText.isVisible(), "Current balance Text is present on the page");
        PageElementTextUtility.waitUntilStableText(balanceAmount,5000,1000,regex);
        ReportUtil.verifyText(balanceAmount.textContent(), "$5680", "Balance amount is " + balanceAmount.textContent());
        ReportUtil.verifyTrue(balanceChart.isVisible(), "Balance Chart is present on the page");
        
    }

}
