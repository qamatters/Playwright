package base.fields;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import listeners.ReportUtil;

public class AlertHelper {

    private static final int DEFAULT_TIMEOUT = 2000; // 2 seconds default wait

    // ================= BASIC ALERTS =================
    public static void handleSimpleAlert(Page page, Locator triggerElement, int timeoutMs) {
        page.onceDialog(dialog -> {
            String message = dialog.message();
            System.out.println("[Simple Alert] " + message);
            ReportUtil.logInfo("Simple Alert message is : " + message);
            page.waitForTimeout(timeoutMs);
            dialog.accept();
        });
        triggerElement.click();
    }

    public static void handleSimpleAlert(Page page, Locator triggerElement) {
        handleSimpleAlert(page, triggerElement, DEFAULT_TIMEOUT);
    }

    public static boolean handleConfirmAlert(Page page, Locator triggerElement, boolean accept, int timeoutMs) {
        final boolean[] result = {false};
        page.onceDialog(dialog -> {
            String message = dialog.message();
            System.out.println("[Confirm Alert] " + message);
            ReportUtil.logInfo("Confirm Alert message is : " + message);
            page.waitForTimeout(timeoutMs);
            result[0] = accept;
            if (accept) dialog.accept();
            else dialog.dismiss();
        });
        triggerElement.click();
        return result[0];
    }

    public static boolean handleConfirmAlert(Page page, Locator triggerElement, boolean accept) {
        return handleConfirmAlert(page, triggerElement, accept, DEFAULT_TIMEOUT);
    }

    public static String handlePromptAlert(Page page, Locator triggerElement, String inputText, int timeoutMs) {
        final String[] entered = {null};
        page.onceDialog(dialog -> {
            String message = dialog.message();
            System.out.println("[Prompt Alert] " + message);
            ReportUtil.logInfo("Prompt Alert message is : " + message);
            page.waitForTimeout(timeoutMs);
            entered[0] = inputText;
            dialog.accept(inputText);
        });
        triggerElement.click();
        return entered[0];
    }

    public static String handlePromptAlert(Page page, Locator triggerElement, String inputText) {
        return handlePromptAlert(page, triggerElement, inputText, DEFAULT_TIMEOUT);
    }

    // ================= DELAYED ALERT =================
    public static void handleDelayedAlert(Page page, Locator triggerElement, int alertWaitMs, int timeoutMs) {
        page.onceDialog(dialog -> {
            String message = dialog.message();
            System.out.println("[Delayed Alert] " + message);
            ReportUtil.logInfo("Delayed Alert message is : " + message);
            page.waitForTimeout(timeoutMs);
            dialog.accept();
        });
        triggerElement.click();
        page.waitForTimeout(alertWaitMs);
    }

    public static void handleDelayedAlert(Page page, Locator triggerElement, int alertWaitMs) {
        handleDelayedAlert(page, triggerElement, alertWaitMs, DEFAULT_TIMEOUT);
    }

    // ================= CHAINED ALERTS =================
    public static void handleChainedAlerts(Page page, Locator triggerElement, boolean secondConfirm, String promptInput, int timeoutMs) {
        page.onceDialog(dialog -> {
            System.out.println("[Chained Alert 1] " + dialog.message());
            ReportUtil.logInfo("[Chained Alert 1] " + dialog.message());
            page.waitForTimeout(timeoutMs);
            dialog.accept();
            // Step 2: Second confirm alert
            page.onceDialog(dialog2 -> {
                System.out.println("[Chained Alert 2] " + dialog2.message());
                ReportUtil.logInfo("[Chained Alert 2] " + dialog2.message());
                page.waitForTimeout(timeoutMs);
                if (secondConfirm) dialog2.accept();
                else dialog2.dismiss();
                // Step 3: Third prompt alert
                page.onceDialog(dialog3 -> {
                    System.out.println("[Chained Alert 3] " + dialog3.message());
                    ReportUtil.logInfo("[Chained Alert 3] " + dialog3.message());
                    page.waitForTimeout(timeoutMs);
                    dialog3.accept(promptInput);
                });
            });
        });
        triggerElement.click();
    }

    public static void handleChainedAlerts(Page page, Locator triggerElement, boolean secondConfirm, String promptInput) {
        handleChainedAlerts(page, triggerElement, secondConfirm, promptInput, DEFAULT_TIMEOUT);
    }

    // ================= DYNAMIC CONTENT ALERT =================
    public static void handleDynamicAlert(Page page, Locator triggerElement, int timeoutMs) {
        page.onceDialog(dialog -> {
            System.out.println("[Dynamic Alert] " + dialog.message());
            ReportUtil.logInfo("[Dynamic Alert] " + dialog.message());
            page.waitForTimeout(timeoutMs);
            dialog.accept();
        });
        triggerElement.click();
    }

    public static void handleDynamicAlert(Page page, Locator triggerElement) {
        handleDynamicAlert(page, triggerElement, DEFAULT_TIMEOUT);
    }

    // ================= AJAX / DELAYED ALERT =================
    public static void handleAjaxAlert(Page page, Locator triggerElement, int ajaxDelayMs, int timeoutMs) {
        page.onceDialog(dialog -> {
            System.out.println("[AJAX Alert] " + dialog.message());
            ReportUtil.logInfo("[AJAX Alert] " + dialog.message());
            page.waitForTimeout(timeoutMs);
            dialog.accept();
        });
        triggerElement.click();
        page.waitForTimeout(ajaxDelayMs);
    }

    public static void handleAjaxAlert(Page page, Locator triggerElement, int ajaxDelayMs) {
        handleAjaxAlert(page, triggerElement, ajaxDelayMs, DEFAULT_TIMEOUT);
    }

    // ================= HOVER ALERT =================
    public static void handleHoverAlert(Page page, Locator triggerElement, int timeoutMs) {
        page.onceDialog(dialog -> {
            System.out.println("[Hover Alert] " + dialog.message());
            ReportUtil.logInfo("[Hover Alert] " + dialog.message());
            page.waitForTimeout(timeoutMs);
            dialog.accept();
        });
        triggerElement.hover();
    }

    public static void handleHoverAlert(Page page, Locator triggerElement) {
        handleHoverAlert(page, triggerElement, DEFAULT_TIMEOUT);
    }

    // ================= SCROLL ALERT =================
    public static void handleScrollAlert(Page page, int scrollY, int timeoutMs) {
        page.onceDialog(dialog -> {
            System.out.println("[Scroll Alert] " + dialog.message());
            page.waitForTimeout(timeoutMs);
            dialog.accept();
        });
        page.evaluate("window.scrollTo(0," + scrollY + ");");
    }

    public static void handleScrollAlert(Page page, int scrollY) {
        handleScrollAlert(page, scrollY, DEFAULT_TIMEOUT);
    }

    // ================= CUSTOM MODAL =================
    public static void closeCustomModal(Page page, Locator triggerElement, Locator modal, Locator closeElement, int timeoutMs) {
        triggerElement.click();
        modal.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout((double) timeoutMs));
        page.waitForTimeout(timeoutMs); // observe modal
        closeElement.click();
    }

    public static void closeCustomModal(Page page, Locator triggerElement, Locator modal, Locator closeElement) {
        closeCustomModal(page, triggerElement, modal, closeElement, DEFAULT_TIMEOUT);
    }

    // ================= FUNCTION ALERT =================
    public static void triggerAlertFunction(Page page, Locator triggerElement, int timeoutMs) {
        page.onceDialog(dialog -> {
            System.out.println("[Function Alert] " + dialog.message());
            ReportUtil.logInfo("[Function Alert] " + dialog.message());
            page.waitForTimeout(timeoutMs);
            dialog.accept();
        });
        triggerElement.click();
    }

    public static void triggerAlertFunction(Page page, Locator triggerElement) {
        triggerAlertFunction(page, triggerElement, DEFAULT_TIMEOUT);
    }
}
