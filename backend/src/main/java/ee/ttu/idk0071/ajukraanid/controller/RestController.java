package ee.ttu.idk0071.ajukraanid.controller;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.Objects;


@org.springframework.web.bind.annotation.RestController
public class RestController {

    private final GameController gameController;

    @Autowired
    private RestController(GameController gameController) {
        this.gameController = gameController;
    }

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
            return gameController.submitAnswer(obj.getInt("Code"), obj.getInt("Question number"), obj.get("Name").toString(), obj.get("Answer").toString());
        } else if (Objects.equals(action, "GivePoints")) {
            return gameController.GivePoints(obj.getInt("Code"), obj.getInt("Question number"),  obj.get("Name").toString(), obj.get("Target").toString()) + " ++++";
        } else if (Objects.equals(action, "GetPoints")) {
            return gameController.getTotalPlayerPointStatistics(obj.getInt("Code"));
        } else if (Objects.equals(action, "GetAnswers")) {
            return gameController.getAnswers(obj.getInt("Code"),  obj.getInt("Question number"));
        }
        return "400 check your body parameters.";
    }


}


