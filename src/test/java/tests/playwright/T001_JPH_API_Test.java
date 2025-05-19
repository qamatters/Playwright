package tests.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
//import org.junit.jupiter.api.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import utils.Logger;
import utils.enums.LogMode;

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
    @Parameters({"logMode"})
    public void getSinglePost(String mode) {
        LogMode l = LogMode.parse(mode); 
        Logger.formattedLog("Testing T001_JPH_API_Test.getSinglePost",l);
        APIResponse response = api.get("/posts/1");
        Logger.formattedLog("GET Request at /posts/1 completed with response status : " + response.status(),l);
        assertEquals(response.status(), 200);
        String json = response.text();
        assertTrue(json.contains("\"id\": 1"));
        Logger.formattedLog(json.contains("\"id\": 1") ? "Test T001_JPH_API_Test.getSinglePost passed"
                : "Test T001_JPH_API_Test.getSinglePost failed",l);
    }

    @Test
    @Parameters({"logMode"})
    public void createNewPost(String mode) {
        LogMode l = LogMode.parse(mode); 
        Logger.formattedLog("Testing T001_JPH_API_Test.createNewPost",l);
        APIResponse response = api.post("/posts", RequestOptions.create()
                .setData("{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1}")
                .setHeader("Content-type", "application/json; charset=UTF-8"));
        Logger.formattedLog("POST Request at /posts completed with response status : " + response.status(),l);

        assertEquals(response.status(), 201);
        String json = response.text();
        assertTrue(json.contains("\"id\""));
        Logger.formattedLog(json.contains("\"id\"") ? "Test T001_JPH_API_Test.createNewPost passed"
                : "Test T001_JPH_API_Test.createNewPost failed",l);
    }

    @Test
    @Parameters({"logMode"})
    public void updatePost(String mode) {
        LogMode l = LogMode.parse(mode); 
        Logger.formattedLog("Testing T001_JPH_API_Test.updatePost",l);
        APIResponse response = api.put("/posts/1", RequestOptions.create()
                .setData("{\"id\":1, \"title\": \"updated\", \"body\": \"data\", \"userId\":1}")
                .setHeader("Content-type", "application/json; charset=UTF-8"));
        Logger.formattedLog("PUT Request at /posts/1 completed with response status : " + response.status(),l);

        assertEquals(response.status(), 200);
        String json = response.text();
        assertTrue(json.contains("\"title\": \"updated\""));
        Logger.formattedLog(json.contains("\"title\": \"updated\"") ? "Test T001_JPH_API_Test.updatePost passed"
                : "Test T001_JPH_API_Test.updatePost failed",l);
    }

    @Test
    @Parameters({"logMode"})
    public void deletePost(String mode) {
        LogMode l = LogMode.parse(mode); 
        Logger.formattedLog("Testing T001_JPH_API_Test.deletePost",l);
        APIResponse response = api.delete("/posts/1");
        Logger.formattedLog("DELETE Request at /posts/1 completed with response status : " + response.status(),l);
        assertEquals(response.status(), 200);
        Logger.formattedLog(response.status() == 200 ? "Test T001_JPH_API_Test.deletePost passed"
                : "Test T001_JPH_API_Test.deletePost failed",l);
    }
}
