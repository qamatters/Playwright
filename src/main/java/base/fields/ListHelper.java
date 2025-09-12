package base.fields;

import com.microsoft.playwright.Locator;

import java.util.LinkedList;
import java.util.List;

public class ListHelper {

    public static void selectFromList(Locator list, String value) {
        list.locator("li", new Locator.LocatorOptions().setHasText(value)).first().click();
    }

    public static void selectFromListByIndex(Locator list, int index) {
        list.locator("li").nth(index).click();
    }

    public static void selectFromListByPartialText(Locator list, String partialText) {
        list.locator("li", new Locator.LocatorOptions().setHasText(partialText)).first().click();
    }

    public static void selectFirstItemFromListIfExists(Locator list, String value) {
        Locator item = list.locator("li", new Locator.LocatorOptions().setHasText(value));
        if (item.count() > 0) {
            item.first().click();
        }
    }

    public static int getListItemCount(Locator list) {
        return list.locator("li").count();
    }

    public static boolean isItemInList(Locator list, String value) {
        return list.locator("li", new Locator.LocatorOptions().setHasText(value)).count() > 0;
    }

    public static boolean isListEmpty(Locator list) {
        return list.locator("li").count() == 0;
    }

    public static String getSelectedItemText(Locator list, String selectedClass) {
        Locator selectedItem = list.locator("li." + selectedClass);
        return selectedItem.count() > 0 ? selectedItem.first().innerText() : null;
    }

    public static List<String> getAllListItemsText(Locator list) {
        List<String> itemsText = new LinkedList<>();
        int itemCount = list.locator("li").count();
        for (int i = 0; i < itemCount; i++) {
            itemsText.add(list.locator("li").nth(i).innerText());
        }
        return itemsText;
    }

}
