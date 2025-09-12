package base.fields;

import com.microsoft.playwright.Locator;

public class checkBoxFieldHelper {

    public static void selectCheckboxOption(Locator checkboxOptions, String optionText) {
        for (Locator option : checkboxOptions.all()) {
            if (option.textContent().trim().equalsIgnoreCase(optionText)) {
                String ariaChecked = option.getAttribute("aria-checked");
                if (!"true".equals(ariaChecked)) {
                    option.click();
                }
                break;
            }
        }
    }

    public static void verifyCheckboxOptionSelected(Locator checkboxOptions, String expectedOptionText) {
        for (Locator option : checkboxOptions.all()) {
            if (option.textContent().trim().equalsIgnoreCase(expectedOptionText)) {
                String ariaChecked = option.getAttribute("aria-checked");
                if (!"true".equals(ariaChecked)) {
                    throw new AssertionError("Expected option to be selected: " + expectedOptionText + ", but it is not.");
                }
                return; // Found the option and it is selected
            }
        }
        throw new AssertionError("Option not found: " + expectedOptionText);
    }

    public static boolean isCheckboxOptionSelected(Locator checkboxOptions, String expectedOptionText) {
        for (Locator option : checkboxOptions.all()) {
            if (option.textContent().trim().equalsIgnoreCase(expectedOptionText)) {
                String ariaChecked = option.getAttribute("aria-checked");
                return "true".equals(ariaChecked);
            }
        }
        return false; // Option not found
    }

    public static void deselectCheckboxOption(Locator checkboxOptions, String optionText) {
        for (Locator option : checkboxOptions.all()) {
            if (option.textContent().trim().equalsIgnoreCase(optionText)) {
                String ariaChecked = option.getAttribute("aria-checked");
                if ("true".equals(ariaChecked)) {
                    option.click(); // Click to deselect
                }
                break;
            }
        }
    }


    public static void checkField(Locator checkBoxField) {
        if(!checkBoxField.isChecked()) {
            checkBoxField.check();
        }
    }

    public static void UnCheckField(Locator checkBoxField) {
        if(checkBoxField.isChecked()) {
            checkBoxField.uncheck();
        }
    }

    public static boolean isFieldChecked(Locator checkBoxField) {
        return checkBoxField.isChecked();
    }


}
