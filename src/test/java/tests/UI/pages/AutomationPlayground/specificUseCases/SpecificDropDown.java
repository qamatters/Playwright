package tests.UI.pages.AutomationPlayground.specificUseCases;
import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import listeners.ReportUtil;

import java.util.List;

public class SpecificDropDown extends BasePage {

    private Locator filterBar;
    private Locator seriesFilterButton;
    private Locator seriesFiledOptions;
    private Locator themeFilterButton;
    private Locator seriesAccordion;

    private Locator removeSelectedOption;

    public SpecificDropDown(Page page) {
        super(page);
        this.filterBar = page.locator("div.flex.flex-wrap.gap-4");
        this.seriesFilterButton = page.locator("//button[contains(.,'Series')]");
        this.seriesAccordion= page.locator("(//button[contains(.,'Series')]//following::div[contains(@class,'accordion')])[1]");
        this.seriesFiledOptions = page.locator("//button[contains(.,'Series')]//following-sibling::div/ul/li//span");
        this.themeFilterButton = page.locator("//button[contains(.,'Theme')]");
        this.removeSelectedOption = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("✕"));
    }

    private List<String> getAllOptionsFromSeriesFilter() {
        seriesFilterButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        List<String> filterOptions = seriesFiledOptions.allInnerTexts();
        for(String option: filterOptions) {
            ReportUtil.logInfo(" Options present inside series filter : " + option);
        }
        return filterOptions;
    }

    public void selectOption(String option) {
        List<String> allOptions = getAllOptionsFromSeriesFilter();
        if(allOptions.contains(option)) {
            ReportUtil.logPass(option + "  is present inside the series filter dropdown");
            seriesFilterButton.click(new Locator.ClickOptions().setForce(true));
            seriesAccordion.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            waitForVisible(seriesAccordion);
            selectSeriesOption(option);
        }
    }

    private void selectSeriesOption(String optionText) {
        Locator optionCheckbox = seriesFiledOptions
                .filter(new Locator.FilterOptions().setHasText(optionText));
        ReportUtil.logInfo("Selecting option :" + optionText);
        optionCheckbox.check();
        page.waitForTimeout(1000);// to see the changes in UI
        ReportUtil.assertTrue(optionCheckbox.isChecked(), optionText + " is selected");
        Locator selectedOption= page.getByText(optionText+"✕");
        ReportUtil.assertTrue(selectedOption.isVisible(), "Selected option is visible");
        ReportUtil.assertTrue(removeSelectedOption.isVisible(), "Remove button as a cross icon is present");
        page.waitForTimeout(3000);// to see the changes in UI
        removeSelectedOption.click();
        page.waitForTimeout(1000);// to see the changes in UI
        ReportUtil.assertTrue(!optionCheckbox.isChecked(), optionText + " is not selected");
        seriesFilterButton.click(new Locator.ClickOptions().setForce(true));
        page.waitForTimeout(1000);// to see the changes in UI
        seriesAccordion.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }


}
