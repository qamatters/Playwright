package base.fields;

import com.microsoft.playwright.Locator;

public class TextFieldHelper {

    // Methods for interacting with text fields can be added here

    public static void enterText(Locator textField, String text) {
        textField.fill(text);
    }

    public static void enterTextWithPressSequentially(Locator textField, String text) {
        textField.pressSequentially(text);
    }

    public static String getText(Locator textField) {
        return textField.inputValue();
    }

    public static void clearText(Locator textField) {
        textField.fill("");
    }

    public static void clearAndEnterText(Locator textField, String text) {
        textField.fill(text);
    }

    public static void appendText(Locator textField, String text) {
        String currentValue = textField.inputValue();
        textField.fill(currentValue + text);
    }

    public static boolean isTextFieldEnabled(Locator textField) {
        return textField.isEnabled();
    }

    public static boolean isTextFieldVisible(Locator textField) {
        return textField.isVisible();
    }

    public static String getTextFieldAttribute(Locator textField, String attribute) {
        return textField.getAttribute(attribute);
    }

    public static String getTextFieldPlaceholder(Locator textField) {
        return textField.getAttribute("placeholder");
    }

    public static String getTextFieldType(Locator textField) {
        return textField.getAttribute("type");
    }

    public static String fillAndGetText(Locator textField, String text) {
        textField.fill(text);
        return textField.inputValue();
    }

    public static String getTextFieldValue(Locator textField) {
        return textField.inputValue();
    }

    public static String getTextFieldName(Locator textField) {
        return textField.getAttribute("name");
    }

    public static void bringTextFieldIntoView(Locator textField) {
        textField.scrollIntoViewIfNeeded();
    }

    public static void focusTextField(Locator textField) {
        textField.focus();
    }

    public static boolean isTextFieldReadOnly(Locator textField) {
        String readOnlyAttr = textField.getAttribute("readonly");
        return readOnlyAttr != null;
    }

    public static boolean isTextFieldRequired(Locator textField) {
        String requiredAttr = textField.getAttribute("required");
        return requiredAttr != null;
    }

}
