package tests.UI.tests.playground.fields;

import base.BaseUITest;
import base.fields.AlertHelper;
import listeners.ReportUtil;
import org.testng.annotations.Test;
import tests.UI.pages.AutomationPlayground.fields.AlertPage;

import static base.fields.AlertHelper.*;

public class Test_AlertBehviours extends BaseUITest {
    @Test
    public void TestAlertBehaviourInPlaywright() {
        AlertPage alertPage = new AlertPage(page);
        page.navigate("https://qamatters.github.io/demoautomationWebSite/Fields/Alert.html");
        ReportUtil.logInfo("Basic Alert Handling");
        // ================= BASIC ALERTS =================
        handleSimpleAlert(page,alertPage.simpleAlertTrigger);
        handleConfirmAlert(page,alertPage.confirmAlertTrigger, true);
        String alertMessage  =handlePromptAlert(page,alertPage.promptAlertTrigger, "Deepak Mathpal");
        ReportUtil.logInfo(" Alert message is :" + alertMessage);


        // ================= DELAYED ALERT =================
        handleDelayedAlert(page,alertPage.delayedAlertTrigger, 3000);

        // ================= AJAX ALERT =================
        handleAjaxAlert(page,alertPage.ajaxAlertTrigger, 2000);

        // ================= HOVER ALERT =================
        handleHoverAlert(page,alertPage.hoverAlertTrigger);

        // ================= CHAINED ALERTS =================
        handleChainedAlerts(page,alertPage.chainedAlertTrigger, true, "Chained Input", 3000);

        // ================= DYNAMIC ALERT =================
        handleDynamicAlert(page,alertPage.dynamicAlertTrigger);

        // ================= CUSTOM MODAL =================
       closeCustomModal(page,alertPage.customModalTrigger, alertPage.customModalTrigger, alertPage.closeModalTrigger);

        // ================= FUNCTION ALERT =================
       triggerAlertFunction(page, alertPage.functionAlertTrigger);


    }
}
