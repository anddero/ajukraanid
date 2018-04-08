package ee.ttu.idk0071.ajukraanid.message;

import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageTest {
    @Test
    void createErrorResponseTest() {
        String response = Message.createErrorResponse("1");
        assertEquals("{\"Action\":\"FetchState\",\"State\":\"Error\",\"Data\":\"1\"}", response);

        response = Message.createErrorResponse("An unknown err");
        assertEquals("{\"Action\":\"FetchState\",\"State\":\"Error\",\"Data\":\"An unknown err\"}", response);
    }

    @Test
    void createSuccessResponseTest() {
        String response = Message.createSuccessResponse("asdasd");
        assertEquals("{\"Action\":\"FetchState\",\"State\":\"Success\",\"Data\":\"asdasd\"}", response);

        response = Message.createSuccessResponse("a very meaningful Message");
        assertEquals("{\"Action\":\"FetchState\",\"State\":\"Success\",\"Data\":\"a very meaningful Message\"}",
                response);
    }

    @Test
    void createFetchStateResponseFromStringTest() {
        String response = Message.createFetchStateResponse("state", "data");
        assertEquals("{\"Action\":\"FetchState\",\"State\":\"state\",\"Data\":\"data\"}", response);

        response = Message.createFetchStateResponse("", "");
        assertEquals("{\"Action\":\"FetchState\",\"State\":\"\",\"Data\":\"\"}", response);
    }

    @Test
    void createFetchStateResponseFromIntegerTest() {
        String response = Message.createFetchStateResponse("s", 124);
        assertEquals("{\"Action\":\"FetchState\",\"State\":\"s\",\"Data\":124}", response);

        response = Message.createFetchStateResponse("t", 0);
        assertEquals("{\"Action\":\"FetchState\",\"State\":\"t\",\"Data\":0}", response);
    }

    @Test
    void createFetchStateResponseFromJSONArrayTest() {
        JSONArray jsonArray = new JSONArray().put(10).put("asd");
        String response = Message.createFetchStateResponse("s", jsonArray);
        assertEquals("{\"Action\":\"FetchState\",\"State\":\"s\",\"Data\":[10,\"asd\"]}", response);

        jsonArray = new JSONArray();
        response = Message.createFetchStateResponse("u", jsonArray);
        assertEquals("{\"Action\":\"FetchState\",\"State\":\"u\",\"Data\":[]}", response);
    }

    @Test
    void createFetchStateResponseFromNullTest() {
        // If the key is null, the value will be removed from the object by definition.
        String response = Message.createFetchStateResponse("Lobby", null);
        assertEquals("{\"Action\":\"FetchState\",\"State\":\"Lobby\"}", response);

        response = Message.createFetchStateResponse("Inactive", null);
        assertEquals("{\"Action\":\"FetchState\",\"State\":\"Inactive\"}", response);
    }
}
