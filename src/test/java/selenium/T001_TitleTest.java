package selenium;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class T001_TitleTest {

        @Test
        public void openGoogleTest() {
            System.setProperty("webdriver.chrome.driver", "/Users/khushal/Downloads/Makaia-master/drivers/chromedriver"); // 👈 Update path
            WebDriver driver = new ChromeDriver();
            driver.get("https://www.google.com");
            String title = driver.getTitle();
            System.out.println("Page title is: " + title);
            assert title.contains("Google") : "Title does not contain 'Google'";
//        driver.quit();
        }
    }
