package listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.microsoft.playwright.Page;
import org.testng.asserts.SoftAssert;

import java.io.IOException;

/**
 * ReportUtil: central utility for logging, hard assertions, soft verifications,
 * global uncaught exception handling, optional screenshot on pass, and live ExtentReport updates.
 */
public class ReportUtil extends ExtentTestNGReporter {

    private static SoftAssert softAssert = new SoftAssert();
    private static boolean capturePassScreenshots = false;

    public static void enablePassScreenshots(boolean enable) {
        capturePassScreenshots = enable;
    }

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
        ExtentTest test = getCurrentTest();
        if (test != null) test.info(message);
        flushReport();
    }

    public static void logPass(String message) {
        ExtentTest test = getCurrentTest();
        if (test != null) {
            String liveMessage = "Validated Successfully: " + message;
            if (capturePassScreenshots) {
                try {
                    String path = ScreenshotUtil.captureScreenshot("Pass_" + System.currentTimeMillis());
                    test.pass(liveMessage, MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                } catch (IOException e) {
                    test.pass(liveMessage + " (Screenshot capture failed)");
                }
            } else {
                test.pass(liveMessage);
            }
        }
        flushReport();
    }

    public static void logFail(String message) {
        ExtentTest test = getCurrentTest();
        if (test != null) {
            String liveMessage = "Validation Failed: " + message;
            test.fail(liveMessage);
        }
        flushReport();
    }

    public static void logWarning(String message) {
        ExtentTest test = getCurrentTest();
        if (test != null) {
            String liveMessage = "Warning: " + message;
            test.warning(liveMessage);
        }
        flushReport();
    }

    // ===================== HARD ASSERTIONS =====================
    public static void assertText(String actual, String expected, String message) {
        if (actual.equals(expected)) {
            logPass(message + " | Actual: " + actual + ", Expected: " + expected);
        } else {
            captureFail(message, actual, expected);
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
            logPass(message + " | Actual: " + actual + ", Expected: " + expected);
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
    private static void captureFail(String message, Object actual, Object expected) {
        String fullMessage = "Validation Failed: " + message + " | Actual: " + actual + ", Expected: " + expected;
        try {
            String path = ScreenshotUtil.captureScreenshot("AssertFail_" + System.currentTimeMillis());
            getCurrentTest().fail(fullMessage, MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        } catch (IOException e) {
        }
        flushReport();
    }

    private static void captureSoftFail(String message, Object actual, Object expected) {
        String fullMessage = "Validation Failed: " + message + " | Actual: " + actual + ", Expected: " + expected;
        try {
            String path = ScreenshotUtil.captureScreenshot("VerificationFail_" + System.currentTimeMillis());
            getCurrentTest().fail(fullMessage, MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        } catch (IOException e) {

        }
        softAssert.fail(fullMessage);
        flushReport();
    }

    // ===================== FINAL ASSERT ALL =====================
    public static void assertAll() {
        try {
            softAssert.assertAll();
        } finally {
            initSoftAssert();
        }
        flushReport();
    }

    public static void initSoftAssert() {
        softAssert = new SoftAssert();
    }

    // ===================== OTHER ASSERTIONS =====================
    public static void assertEquals(Object actual, Object expected, String message) {
        if (actual == null && expected == null) {
            logPass(message + " | Both values are null");
            return;
        }
        if (actual != null && actual.equals(expected)) {
            logPass(message + " | Actual: " + actual + ", Expected: " + expected);
        } else {
            captureFail(message, actual, expected);
            throw new AssertionError(message + " | Expected: " + expected + ", Actual: " + actual);
        }
    }

    public static void verifyEquals(Object actual, Object expected, String message) {
        if (actual == null && expected == null) {
            logPass(message + " | Both values are null");
            return;
        }
        if (actual != null && actual.equals(expected)) {
            logPass(message + " | Actual: " + actual + ", Expected: " + expected);
        } else {
            captureSoftFail(message, actual, expected);
        }
    }

    public static void assertTrue(boolean condition, String message) {
        if (condition) {
            logPass(message);
        } else {
            captureFail(message, false, true);
            throw new AssertionError("Assert Failed: " + message);
        }
    }

    public static void verifyTrue(boolean condition, String message) {
        if (!condition) {
            captureSoftFail(message, false, true);
        } else {
            logPass(message + " | Condition is true");
        }
    }

    // ===================== TEST CASE WRAPPER =====================
    public static void runTestCase(String testName, Runnable steps) {
        logInfo("==== Test Case Started: " + testName + " ====");
        try {
            steps.run();
        } catch (Throwable t) {
            logFail("Test case failed due to: " + t.getMessage());
            throw t;
        } finally {
            logInfo("==== Test Case Finished: " + testName + " ====");
        }
    }

    // ===================== GLOBAL EXCEPTION HANDLER =====================
    public static void initGlobalExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            ExtentTest test = getCurrentTest();
            String message = "Error: Uncaught Exception in thread " + thread.getName() + ": " + throwable.getMessage();

            if (test != null) {
                test.fail(message);

                try {
                    String path = ScreenshotUtil.captureScreenshot("UncaughtException_" + System.currentTimeMillis());
                    test.fail("Screenshot for uncaught exception", MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                } catch (IOException e) {

                }
            } else {

            }
            flushReport();
        });
    }

    // ===================== LIVE FLUSH HELPER =====================
    private static void flushReport() {
        ExtentTestNGReporter.getExtentReportsInstance(); // ensures extent is initialized
        try {
            if (ExtentTestNGReporter.getExtentReportsInstance() != null) {
                synchronized (ExtentTestNGReporter.getExtentReportsInstance()) {
                    ExtentTestNGReporter.getExtentReportsInstance().flush();
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to flush extent report: " + e.getMessage());
        }
    }
}
