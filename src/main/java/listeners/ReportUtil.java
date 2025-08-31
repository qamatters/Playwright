package listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.microsoft.playwright.Page;
import org.testng.asserts.SoftAssert;

import java.io.IOException;

/**
 * ReportUtil: central utility for logging, hard assertions, and soft verifications.
 * - Hard assertions fail immediately.
 * - Soft verifications continue execution but mark the test as fail in the end.
 * - All failures capture screenshots and attach to ExtentReports.
 */
public class ReportUtil extends ExtentTestNGReporter {

    private static SoftAssert softAssert = new SoftAssert();

    // ===================== PAGE SETUP =====================
    public static void setPage(Page page) {
        ExtentTestNGReporter.setPage(page);
    }

    public static Page getPage() {
        return ExtentTestNGReporter.getPage();
    }

    // ===================== CURRENT TEST ACCESS =====================
    public static ExtentTest getCurrentTest() {
        return getTest();
    }

    // ===================== SOFT ASSERT GETTER =====================
    public static SoftAssert getSoftAssert() {
        return softAssert;
    }

    // ===================== LOGGING =====================
    public static void logInfo(String message) {
        ExtentTest extentTest = getCurrentTest();
        if (extentTest != null) extentTest.info(message);
    }

    public static void logWarning(String message) {
        ExtentTest extentTest = getCurrentTest();
        if (extentTest != null) extentTest.warning(message);
    }

    public static void logFail(String message) {
        ExtentTest extentTest = getCurrentTest();
        if (extentTest != null) extentTest.fail(message);
    }

    // ===================== HARD ASSERTIONS =====================
    public static void assertText(String actual, String expected, String message) {
        if (actual.equals(expected)) {
            logInfo("Assert Passed: " + message + " | Actual: " + actual + ", Expected: " + expected);
        } else {
            captureFail("Assert Failed: " + message + " | Actual: " + actual + ", Expected: " + expected);
            throw new AssertionError(message + " | Expected: " + expected + ", Actual: " + actual);
        }
    }

    public static void assertTitle(String expectedTitle, String message) {
        String actual = getPage().title();
        assertText(actual, expectedTitle, message);
    }

    public static void assertURL(String expectedURL, String message) {
        String actual = getPage().url();
        assertText(actual, expectedURL, message);
    }

    // ===================== SOFT VERIFICATIONS =====================
    public static void verifyText(String actual, String expected, String message) {
        if (!actual.equals(expected)) {
            captureSoftFail(message, actual, expected);
        } else {
            logInfo("Soft Verify Passed: " + message + " | Actual: " + actual + ", Expected: " + expected);
        }
    }

    public static void verifyTitle(String expectedTitle, String message) {
        String actual = getPage().title();
        verifyText(actual, expectedTitle, message);
    }

    public static void verifyURL(String expectedURL, String message) {
        String actual = getPage().url();
        verifyText(actual, expectedURL, message);
    }

    // ===================== SCREENSHOT HELPERS =====================
    private static void captureFail(String message) {
        try {
            String path = ScreenshotUtil.captureScreenshot("AssertFail_" + System.currentTimeMillis());
            getCurrentTest().fail(message,
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        } catch (IOException e) {
            logFail(message + " (Screenshot capture failed)");
        }
    }

    private static void captureSoftFail(String message, Object actual, Object expected) {
        try {
            String path = ScreenshotUtil.captureScreenshot("SoftVerifyFail_" + System.currentTimeMillis());
            getCurrentTest().fail(
                    message + " | Expected: " + expected + ", Actual: " + actual,
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build()
            );
        } catch (IOException e) {
            logFail(message + " (Screenshot capture failed)");
        }
        // Mark as soft assert failed
        softAssert.fail(message + " | Expected: " + expected + ", Actual: " + actual);
    }

    // ===================== FINAL ASSERT ALL =====================
    public static void assertAll() {
        try {
            softAssert.assertAll(); // throws AssertionError if any soft assertion failed
        } finally {
            initSoftAssert(); // reset for next test
        }
    }

    public static void initSoftAssert() {
        softAssert = new SoftAssert();
    }

    // ===================== HARD ASSERTIONS =====================
    public static void assertEquals(Object actual, Object expected, String message) {
        if (actual == null && expected == null) {
            logInfo("Assert Passed: " + message + " | Both values are null");
            return;
        }
        if (actual != null && actual.equals(expected)) {
            logInfo("Assert Passed: " + message + " | Actual: " + actual + ", Expected: " + expected);
        } else {
            captureFail("Assert Failed: " + message + " | Actual: " + actual + ", Expected: " + expected);
            throw new AssertionError(message + " | Expected: " + expected + ", Actual: " + actual);
        }
    }

    // ===================== SOFT VERIFICATION =====================
    public static void verifyEquals(Object actual, Object expected, String message) {
        if (actual == null && expected == null) {
            logInfo("Soft Verify Passed: " + message + " | Both values are null");
            return;
        }
        if (actual != null && actual.equals(expected)) {
            logInfo("Soft Verify Passed: " + message + " | Actual: " + actual + ", Expected: " + expected);
        } else {
            captureSoftFail(message, actual, expected);
        }
    }

    // ===================== HARD ASSERTIONS =====================
    public static void assertTrue(boolean condition, String message) {
        if (condition) {
            logInfo("Assert Passed: " + message);
        } else {
            captureFail("Assert Failed: " + message);
            throw new AssertionError("Assert Failed: " + message);
        }
    }

    // ===================== SOFT VERIFICATIONS =====================
    public static void verifyTrue(boolean condition, String message) {
        if (!condition) {
            captureSoftFail(message, false, true); // Expected: true, Actual: false
        } else {
            logInfo("Soft Verify Passed: " + message + " | Condition is true");
        }
    }

    // ===================== TEST CASE WRAPPER =====================
    /**
     * Wraps a test case to ensure "Test Case Finished" is always logged in the report
     */
    public static void runTestCase(String testName, Runnable steps) {
        logInfo("==== Test Case Started: " + testName + " ====");
        try {
            steps.run();
        } catch (AssertionError | Exception e) {
            logFail("Test case failed due to: " + e.getMessage());
            throw e; // propagate so test is marked FAIL
        } finally {
            logInfo("==== Test Case Finished: " + testName + " ====");
        }
    }
}
