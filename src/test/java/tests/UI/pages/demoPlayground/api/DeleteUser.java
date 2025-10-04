package tests.UI.pages.demoPlayground.api;

import base.services.BaseAPIClass;
import com.microsoft.playwright.APIResponse;

public class DeleteUser extends BaseAPIClass {

    public static String baseURL = GetAllUser.baseURL;
    public static String endpoint = GetAllUser.endpoint;

    public void deleteCreatedUser(String userName) throws Exception {
        GetAllUser getAllUser = new GetAllUser();
        int id = getAllUser.getUserIdByName(userName);
        if (id != -1) {
            init(baseURL, "admin", "admin");
            APIResponse response = request.delete(endpoint + "/" + id);
            System.out.println("Response status: " + response.status());
            if (response.status() == 200) {
                System.out.println(userName + " with ID " + id + " deleted successfully.");
            } else {
                System.out.println("Failed to delete " + userName + " with ID " + id + ". Status: " + response.status());
            }
            close();
        } else {
            System.out.println("User with name " + userName + " not found.");
        }
    }
}