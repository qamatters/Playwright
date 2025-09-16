package listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JsonLiveTestListener implements ITestListener {

    private static final String REPORT_PATH = "reports/test-report/test-progress.json";
    private static final String HISTORY_PATH = "reports/test-report/history.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // ------------------- TestNG Listener Hooks -------------------

    @Override
    public void onStart(ITestContext context) {
        int totalTests = context.getAllTestMethods().length;
        initializeRun(context.getName(), totalTests);
    }

    @Override
    public void onTestStart(ITestResult result) {
        updateTestProgress(result.getName(), "Running", null, result.getStartMillis(), null, null);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        updateTestProgress(result.getName(), "Passed", null, result.getStartMillis(), result.getEndMillis(), null);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String failureReason = result.getThrowable() != null ? result.getThrowable().toString() : "Unknown Error";
        updateTestProgress(result.getName(), "Failed", failureReason, result.getStartMillis(), result.getEndMillis(), failureReason);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        updateTestProgress(result.getName(), "Skipped", null, result.getStartMillis(), result.getEndMillis(), null);
    }

    @Override public void onFinish(ITestContext context) {}
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

    // ------------------- Initialization -------------------

    private void initializeRun(String suiteName, int totalTests) {
        try {
            File reportFile = new File(REPORT_PATH);
            reportFile.getParentFile().mkdirs();

            Map<String, Object> progress = new HashMap<>();
            progress.put("suiteName", suiteName);
            progress.put("totalTests", totalTests);
            progress.put("testsCompleted", 0);
            progress.put("runTimestamp", sdf.format(new Date()));
            progress.put("tests", new ArrayList<Map<String, Object>>());

            mapper.writerWithDefaultPrettyPrinter().writeValue(reportFile, progress);
            System.out.println("[INFO] Test run initialized: " + suiteName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ------------------- Progress Update -------------------

    private synchronized void updateTestProgress(String testName, String status, String failureReason,
                                                 Long startMillis, Long endMillis, String failureLog) {
        try {
            File reportFile = new File(REPORT_PATH);
            if (!reportFile.exists()) {
                System.err.println("[WARN] Test progress file not found. Initializing new file.");
                initializeRun("Unknown Suite", 1);
            }

            Map<String, Object> progress = mapper.readValue(reportFile, new TypeReference<Map<String, Object>>() {});

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> tests = (List<Map<String, Object>>) progress.get("tests");

            Optional<Map<String, Object>> existing = tests.stream()
                    .filter(t -> t.get("testName").equals(testName))
                    .findFirst();

            Map<String, Object> testData = existing.orElseGet(() -> {
                Map<String, Object> map = new HashMap<>();
                map.put("testName", testName);
                map.put("logs", new ArrayList<String>());
                tests.add(map);
                return map;
            });

            testData.put("status", status);
            testData.put("startTime", startMillis != null ? sdf.format(new Date(startMillis)) : null);
            testData.put("endTime", endMillis != null ? sdf.format(new Date(endMillis)) : null);
            testData.put("durationSec", (startMillis != null && endMillis != null) ? (endMillis - startMillis) / 1000 : 0);

            if (failureReason != null) {
                testData.put("failureReason", failureReason);
                @SuppressWarnings("unchecked")
                List<String> logs = (List<String>) testData.get("logs");
                logs.add(failureReason);
            } else {
                testData.put("failureReason", null);
            }

            long completed = tests.stream()
                    .filter(t -> "Passed".equals(t.get("status")) || "Failed".equals(t.get("status")))
                    .count();
            progress.put("testsCompleted", (int) completed);

            mapper.writerWithDefaultPrettyPrinter().writeValue(reportFile, progress);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
