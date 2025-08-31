package tests.UI.pages.AutomationPlayground.BankingWorkFlow;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import listeners.ReportUtil;

public class Dashboard extends BasePage {
    private final Locator userName;
    private final Locator fundTransferButton;
    private final Locator viewTransactionHistory;

    public Dashboard(Page page) {
        super(page);
        this.userName = page.locator("#user");
        this.fundTransferButton = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Transfer Funds"));
        this.viewTransactionHistory = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View Transaction History"));
    }

    public String getUserName() {
        return userName.textContent();
    }

    public void validateOperations() {
        ReportUtil.verifyText(fundTransferButton.textContent().trim(), "Transfer Funds", "Validate Transfer Funds button text on page");
        ReportUtil.verifyText(viewTransactionHistory.textContent().trim(), "View Transaction History", "Validate view transaction history Button Text");

    }

}
