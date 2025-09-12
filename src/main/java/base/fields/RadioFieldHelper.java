package base.fields;

import com.microsoft.playwright.Locator;

public class RadioFieldHelper {

    public static void selectRadioOption(Locator radioOptions, String optionText) {
        for (Locator option : radioOptions.all()) {
            if (option.textContent().trim().equalsIgnoreCase(optionText)) {
                option.click();
                break;
            }
        }
    }

    public static void verifyRadioOptionSelected(Locator radioOptions, String expectedOptionText) {
        for (Locator option : radioOptions.all()) {
            String ariaChecked = option.getAttribute("aria-checked");
            if ("true".equals(ariaChecked)) {
                String selectedText = option.textContent().trim();
                if (!selectedText.equalsIgnoreCase(expectedOptionText)) {
                    throw new AssertionError("Expected selected option: " + expectedOptionText + ", but found: " + selectedText);
                }
                return; // Found the selected option and it matches
            }
        }
        throw new AssertionError("No option is selected.");
    }

    public static boolean isRadioOptionSelected(Locator radioOptions, String expectedOptionText) {
        for (Locator option : radioOptions.all()) {
            String ariaChecked = option.getAttribute("aria-checked");
            if ("true".equals(ariaChecked)) {
                String selectedText = option.textContent().trim();
                return selectedText.equalsIgnoreCase(expectedOptionText);
            }
        }
        return false; // No option is selected
    }

    public static void deselectRadioOption(Locator radioOptions) {
        for (Locator option : radioOptions.all()) {
            String ariaChecked = option.getAttribute("aria-checked");
            if ("true".equals(ariaChecked)) {
                option.click(); // Click to deselect
                break;
            }
        }
    }

}
