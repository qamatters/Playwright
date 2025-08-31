package listeners;

import com.microsoft.playwright.Page;

import java.io.File;
import java.io.IOException;

public class ScreenshotUtil {

    private static Page page;

    public static void setPage(Page pageInstance) {
        page = pageInstance;
    }

    public static String captureScreenshot(String testName) throws IOException {
        if (page == null) return null;

        String relativePath = "screenshots/" + testName + ".png";
        File destFile = new File("reports/" + relativePath);
        destFile.getParentFile().mkdirs();

        page.screenshot(new Page.ScreenshotOptions().setPath(destFile.toPath()));
        return relativePath.replace("\\", "/"); // Forward slashes for ExtentReports
    }
}
