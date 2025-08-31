package base.fields;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;

import java.util.List;
import java.util.stream.Collectors;

public class DropDownHelper {
    private Page page;
    private final int DEFAULT_TIMEOUT = 2000;

    public DropDownHelper(Page page) {
        this.page = page;
    }

    // ================= SINGLE SELECT =================
    public void selectByValue(Locator dropdown, String value) {
        dropdown.selectOption(value);
    }

    public void selectByLabel(Locator dropdown, String label) {
        dropdown.selectOption(new SelectOption().setLabel(label));
    }

    public void selectByIndex(Locator dropdown, int index) {
        dropdown.selectOption(new SelectOption().setIndex(index));
    }

    // ================= MULTI SELECT =================
    public void selectMultipleByValues(Locator dropdown, List<String> values) {
        dropdown.selectOption(values.toArray(new String[0]));
    }

    public void selectMultipleByLabels(Locator dropdown, List<String> labels) {
        List<SelectOption> options = labels.stream()
                .map(label -> new SelectOption().setLabel(label))
                .collect(Collectors.toList());
        dropdown.selectOption(options.toArray(new SelectOption[0]));
    }

    public void selectMultipleByIndices(Locator dropdown, List<Integer> indices) {
        List<SelectOption> options = indices.stream()
                .map(idx -> new SelectOption().setIndex(idx))
                .collect(Collectors.toList());
        dropdown.selectOption(options.toArray(new SelectOption[0]));
    }

    // ================= DYNAMIC / AJAX DROPDOWN =================
    public void selectDynamicSuggestion(Locator inputField, String value, Locator suggestionList) {
        inputField.fill(value);
        page.waitForTimeout(DEFAULT_TIMEOUT); // small wait for suggestions
        Locator suggestion = suggestionList.locator("li", new Locator.LocatorOptions().setHasText(value));
        suggestion.first().click();
    }

    // ================= SEARCHABLE / CUSTOM DROPDOWN =================
    public void searchAndSelect(Locator searchField, String value, Locator optionsList) {
        searchField.fill(value);
        page.waitForTimeout(DEFAULT_TIMEOUT);
        Locator option = optionsList.locator("li", new Locator.LocatorOptions().setHasText(value));
        option.first().click();
    }

    // ================= DISABLED CHECK =================
    public boolean isDropdownDisabled(Locator dropdown) {
        return dropdown.isDisabled();
    }

    // ================= GET SELECTED =================
    public String getSelectedValue(Locator dropdown) {
        return dropdown.inputValue(); // for <select> gets the value
    }

    @SuppressWarnings("unchecked")
    public List<String> getSelectedValues(Locator dropdown) {
        return (List<String>) dropdown.evaluate("el => Array.from(el.selectedOptions).map(opt => opt.value)");
    }


    // ================= EVENT-TRIGGERED DROPDOWN =================
    public void selectWithEvent(Locator dropdown, String value) {
        selectByValue(dropdown, value);
        page.waitForTimeout(DEFAULT_TIMEOUT); // let change event fire
    }

    // ===================== CHECK IF DROPDOWN IS DISABLED =====================
    public boolean isDisabled(Locator dropdown) {
        String disabledAttr = dropdown.getAttribute("disabled");
        return disabledAttr != null && (disabledAttr.equals("true") || disabledAttr.equals(""));
    }

    // ===================== TYPE IN DYNAMIC DROPDOWN AND SELECT =====================
    public void typeAndSelectDynamic(Locator inputField, Locator suggestionsList, String valueToSelect) {
        inputField.fill(valueToSelect); // Type in input
        page.waitForTimeout(500); // small wait for suggestions to appear

        List<ElementHandle> options = suggestionsList.elementHandles();
        for (ElementHandle option : options) {
            if (option.innerText().equalsIgnoreCase(valueToSelect)) {
                option.click();
                break;
            }
        }
    }

    // ===================== GET OPTIONS COUNT =====================
    public int getOptionsCount(Locator dropdown) {
        List<ElementHandle> options = dropdown.locator("option").elementHandles();
        return options.size();
    }

}
