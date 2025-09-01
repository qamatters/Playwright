package tests.UI.pages.AutomationPlayground.BankingWorkFlow;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import listeners.ReportUtil;

public class PayeePage extends BasePage {
    private final Locator addPayeeName;
    private final Locator addPayeeBank;
    private final Locator addPayeeBaranch;
    private final Locator payeeAccountNumber;
    private final Locator addPayeeButton;
    private final Locator fundTransferLink;
    private final Locator addedPayeeName;
    private final Locator addedPayeeBankName;
    private final Locator addedPayeeAccountNumber;
    private final Locator addedPayeeBranchName;


    public PayeePage(Page page) {
        super(page);
        this.addPayeeName = page.locator("#addName");
        this.addPayeeBank = page.locator("#addBank");
        this.addPayeeBaranch = page.locator("#addBranch");
        this.payeeAccountNumber = page.locator("#addAccount");
        this.addPayeeButton = page.locator("#addPayeeBtn");
        this.fundTransferLink = page.locator("//a[contains(@href,'transfer')]");
        this.addedPayeeName = page.getByRole(AriaRole.CELL, new Page.GetByRoleOptions().setName("QA Matters"));
        this.addedPayeeBankName = page.getByRole(AriaRole.CELL, new Page.GetByRoleOptions().setName("Union Bank"));
        this.addedPayeeAccountNumber = page.getByRole(AriaRole.CELL, new Page.GetByRoleOptions().setName("UNIB00045320"));
        this.addedPayeeBranchName = page.getByRole(AriaRole.CELL, new Page.GetByRoleOptions().setName("DLF Cyber Hub"));
    }

    public void addPayee() {
        addPayeeName.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        ReportUtil.verifyTrue(addPayeeName.isVisible(), "Add payee input field is available on the page");
        addPayeeName.fill("QA Matters");
        addPayeeBank.fill("Union Bank");
        addPayeeBaranch.fill("DLF Cyber Hub");
        payeeAccountNumber.fill("UNIB00045320");
        addPayeeButton.click();
        ReportUtil.logInfo("Payee information is entered and Add Payee is clicked");
        addedPayeeName.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        ReportUtil.verifyTrue(addedPayeeName.isVisible(), "Added payee name is available in payee table");
        ReportUtil.verifyTrue(addedPayeeBankName.isVisible(), "Added payee bank name is available in payee table");
        ReportUtil.verifyTrue(addedPayeeAccountNumber.isVisible(), "Added payee account number is available in payee table");
        ReportUtil.verifyTrue(addedPayeeBranchName.isVisible(), "Added payee branch name is available in payee table");
    }

    public void clickFundTransfer() {
        ReportUtil.verifyTrue(fundTransferLink.isVisible(), "Fund Transfer link is visible");
        fundTransferLink.click();
    }

}
