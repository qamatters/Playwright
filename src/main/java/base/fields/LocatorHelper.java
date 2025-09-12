package base.fields;

import com.microsoft.playwright.Locator;

public class LocatorHelper {

    public static String getLocatorInfo(Locator locator) {
        return locator.toString();
    }

    public static String getLocatorAttribute(Locator locator, String attribute) {
        return locator.getAttribute(attribute);
    }

    public static String getLocatorText(Locator locator) {
        return locator.textContent();
    }

    public static String getLocatorValue(Locator locator) {
        return locator.inputValue();
    }

    public static String getLocatorTagName(Locator locator) {
        return locator.evaluate("el => el.tagName").toString();
    }

    public static String getLocatorId(Locator locator) {
        return locator.getAttribute("id");
    }

    public static boolean isDisplayed(Locator locator) {
        return locator.isVisible();
    }

    public static boolean isEnabled(Locator locator) {
        return locator.isEnabled();
    }

    public boolean isTextPresent(Locator locator, String text) {
        return locator.textContent().contains(text);
    }



}
