package base.fields;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;

import java.util.List;

public class IFrameHelper {

    private static final int DEFAULT_TIMEOUT = 30000;

    /**
     * Waits for an iframe to be attached and loaded
     */
    private static void waitForIframeToLoad(FrameLocator frameLocator, int timeoutMillis) {
        try {
            frameLocator.locator("body").waitFor(new Locator.WaitForOptions().setTimeout(timeoutMillis));
        } catch (TimeoutError e) {
            throw new RuntimeException("Iframe not loaded within " + timeoutMillis + "ms");
        }
    }

    /**
     * Navigates through a list of nested iframes and returns the deepest FrameLocator
     */
    public static FrameLocator getNestedIframeFrameLocator(Page page, List<String> iframeLocators, int timeoutMillis) {
        if (iframeLocators == null || iframeLocators.isEmpty()) {
            throw new IllegalArgumentException("Iframe locators list cannot be empty");
        }

        FrameLocator currentFrame = page.frameLocator(iframeLocators.get(0));
        waitForIframeToLoad(currentFrame, timeoutMillis);

        for (int i = 1; i < iframeLocators.size(); i++) {
            currentFrame = currentFrame.frameLocator(iframeLocators.get(i));
            waitForIframeToLoad(currentFrame, timeoutMillis);
        }

        return currentFrame;
    }

    /**
     * Returns a Locator for an element inside nested iframes
     */
    public static Locator elementInsideNestedIframes(Page page, List<String> iframeLocators, String elementSelector, int timeoutMillis) {
        FrameLocator deepestFrame = getNestedIframeFrameLocator(page, iframeLocators, timeoutMillis);
        deepestFrame.locator(elementSelector).waitFor(new Locator.WaitForOptions().setTimeout(timeoutMillis));
        return deepestFrame.locator(elementSelector);
    }

    // ---------------- Direct Actions ---------------- //

    // Click
    public static void click(Page page, List<String> iframeLocators, String elementSelector) {
        click(page, iframeLocators, elementSelector, DEFAULT_TIMEOUT);
    }

    public static void click(Page page, List<String> iframeLocators, String elementSelector, int timeoutMillis) {
        elementInsideNestedIframes(page, iframeLocators, elementSelector, timeoutMillis).click();
    }

    // Type / Fill
    public static void type(Page page, List<String> iframeLocators, String elementSelector, String text) {
        type(page, iframeLocators, elementSelector, text, DEFAULT_TIMEOUT);
    }

    public static void type(Page page, List<String> iframeLocators, String elementSelector, String text, int timeoutMillis) {
        elementInsideNestedIframes(page, iframeLocators, elementSelector, timeoutMillis).fill(text);
    }

    // Get text
    public static String getText(Page page, List<String> iframeLocators, String elementSelector) {
        return getText(page, iframeLocators, elementSelector, DEFAULT_TIMEOUT);
    }

    public static String getText(Page page, List<String> iframeLocators, String elementSelector, int timeoutMillis) {
        return elementInsideNestedIframes(page, iframeLocators, elementSelector, timeoutMillis).textContent();
    }

    // Check visibility
    public static boolean isVisible(Page page, List<String> iframeLocators, String elementSelector) {
        return isVisible(page, iframeLocators, elementSelector, DEFAULT_TIMEOUT);
    }

    public static boolean isVisible(Page page, List<String> iframeLocators, String elementSelector, int timeoutMillis) {
        return elementInsideNestedIframes(page, iframeLocators, elementSelector, timeoutMillis).isVisible();
    }

    // Check enabled
    public static boolean isEnabled(Page page, List<String> iframeLocators, String elementSelector) {
        return isEnabled(page, iframeLocators, elementSelector, DEFAULT_TIMEOUT);
    }

    public static boolean isEnabled(Page page, List<String> iframeLocators, String elementSelector, int timeoutMillis) {
        return elementInsideNestedIframes(page, iframeLocators, elementSelector, timeoutMillis).isEnabled();
    }

    // Hover
    public static void hover(Page page, List<String> iframeLocators, String elementSelector) {
        hover(page, iframeLocators, elementSelector, DEFAULT_TIMEOUT);
    }

    public static void hover(Page page, List<String> iframeLocators, String elementSelector, int timeoutMillis) {
        elementInsideNestedIframes(page, iframeLocators, elementSelector, timeoutMillis).hover();
    }

    // Double click
    public static void doubleClick(Page page, List<String> iframeLocators, String elementSelector) {
        doubleClick(page, iframeLocators, elementSelector, DEFAULT_TIMEOUT);
    }

    public static void doubleClick(Page page, List<String> iframeLocators, String elementSelector, int timeoutMillis) {
        elementInsideNestedIframes(page, iframeLocators, elementSelector, timeoutMillis).dblclick();
    }

    // Select option (for dropdowns)
    public static void selectOption(Page page, List<String> iframeLocators, String elementSelector, String value) {
        selectOption(page, iframeLocators, elementSelector, value, DEFAULT_TIMEOUT);
    }

    public static void selectOption(Page page, List<String> iframeLocators, String elementSelector, String value, int timeoutMillis) {
        elementInsideNestedIframes(page, iframeLocators, elementSelector, timeoutMillis).selectOption(value);
    }
}
