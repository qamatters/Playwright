package tests.UI.tests.playground.fields;

import base.BaseUITest;
import base.fields.DropDownHelper;
import listeners.ReportUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import tests.UI.pages.AutomationPlayground.fields.DropDownPage;

import java.util.List;

public class Test_DropDownBehviours extends BaseUITest {
    private DropDownPage dropDownPage;
    private DropDownHelper dropDownHelper;
    private final String URL = "https://qamatters.github.io/demoautomationWebSite/Fields/dropdown.html";


    @BeforeClass
    public void setup() {
        page.navigate(URL);
        dropDownPage = new DropDownPage(page);
        dropDownHelper = new DropDownHelper(page);
    }

    @Test
    public void testBasicDropdown() {
        dropDownHelper.selectByValue(dropDownPage.basicDropdown, "2");
        String selected = dropDownHelper.getSelectedValue(dropDownPage.basicDropdown);
        ReportUtil.logInfo("Basic Dropdown selected: " + selected);
        ReportUtil.assertEquals(selected, "2", "Basic Dropdown selection validation");
    }

    @Test
    public void testMultiSelectDropdown() {
        dropDownHelper.selectMultipleByValues(dropDownPage.multiSelectDropdown, List.of("apple", "cherry"));
        List<String> selected = dropDownHelper.getSelectedValues(dropDownPage.multiSelectDropdown);
        ReportUtil.logInfo("MultiSelect Dropdown selected: " + selected);
        ReportUtil.assertTrue(selected.containsAll(List.of("apple", "cherry")), "MultiSelect Dropdown validation");
    }

    @Test
    public void testDisabledDropdown() {

        boolean isDisabled = dropDownHelper.isDisabled(dropDownPage.disabledDropdown);
        ReportUtil.logInfo("Disabled Dropdown state: " + isDisabled);
        ReportUtil.assertTrue(isDisabled, "Disabled dropdown should be disabled");
    }

    @Test
    public void testDropdownWithDisabledOption() {
        dropDownHelper.selectByValue(dropDownPage.dropdownWithDisabledOption, "1");
        String selected = dropDownHelper.getSelectedValue(dropDownPage.dropdownWithDisabledOption);
        ReportUtil.logInfo("Dropdown with Disabled Option selected: " + selected);
        ReportUtil.assertEquals(selected, "1", "Dropdown with disabled option validation");
    }

    @Test
    public void testGroupedDropdown() {
        dropDownHelper.selectByValue(dropDownPage.groupedDropdown, "carrot");
        String selected = dropDownHelper.getSelectedValue(dropDownPage.groupedDropdown);
        ReportUtil.logInfo("Grouped Dropdown selected: " + selected);
        ReportUtil.assertEquals(selected, "carrot", "Grouped Dropdown validation");
    }

    @Test
    public void testDynamicDropdown() {
        dropDownHelper.typeAndSelectDynamic(dropDownPage.dynamicDropdownInput, dropDownPage.dynamicSuggestionsList, "Banana");
        String value = dropDownPage.dynamicDropdownInput.inputValue();
        ReportUtil.logInfo("Dynamic Dropdown input value: " + value);
        ReportUtil.assertEquals(value, "Banana", "Dynamic dropdown validation");
    }

    @Test
    public void testEventDropdown() {
        dropDownHelper.selectByValue(dropDownPage.eventDropdown, "2");
        String selected = dropDownHelper.getSelectedValue(dropDownPage.eventDropdown);
        ReportUtil.logInfo("Event Dropdown selected: " + selected);
        ReportUtil.assertEquals(selected, "2", "Event dropdown selection validation");
    }

    @Test
    public void testLargeDatasetDropdown() {
        dropDownHelper.selectByValue(dropDownPage.largeDatasetDropdown, "opt25");
        String selected = dropDownHelper.getSelectedValue(dropDownPage.largeDatasetDropdown);
        ReportUtil.logInfo("Large Dataset Dropdown selected: " + selected);
        ReportUtil.assertEquals(selected, "opt25", "Large dataset dropdown validation");
    }

    @Test
    public void testSpecialCharDropdown() {
        dropDownHelper.selectByValue(dropDownPage.specialCharDropdown, "heart");
        String selected = dropDownHelper.getSelectedValue(dropDownPage.specialCharDropdown);
        ReportUtil.logInfo("Special Char Dropdown selected: " + selected);
        ReportUtil.assertEquals(selected, "heart", "Special character dropdown validation");
    }

    @Test
    public void testEmptyDropdown() {
        int optionsCount = dropDownHelper.getOptionsCount(dropDownPage.emptyDropdown);
        ReportUtil.logInfo("Empty Dropdown options count: " + optionsCount);
        ReportUtil.assertEquals(optionsCount, 0, "Empty dropdown validation");
    }

    @Test
    public void testReadonlyDropdown() {
        boolean isDisabled = dropDownHelper.isDisabled(dropDownPage.readonlyDropdown);
        ReportUtil.logInfo("Readonly Dropdown state: " + isDisabled);
        ReportUtil.assertTrue(isDisabled, "Readonly dropdown should be disabled");
    }

    @Test
    public void testCustomSearchableDropdown() {
        dropDownHelper.typeAndSelectDynamic(dropDownPage.customSearchInput, dropDownPage.customOptionsList, "Kiwi");
        String value = dropDownPage.customSearchInput.inputValue();
        ReportUtil.logInfo("Custom Searchable Dropdown selected: " + value);
        ReportUtil.assertEquals(value, "Kiwi", "Custom searchable dropdown validation");
    }
}
