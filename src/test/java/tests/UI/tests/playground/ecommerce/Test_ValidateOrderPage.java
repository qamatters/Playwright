package tests.UI.tests.playground.ecommerce;

import base.BaseUITest;
import listeners.ReportUtil;
import org.testng.annotations.Test;
import tests.UI.pages.AutomationPlayground.ecommerce.Order;
import utils.Logger;

public class Test_ValidateOrderPage extends BaseUITest {
    @Test
    public void validatePlaceOrder() {
        Logger.formattedLog("Testing_Ecommerce PlaceOrder Page", logMode);
        Order order = new Order(page);
        page.navigate("https://qamatters.github.io/demoautomationWebSite/WorkFlows/ecommerce/order.html");
        ReportUtil.verifyTrue(order.pageName.isVisible(), "Order page is displayed as "+ order.pageName.innerText());
        ReportUtil.verifyEquals(order.getItemCount() , 5, "Validate total Items");
        order.getAllItems();
    }
}
