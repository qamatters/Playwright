package tests.Api;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
//import org.junit.jupiter.api.*;
// import org.testng.annotations.AfterClass;
// import org.testng.annotations.BeforeClass;
// import org.testng.annotations.BeforeMethod;
// import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import utils.Logger;
// import utils.enums.LogMode;

//import static org.junit.jupiter.api.Assertions.*;

public class T001_JPH_API_Test extends BaseTest {
    // This class is used to test the JSON Placeholder API

    @Test
    public void getSinglePost() {
        Logger.formattedLog("Testing T001_JPH_API_Test.getSinglePost",this.logMode);
        APIResponse response = api.get("/posts/1");
        Logger.formattedLog("GET Request at /posts/1 completed with response status : " + response.status(),this.logMode);
        assertEquals(response.status(), 200);
        String json = response.text();
        assertTrue(json.contains("\"id\": 1"));
        Logger.formattedLog(json.contains("\"id\": 1") ? "Test T001_JPH_API_Test.getSinglePost passed"
                : "Test T001_JPH_API_Test.getSinglePost failed",this.logMode);
    }

    @Test
    public void createNewPost() {
        Logger.formattedLog("Testing T001_JPH_API_Test.createNewPost",this.logMode);
        APIResponse response = api.post("/posts", RequestOptions.create()
                .setData("{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1}")
                .setHeader("Content-type", "application/json; charset=UTF-8"));
        Logger.formattedLog("POST Request at /posts completed with response status : " + response.status(),this.logMode);

        assertEquals(response.status(), 201);
        String json = response.text();
        assertTrue(json.contains("\"id\""));
        Logger.formattedLog(json.contains("\"id\"") ? "Test T001_JPH_API_Test.createNewPost passed"
                : "Test T001_JPH_API_Test.createNewPost failed",this.logMode);
    }

    @Test
    public void updatePost() {
        Logger.formattedLog("Testing T001_JPH_API_Test.updatePost",this.logMode);
        APIResponse response = api.put("/posts/1", RequestOptions.create()
                .setData("{\"id\":1, \"title\": \"updated\", \"body\": \"data\", \"userId\":1}")
                .setHeader("Content-type", "application/json; charset=UTF-8"));
        Logger.formattedLog("PUT Request at /posts/1 completed with response status : " + response.status(),this.logMode);

        assertEquals(response.status(), 200);
        String json = response.text();
        assertTrue(json.contains("\"title\": \"updated\""));
        Logger.formattedLog(json.contains("\"title\": \"updated\"") ? "Test T001_JPH_API_Test.updatePost passed"
                : "Test T001_JPH_API_Test.updatePost failed",this.logMode);
    }

    @Test
    public void deletePost() {
        Logger.formattedLog("Testing T001_JPH_API_Test.deletePost",this.logMode);
        APIResponse response = api.delete("/posts/1");
        Logger.formattedLog("DELETE Request at /posts/1 completed with response status : " + response.status(),this.logMode);
        assertEquals(response.status(), 200);
        Logger.formattedLog(response.status() == 200 ? "Test T001_JPH_API_Test.deletePost passed"
                : "Test T001_JPH_API_Test.deletePost failed",this.logMode);
    }
}
