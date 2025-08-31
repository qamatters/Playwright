package base.fields;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import listeners.ReportUtil;

public class AlertHelper {
    private Page page;
    private final int DEFAULT_TIMEOUT = 2000; // 2 seconds default wait

    public AlertHelper(Page page) {
        this.page = page;
    }

    // ================= BASIC ALERTS =================
    public void handleSimpleAlert(Locator triggerElement, int timeoutMs) {
        page.onceDialog(dialog -> {
            String message = dialog.message();
            System.out.println("[Simple Alert] " + message);
            ReportUtil.logInfo("Simple Alert message is : " +message);
            page.waitForTimeout(timeoutMs);
            dialog.accept();
        });
        triggerElement.click();
    }

    public void handleSimpleAlert(Locator triggerElement) {
        handleSimpleAlert(triggerElement, DEFAULT_TIMEOUT);
    }

    public boolean handleConfirmAlert(Locator triggerElement, boolean accept, int timeoutMs) {
        final boolean[] result = {false};
        page.onceDialog(dialog -> {
            String message = dialog.message();
            System.out.println("[Confirm Alert] " + message);
            ReportUtil.logInfo("Confirm Alert message is : " +message);
            page.waitForTimeout(timeoutMs);
            result[0] = accept;
            if (accept) dialog.accept();
            else dialog.dismiss();
        });
        triggerElement.click();
        return result[0];
    }

    public boolean handleConfirmAlert(Locator triggerElement, boolean accept) {
        return handleConfirmAlert(triggerElement, accept, DEFAULT_TIMEOUT);
    }

    public String handlePromptAlert(Locator triggerElement, String inputText, int timeoutMs) {
        final String[] entered = {null};
        page.onceDialog(dialog -> {
            String message = dialog.message();
            System.out.println("[Prompt Alert] " + message);
            ReportUtil.logInfo("Prompt Alert message is : " +message);
            page.waitForTimeout(timeoutMs);
            entered[0] = inputText;
            dialog.accept(inputText);
        });
        triggerElement.click();
        return entered[0];
    }

    public String handlePromptAlert(Locator triggerElement, String inputText) {
        return handlePromptAlert(triggerElement, inputText, DEFAULT_TIMEOUT);
    }

    // ================= DELAYED ALERT =================
    public void handleDelayedAlert(Locator triggerElement, int alertWaitMs, int timeoutMs) {
        page.onceDialog(dialog -> {
            String message = dialog.message();
            System.out.println("[Delayed Alert] " + message);
            ReportUtil.logInfo("Delayed Alert message is : " +message);
            page.waitForTimeout(timeoutMs);
            dialog.accept();
        });
        triggerElement.click();
        page.waitForTimeout(alertWaitMs);
    }

    public void handleDelayedAlert(Locator triggerElement, int alertWaitMs) {
        handleDelayedAlert(triggerElement, alertWaitMs, DEFAULT_TIMEOUT);
    }

    // ================= CHAINED ALERTS =================
    public void handleChainedAlerts(Locator triggerElement, boolean secondConfirm, String promptInput, int timeoutMs) {
        page.onceDialog(dialog -> {
            System.out.println("[Chained Alert 1] " + dialog.message());
            ReportUtil.logInfo("[Chained Alert 1] " + dialog.message());
            page.waitForTimeout(timeoutMs);
            dialog.accept();
            // Step 2: Second confirm alert
            page.onceDialog(dialog2 -> {
                System.out.println("[Chained Alert 2] " + dialog2.message());
                ReportUtil.logInfo("[Chained Alert 2] " + dialog.message());
                page.waitForTimeout(timeoutMs);
                if (secondConfirm) dialog2.accept();
                else dialog2.dismiss();
                // Step 3: Third prompt alert
                page.onceDialog(dialog3 -> {
                    System.out.println("[Chained Alert 3] " + dialog3.message());
                    ReportUtil.logInfo("[Chained Alert 3] " + dialog.message());
                    page.waitForTimeout(timeoutMs);
                    dialog3.accept(promptInput);
                });
            });
        });
        triggerElement.click();
    }

    public void handleChainedAlerts(Locator triggerElement, boolean secondConfirm, String promptInput) {
        handleChainedAlerts(triggerElement, secondConfirm, promptInput, DEFAULT_TIMEOUT);
    }

    // ================= DYNAMIC CONTENT ALERT =================
    public void handleDynamicAlert(Locator triggerElement, int timeoutMs) {
        page.onceDialog(dialog -> {
            System.out.println("[Dynamic Alert] " + dialog.message());
            ReportUtil.logInfo("[Dynamic Alert] " + dialog.message());
            page.waitForTimeout(timeoutMs);
            dialog.accept();
        });
        triggerElement.click();
    }

    public void handleDynamicAlert(Locator triggerElement) {
        handleDynamicAlert(triggerElement, DEFAULT_TIMEOUT);
    }

    // ================= AJAX / DELAYED ALERT =================
    public void handleAjaxAlert(Locator triggerElement, int ajaxDelayMs, int timeoutMs) {
        page.onceDialog(dialog -> {
            System.out.println("[AJAX Alert] " + dialog.message());
            ReportUtil.logInfo("[AJAX Alert] " + dialog.message());
            page.waitForTimeout(timeoutMs);
            dialog.accept();
        });
        triggerElement.click();
        page.waitForTimeout(ajaxDelayMs);
    }

    public void handleAjaxAlert(Locator triggerElement, int ajaxDelayMs) {
        handleAjaxAlert(triggerElement, ajaxDelayMs, DEFAULT_TIMEOUT);
    }

    // ================= HOVER ALERT =================
    public void handleHoverAlert(Locator triggerElement, int timeoutMs) {
        page.onceDialog(dialog -> {
            System.out.println("[Hover Alert] " + dialog.message());
            ReportUtil.logInfo("[Hover Alert] " + dialog.message());
            page.waitForTimeout(timeoutMs);
            dialog.accept();
        });
        triggerElement.hover();
    }

    public void handleHoverAlert(Locator triggerElement) {
        handleHoverAlert(triggerElement, DEFAULT_TIMEOUT);
    }

    // ================= SCROLL ALERT =================
    public void handleScrollAlert(int scrollY, int timeoutMs) {
        page.onceDialog(dialog -> {
            System.out.println("[Scroll Alert] " + dialog.message());
            page.waitForTimeout(timeoutMs);
            dialog.accept();
        });
        page.evaluate("window.scrollTo(0," + scrollY + ");");
    }

    public void handleScrollAlert(int scrollY) {
        handleScrollAlert(scrollY, DEFAULT_TIMEOUT);
    }

    // ================= CUSTOM MODAL =================
    public void closeCustomModal(Locator triggerElement, Locator modal, Locator closeElement, int timeoutMs) {
        triggerElement.click();
        modal.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout((double) timeoutMs));
        page.waitForTimeout(timeoutMs); // observe modal
        closeElement.click();
    }

    public void closeCustomModal(Locator triggerElement, Locator modal, Locator closeElement) {
        closeCustomModal(triggerElement, modal, closeElement, DEFAULT_TIMEOUT);
    }

    // ================= FUNCTION ALERT =================
    public void triggerAlertFunction(Locator triggerElement, int timeoutMs) {
        page.onceDialog(dialog -> {
            System.out.println("[Function Alert] " + dialog.message());
            ReportUtil.logInfo("[Function Alert] " + dialog.message());
            page.waitForTimeout(timeoutMs);
            dialog.accept();
        });
        triggerElement.click();
    }

    public void triggerAlertFunction(Locator triggerElement) {
        triggerAlertFunction(triggerElement, DEFAULT_TIMEOUT);
    }
}
