package tests.UI.pages.AutomationPlayground.BankingWorkFlow;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import listeners.ReportUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static base.fields.DropDownHelper.getAllOptionsFastImplementation;

public class FundTransferPage extends BasePage {
    protected Locator payeeTransferList;

    public FundTransferPage(Page page) {
        super(page);
        this.payeeTransferList = page.locator("#payeeSelect");
    }

    public List<String> getAllPayeeNames() {
        List<String> payeeNamesWithBankNames = getAllOptionsFastImplementation(payeeTransferList);
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
