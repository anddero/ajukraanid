package ee.ttu.idk0071.ajukraanid.controller;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Objects;


@org.springframework.web.bind.annotation.RestController
public class RestController {

    private GameController gameController = new GameController();

    @RequestMapping(method = RequestMethod.POST, value = "")
    public String getHTTPBody(@RequestBody String request) throws JSONException {
        JSONObject obj = new JSONObject(request);
        String action = obj.get("Action").toString();
        if (Objects.equals(action, "CreateGame")) {
            System.out.println("Someone wanted to create a new Game");
            return gameController.createNewGame();
        } else if (Objects.equals(action, "JoinGame")) {
            return gameController.addPlayerToGame(obj.getInt("Code"), obj.get("Name").toString());
        } else if (Objects.equals(action, "BeginGame")) {
            return gameController.startGame(obj.getInt("Code"));
        } else if (Objects.equals(action, "FetchState")) {
            return gameController.fetchGameState(obj.getInt("Code"), "No such game found.");
            // AJAJA SEE SUUR SITT SPLITITAKSE TULEVIKUS ÄRA JAJAJAJAJA
        } else if (Objects.equals(action, "SubmitAnswer")) {
            return gameController.fetchGameState(obj.getInt("Code"), "No such game found.");
        } else if (Objects.equals(action, "GiveScore")) {
            return gameController.fetchGameState(obj.getInt("Code"), "No such game found.");
        }
        return "400 Bad Request";
    }

}


