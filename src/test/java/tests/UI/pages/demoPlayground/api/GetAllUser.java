// src/test/java/tests/UI/pages/demoPlayground/services/GetAllUser.java
package tests.UI.pages.demoPlayground.api;

import base.services.BaseAPIClass;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllUser extends BaseAPIClass {
    public static String baseURL = "http://localhost:9090/";
    public static String endpoint = "users";

    public Map<Integer, String> getAllUsers() throws Exception {
        init(baseURL, "deepak", "deepak");
        APIResponse response = request.get(endpoint);
        System.out.println("Response status: " + response.status());
        Map<Integer, String> result = null;
        if (response.status() == 200) {
            result = extractIdNameMap(response.text());
        }
        close();
        return result;
    }

    public static Map<Integer, String> extractIdNameMap(String jsonResponse) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> users = mapper.readValue(jsonResponse, new TypeReference<List<Map<String, Object>>>() {
        });
        Map<Integer, String> idNameMap = new HashMap<>();
        for (Map<String, Object> user : users) {
            Integer id = (Integer) user.get("id");
            String name = (String) user.get("name");
            idNameMap.put(id, name);
        }
        return idNameMap;
    }

    public int getUserIdByName(String userName) throws Exception {
        Map<Integer, String> idNameMap = getAllUsers();
        for (Map.Entry<Integer, String> entry : idNameMap.entrySet()) {
            if (entry.getValue().equals(userName)) {
                return entry.getKey();
            }
        }
        return -1;
    }
}