package listeners;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class JsonLiveTestListener implements ITestListener {

    private static final String REPORT_PATH = "reports/test-report/test-progress.json";
    private static final String HISTORY_PATH = "reports/test-report/history.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private long suiteStartTime;

    // ------------------- TestNG Listener Hooks -------------------

    @Override
    public void onStart(ITestContext context) {
        suiteStartTime = System.currentTimeMillis();
        int totalTests = context.getAllTestMethods().length;
        initializeRun(context.getName(), totalTests);
    }

    @Override
    public void onTestStart(ITestResult result) {
        updateTestProgress(result, "Running", null);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        updateTestProgress(result, "Passed", null);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String failureReason = result.getThrowable() != null
                ? result.getThrowable().toString()
                : "Unknown Error";

        updateTestProgress(result, "Failed", failureReason);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        updateTestProgress(result, "Skipped", null);
    }

    @Override
    public void onFinish(ITestContext context) {
        try {
            File reportFile = new File(REPORT_PATH);
            if (!reportFile.exists()) return;

            Map<String, Object> progress = mapper.readValue(reportFile, new TypeReference<>() {});
            progress.put("suiteEndTime", sdf.format(new Date()));
            progress.put("durationSec", (System.currentTimeMillis() - suiteStartTime) / 1000);

            // derive overall status
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> tests = (List<Map<String, Object>>) progress.get("tests");

            boolean hasFailed = tests.stream().anyMatch(t -> "Failed".equals(t.get("status")));
            boolean hasSkipped = tests.stream().anyMatch(t -> "Skipped".equals(t.get("status")));

            String overallStatus;
            if (hasFailed) {
                overallStatus = "Failed";
            } else if (hasSkipped) {
                overallStatus = "Skipped";
            } else {
                overallStatus = "Passed";
            }
            progress.put("overallStatus", overallStatus);

            // write final report
            mapper.writerWithDefaultPrettyPrinter().writeValue(reportFile, progress);

            // append to history.json
            appendHistory(progress);

            System.out.println("[INFO] Suite finished. Report and history updated.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

    // ------------------- Initialization -------------------

    private void initializeRun(String suiteName, int totalTests) {
        try {
            File reportFile = new File(REPORT_PATH);
            reportFile.getParentFile().mkdirs();

            Map<String, Object> progress = new LinkedHashMap<>();
            progress.put("suiteName", suiteName);
            progress.put("totalTests", totalTests);
            progress.put("testsCompleted", 0);
            progress.put("runTimestamp", sdf.format(new Date()));
            progress.put("suiteStartTime", sdf.format(new Date(suiteStartTime)));
            progress.put("tests", new ArrayList<Map<String, Object>>());

            mapper.writerWithDefaultPrettyPrinter().writeValue(reportFile, progress);
            System.out.println("[INFO] Test run initialized: " + suiteName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ------------------- Progress Update -------------------

    private synchronized void updateTestProgress(ITestResult result, String status, String failureReason) {
        try {
            File reportFile = new File(REPORT_PATH);
            if (!reportFile.exists()) {
                System.err.println("[WARN] Test progress file not found. Initializing new file.");
                initializeRun("Unknown Suite", 1);
            }

            Map<String, Object> progress = mapper.readValue(reportFile, new TypeReference<>() {});

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> tests = (List<Map<String, Object>>) progress.get("tests");

            String testId = result.getTestClass().getName() + "." + result.getMethod().getMethodName() +
                    Arrays.toString(result.getParameters());

            Optional<Map<String, Object>> existing = tests.stream()
                    .filter(t -> t.get("testId").equals(testId))
                    .findFirst();

            Map<String, Object> testData = existing.orElseGet(() -> {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("testId", testId);
                map.put("testName", result.getMethod().getMethodName());
                map.put("className", result.getTestClass().getName());
                map.put("threadId", Thread.currentThread().getId());
                map.put("logs", new ArrayList<String>());
                tests.add(map);
                return map;
            });

            testData.put("status", status);
            testData.put("startTime", sdf.format(new Date(result.getStartMillis())));
            testData.put("endTime", (result.getEndMillis() > 0) ? sdf.format(new Date(result.getEndMillis())) : null);
            testData.put("durationSec", (result.getEndMillis() - result.getStartMillis()) / 1000.0);

            if (failureReason != null) {
                testData.put("failureReason", failureReason);

                String stackTrace = result.getThrowable() != null
                        ? Arrays.stream(result.getThrowable().getStackTrace())
                        .map(StackTraceElement::toString)
                        .limit(5) // capture only first 5 lines
                        .collect(Collectors.joining("\n"))
                        : "";

                @SuppressWarnings("unchecked")
                List<String> logs = (List<String>) testData.get("logs");
                logs.add(failureReason);
                if (!stackTrace.isEmpty()) logs.add(stackTrace);
            } else {
                testData.put("failureReason", null);
            }

            long completed = tests.stream()
                    .filter(t -> "Passed".equals(t.get("status")) || "Failed".equals(t.get("status")))
                    .count();
            progress.put("testsCompleted", (int) completed);

            int total = (int) progress.getOrDefault("totalTests", 0);
            double percent = (total > 0) ? (completed * 100.0 / total) : 0.0;
            progress.put("progressPercent", String.format("%.2f", percent));

            mapper.writerWithDefaultPrettyPrinter().writeValue(reportFile, progress);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ------------------- History Append -------------------

    private void appendHistory(Map<String, Object> currentRun) {
        try {
            File historyFile = new File(HISTORY_PATH);
            historyFile.getParentFile().mkdirs();

            List<Map<String, Object>> history;
            if (historyFile.exists()) {
                history = mapper.readValue(historyFile, new TypeReference<>() {});
            } else {
                history = new ArrayList<>();
            }

            // store only summary in history
            Map<String, Object> summary = new LinkedHashMap<>();
            summary.put("suiteName", currentRun.get("suiteName"));
            summary.put("runTimestamp", currentRun.get("runTimestamp"));
            summary.put("totalTests", currentRun.get("totalTests"));
            summary.put("testsCompleted", currentRun.get("testsCompleted"));
            summary.put("durationSec", currentRun.get("durationSec"));
            summary.put("overallStatus", currentRun.get("overallStatus"));

            history.add(summary);

            mapper.writerWithDefaultPrettyPrinter().writeValue(historyFile, history);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
