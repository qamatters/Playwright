package tests.UI.pages.AutomationPlayground.BankingWorkFlow;

import base.BasePage;
import base.fields.DropDownHelper;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import freemarker.template.utility.StringUtil;
import listeners.ReportUtil;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FundTransferPage extends BasePage {
    DropDownHelper dropDownHelper = new DropDownHelper(page);
    protected Locator payeeTransferList;

    public FundTransferPage(Page page) {
        super(page);
        this.payeeTransferList = page.locator("#payeeSelect");
    }

    public List<String> getAllPayeeNames() {
        List<String> payeeNamesWithBankNames = dropDownHelper.getAllOptions(payeeTransferList);
        List<String> payeeNames = new LinkedList<>();
        for (String payee : payeeNamesWithBankNames) {
            payeeNames.add(StringUtils.substringBefore(payee.trim(), "("));
        }
        return payeeNames.stream().map(String::trim).collect(Collectors.toList());
    }

    public void validateAddedPayee(String addedPayee) {
        List<String> payeeNames = getAllPayeeNames();
        ReportUtil.logInfo("All payee names are :" + payeeNames);
        ReportUtil.assertTrue(payeeNames.contains(addedPayee), "Payee Name " + addedPayee + " is available in the list for fund transfer available payees");
    }
}
