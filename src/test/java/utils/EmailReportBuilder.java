package utils;

import org.testng.*;
import java.util.*;

public class EmailReportBuilder {

    public static String generateHtmlReportBody(ITestContext context) {
        int total = context.getAllTestMethods().length;
        int passed = context.getPassedTests().size();
        int failed = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();

        StringBuilder html = new StringBuilder();

        html.append("<html><body>");
        html.append("<h2 style='color:#2E86C1;'>Mocker Test Report</h2>");

        // Summary
        html.append("<h3>Test Summary</h3>");
        html.append("<table border='1' cellpadding='8' cellspacing='0' style='border-collapse:collapse;'>");
        html.append("<tr><th>Total</th><th>Passed</th><th>Failed</th><th>Skipped</th></tr>");
        html.append("<tr>");
        html.append("<td>").append(total).append("</td>");
        html.append("<td style='color:green;'>").append(passed).append("</td>");
        html.append("<td style='color:red;'>").append(failed).append("</td>");
        html.append("<td style='color:orange;'>").append(skipped).append("</td>");
        html.append("</tr></table><br>");

        // Detailed table
        html.append("<h3>Test Details</h3>");
        html.append("<table border='1' cellpadding='8' cellspacing='0' style='border-collapse:collapse;'>");
        html.append("<tr style='background-color:#f2f2f2;'>");
        html.append("<th>Test Case</th><th>Status</th><th>Duration (ms)</th></tr>");

        List<ITestResult> allResults = new ArrayList<>();
        allResults.addAll(context.getPassedTests().getAllResults());
        allResults.addAll(context.getFailedTests().getAllResults());
        allResults.addAll(context.getSkippedTests().getAllResults());

        for (ITestResult result : allResults) {
            String testName = result.getMethod().getMethodName();
            String status;
            String color;

            switch (result.getStatus()) {
                case ITestResult.SUCCESS:
                    status = "Passed";
                    color = "green";
                    break;
                case ITestResult.FAILURE:
                    status = "Failed";
                    color = "red";
                    break;
                case ITestResult.SKIP:
                    status = "Skipped";
                    color = "orange";
                    break;
                default:
                    status = "Unknown";
                    color = "gray";
                    break;
            }

            long duration = result.getEndMillis() - result.getStartMillis();

            html.append("<tr>");
            html.append("<td>").append(testName).append("</td>");
            html.append("<td style='color:").append(color).append(";'>").append(status).append("</td>");
            html.append("<td>").append(duration).append("</td>");
            html.append("</tr>");
        }

        html.append("</table>");
        html.append("</body></html>");

        return html.toString();
    
    }
}
