package tests.UI.pages.AutomationPlayground.ecommerce;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import listeners.ReportUtil;

import java.util.List;

public class Order extends BasePage {
    public Locator pageName;
    public Locator checkout;
    public List<Locator> items;
    public Locator cartCount;

    public Order(Page page) {
        super(page);
        this.pageName = page.getByText("Select Your Items");
        this.checkout = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Checkout"));
        this.cartCount = page.locator("#cartCount");
    }

    public int getItemCount() {
        this.items = page.locator("#productList>div").all();
        return items.size();
    }

    public String getItemName(int index) {
        return items.get(index).getByRole(AriaRole.HEADING).innerText();
    }

    public String getItemPrice(int index) {
        return items.get(index).getByRole(AriaRole.PARAGRAPH).innerText();
    }

    public void getAllItems() {
        for (int i = 0; i < getItemCount(); i++) {
           ReportUtil.logInfo("Item Name: " + getItemName(i) + ", Price: " + getItemPrice(i));
        }
    }

    public void selectItem(int index) {
        if (items == null) {
            this.items = page.locator("#productList>div").all();
        }
        items.get(index).click();
        String classAttr = items.get(index).getAttribute("class");
        if (!classAttr.contains("bg-green")) {
           ReportUtil.logFail("Item does not have 'bg-green' class after click.");
        } else {
              ReportUtil.logPass("Item selected successfully and selection is reflecting.");
        }
    }

    public void removeItem(int index) {
        if (items == null) {
            this.items = page.locator("#productList>div").all();
        }
        items.get(index).click();
        String classAttr = items.get(index).getAttribute("class");
        if (classAttr.contains("bg-green")) {
            ReportUtil.logFail("Item does not have 'bg-green' class after click.");
        } else {
            ReportUtil.logPass("Item selected successfully and selection is reflecting.");
        }
    }

    public int getSelectedItemCount() {
        String countText = cartCount.innerText();
        return Integer.parseInt(countText);
    }

}