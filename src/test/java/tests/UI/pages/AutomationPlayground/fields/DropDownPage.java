package tests.UI.pages.AutomationPlayground.fields;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class DropDownPage extends BasePage {

    public Locator basicDropdown;
    public Locator multiSelectDropdown;
    public Locator disabledDropdown;
    public Locator dropdownWithDisabledOption;
    public Locator groupedDropdown;
    public Locator dynamicDropdownInput;
    public Locator dynamicSuggestionsList;
    public Locator eventDropdown;
    public Locator largeDatasetDropdown;
    public Locator specialCharDropdown;
    public Locator emptyDropdown;
    public Locator readonlyDropdown;
    public Locator customSearchInput;
    public Locator customOptionsList;

    public DropDownPage(Page page) {
        super(page);

        this.basicDropdown = page.locator("#basicDropdown");
        this.multiSelectDropdown = page.locator("#multiSelectDropdown");
        this.disabledDropdown = page.locator("(//select[contains(@class,'cursor-not-allowed')])[1]");
        this.dropdownWithDisabledOption = page.locator("//label[text()='Dropdown with Disabled Option']/following-sibling::select");
        this.groupedDropdown = page.locator("//label[text()='Grouped Dropdown']/following-sibling::select");
        this.dynamicDropdownInput = page.locator("#dynamicDropdown");
        this.dynamicSuggestionsList = page.locator("#dynamicSuggestions");
        this.eventDropdown = page.locator("#eventDropdown");
        this.largeDatasetDropdown = page.locator("#largeDatasetDropdown");
        this.specialCharDropdown = page.locator("//label[text()='Special Characters / Unicode']/following-sibling::select");
        this.emptyDropdown = page.locator("//label[text()='Empty Dropdown']/following-sibling::select");
        this.readonlyDropdown = page.locator("//label[text()='Readonly / Locked Dropdown']/following-sibling::select");
        this.customSearchInput = page.locator("#customSearch");
        this.customOptionsList = page.locator("#customOptions");
    }
}
