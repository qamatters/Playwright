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

            Map<String, Object> progress = mapper.readValue(reportFile, new TypeReference<>() {
            });
            progress.put("suiteEndTime", sdf.format(new Date()));
            progress.put("durationSec", (System.currentTimeMillis() - suiteStartTime) / 1000);

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> tests = (List<Map<String, Object>>) progress.get("tests");

            // ✅ Final reconciliation: overwrite each test’s status with ITestResult
            context.getPassedTests().getAllResults().forEach(r -> reconcileTest(tests, r));
            context.getFailedTests().getAllResults().forEach(r -> reconcileTest(tests, r));
            context.getSkippedTests().getAllResults().forEach(r -> reconcileTest(tests, r));

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

    private void reconcileTest(List<Map<String, Object>> tests, ITestResult result) {
        String testId = result.getTestClass().getName() + "." + result.getMethod().getMethodName() +
                Arrays.toString(result.getParameters());

        tests.stream()
                .filter(t -> t.get("testId").equals(testId))
                .findFirst()
                .ifPresent(testData -> {
                    String status;
                    String failureReason = null;

                    if (result.getStatus() == ITestResult.FAILURE) {
                        status = "Failed";

                        @SuppressWarnings("unchecked")
                        List<String> logs = (List<String>) testData.getOrDefault("logs", new ArrayList<>());

                        failureReason = logs.stream()
                                .filter(log -> log.contains("Validation Failed:") || log.contains("❌"))
                                .findFirst()
                                .orElse(result.getThrowable() != null
                                        ? result.getThrowable().getMessage()
                                        : "Unknown failure");
                    } else if (result.getStatus() == ITestResult.SKIP) {
                        status = "Skipped";
                    } else {
                        status = "Passed";
                    }

                    testData.put("status", status);
                    testData.put("failureReason", failureReason);
                });
    }


    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

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
            ensureReportFileExists(reportFile);

            Map<String, Object> progress = readProgress(reportFile);
            List<Map<String, Object>> tests = getTests(progress);

            String testId = buildTestId(result);
            Map<String, Object> testData = findOrCreateTestData(tests, testId, result);

            List<String> logs = processLogs(testData);
            String finalFailureReason = determineFailureReason(result, logs);

            updateTestStatusAndLogs(result, status, testData, logs, finalFailureReason);

            updateProgressStats(progress, tests);
            writeProgress(reportFile, progress);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ensureReportFileExists(File reportFile) {
        if (!reportFile.exists()) {
            System.err.println("[WARN] Test progress file not found. Initializing new file.");
            initializeRun("Unknown Suite", 1);
        }
    }

    private Map<String, Object> readProgress(File reportFile) throws IOException {
        return mapper.readValue(reportFile, new TypeReference<>() {
        });
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getTests(Map<String, Object> progress) {
        return (List<Map<String, Object>>) progress.get("tests");
    }

    private String buildTestId(ITestResult result) {
        return result.getTestClass().getName() + "." + result.getMethod().getMethodName() +
                Arrays.toString(result.getParameters());
    }

    private Map<String, Object> findOrCreateTestData(List<Map<String, Object>> tests, String testId, ITestResult result) {
        return tests.stream()
                .filter(t -> t.get("testId").equals(testId))
                .findFirst()
                .orElseGet(() -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("testId", testId);
                    map.put("testName", result.getMethod().getMethodName());
                    map.put("className", result.getTestClass().getName());
                    map.put("threadId", Thread.currentThread().getId());
                    map.put("logs", new ArrayList<String>());
                    tests.add(map);
                    return map;
                });
    }

    @SuppressWarnings("unchecked")
    private List<String> processLogs(Map<String, Object> testData) {
        List<Object> existingLogs = (List<Object>) testData.getOrDefault("logs", new ArrayList<>());
        List<String> logs = new ArrayList<>();
        for (Object logEntry : existingLogs) {
            if (logEntry instanceof Map) {
                Map<Object, Object> logMap = (Map<Object, Object>) logEntry;
                String timestamp = logMap.getOrDefault("timestamp", "").toString();
                String message = logMap.getOrDefault("message", "").toString();
                logs.add(timestamp + " - " + message);
            } else {
                logs.add(logEntry.toString());
            }
        }
        return logs;
    }

    private String determineFailureReason(ITestResult result, List<String> logs) {
        String reason = null;
        if (result.getStatus() == ITestResult.FAILURE) {
            reason = logs.stream()
                    .filter(log -> log.contains("Validation Failed:") || log.contains("❌"))
                    .findFirst()
                    .orElse(null);

            if (reason == null && result.getThrowable() != null) {
                reason = result.getThrowable().getMessage();
            }
            if (reason == null) {
                reason = "Test failed";
            }
        }
        return reason;
    }

    private void updateTestStatusAndLogs(ITestResult result, String status, Map<String, Object> testData,
                                         List<String> logs, String finalFailureReason) {
        if (result.getStatus() == ITestResult.FAILURE) {
            logs.add(sdf.format(new Date()) + " - ❌ " + finalFailureReason);
            if (result.getThrowable() != null) {
                String stackTrace = Arrays.stream(result.getThrowable().getStackTrace())
                        .map(StackTraceElement::toString)
                        .limit(5)
                        .collect(Collectors.joining("\n"));
                logs.add(stackTrace);
            }
            testData.put("status", "Failed");
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            testData.put("status", "Passed");
        } else if (result.getStatus() == ITestResult.SKIP) {
            testData.put("status", "Skipped");
        } else {
            testData.put("status", status); // "In Progress"
        }

        testData.put("failureReason", finalFailureReason);
        testData.put("startTime", sdf.format(new Date(result.getStartMillis())));
        testData.put("endTime", (result.getEndMillis() > 0) ? sdf.format(new Date(result.getEndMillis())) : null);
        testData.put("durationSec", (result.getEndMillis() - result.getStartMillis()) / 1000.0);
       testData.put("logs", logs.stream()
           .filter(log -> log.toLowerCase().contains("fail") || log.contains("Error"))
           .collect(Collectors.toList()));
    }

    private void updateProgressStats(Map<String, Object> progress, List<Map<String, Object>> tests) {
        long completed = tests.stream()
                .filter(t -> "Passed".equals(t.get("status")) || "Failed".equals(t.get("status")))
                .count();
        progress.put("testsCompleted", (int) completed);

        int total = (int) progress.getOrDefault("totalTests", 0);
        double percent = (total > 0) ? (completed * 100.0 / total) : 0.0;
        progress.put("progressPercent", String.format("%.2f", percent));
    }

    private void writeProgress(File reportFile, Map<String, Object> progress) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(reportFile, progress);
    }

    // ------------------- History Append -------------------

    private void appendHistory(Map<String, Object> currentRun) {
        try {
            File historyFile = new File(HISTORY_PATH);
            historyFile.getParentFile().mkdirs();

            List<Map<String, Object>> history;
            if (historyFile.exists()) {
                history = mapper.readValue(historyFile, new TypeReference<>() {
                });
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
