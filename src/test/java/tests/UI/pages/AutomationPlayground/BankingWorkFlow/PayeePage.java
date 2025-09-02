package tests.UI.pages.AutomationPlayground.BankingWorkFlow;

import base.BasePage;
import base.fields.AlertHelper;
import base.fields.DropDownHelper;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import listeners.ReportUtil;

import java.util.List;
import java.util.stream.Collectors;

import static tests.UI.pages.AutomationPlayground.BankingWorkFlow.constants.PayeeConstants.*;

public class PayeePage extends BasePage {
    DropDownHelper dropDownHelper = new DropDownHelper(page);
    AlertHelper alertHelper = new AlertHelper(page);
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
    private final Locator removePayeeDropDown;
    private final Locator removePayeeButton;


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
        this.removePayeeDropDown = page.locator("#removePayeeSelect");
        this.removePayeeButton = page.locator("#removePayeeBtn");
    }

    public void addPayee() {
        addPayeeName.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        ReportUtil.verifyTrue(addPayeeName.isVisible(), "Add payee input field is available on the page");
        addPayeeName.fill(PAYEE_NAME);
        addPayeeBank.fill(PAYEE_BANK_NAME);
        addPayeeBaranch.fill(PAYEE_BRANCH_NAME);
        payeeAccountNumber.fill(PAYEE_ACCOUNT_NUMBER);
        addPayeeButton.click();
        ReportUtil.logInfo("Payee information is entered and Add Payee is clicked");
        addedPayeeName.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        ReportUtil.verifyTrue(addedPayeeName.isVisible(), "Added payee name is available in payee table");
        ReportUtil.verifyTrue(addedPayeeBankName.isVisible(), "Added payee bank name is available in payee table");
        ReportUtil.verifyTrue(addedPayeeAccountNumber.isVisible(), "Added payee account number is available in payee table");
        ReportUtil.verifyTrue(addedPayeeBranchName.isVisible(), "Added payee branch name is available in payee table");
        validateRemoveButtonPresenceInPayeeTable(PAYEE_NAME);
    }

    public void clickFundTransfer() {
        ReportUtil.verifyTrue(fundTransferLink.isVisible(), "Fund Transfer link is visible");
        fundTransferLink.click();
    }

    public void getPayeeFromRemovePayeeList() {
        removePayeeDropDown.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        ReportUtil.verifyTrue(removePayeeDropDown.isVisible(), "Remove payee drop down field is available on the page");
        List<String> allPayees = dropDownHelper.getAllOptions(removePayeeDropDown);
        List<String> allPayeeWithoutExtraSpaces = allPayees.stream().map(String::trim).collect(Collectors.toList());
        ReportUtil.logInfo("All payee in remove payee list :" + allPayeeWithoutExtraSpaces);
        ReportUtil.assertTrue(allPayeeWithoutExtraSpaces.contains(PAYEE_NAME), "Payee is available in the remove payee list");
    }

    public void removePayee() {
        getPayeeFromRemovePayeeList();
        dropDownHelper.selectByValue(removePayeeDropDown, PAYEE_NAME);
        List<String> payees = dropDownHelper.getSelectedText(removePayeeDropDown);
        ReportUtil.assertEquals(payees.get(0), PAYEE_NAME, PAYEE_NAME + " is selected for removal");
        removePayeeButton.click();
    }

    public void validateRemoveButtonPresenceInPayeeTable(String payeeName) {
        Locator payeeNameInTable = page.locator("//tbody[@id='payeesTableBody']//td[contains(.,'"+payeeName+"')]//following-sibling::td//button[contains(text(),'Remove')]");
        ReportUtil.assertTrue(payeeNameInTable.isVisible(), payeeName + " is available in Payee Table");
    }

}
