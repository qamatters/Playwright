package tests.UI.tests.playground.ecommerce;

import base.BaseUITest;
import listeners.ReportUtil;
import org.testng.annotations.Test;
import tests.UI.pages.AutomationPlayground.ecommerce.Order;
import utils.Logger;

public class Test_ValidateCheckoutBehaviour extends BaseUITest {
    @Test
    public void validateCheckout() {
        Logger.formattedLog("Testing_Ecommerce Checkout functionality", logMode);
        Order order = new Order(page);
        page.navigate("https://qamatters.github.io/demoautomationWebSite/WorkFlows/ecommerce/order.html");
        ReportUtil.verifyTrue(order.pageName.isVisible(), "Order page is displayed as "+ order.pageName.innerText());
        order.selectItem(1);
        ReportUtil.verifyEquals(order.getSelectedItemCount() , 1, "Validate selected Items in cart");
        order.removeItem(1);
        ReportUtil.verifyEquals(order.getSelectedItemCount() , 0, "Validate selected Items in cart after deselecting");
    }
}
