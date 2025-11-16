package tests.UI.pages.AutomationPlayground.insurance;

import base.BasePage;
import base.fields.TableHelper;
import base.fields.WaitHelper;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import listeners.ReportUtil;

import java.nio.file.Paths;
import java.util.List;

import static base.fields.AlertHelper.handleSimpleAlert;
import static base.fields.TableHelper.getCellValueLocator;

public class Claims extends BasePage {
    private final Locator myClaimsTitle;
    private final Locator tableLocator;
    private final Locator policyNumber;
    private final Locator claimType;
    private final Locator claimAmount;

    private final Locator claimFile;
    private final Locator submitButton;
    private final Locator approveButton;
    private final Locator rejectButton;
    private final Locator settleButton;

    public Claims(Page page) {
        super(page);
        this.myClaimsTitle = page.getByText("My Claims");
        this.tableLocator = page.locator("//table[contains(@class,'min-w-full')]");
        this.policyNumber = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Policy Number"));
        this.claimType = page.locator("#claimType");
        this.claimAmount = page.getByPlaceholder("Claim Amount");
        this.claimFile = page.locator("#claimFile");
        this.submitButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        this.approveButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Approve"));
        this.rejectButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Reject"));
        this.settleButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Settle"));
    }

    public List<String> getExistingClaims() {
        ReportUtil.assertTrue(myClaimsTitle.isVisible(), "My claims text is present");
        int totalClaims = TableHelper.getRowCount(tableLocator);
        ReportUtil.logInfo("Total claims :" + (totalClaims - 1)); // Remove header row
        List<String> allClaims = TableHelper.getColumnValues(tableLocator, 0);
        allClaims.remove(0); //Removing header row
        ReportUtil.logInfo("All claims :" + allClaims);
        return allClaims;
    }

    public void submitNewClaim(String claim, String claimAmount) {
        int totalRowsInitially = TableHelper.getRowCount(tableLocator);
        ReportUtil.logInfo("Total Claims before Submitting a new one are: " + (totalRowsInitially - 1));
        policyNumber.click();
        policyNumber.fill(claim);
        claimType.selectOption("Vehicle");
        this.claimAmount.fill(claimAmount);
        claimFile.setInputFiles(Paths.get("src\\test\\resources\\SampleData\\TestFile.png"));
        handleSimpleAlert(page, submitButton);
        validateSubmittedClaim(totalRowsInitially,claim);
    }

    public void validateSubmittedClaim(int claimsInitially, String claim) {
        int totalRowsAfterSubmittingClaim = TableHelper.getRowCount(tableLocator);
        ReportUtil.logInfo("Total Claims after Submitting a new one are: " + (totalRowsAfterSubmittingClaim - 1));
        ReportUtil.assertEquals((totalRowsAfterSubmittingClaim - claimsInitially), 1, "A new entry has been added to table");
        ReportUtil.assertTrue(TableHelper.getColumnValues(tableLocator, 1).contains(claim), "Claim added successfully");
        List<String> allClaimsStatus = TableHelper.getColumnValues(tableLocator, 4);
        String lastValue = allClaimsStatus.stream()
                .reduce((first, second) -> second)
                .orElse(null);
        System.out.println("Last value: " + lastValue);
        ReportUtil.assertTrue(lastValue.contains("Review"), "Status is " + lastValue);
    }

    public void approveClaim(String policy) {
        int row = TableHelper.findRowIndexByCellValue(tableLocator, 1, policy);
        System.out.println("row is :" + row);
        Locator actionClassLocator = getCellValueLocator(tableLocator, row, 7);
        String actionCellValues = TableHelper.getCellValue(tableLocator, row, 7);
        System.out.println("Action Available are :" + actionCellValues);
        String statusValue = TableHelper.getCellValue(tableLocator, row, 4);
        System.out.println("Current status is :" + statusValue);
        if (actionCellValues.contains("Approve")) {
            Locator approveActionLocator = actionClassLocator.locator(approveButton);
            approveActionLocator.click();
        }
        WaitHelper.waitForSpecificTextToAppear(getCellValueLocator(tableLocator, row, 4), "Approved", 5000, "Claim status");
        String updatedStatus = TableHelper.getCellValue(tableLocator, row, 4);
        ReportUtil.assertTrue(updatedStatus.contains("Approved"), policy + " is  Approved now ");
    }

    public void rejectClaim(String policy) {
        int row = TableHelper.findRowIndexByCellValue(tableLocator, 1, policy);
        System.out.println("row is :" + row);
        Locator actionClassLocator = getCellValueLocator(tableLocator, row, 7);
        String actionCellValues = TableHelper.getCellValue(tableLocator, row, 7);
        System.out.println("Action Available are :" + actionCellValues);
        String statusValue = TableHelper.getCellValue(tableLocator, row, 4);
        System.out.println("Current status is :" + statusValue);
        if (actionCellValues.contains("Reject")) {
            Locator actionLocator = actionClassLocator.locator(rejectButton);
            actionLocator.click();
        }
        WaitHelper.waitForSpecificTextToAppear(getCellValueLocator(tableLocator, row, 4), "Rejected", 5000, "Claim status");
        String updatedStatus = TableHelper.getCellValue(tableLocator, row, 4);
        ReportUtil.assertEquals(updatedStatus,"Rejected", policy + " is Rejected now");
    }

    public void settleClaim(String policy) {
        int row = TableHelper.findRowIndexByCellValue(tableLocator, 1, policy);
        System.out.println("row is :" + row);
        Locator actionClassLocator = getCellValueLocator(tableLocator, row, 7);
        String actionCellValues = TableHelper.getCellValue(tableLocator, row, 7);
        System.out.println("Action Available are :" + actionCellValues);
        String statusValue = TableHelper.getCellValue(tableLocator, row, 4);
        System.out.println("Current status is :" + statusValue);
        if (actionCellValues.contains("Settle")) {
            Locator actionLocator = actionClassLocator.locator(settleButton);
            actionLocator.click();
        }
        WaitHelper.waitForSpecificTextToAppear(getCellValueLocator(tableLocator, row, 4), "Settled", 5000, "Claim status");
        String updatedStatus = TableHelper.getCellValue(tableLocator, row, 4);
        ReportUtil.assertEquals(updatedStatus,"Settled", policy + " is Settled now");
    }

}
