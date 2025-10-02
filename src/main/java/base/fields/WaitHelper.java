package base.fields;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;
import listeners.ReportUtil;

public class WaitHelper {

    public static void waitForElementToAppear(Locator locator, int timeoutMillis) {
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(timeoutMillis));
    }

    public static void waitForElementToDisappear(Locator locator, int timeoutMillis) {
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(timeoutMillis));
    }

    public static void waitForElementToBeAttached(Locator locator, int timeoutMillis) {
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.ATTACHED).setTimeout(timeoutMillis));
    }

    public static void waitForElementToBeDetached(Locator locator, int timeoutMillis) {
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.DETACHED).setTimeout(timeoutMillis));
    }

    public static void waitForElementToBeVisibleAttached(Locator locator, int timeoutMillis) {
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(timeoutMillis));
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.ATTACHED).setTimeout(timeoutMillis));
    }

    public static void waitForPageToCompleteLoad(Page page, String url, int timeoutMillis) {
        page.navigate(url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.LOAD).setTimeout(timeoutMillis));
    }

    public static void waitForPageToBeIdle(Page page, String url, int timeoutMillis) {
        page.navigate(url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.NETWORKIDLE).setTimeout(timeoutMillis));
    }

    public static void waitForPageToBeInteractive(Page page, String url, int timeoutMillis) {
        page.navigate(url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED).setTimeout(timeoutMillis));
    }

    /**
     * Waits for a specific element to appear within a given timeout period.
     *
     * @param locator       The Playwright Locator object representing the element to wait for.
     * @param timeoutMillis The maximum time to wait for the element to appear, in milliseconds.
     * @param elementName   A descriptive name of the element, used for logging purposes.
     * @return true if the element becomes visible within the timeout period, false otherwise.
     * <p>
     * This method continuously checks if the element is visible until the timeout is reached.
     * - Logs the start of the waiting process.
     * - If the element becomes visible, logs success and returns true.
     * - If an exception occurs, it is ignored, and the method continues polling.
     * - If the timeout is reached, logs a timeout message and returns false.
     */
    public static boolean waitForElementToAppear(Locator locator, int timeoutMillis, String elementName) {
        ReportUtil.logInfo("Waiting for element to appear: " + elementName);
        long endTime = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < endTime) {
            try {
                if (locator.isVisible()) {
                    ReportUtil.logInfo(elementName + " appeared");
                    return true;
                }
                Thread.sleep(500); // Polling interval
            } catch (Exception ignored) {
            }
        }
        ReportUtil.logInfo("Timeout waiting for element to appear: " + elementName);
        return false;
    }

    public static boolean waitForElementToDisappear(Locator locator, int timeoutMillis, String elementName) {
        ReportUtil.logInfo("Waiting for element to disappear: " + elementName);
        long endTime = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < endTime) {
            try {
                if (!locator.isVisible()) {
                    ReportUtil.logInfo(elementName + " disappeared");
                    return true;
                }
                Thread.sleep(500); // Polling interval
            } catch (Exception ignored) {
            }
        }
        ReportUtil.logInfo("Timeout waiting for element to disappear: " + elementName);
        return false;
    }

    public static boolean waitForElementToBeAttached(Locator locator, int timeoutMillis, String elementName) {
        ReportUtil.logInfo("Waiting for element to be attached: " + elementName);
        long endTime = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < endTime) {
            try {
                if (locator.count() > 0) {
                    ReportUtil.logInfo(elementName + " is attached");
                    return true;
                }
                Thread.sleep(500); // Polling interval
            } catch (Exception ignored) {
            }
        }
        ReportUtil.logInfo("Timeout waiting for element to be attached: " + elementName);
        return false;
    }

    public static boolean waitForElementToBeDetached(Locator locator, int timeoutMillis, String elementName) {
        ReportUtil.logInfo("Waiting for element to be detached: " + elementName);
        long endTime = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < endTime) {
            try {
                if (locator.count() == 0) {
                    ReportUtil.logInfo(elementName + " is detached");
                    return true;
                }
                Thread.sleep(500); // Polling interval
            } catch (Exception ignored) {
            }
        }
        ReportUtil.logInfo("Timeout waiting for element to be detached: " + elementName);
        return false;
    }

    public static boolean waitForElementToBeVisibleAttached(Locator locator, int timeoutMillis, String elementName) {
        ReportUtil.logInfo("Waiting for element to be visible and attached: " + elementName);
        long endTime = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < endTime) {
            try {
                if (locator.isVisible() && locator.count() > 0) {
                    ReportUtil.logInfo(elementName + " is visible and attached");
                    return true;
                }
                Thread.sleep(500); // Polling interval
            } catch (Exception ignored) {
            }
        }
        ReportUtil.logInfo("Timeout waiting for element to be visible and attached: " + elementName);
        return false;
    }

    public static boolean waitForSpecificTextToAppear(Locator locator, String text, int timeoutMillis, String elementName) {
        ReportUtil.logInfo("Waiting for specific text to appear in element: " + elementName);
        long endTime = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < endTime) {
            try {
                if (locator.isVisible() && locator.innerText().contains(text)) {
                    ReportUtil.logInfo("Text '" + text + "' appeared in " + elementName);
                    return true;
                }
                Thread.sleep(500); // Polling interval
            } catch (Exception ignored) {
            }
        }
        ReportUtil.logInfo("Timeout waiting for text '" + text + "' to appear in element: " + elementName);
        return false;
    }

    public static boolean waitForSpecificTextToDisappear(Locator locator, String text, int timeoutMillis, String elementName) {
        ReportUtil.logInfo("Waiting for specific text to disappear from element: " + elementName);
        long endTime = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < endTime) {
            try {
                if (locator.isVisible() && !locator.innerText().contains(text)) {
                    ReportUtil.logInfo("Text '" + text + "' disappeared from " + elementName);
                    return true;
                }
                Thread.sleep(500); // Polling interval
            } catch (Exception ignored) {
            }
        }
        ReportUtil.logInfo("Timeout waiting for text '" + text + "' to disappear from element: " + elementName);
        return false;
    }

    public static void waitForIFrameToLoad(Page page, String iframeLocator, int timeoutMillis) {
        ReportUtil.logInfo("Waiting for iframe to load: " + iframeLocator);
        long endTime = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < endTime) {
            try {
                if (page.frameLocator(iframeLocator).locator("body").isVisible()) {
                    ReportUtil.logInfo("Iframe loaded: " + iframeLocator);
                    return;
                }
                Thread.sleep(500); // Polling interval
            } catch (Exception ignored) {
            }
        }
        ReportUtil.logInfo("Timeout waiting for iframe to load: " + iframeLocator);
    }

    public static void waitForIframeToAttached(Page page, String iframeLocator, int timeoutMillis) {
        ReportUtil.logInfo("Waiting for iframe to be attached: " + iframeLocator);
        long endTime = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < endTime) {
            try {
                if (page.frameLocator(iframeLocator).locator("body").count() > 0) {
                    ReportUtil.logInfo("Iframe is attached: " + iframeLocator);
                    return;
                }
                Thread.sleep(500); // Polling interval
            } catch (Exception ignored) {
            }
        }
        ReportUtil.logInfo("Timeout waiting for iframe to be attached: " + iframeLocator);
    }

}