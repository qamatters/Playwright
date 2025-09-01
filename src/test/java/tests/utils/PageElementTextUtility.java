package tests.utils;

import com.microsoft.playwright.Locator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageElementTextUtility {
    public static String waitUntilStableText(Locator locator, int timeoutMs, int checkIntervalMs, String regexPattern) {
        Pattern pattern = Pattern.compile(regexPattern);

        String previous = locator.innerText();
        String current = previous;
        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - start < timeoutMs) {
            try {
                Thread.sleep(checkIntervalMs);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            current = locator.innerText();

            // Check regex validity
            Matcher matcher = pattern.matcher(current);
            if (!matcher.matches()) {
                continue; // ignore values that don't match pattern
            }

            // if text hasn't changed between checks, assume it's stable
            if (current.equals(previous)) {
                return current; // stable and valid
            }
            previous = current;
        }
        throw new RuntimeException("Text did not stabilize within timeout or didn't match regex");
    }
}
