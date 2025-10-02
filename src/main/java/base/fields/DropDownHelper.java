package base.fields;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DropDownHelper {

    private static final int DEFAULT_TIMEOUT = 2000;

    // ================= SINGLE SELECT =================
    public static void selectByValue(Locator dropdown, String value) {
        dropdown.selectOption(value);
    }

    public static void selectByLabel(Locator dropdown, String label) {
        dropdown.selectOption(new SelectOption().setLabel(label));
    }

    public static void selectByIndex(Locator dropdown, int index) {
        dropdown.selectOption(new SelectOption().setIndex(index));
    }

    // ================= MULTI SELECT =================
    public static void selectMultipleByValues(Locator dropdown, List<String> values) {
        dropdown.selectOption(values.toArray(new String[0]));
    }

    public static void selectMultipleByLabels(Locator dropdown, List<String> labels) {
        List<SelectOption> options = labels.stream()
                .map(label -> new SelectOption().setLabel(label))
                .collect(Collectors.toList());
        dropdown.selectOption(options.toArray(new SelectOption[0]));
    }

    public static void selectMultipleByIndices(Locator dropdown, List<Integer> indices) {
        List<SelectOption> options = indices.stream()
                .map(idx -> new SelectOption().setIndex(idx))
                .collect(Collectors.toList());
        dropdown.selectOption(options.toArray(new SelectOption[0]));
    }

    // ================= DYNAMIC / AJAX DROPDOWN =================
    public static void selectDynamicSuggestion(Page page, Locator inputField, String value, Locator suggestionList) {
        inputField.fill(value);
        page.waitForTimeout(DEFAULT_TIMEOUT); // small wait for suggestions
        Locator suggestion = suggestionList.locator("li", new Locator.LocatorOptions().setHasText(value));
        suggestion.first().click();
    }

    // ================= SEARCHABLE / CUSTOM DROPDOWN =================
    public static void searchAndSelect(Page page, Locator searchField, String value, Locator optionsList) {
        searchField.fill(value);
        page.waitForTimeout(DEFAULT_TIMEOUT);
        Locator option = optionsList.locator("li", new Locator.LocatorOptions().setHasText(value));
        option.first().click();
    }

    // ================= DISABLED CHECK =================
    public static boolean isDropdownDisabled(Locator dropdown) {
        return dropdown.isDisabled();
    }

    // ================= GET SELECTED =================
    public static String getSelectedValue(Locator dropdown) {
        return dropdown.inputValue(); // for <select> gets the value
    }

    @SuppressWarnings("unchecked")
    public static List<String> getSelectedValues(Locator dropdown) {
        return (List<String>) dropdown.evaluate("el => Array.from(el.selectedOptions).map(opt => opt.value)");
    }

    public static List<String> getSelectedText(Locator dropdown) {
        return (List<String>) dropdown.evaluate("el => Array.from(el.selectedOptions).map(opt => opt.textContent.trim())");
    }

    // ================= EVENT-TRIGGERED DROPDOWN =================
    public static void selectWithEvent(Page page, Locator dropdown, String value) {
        selectByValue(dropdown, value);
        page.waitForTimeout(DEFAULT_TIMEOUT); // let change event fire
    }

    // ===================== CHECK IF DROPDOWN IS DISABLED =====================
    public static boolean isDisabled(Locator dropdown) {
        String disabledAttr = dropdown.getAttribute("disabled");
        return disabledAttr != null && (disabledAttr.equals("true") || disabledAttr.equals(""));
    }

    // ===================== TYPE IN DYNAMIC DROPDOWN AND SELECT =====================
    public static void typeAndSelectDynamic(Page page, Locator inputField, Locator suggestionsList, String valueToSelect) {
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
    public static int getOptionsCount(Locator dropdown) {
        List<ElementHandle> options = dropdown.locator("option").elementHandles();
        return options.size();
    }

    private static List<ElementHandle> getAllOptionsElements(Locator dropdown) {
        return dropdown.locator("option").elementHandles();
    }

    public static List<String> getAllOptions(Locator dropDown) {
        List<String> choices = new LinkedList<>();
        List<ElementHandle> elements = getAllOptionsElements(dropDown);
        for (ElementHandle locator : elements) {
            choices.add(locator.innerText().trim());
        }
        return choices;
    }

    public static List<String> getAllOptionsFastImplementation(Locator dropDown) {
        return dropDown.locator("option").allInnerTexts().stream()
                .map(String::trim)
                .collect(Collectors.toList());
    }

}
