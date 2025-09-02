package tests.UI.pages.AutomationPlayground.fields;
import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class AlertPage extends BasePage {

    public Locator simpleAlertTrigger;
    public Locator confirmAlertTrigger;
    public Locator promptAlertTrigger;
    public Locator delayedAlertTrigger;
    public Locator hoverAlertTrigger;
    public Locator customModalTrigger;    // Updated: modal trigger button
    public Locator closeModalTrigger;
    public Locator functionAlertTrigger;
    public Locator chainedAlertTrigger;
    public Locator dynamicAlertTrigger;
    public Locator ajaxAlertTrigger;

    public AlertPage(Page page) {
        super(page);
        this.simpleAlertTrigger = page.locator("#alertBtn");
        this.confirmAlertTrigger = page.locator("#confirmBtn");
        this.promptAlertTrigger = page.locator("#promptBtn");
        this.delayedAlertTrigger = page.locator("#delayedAlertBtn");
        this.hoverAlertTrigger = page.locator("#hoverAlertBtn");
        this.customModalTrigger = page.locator("#customModalBtn");  // Modal trigger
        this.closeModalTrigger = page.locator("#closeModalBtn");
        this.functionAlertTrigger = page.locator("#functionAlertBtn");
        this.chainedAlertTrigger = page.locator("#chainedAlertBtn");
        this.dynamicAlertTrigger = page.locator("#dynamicAlertBtn");
        this.ajaxAlertTrigger = page.locator("#ajaxAlertBtn");
    }
}
