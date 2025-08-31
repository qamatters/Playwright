package base;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.options.WaitForSelectorState;

public class BasePage {

    public Page page;
    public BrowserContext context;
    private final int DEFAULT_TIMEOUT = 5000; // 5 seconds default wait

    public BasePage(Page page) {
        this.page = page;
        this.context = page.context();
    }

    // ================= COMMON UTILITIES =================

    // Navigate to URL
    public void navigate(String url) {
        page.navigate(url);
    }

    // Wait for element to be visible
    public void waitForVisible(Locator locator, int timeoutMs) {
        locator.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout((double) timeoutMs));
    }

    public void waitForVisible(Locator locator) {
        waitForVisible(locator, DEFAULT_TIMEOUT);
    }

    // Click element safely
    public void click(Locator locator) {
        locator.click();
    }

    // Hover over element
    public void hover(Locator locator) {
        locator.hover();
    }

    // Scroll to element
    public void scrollTo(Locator locator) {
        page.evaluate("element => element.scrollIntoView()", locator);
    }

    // Type text into input
    public void typeText(Locator locator, String text) {
        locator.fill(text);
    }

    // Get element text
    public String getText(Locator locator) {
        return locator.innerText();
    }

    // Wait for timeout (ms)
    public void waitForTimeout(int timeoutMs) {
        page.waitForTimeout(timeoutMs);
    }
}

