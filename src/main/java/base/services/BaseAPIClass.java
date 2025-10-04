package base.services;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;

public abstract class BaseAPIClass {
    protected static Playwright playwright;
    protected static APIRequestContext request;
    protected String baseURL;

    public void init(String baseURL, String username, String password) {
        this.baseURL = baseURL;
        playwright = Playwright.create();
        request = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL(baseURL)
                        .setHttpCredentials(username, password)
        );
    }

    public static void close() {
        if (playwright != null) {
            playwright.close();
        }
    }
}