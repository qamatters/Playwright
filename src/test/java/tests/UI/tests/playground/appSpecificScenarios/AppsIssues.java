package tests.UI.tests.playground.appSpecificScenarios;

import base.BaseUITest;
import base.fields.DropDownHelper;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLOutput;
import java.util.LinkedList;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AppsIssues extends BaseUITest {

    @BeforeMethod
    public void launchPage() {
        page.navigate("https://www.mfs.com/en-us/investment-professional/product-strategies/mutual-funds.html?tabname=performance");
    }

    @Test
    public void Test_DropDownIssue() {
        page.waitForTimeout(2000);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("CONTINUE")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Accept Do not save my")).click();
        assertThat(page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName("share class")).locator("span")).isVisible();
//        page.getByRole(AriaRole.COMBOBOX, new Page.GetByRoleOptions().setName("share class")).click();
        Locator selectDropDown = page.locator("#share-dropdown");
        System.out.println("1: " +selectDropDown.isVisible());
        System.out.println("2: " + selectDropDown.isHidden());
        System.out.println("3: " +selectDropDown.isEditable());
        System.out.println("4: " +selectDropDown.isEnabled());
        int options = DropDownHelper.getOptionsCount(selectDropDown);
        List<String> allOptions = DropDownHelper.getAllOptions(selectDropDown);
        System.out.println("All options are :" + allOptions);
        System.out.println(" size is :" + options);
    }

    @Test
    public void Test_NavigationHeadersItemsValidation() {
        Locator headersOnPage = page.locator("div.navHeader__right> ul>li");
        List<String> headersName = headersOnPage.allInnerTexts();
        for (String header: headersName) {
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("Header Name :" + header);
            Locator headers = page.locator("//li[contains(@class,'navLinks')]//a[contains(.,'"+header+"')]").first();
            headers.hover();
            List<Locator> allTitles = page.locator("(//a[contains(text(),'PRODUCTS')][1]//following::div)[1]//li[contains(@class,'heading-title') and contains(@class,'visible-md') and contains(@class,'visible-lg')]").all();
            List<String> allHeaderTitle = new LinkedList<>();
            for (Locator title : allTitles) {
                allHeaderTitle.add(title.textContent().trim());
            }
            for (String title : allHeaderTitle) {
                System.out.println("--------------------------------------------------------------------------------");
                System.out.println("Title :" + title);
                Locator investmentOptions = page.locator("(//li[contains(@title,'" + title + "')]//following-sibling::div[@class='sub-links-list with-heading'])[1]");
                List<Locator> allLinks = investmentOptions.locator("//li//a").all();
                int i = 1;
                for (Locator link : allLinks) {
                    System.out.println("------------------------------" + i + " option -----------------");
                    System.out.println("Option: " + link.textContent().trim());
                    System.out.println("link " + link.getAttribute("href"));
                    i = i + 1;
                }
            }
        }
    }
}
