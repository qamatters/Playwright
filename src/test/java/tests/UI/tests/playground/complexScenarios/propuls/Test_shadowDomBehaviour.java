package tests.UI.tests.playground.complexScenarios.propuls;

import base.BaseUITest;
import base.fields.AlertHelper;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.Test;

public class Test_shadowDomBehaviour extends BaseUITest {
    @Test
    public void TestAlertBehaviourInPlaywright() {
        Locator shadowButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Click Shadow Button"));
        page.navigate("https://qamatters.github.io/demoautomationWebSite/WorkFlows/ProVersion/proplus.html");
        page.onceDialog(dialog -> {
            System.out.println(String.format("Dialog message: %s", dialog.message()));
            page.waitForTimeout(2000);
            dialog.accept();
        });
        shadowButton.click();

    }
}
