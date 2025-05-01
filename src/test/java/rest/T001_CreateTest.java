package rest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

public class T001_CreateTest {


        @Test
        public void getApiTest() {
            Response response = RestAssured.get("https://jsonplaceholder.typicode.com/posts/1");
            System.out.println("Response: " + response.asString());
            assertEquals(response.statusCode(), 200);
        }
    }

