package tests.UI.pages.AutomationPlayground.insurance;

import base.BasePage;
import base.fields.TableHelper;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import listeners.ReportUtil;

import java.util.List;

public class Claims extends BasePage {
    private final Locator myClaimsTitle;
    private final Locator tableLocator;

    public Claims(Page page) {
        super(page);
        this.myClaimsTitle = page.getByText("My Claims");
        this.tableLocator = page.locator("//table[contains(@class,'min-w-full')]");

    }

    public void getExistingClaims() {
        ReportUtil.assertTrue(myClaimsTitle.isVisible(), "My claims text is present");
        int totalClaims = TableHelper.getRowCount(tableLocator);
        ReportUtil.logInfo("Total claims :" + (totalClaims-1)); // Remove header row
        List<String> allClaims = TableHelper.getColumnValues(tableLocator, 0);
        allClaims.remove(0); //Removing header row
        ReportUtil.logInfo("All claims :" + allClaims);
    }
}
