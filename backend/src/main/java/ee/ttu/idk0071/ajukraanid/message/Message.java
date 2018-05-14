package ee.ttu.idk0071.ajukraanid.message;

import org.json.JSONObject;

public abstract class Message {
    public static String createErrorResponse(String errorMessage) {
        return createFetchStateResponse("Error", errorMessage);
    }

    public static JSONObject createSuccessResponseJson(String successMessage) {
        return createFetchStateResponseJson("Success", successMessage);
    }

    public static String createSuccessResponse(String successMessage) {
        return createSuccessResponseJson(successMessage).toString();
    }

    private static JSONObject createFetchStateResponseJson(String state, Object data) {
        return new JSONObject()
                .put("Action", "FetchState")
                .put("State", state)
                .put("Data", data);
    }

    public static String createFetchStateResponse(String state, Object data) {
        return createFetchStateResponseJson(state, data).toString();
    }
}
