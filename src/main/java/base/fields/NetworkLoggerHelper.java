package base.fields;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Request;
import com.microsoft.playwright.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NetworkLoggerHelper {

    private static final List<Request> capturedRequests = new ArrayList<>();
    private static final List<Response> capturedResponses = new ArrayList<>();

    /**
     * Start capturing network requests and responses on the given page
     */
    public static void startLogging(Page page) {
        clearLogs();

        page.onRequest(request -> {
            capturedRequests.add(request);
        });

        page.onResponse(response -> {
            capturedResponses.add(response);
        });
    }

    /**
     * Stop logging network events
     */
    public static void stopLogging(Page page) {
        page.offRequest(request -> capturedRequests.remove(request));
        page.offResponse(response -> capturedResponses.remove(response));
    }

    /**
     * Get all captured requests
     */
    public static List<Request> getAllRequests() {
        return new ArrayList<>(capturedRequests);
    }

    /**
     * Get all captured responses
     */
    public static List<Response> getAllResponses() {
        return new ArrayList<>(capturedResponses);
    }

    /**
     * Get requests filtered by URL containing a keyword
     */
    public static List<Request> getRequestsByUrl(Page page, String keyword) {
        return capturedRequests.stream()
                .filter(req -> req.url().contains(keyword))
                .collect(Collectors.toList());
    }

    /**
     * Get responses filtered by URL containing a keyword
     */
    public static List<Response> getResponsesByUrl(Page page, String keyword) {
        return capturedResponses.stream()
                .filter(res -> res.url().contains(keyword))
                .collect(Collectors.toList());
    }

    /**
     * Get responses by status code
     */
    public static List<Response> getResponsesByStatusCode(int statusCode) {
        return capturedResponses.stream()
                .filter(res -> res.status() == statusCode)
                .collect(Collectors.toList());
    }

    /**
     * Clear all captured logs
     */
    public static void clearLogs() {
        capturedRequests.clear();
        capturedResponses.clear();
    }

    /**
     * Print summary of captured network activity
     */
    public static void printSummary() {
        System.out.println("==== Network Logs Summary ====");
        System.out.println("Total Requests: " + capturedRequests.size());
        System.out.println("Total Responses: " + capturedResponses.size());

        capturedResponses.forEach(res -> {
            System.out.println("URL: " + res.url() + " | Status: " + res.status());
        });
        System.out.println("==============================");
    }
}
