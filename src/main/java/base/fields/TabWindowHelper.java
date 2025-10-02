package base.fields;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.TimeoutError;

import java.util.List;

public class TabWindowHelper {

    private static final int DEFAULT_TIMEOUT = 30000;

    /**
     * Waits for a new page/tab to open and returns it
     */
    public static Page waitForNewTab(BrowserContext context, int timeoutMillis) {
        long startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - startTime) < timeoutMillis) {
            List<Page> pages = context.pages();
            if (!pages.isEmpty()) {
                return pages.get(pages.size() - 1); // return the latest page
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        throw new RuntimeException("No new tab opened within " + timeoutMillis + "ms");
    }

    public static Page waitForNewTab(BrowserContext context) {
        return waitForNewTab(context, DEFAULT_TIMEOUT);
    }

    /**
     * Switch to a page/tab by index (0 = first tab)
     */
    public static Page switchToTab(BrowserContext context, int index) {
        List<Page> pages = context.pages();
        if (index < 0 || index >= pages.size()) {
            throw new IllegalArgumentException("Invalid tab index: " + index);
        }
        return pages.get(index);
    }

    /**
     * Close a specific tab by index
     */
    public static void closeTab(BrowserContext context, int index) {
        Page page = switchToTab(context, index);
        page.close();
    }

    /**
     * Close the last opened tab
     */
    public static void closeLastTab(BrowserContext context) {
        List<Page> pages = context.pages();
        if (!pages.isEmpty()) {
            pages.get(pages.size() - 1).close();
        } else {
            throw new RuntimeException("No tabs available to close");
        }
    }

    // ---------------- Element actions on a specific tab ---------------- //

    public static Locator getElement(Page page, String selector, int timeoutMillis) {
        try {
            page.locator(selector).waitFor(new Locator.WaitForOptions().setTimeout(timeoutMillis));
        } catch (TimeoutError e) {
            throw new RuntimeException("Element '" + selector + "' not found within " + timeoutMillis + "ms on page: " + page.url());
        }
        return page.locator(selector);
    }

    public static void click(Page page, String selector) {
        click(page, selector, DEFAULT_TIMEOUT);
    }

    public static void click(Page page, String selector, int timeoutMillis) {
        getElement(page, selector, timeoutMillis).click();
    }

    public static void type(Page page, String selector, String text) {
        type(page, selector, text, DEFAULT_TIMEOUT);
    }

    public static void type(Page page, String selector, String text, int timeoutMillis) {
        getElement(page, selector, timeoutMillis).fill(text);
    }

    public static String getText(Page page, String selector) {
        return getText(page, selector, DEFAULT_TIMEOUT);
    }

    public static String getText(Page page, String selector, int timeoutMillis) {
        return getElement(page, selector, timeoutMillis).textContent();
    }

    public static boolean isVisible(Page page, String selector) {
        return isVisible(page, selector, DEFAULT_TIMEOUT);
    }

    public static boolean isVisible(Page page, String selector, int timeoutMillis) {
        return getElement(page, selector, timeoutMillis).isVisible();
    }

    public static boolean isEnabled(Page page, String selector) {
        return isEnabled(page, selector, DEFAULT_TIMEOUT);
    }

    public static boolean isEnabled(Page page, String selector, int timeoutMillis) {
        return getElement(page, selector, timeoutMillis).isEnabled();
    }

    public static void hover(Page page, String selector) {
        hover(page, selector, DEFAULT_TIMEOUT);
    }

    public static void hover(Page page, String selector, int timeoutMillis) {
        getElement(page, selector, timeoutMillis).hover();
    }

    public static void doubleClick(Page page, String selector) {
        doubleClick(page, selector, DEFAULT_TIMEOUT);
    }

    public static void doubleClick(Page page, String selector, int timeoutMillis) {
        getElement(page, selector, timeoutMillis).dblclick();
    }

    public static void selectOption(Page page, String selector, String value) {
        selectOption(page, selector, value, DEFAULT_TIMEOUT);
    }

    public static void selectOption(Page page, String selector, String value, int timeoutMillis) {
        getElement(page, selector, timeoutMillis).selectOption(value);
    }
}
