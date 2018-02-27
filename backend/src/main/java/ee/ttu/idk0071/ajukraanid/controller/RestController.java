package ee.ttu.idk0071.ajukraanid.controller;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.Objects;


@org.springframework.web.bind.annotation.RestController
public class RestController {

    private GameController gameController = new GameController();

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "" )
    public String getHTTPBody(@RequestBody String request) throws JSONException {
        JSONObject obj = new JSONObject(request);
        String action = obj.get("Action").toString();
        if (Objects.equals(action, "CreateGame")) {
            return gameController.createNewGame();
        } else if (Objects.equals(action, "JoinGame")) {
            return gameController.findGameWithGameCodeAndAddPlayerToThatGame(obj.getInt("Code"), obj.get("Name").toString());
        } else if (Objects.equals(action, "StartGame")) {
            return gameController.startGame(obj.getInt("Code"));
        } else if (Objects.equals(action, "FetchState")) {
            return gameController.fetchState(obj.getInt("Code"));
        } else if (Objects.equals(action, "SubmitAnswer")) {
            return gameController.submitAnswer(obj.getInt("Code"), obj.get("Name").toString());
        } else if (Objects.equals(action, "GiveScore")) {
            return gameController.giveScore(obj.getInt("Code"), obj.get("Name").toString(), obj.get("Target").toString());
        } return "400 Bad Request";
    }


}


