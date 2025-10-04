package tests.UI.pages.demoPlayground.api;

import base.services.BaseAPIClass;
import com.microsoft.playwright.APIResponse;
import listeners.ReportUtil;

public class DeleteUser extends BaseAPIClass {

    public static String baseURL = GetAllUser.baseURL;
    public static String endpoint = GetAllUser.endpoint;

    public void deleteCreatedUser(String userName) throws Exception {
        GetAllUser getAllUser = new GetAllUser();
        int id = getAllUser.getUserIdByName(userName);
        if (id != -1) {
            init(baseURL, "admin", "admin");
            APIResponse response = request.delete(endpoint + "/" + id);
            ReportUtil.logInfo("Response status: " + response.status());
            if (response.status() == 200) {
                ReportUtil.logPass(userName + " with ID " + id + " deleted successfully.");
            } else {
                ReportUtil.logFail("Failed to delete " + userName + " with ID " + id + ". Status: " + response.status());
            }
            close();
        } else {
            ReportUtil.logFail("User with name " + userName + " not found.");
        }
    }
}