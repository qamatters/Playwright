package tests.UI.tests.playground.fields;

import base.BaseUITest;
import base.fields.AlertHelper;
import listeners.ReportUtil;
import org.testng.annotations.Test;
import tests.UI.pages.AutomationPlayground.fields.AlertPage;

public class Test_AlertBehviours extends BaseUITest {

    @Test
    public void TestAlertBehaviourInPlaywright() {
        page.navigate("https://qamatters.github.io/demoautomationWebSite/Fields/Alert.html");
        AlertHelper alertHelper = new AlertHelper(page);
        AlertPage alertPage = new AlertPage(page);

        ReportUtil.logInfo("Basic Alert Handling");
        // ================= BASIC ALERTS =================
        alertHelper.handleSimpleAlert(alertPage.simpleAlertTrigger);
        alertHelper.handleConfirmAlert(alertPage.confirmAlertTrigger, true);
        String alertMessage  = alertHelper.handlePromptAlert(alertPage.promptAlertTrigger, "Deepak Mathpal");
        ReportUtil.logInfo(" Alert message is :" + alertMessage);


        // ================= DELAYED ALERT =================
        alertHelper.handleDelayedAlert(alertPage.delayedAlertTrigger, 3000);

        // ================= AJAX ALERT =================
        alertHelper.handleAjaxAlert(alertPage.ajaxAlertTrigger, 2000);

        // ================= HOVER ALERT =================
        alertHelper.handleHoverAlert(alertPage.hoverAlertTrigger);

        // ================= CHAINED ALERTS =================
        alertHelper.handleChainedAlerts(alertPage.chainedAlertTrigger, true, "Chained Input", 3000);

        // ================= DYNAMIC ALERT =================
        alertHelper.handleDynamicAlert(alertPage.dynamicAlertTrigger);

        // ================= CUSTOM MODAL =================
        alertHelper.closeCustomModal(alertPage.customModalTrigger, alertPage.customModalTrigger, alertPage.closeModalTrigger);

        // ================= FUNCTION ALERT =================
        alertHelper.triggerAlertFunction(alertPage.functionAlertTrigger);


    }
}
