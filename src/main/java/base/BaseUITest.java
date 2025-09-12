package base;

import com.aventstack.extentreports.Status;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RecordVideoSize;
import listeners.ExtentTestNGReporter;
import listeners.ReportUtil;
import listeners.ScreenshotUtil;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.Logger;
import utils.enums.BrowserEngine;
import utils.enums.LogMode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class BaseUITest {
    protected static final Path REPORTS_DIR = Paths.get("reports");
    protected static final Path TRACE_PATH = REPORTS_DIR.resolve("trace/trace.zip");
    protected static final Path VIDEO_DIR = REPORTS_DIR.resolve("videos");
    protected boolean videoRecording;
    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext context;
    public static Page page;
    protected LogMode logMode;
    protected BrowserEngine browserType;
    protected String url;
    protected boolean headless;

    @BeforeClass(alwaysRun = true)
    @Parameters({"logMode", "url", "browser", "headless", "videoRecording"})
    public void baseSetup(String mode, String url, String browserType, String headless, String videoRecording) throws IOException {
        // Ensure reports/videos and reports/trace directories exist
        Files.createDirectories(VIDEO_DIR);
        Files.createDirectories(TRACE_PATH.getParent());

        playwright = Playwright.create();
        this.logMode = LogMode.parse(System.getProperty("logMode", mode));
        this.url = url;
        this.browserType = BrowserEngine.parse(System.getProperty("browser", browserType));
       this.headless = Boolean.parseBoolean(System.getProperty("headless", String.valueOf(headless)).toLowerCase());
        this.videoRecording = Boolean.parseBoolean(System.getProperty("videoRecording", String.valueOf(videoRecording)).toLowerCase());
        if (this.videoRecording) {
            Logger.log("WARNING: Video recording is discouraged and should only be enabled for debugging!", logMode);
        }
        Logger.log("Testing class " + this.getClass().getSimpleName(), logMode);

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(this.headless);
        switch (this.browserType) {
            case Firefox -> browser = playwright.firefox().launch(options);
            case Webkit -> browser = playwright.webkit().launch(options);
            default -> browser = playwright.chromium().launch(options);
        }

        Browser.NewContextOptions contextOptions = null;
        if (this.videoRecording) {
            contextOptions = new Browser.NewContextOptions()
                    .setRecordVideoDir(VIDEO_DIR)
                    .setRecordVideoSize(new RecordVideoSize(1280, 720));
        } else {
            contextOptions = new Browser.NewContextOptions();
        }

        context = browser.newContext(contextOptions);
        startTracing();
        page = context.newPage();

        ScreenshotUtil.setPage(page);
        ReportUtil.setPage(page);
    }

    public void startTracing() {
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
    }

    public void stopTracingAndSave() {
        try {
            context.tracing().stop(new Tracing.StopOptions()
                    .setPath(TRACE_PATH));
        } catch (Exception e) {
            System.out.println("Failed to save trace: " + e.getMessage());
        }
    }

    public void openTraceViewer() throws IOException, InterruptedException {
        new ProcessBuilder("npx", "playwright", "show-trace", TRACE_PATH.toString())
                .inheritIO()
                .start()
                .waitFor();
    }

    @AfterClass(alwaysRun = true)
    public void baseTeardown() {
        stopTracingAndSave();

        if (page != null) {
            try {
                Video video = page.video();
                if (video != null) {
                    Path videoPath = VIDEO_DIR.resolve("test-video.webm");
                    video.saveAs(videoPath);
                    Logger.log("Video saved at " + videoPath, logMode);
                }
            } catch (Exception e) {
                System.out.println("Video save failed: " + e.getMessage());
            }
            page.close();
        }

        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();

        context = null;
        page = null;
        browser = null;
        playwright = null;

        Logger.log("Executed tests class " + this.getClass().getSimpleName(), logMode);
    }

    @BeforeMethod(alwaysRun = true)
    public void setupLogger() {
        Logger.increment_test_number();
        ReportUtil.initSoftAssert();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            ReportUtil.assertAll();
        } catch (AssertionError e) {
            System.out.println("Assertion failure details: " + e.getMessage());
            if (result.getAttribute("extentLogged") == null) {
                if (result.getStatus() == ITestResult.SUCCESS) {
                    result.setStatus(ITestResult.FAILURE);
                    result.setThrowable(e);
                }
                ExtentTestNGReporter.getTest().log(
                        Status.FAIL,
                        "Test failed due to assertion error(s). See step-level details above."
                );
            }
            throw e;
        } finally {
            ReportUtil.logInfo("Test Case Finished");
        }
    }
}
