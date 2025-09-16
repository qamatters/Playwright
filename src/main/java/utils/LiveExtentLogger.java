package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LiveExtentLogger {

    private static final Path LIVE_PATH = Paths.get("reports/test-report/test-progress.json");
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String testName, String message, String status) {
        try {
            Map<String, Object> progress;
            if (Files.exists(LIVE_PATH)) {
                progress = mapper.readValue(LIVE_PATH.toFile(), new TypeReference<>() {});
            } else {
                progress = new HashMap<>();
                progress.put("tests", new ArrayList<Map<String, Object>>());
                progress.put("inProgress", true); // mark test run in progress
            }

            List<Map<String, Object>> tests = (List<Map<String,Object>>) progress.get("tests");

            Map<String, Object> testEntry = tests.stream()
                    .filter(t -> t.get("testName").equals(testName))
                    .findFirst()
                    .orElseGet(() -> {
                        Map<String,Object> newEntry = new HashMap<>();
                        newEntry.put("testName", testName);
                        newEntry.put("logs", new ArrayList<Map<String, String>>());
                        newEntry.put("status", "Running");
                        newEntry.put("startTime", dtf.format(LocalDateTime.now()));
                        tests.add(newEntry);
                        return newEntry;
                    });

            // Add timestamp to each log
            Map<String, String> logEntry = new HashMap<>();
            logEntry.put("timestamp", dtf.format(LocalDateTime.now()));
            logEntry.put("message", message);

            ((List<Map<String, String>>)testEntry.get("logs")).add(logEntry);

            if (status != null) testEntry.put("status", status);

            // Update totals
            int totalTests = tests.size();
            progress.put("totalTests", totalTests);

            long completed = tests.stream()
                    .filter(t -> "Passed".equals(t.get("status")) || "Failed".equals(t.get("status")))
                    .count();
            progress.put("testsCompleted", completed);

            mapper.writerWithDefaultPrettyPrinter().writeValue(LIVE_PATH.toFile(), progress);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Call this to mark end of test run
    public static void markComplete() {
        try {
            if (!Files.exists(LIVE_PATH)) return;
            Map<String, Object> progress = mapper.readValue(LIVE_PATH.toFile(), new TypeReference<>() {});
            progress.put("inProgress", false);
            mapper.writerWithDefaultPrettyPrinter().writeValue(LIVE_PATH.toFile(), progress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
