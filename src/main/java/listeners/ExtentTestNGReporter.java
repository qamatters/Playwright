package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.microsoft.playwright.Page;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.EmailClient;
import utils.EmailReportBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ExtentTestNGReporter: TestNG listener with ExtentReports integration.
 * Added: live flush after each test log for real-time updates.
 * All existing features preserved: screenshots, emails, parameters, offline mode, system info.
 */
public class ExtentTestNGReporter implements ITestListener {

    private static ExtentReports extent;
    private static ConcurrentHashMap<Long, ExtentTest> testsMap = new ConcurrentHashMap<>();
    private static String reportPath = "";
    private static Page page;

    // ===================== ExtentReports Live Flush =====================
    private static void flushReport() {
        try {
            if (extent != null) {
                synchronized (extent) { // thread-safe flush
                    extent.flush();
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to flush extent report: " + e.getMessage());
        }
    }

    @Override
    public void onStart(ITestContext context) {
        try {
            // Ensure reports directory exists
            File reportsDir = new File("reports");
            if (!reportsDir.exists()) reportsDir.mkdirs();

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

            // Timestamped HTML Report
            String reportPath = String.format("reports/ExtentReport-%s.html", dtf.format(now));
            ExtentSparkReporter timestampedReporter = new ExtentSparkReporter(reportPath);
            timestampedReporter.loadXMLConfig("src/test/resources/extent-config.xml");
            timestampedReporter.config().setOfflineMode(true);

            // Default HTML Report (always overwritten)
            String defaultReport = "reports/ExtentReport.html";
            ExtentSparkReporter defaultReporter = new ExtentSparkReporter(defaultReport);
            defaultReporter.loadXMLConfig("src/test/resources/extent-config.xml");
            defaultReporter.config().setOfflineMode(true);

            // JSON Report (timestamped + default)
            String jsonReportPath = String.format("reports/ExtentReport-%s.json", dtf.format(now));
            com.aventstack.extentreports.reporter.JsonFormatter timestampedJsonReporter =
                    new com.aventstack.extentreports.reporter.JsonFormatter(jsonReportPath);

            String defaultJsonPath = "reports/ExtentReport.json";
            com.aventstack.extentreports.reporter.JsonFormatter defaultJsonReporter =
                    new com.aventstack.extentreports.reporter.JsonFormatter(defaultJsonPath);

            // Initialize ExtentReports
            extent = new ExtentReports();
            extent.attachReporter(timestampedReporter, defaultReporter,
                    timestampedJsonReporter, defaultJsonReporter);

            // System info
            extent.setSystemInfo("Tester", System.getProperty("user.name", "qamaters"));
            extent.setSystemInfo("Operating System", System.getProperty("os.name", "Unknown"));
            extent.setSystemInfo("OS Version", System.getProperty("os.version", "Unknown"));
            extent.setSystemInfo("Architecture", System.getProperty("os.arch", "Unknown"));

        } catch (IOException e) {
            System.err.println("Failed to initialize ExtentReports: " + e);
        }
        flushReport();
    }


    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        long threadId = Thread.currentThread().getId();
        testsMap.put(threadId, test);

        test.info("Test started at: " + LocalDateTime.now());
        if (result.getParameters().length > 0) {
            test.info("Test parameters: " + Arrays.toString(result.getParameters()));
        }

        flushReport(); // live flush
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = testsMap.get(Thread.currentThread().getId());
        if (test != null) test.pass("Test passed");
        flushReport(); // live flush
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = testsMap.get(Thread.currentThread().getId());
        if (test != null) {
            Throwable th = result.getThrowable();
            if (th != null) test.fail("Test failed due to exception: " + th.getMessage());
            else test.fail("Test failed with unknown error");

            try {
                String path = ScreenshotUtil.captureScreenshot(result.getMethod().getMethodName() + "_failure");
                test.fail("Failure screenshot",
                        com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            } catch (Exception e) {
                test.warning("Could not attach failure screenshot: " + e.getMessage());
            }
        }
        result.setAttribute("extentLogged", true);
        flushReport(); // live flush
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = testsMap.get(Thread.currentThread().getId());
        if (test != null) test.skip("Test skipped");
        flushReport(); // live flush
    }

    @Override
    public void onFinish(ITestContext context) {
        flushReport();  // final flush
        testsMap.clear();

        try {
            String htmlReportBody = EmailReportBuilder.generateHtmlReportBody(context);
            String username = System.getProperty("smtp.username");
            String password = System.getProperty("smtp.password");

            if (username != null && password != null) {
                EmailClient.sendEmailWithReport(htmlReportBody, reportPath, username, password);
            }
        } catch (FileNotFoundException e) {
            System.out.println("smtp.properties not found, skipping email");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO Error reading smtp.properties file, skipping email");
        }
    }

    // ===================== PAGE ACCESSORS =====================
    public static void setPage(Page p) {
        page = p;
    }

    public static Page getPage() {
        return page;
    }

    // ===================== TEST ACCESSORS =====================
    public static ExtentTest getTest() {
        return testsMap.get(Thread.currentThread().getId());
    }

    public static String getReportPath() {
        return reportPath;
    }

    // ===================== EXTENT REPORT INSTANCE =====================
    public static ExtentReports getExtentReportsInstance() {
        return extent;
    }
}
