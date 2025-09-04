package tests.UI.tests.playground.specificUseCases;

import base.BaseUITest;
import listeners.ReportUtil;
import org.testng.annotations.Test;
import tests.UI.pages.AutomationPlayground.fields.AlertPage;
import tests.UI.pages.AutomationPlayground.specificUseCases.SpecificDropDown;

import static base.fields.AlertHelper.*;

public class Test_SpecificDropDownBehviour extends BaseUITest {
    @Test
    public void TestAlertBehaviourInPlaywright() {
        page.navigate("https://qamatters.github.io/demoautomationWebSite/SpecialFields/dropdown.html");
        SpecificDropDown specificDropDown = new SpecificDropDown(page);
        specificDropDown.selectOption("Fixed Income Insight");

    }
}
