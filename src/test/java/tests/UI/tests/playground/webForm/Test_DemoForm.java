package tests.UI.tests.playground.webForm;

import base.BaseUITest;
import org.testng.annotations.Test;
import tests.UI.pages.AutomationPlayground.webForm.DemoForm;

public class Test_DemoForm extends BaseUITest {
    @Test
    public void TestDemoWebFormInPlaywright() {
        DemoForm demoForm = new DemoForm(page);
        page.navigate("https://qamatters.github.io/demoautomationWebSite/WorkFlows/Forms/demo-form.html");
        page.setViewportSize(1980, 1400);
        demoForm.fillWebFormDetails();

    }
}
