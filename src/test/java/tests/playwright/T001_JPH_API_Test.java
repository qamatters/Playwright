package tests.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
//import org.junit.jupiter.api.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

//import static org.junit.jupiter.api.Assertions.*;

public class T001_JPH_API_Test {

    static Playwright playwright;
    static APIRequestContext api;

    @BeforeClass
    static void setup() {
        playwright = Playwright.create();
        api = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL("https://jsonplaceholder.typicode.com"));
    }

    @AfterClass
    static void teardown() {
        api.dispose();
        playwright.close();
    }

    @Test
    public void getSinglePost() {
        APIResponse response = api.get("/posts/1");

        assertEquals(response.status(),200);
        String json = response.text();
        assertTrue(json.contains("\"id\": 1"));
    }

    @Test
    public void createNewPost() {
        APIResponse response = api.post("/posts", RequestOptions.create()
                .setData("{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1}")
                .setHeader("Content-type", "application/json; charset=UTF-8"));

        assertEquals(response.status(),201);
        String body = response.text();
        assertTrue(body.contains("\"id\""));
    }

    @Test
    public void updatePost() {
        APIResponse response = api.put("/posts/1", RequestOptions.create()
                .setData("{\"id\":1, \"title\": \"updated\", \"body\": \"data\", \"userId\":1}")
                .setHeader("Content-type", "application/json; charset=UTF-8"));

        assertEquals(response.status(),200);
        String json = response.text();
        assertTrue(json.contains("\"title\": \"updated\""));
    }

    @Test
    public void deletePost() {
        APIResponse response = api.delete("/posts/1");
        assertEquals(response.status(),200);
    }
}
