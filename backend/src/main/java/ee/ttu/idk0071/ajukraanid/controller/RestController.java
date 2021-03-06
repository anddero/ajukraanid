package ee.ttu.idk0071.ajukraanid.controller;

import ee.ttu.idk0071.ajukraanid.guard.GuardException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static ee.ttu.idk0071.ajukraanid.message.Message.createErrorResponse;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    private final GameController gameController;

    @Autowired
    private RestController(GameController gameController) {
        this.gameController = gameController;
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/api")
    public String getHTTPBody(@RequestBody String request) {
        JSONObject data;
        String action;
        try {
            data = new JSONObject(request);
            action = data.getString("Action");
        } catch (JSONException e) {
            return createErrorResponse("JSONException: " + e.getMessage());
        }

        try {
            switch (action) {
                case "CreateGame":
                    return gameController.createGame();
                case "JoinGame":
                    return gameController.joinGame(data.getInt("Code"), data.getString("Name"));
                case "StartGame":
                    return gameController.startGame(data.getInt("Code"), data.getString("Token"));
                case "FetchState":
                    return gameController.fetchState(data.getInt("Code"), data.getString("Token"));
                case "SubmitAnswer":
                    return gameController.submitAnswer(data.getInt("Code"), data.getString("Name"),
                            data.getString("Answer"), data.getString("Token"));
                case "GivePoints":
                    return gameController.givePoints(data.getInt("Code"), data.getString("Name"),
                            data.getString("Target"), data.getString("Token"));
                case "RemovePlayer":
                    return gameController.removePlayer(data.getInt("Code"), data.getString("Name"),
                            data.getString("Token"));
                case "GetPlayers":
                    return gameController.getPlayers(data.getInt("Code"), data.getString("Token"));
                default:
                    return createErrorResponse("Invalid Action: " + action);
            }
        } catch (JSONException e) {
            return createErrorResponse("JSONException: " + e.getMessage());
        } catch (GuardException e) {
            return createErrorResponse(e.getMessage());
        }
    }
}
