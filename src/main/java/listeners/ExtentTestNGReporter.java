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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class ExtentTestNGReporter implements ITestListener {

    private static ExtentReports extent;
    protected static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static String reportPath = "";
    private static Page page;

    @Override
    public void onStart(ITestContext context) {
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

            // Timestamped report
            reportPath = String.format("reports/ExtentReport-%s.html", dtf.format(now));
            ExtentSparkReporter timestampedReporter = new ExtentSparkReporter(reportPath);
            timestampedReporter.loadXMLConfig("src/test/resources/extent-config.xml");
            timestampedReporter.config().setOfflineMode(true); // <-- embed CSS/JS for Jenkins

            // Default report (always overwritten)
            String defaultReport = "reports/ExtentReport.html";
            ExtentSparkReporter defaultReporter = new ExtentSparkReporter(defaultReport);
            defaultReporter.loadXMLConfig("src/test/resources/extent-config.xml");
            defaultReporter.config().setOfflineMode(true); // <-- embed CSS/JS for Jenkins

            // Initialize ExtentReports and attach both reporters
            extent = new ExtentReports();
            extent.attachReporter(timestampedReporter, defaultReporter);

            // System info
            extent.setSystemInfo("Tester", System.getProperty("user.name", "qamaters"));
            extent.setSystemInfo("Operating System", System.getProperty("os.name", "Unknown"));
            extent.setSystemInfo("OS Version", System.getProperty("os.version", "Unknown"));
            extent.setSystemInfo("Architecture", System.getProperty("os.arch", "Unknown"));

        } catch (IOException e) {
            System.err.println("Failed to initialize ExtentReports: " + e);
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
        test.get().info("Test started at: " + LocalDateTime.now());

        if (result.getParameters().length > 0) {
            test.get().info("Test parameters: " + Arrays.toString(result.getParameters()));
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // nothing special
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest t = test.get();
        if (t != null) {
            Throwable th = result.getThrowable();
            if (th != null) t.fail("Test failed due to exception: " + th.getMessage());
            else t.fail("Test failed with unknown error");

            try {
                String path = ScreenshotUtil.captureScreenshot(result.getMethod().getMethodName() + "_failure");
                t.fail("Failure screenshot",
                        com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            } catch (Exception e) {
                t.warning("Could not attach failure screenshot: " + e.getMessage());
            }
        }
        result.setAttribute("extentLogged", true);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().skip("Test skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
        test.remove();

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
    public static void setPage(Page p) { page = p; }
    public static Page getPage() { return page; }
    public static ExtentTest getTest() { return test.get(); }
    public static String getReportPath() { return reportPath; }
}
