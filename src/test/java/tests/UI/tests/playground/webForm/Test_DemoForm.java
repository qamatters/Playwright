package tests.UI.tests.playground.webForm;

import base.BaseUITest;
import org.testng.annotations.Test;
import tests.UI.pages.AutomationPlayground.webForm.DemoForm;
import tests.UI.pages.AutomationPlayground.webForm.FormData;

import java.util.Arrays;

public class Test_DemoForm extends BaseUITest {
    @Test
    public void TestDemoWebFormInPlaywright() {
        DemoForm demoForm = new DemoForm(page);

        page.navigate("https://qamatters.github.io/demoautomationWebSite/WorkFlows/Forms/demo-form.html");
        FormData formData = new FormData.Builder()
                .fullName("Deepak Mathpal")
                .password("Secret123")
                .comments("Testing automation form")
                .age("37")
                .email("deepak@example.com")
                .website("https://example.com")
                .phone("9876543210")
                .gender("Male")
                .skills(Arrays.asList("Selenium", "Playwright"))
                .experience("Intermediate")
                .country("India")
                .state("Maharashtra")
                .city("Mumbai")
                .dob("1990-05-15")
                .time("10:30")
                .datetime("2025-09-02T14:45")
                .month("2025-09")
                .week("2025-W35")
                .favColor("#ff5733")
                .volume("80")
                .search("Playwright Testing")
                .uploadFile("src/test/resources/testfile.txt")
                .iframeRadio("Yes")
                .iframeCheckboxes(Arrays.asList("Option1"))
                .iframeText("Inside iframe")
                .build();
        demoForm.fillForm(formData);
        demoForm.validateSubmittedData(formData);

    }
}
