package ee.ttu.idk0071.ajukraanid.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageTest {
    @Test
    void createFetchStateResponseFromStringTest() {
        String response = Message.createFetchStateResponse("state", "data");
        assertEquals("{\"Action\":\"FetchState\",\"State\":\"state\",\"Data\":\"data\"}", response);
    }
}
