package tests.UI.pages.demoPlayground.ui;

import base.BasePage;
import base.fields.TableHelper;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import listeners.ReportUtil;

import java.util.List;

public class HomePage extends BasePage {
    private final Locator enterName;
    private final Locator enterEmail;
    private final Locator addUserButton;
    private final Locator searchEmail;
    private final Locator table;

    public HomePage(Page page) {
        super(page);
        this.enterName = page.locator("#formName");
        this.enterEmail = page.locator("#formEmail");
        this.addUserButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add User"));
        this.searchEmail = page.locator("//span[contains(text(),'Search Email')]//following::input");
        this.table = page.locator("//table[@class='table table-striped table-bordered table-hover']");
    }

    public String createUser(String userName) {
        String email = userName + "@test.com";
        enterName.fill(userName);
        enterEmail.fill(email);
        addUserButton.click();
        ReportUtil.logInfo("User information is entered and login button is clicked");
        searchEmail.fill(email);
        return userName;
    }

    public void validateAddedUser(String userName){
        List<List<String>> allValues = TableHelper.getAllTableValues(table);
        boolean userFound = allValues.stream().anyMatch(row -> row.contains(userName));
        if (userFound) {
            ReportUtil.logPass("User " + userName + " is successfully added and verified in the table.");
        } else {
            ReportUtil.logFail("User " + userName + " is not found in the table.");
        }
    }
}
