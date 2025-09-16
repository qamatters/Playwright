package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class CSVLiveTestListener implements ITestListener {

    private AtomicInteger totalTests = new AtomicInteger(0);
    private AtomicInteger testsCompleted = new AtomicInteger(0);
    private String suiteName;
    private FileWriter writer;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onStart(ITestContext context) {
        try {
            suiteName = context.getName();
            totalTests.set(context.getAllTestMethods().length);

            writer = new FileWriter("test-progress.csv", true); // append mode
            writer.append("Suite Name,Test Name,Status,Start Time,End Time,Progress %,Logs\n");
            writer.flush();

            System.out.println("CSV Live Tracking Started for suite: " + suiteName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        try {
            String testName = result.getMethod().getMethodName();
            String startTime = LocalDateTime.now().format(dtf);

            writer.append(String.format("%s,%s,%s,%s,%s,%s,%s\n",
                    suiteName,
                    testName,
                    "Running",
                    startTime,
                    "",
                    getProgress(),
                    ""));

            writer.flush();

            System.out.println("Test started: " + testName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        writeTestResult(result, "Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        writeTestResult(result, "Failed");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        writeTestResult(result, "Skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        try {
            writer.flush();
            writer.close();
            System.out.println("CSV Live Tracking Finished for suite: " + suiteName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeTestResult(ITestResult result, String status) {
        try {
            String testName = result.getMethod().getMethodName();
            String endTime = LocalDateTime.now().format(dtf);

            writer.append(String.format("%s,%s,%s,%s,%s,%s,%s\n",
                    suiteName,
                    testName,
                    status,
                    "", // Start Time already recorded; or you can implement tracking in-memory
                    endTime,
                    getProgress(),
                    "")); // Logs can be added here if needed

            writer.flush();

            testsCompleted.incrementAndGet();

            System.out.println("Test " + testName + " " + status);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getProgress() {
        int completed = testsCompleted.get();
        int total = totalTests.get();
        int percent = total == 0 ? 0 : (int) ((completed * 100.0) / total);
        return percent + "%";
    }
}


