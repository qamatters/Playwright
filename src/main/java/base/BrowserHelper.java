package base;

import com.microsoft.playwright.*;

import java.util.List;

public class BrowserHelper {

    private static Playwright playwright;
    private static Browser browser;

    /**
     * Launch a fresh browser instance (no user profile, completely isolated)
     */
    public static void launchBrowser(String browserName, boolean headless) {
        playwright = Playwright.create();
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setArgs(List.of(
                        "--no-default-browser-check",
                        "--no-first-run",
                        "--disable-extensions",
                        "--disable-popup-blocking",
                        "--disable-session-crashed-bubble",
                        "--disable-sync"
                ));

        switch (browserName.toLowerCase()) {
            case "chromium":
                browser = playwright.chromium().launch(options);
                break;
            case "firefox":
                browser = playwright.firefox().launch(options);
                break;
            case "webkit":
                browser = playwright.webkit().launch(options);
                break;
            default:
                throw new IllegalArgumentException("Invalid browser name: " + browserName);
        }
    }

    /**
     * Launch a completely **fresh incognito context**.
     * No cookies, cache, or extensions.
     */
    public static BrowserContext launchFreshIncognitoContext() {
        if (browser == null) throw new RuntimeException("Browser not launched");
        Browser.NewContextOptions options = new Browser.NewContextOptions()
                .setIgnoreHTTPSErrors(true)
                .setViewportSize(1280, 800)
                .setUserAgent("PlaywrightIncognito")
                .setBypassCSP(true);
        return browser.newContext(options);
    }

    /** Open a new page in the given context */
    public static Page openNewPage(BrowserContext context) {
        return context.newPage();
    }

    /** Close browser and playwright */
    public static void closeBrowser() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    /**
     * Convenience: launch fresh incognito page and navigate
     */
    public static Page launchFreshIncognitoPage(String browserName, boolean headless, String url) {
        launchBrowser(browserName, headless);
        BrowserContext context = launchFreshIncognitoContext();
        Page page = openNewPage(context);
        page.navigate(url);
        return page;
    }
}