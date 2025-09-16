package base;

import com.aventstack.extentreports.Status;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
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
import java.util.concurrent.*;

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

        Browser.NewContextOptions contextOptions;
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

        // Initialize LiveExtentReporter global exception handling
        ReportUtil.initGlobalExceptionHandler();
        // Optional: Enable screenshots for all passed steps
        ReportUtil.enablePassScreenshots(true);
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
        try {
            long start = System.currentTimeMillis();
            stopTracingAndSave();
            Logger.log("Tracing stopped in " + (System.currentTimeMillis() - start) + "ms", logMode);

            start = System.currentTimeMillis();
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

                page.onDialog(dialog -> {
                    Logger.log("Unexpected dialog dismissed during teardown: " + dialog.message(), logMode);
                    dialog.dismiss();
                });

                try {
                    Logger.log("Waiting for network idle before closing page...", logMode);
                    page.waitForLoadState(LoadState.NETWORKIDLE, new Page.WaitForLoadStateOptions().setTimeout(5000));
                    Logger.log("Network idle reached", logMode);
                } catch (Exception e) {
                    Logger.log("Network idle wait failed: " + e.getMessage(), logMode);
                }

                page.close();
            }
            Logger.log("Video and page close in " + (System.currentTimeMillis() - start) + "ms", logMode);

            start = System.currentTimeMillis();
            if (context != null) {
                Logger.log("Closing context...", logMode);
                context.close();
                Logger.log("Context closed in " + (System.currentTimeMillis() - start) + "ms", logMode);
            }

            start = System.currentTimeMillis();
            if (browser != null) {
                Logger.log("Checking for open pages before closing browser", logMode);
                browser.contexts().forEach(ctx -> ctx.pages().forEach(p ->
                        Logger.log("Page URL: " + p.url(), logMode)
                ));
                Logger.log("Ensure all downloads are completed during test execution; Playwright Java doesn't expose download lists.", logMode);

                Logger.log("Attempting to close browser with timeout wrapper", logMode);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Future<?> future = executor.submit(() -> {
                    try {
                        Logger.log("Closing browser...", logMode);
                        browser.close();
                        Logger.log("Browser closed successfully", logMode);
                    } catch (Exception e) {
                        Logger.log("Browser close failed: " + e.getMessage(), logMode);
                    }
                });

                try {
                    future.get(5, TimeUnit.SECONDS);
                } catch (TimeoutException te) {
                    Logger.log("Browser close timed out after 5 seconds, forcing shutdown", logMode);
                    future.cancel(true);
                } catch (Exception e) {
                    Logger.log("Exception during browser close: " + e.getMessage(), logMode);
                } finally {
                    executor.shutdownNow();
                }
            }

            start = System.currentTimeMillis();
            if (playwright != null) {
                Logger.log("Closing playwright...", logMode);
                playwright.close();
                Logger.log("Playwright closed in " + (System.currentTimeMillis() - start) + "ms", logMode);
            }

        } catch (Exception e) {
            Logger.log("Exception in baseTeardown: " + e.getMessage(), logMode);
            System.out.println("Exception in baseTeardown: " + e.getMessage());
        } finally {
            context = null;
            page = null;
            browser = null;
            playwright = null;
            Logger.log("Executed tests class " + this.getClass().getSimpleName(), logMode);
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void setupLogger() {
        Logger.increment_test_number();
        ReportUtil.initSoftAssert();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            System.out.println("Asserting all");
            ReportUtil.assertAll();
            System.out.println("Assertion validation completed for all use cases");
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
