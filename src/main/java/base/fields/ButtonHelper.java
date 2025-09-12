package base.fields;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.MouseButton;
import com.microsoft.playwright.options.WaitForSelectorState;

public class ButtonHelper {

    public void clickButton(Locator button) {
        button.click();
    }

    public boolean isButtonEnabled(Locator button) {
        return button.isEnabled();
    }

    public boolean isButtonVisible(Locator button) {
        return button.isVisible();
    }

    public String getButtonText(Locator button) {
        return button.textContent();
    }
    public String getButtonAttribute(Locator button, String attribute) {
        return button.getAttribute(attribute);
    }

    public static void doubleClickButton(Locator button) {
        button.dblclick();
    }

    public static void rightClickButton(Locator button) {
        button.click(new Locator.ClickOptions().setButton(MouseButton.RIGHT));
    }

    public static void leftClickButton(Locator button) {
        button.click(new Locator.ClickOptions().setButton(MouseButton.LEFT));
    }

    public static void clickButtonWithDelay(Locator button, int delayMillis) {
        button.click(new Locator.ClickOptions().setDelay(delayMillis));
    }

    public static void waitForButtonAndClick(Locator button, int timeoutMillis) {
        button.waitFor(new Locator.WaitForOptions().setTimeout(timeoutMillis).setState(WaitForSelectorState.VISIBLE));
        button.waitFor(new Locator.WaitForOptions().setTimeout(timeoutMillis).setState(WaitForSelectorState.ATTACHED));
        button.click();
    }

    public static void hoverAndClickButton(Locator button) {
        button.hover();
        button.click();
    }

    public static void forceClickButton(Locator button) {
        button.click(new Locator.ClickOptions().setForce(true));
    }

    public static void scrollToAndClickButton(Locator button) {
        button.scrollIntoViewIfNeeded();
        button.click();
    }

    public static void clickButtonMultipleTimes(Locator button, int times) {
        for (int i = 0; i < times; i++) {
            button.click();
        }
    }

    public static void jsClickButton(Locator button) {
        button.evaluate("el => el.click()");
    }


    public static void clickButtonWithText(Locator buttons, String text) {
        buttons.locator("text=" + text).first().click();
    }

}