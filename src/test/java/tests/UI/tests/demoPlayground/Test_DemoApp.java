package tests.UI.tests.demoPlayground;


import base.BaseUITest;
import org.testng.annotations.Test;
import tests.UI.pages.demoPlayground.api.DeleteUser;
import tests.UI.pages.demoPlayground.ui.HomePage;
import tests.UI.pages.demoPlayground.ui.LoginPage;

import static tests.utils.DateTimeUtility.getCurrentDateTimeInSpecificFormat;

public class Test_DemoApp extends BaseUITest {
    @Test
    public void TestAlertBehaviourInPlaywright() throws Exception {
        LoginPage loginPage = new LoginPage(page);
        HomePage homePage = new HomePage(page);
        DeleteUser deleteUser = new DeleteUser();
        String userName = "Automation_" + getCurrentDateTimeInSpecificFormat();
        page.navigate("http://localhost:3000/users");
        loginPage.loginToDemoWebAndServicesApplication();

        try {
            homePage.createUser(userName);
            System.out.println("Created User: " + userName);
            homePage.validateAddedUser(userName);
        } finally {
            deleteUser.deleteCreatedUser(userName);
        }

    }
}

