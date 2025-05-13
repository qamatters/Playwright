package listeners;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.testng.*;

public class ExtentTestNGReporter implements ITestListener {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        try {
            final LocalDateTime now = LocalDateTime.now();

            final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            final ExtentSparkReporter reporter = new ExtentSparkReporter(
                    String.format("test-output/reports/ExtentReport-%s.html", dtf.format(now)));
            reporter.loadXMLConfig("src/test/resources/extent-config.xml");

            extent = new ExtentReports();
            extent.attachReporter(reporter);
            extent.setSystemInfo("Tester", System.getProperty("user.name","Khushal Jangid"));
            extent.setSystemInfo("Operating System", System.getProperty("os.name","Unknown"));
            extent.setSystemInfo("OS Version", System.getProperty("os.version","Unknown"));
            extent.setSystemInfo("Architecture", System.getProperty("os.arch","Unknown"));
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().pass(String.format("Hash: %s, ClassName: %s, TestName: %s",result.hashCode(),result.getInstanceName(),result.getName()));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().fail(String.format("Test failed : %s",result.getThrowable()));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().skip("Test skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}