package tests.UI.tests.playground.specificUseCases;

import base.BaseUITest;
import base.fields.NetworkLoggerHelper;
import com.microsoft.playwright.Request;
import com.microsoft.playwright.Response;
import org.testng.annotations.Test;

import java.util.List;

public class Test_CaptureNetWorkLogs extends BaseUITest {

    @Test
    public void TestCaptureNetworkLogsDemoQA() {
        // 1️⃣ Navigate to DemoQA WebTables page
        page.navigate("https://demoqa.com/webtables");
        page.waitForTimeout(2000); // wait for page to load

        // 2️⃣ Start network logging
        NetworkLoggerHelper.startLogging(page);

        // 3️⃣ Perform actions to trigger network calls
        page.locator("#addNewRecordButton").click();
        page.locator("#firstName").fill("John");
        page.locator("#lastName").fill("Doe");
        page.locator("#userEmail").fill("john.doe@example.com");
        page.locator("#age").fill("30");
        page.locator("#salary").fill("50000");
        page.locator("#department").fill("QA");
        page.locator("#submit").click();

        page.waitForTimeout(2000); // wait for network requests triggered by form submission

        // 4️⃣ Capture all requests
        List<Request> allRequests = NetworkLoggerHelper.getAllRequests();
        System.out.println("Total Requests Captured: " + allRequests.size());
        allRequests.forEach(req -> System.out.println("Request URL: " + req.url()));

        // 5️⃣ Capture all responses
        List<Response> allResponses = NetworkLoggerHelper.getAllResponses();
        System.out.println("Total Responses Captured: " + allResponses.size());
        allResponses.forEach(res -> System.out.println("Response URL: " + res.url() + " | Status: " + res.status()));

        // 6️⃣ Filter requests by URL keyword
        String keyword = "users";
        List<Request> filteredRequests = NetworkLoggerHelper.getRequestsByUrl(page, keyword);
        System.out.println("Requests containing '" + keyword + "': " + filteredRequests.size());
        filteredRequests.forEach(req -> System.out.println("Filtered Request URL: " + req.url()));

        // 7️⃣ Filter responses by status code
        int statusCode = 200;
        List<Response> successResponses = NetworkLoggerHelper.getResponsesByStatusCode(statusCode);
        System.out.println("Responses with status " + statusCode + ": " + successResponses.size());
        successResponses.forEach(res -> System.out.println("Success Response URL: " + res.url()));

        // 8️⃣ Print summary
        NetworkLoggerHelper.printSummary();

        // 9️⃣ Stop logging
        NetworkLoggerHelper.stopLogging(page);

        // 🔟 Clear logs
        NetworkLoggerHelper.clearLogs();
    }
}
