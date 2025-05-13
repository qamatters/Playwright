package tests.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
//import org.junit.jupiter.api.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import utils.Logger;

//import static org.junit.jupiter.api.Assertions.*;

public class T001_JPH_API_Test {

    static Playwright playwright;
    static APIRequestContext api;

    @BeforeClass
    static void setup() {
        System.out.println("Testing class T001_JPH_API_Test");
        playwright = Playwright.create();
        api = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL("https://jsonplaceholder.typicode.com"));
    }

    @AfterClass
    static void teardown() {
        api.dispose();
        playwright.close();
        System.out.println("Executed tests class T001_JPH_API_Test");
    }

    @BeforeMethod
    static void setup_logger() {
        Logger.increment_test_number();
    }

    @Test
    public void getSinglePost() {
        Logger.log("Testing T001_JPH_API_Test.getSinglePost");
        APIResponse response = api.get("/posts/1");
        Logger.log("GET Request at /posts/1 completed with response status : " + response.status());
        assertEquals(response.status(), 200);
        String json = response.text();
        assertTrue(json.contains("\"id\": 1"));
        Logger.log(json.contains("\"id\": 1") ? "Test T001_JPH_API_Test.getSinglePost passed"
                : "Test T001_JPH_API_Test.getSinglePost failed");
    }

    @Test
    public void createNewPost() {
        Logger.log("Testing T001_JPH_API_Test.createNewPost");
        APIResponse response = api.post("/posts", RequestOptions.create()
                .setData("{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1}")
                .setHeader("Content-type", "application/json; charset=UTF-8"));
        Logger.log("POST Request at /posts completed with response status : " + response.status());

        assertEquals(response.status(), 201);
        String json = response.text();
        assertTrue(json.contains("\"id\""));
        Logger.log(json.contains("\"id\"") ? "Test T001_JPH_API_Test.createNewPost passed"
                : "Test T001_JPH_API_Test.createNewPost failed");
    }

    @Test
    public void updatePost() {
        Logger.log("Testing T001_JPH_API_Test.updatePost");
        APIResponse response = api.put("/posts/1", RequestOptions.create()
                .setData("{\"id\":1, \"title\": \"updated\", \"body\": \"data\", \"userId\":1}")
                .setHeader("Content-type", "application/json; charset=UTF-8"));
        Logger.log("PUT Request at /posts/1 completed with response status : " + response.status());

        assertEquals(response.status(), 200);
        String json = response.text();
        assertTrue(json.contains("\"title\": \"updated\""));
        Logger.log(json.contains("\"title\": \"updated\"") ? "Test T001_JPH_API_Test.updatePost passed"
                : "Test T001_JPH_API_Test.updatePost failed");
    }

    @Test
    public void deletePost() {
        Logger.log("Testing T001_JPH_API_Test.deletePost");
        APIResponse response = api.delete("/posts/1");
        Logger.log("DELETE Request at /posts/1 completed with response status : " + response.status());
        assertEquals(response.status(), 200);
        Logger.log(response.status() == 200 ? "Test T001_JPH_API_Test.deletePost passed"
                : "Test T001_JPH_API_Test.deletePost failed");
    }
}
