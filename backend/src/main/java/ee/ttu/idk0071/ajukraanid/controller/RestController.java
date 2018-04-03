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
    @RequestMapping(method = RequestMethod.POST, value = "")
    public String getHTTPBody(@RequestBody String request) throws InterruptedException {
        JSONObject data;
        String action;
        try {
            data = new JSONObject(request);
            action = data.get("Action").toString();
        } catch (JSONException e) {
            return "400 check your body parameters."; // TODO something better
        }

        try {
            if (Objects.equals(action, "CreateGame")) {
                return gameController.createNewGame();
            } else if (Objects.equals(action, "JoinGame")) {
                return gameController.joinGame(data.getInt("Code"), data.getString("Name"));
            } else if (Objects.equals(action, "StartGame")) {
                return gameController.startGame(data.getInt("Code"));
            } else if (Objects.equals(action, "FetchState")) {
                return gameController.fetchState(data.getInt("Code"));
            } else if (Objects.equals(action, "SubmitAnswer")) {
                return gameController.submitAnswer(data.getInt("Code"), data.getString("Name"), data.getString("Answer"));
            } else if (Objects.equals(action, "GivePoints")) {
                return gameController.GivePoints(data.getInt("Code"), data.getString("Name"), data.getString("Target")) +
                        " ++++"; // TODO What is this?
            } else if (Objects.equals(action, "GetPoints")) {
                return gameController.getTotalPlayerPointStatistics(data.getInt("Code"));
            } else if (Objects.equals(action, "GetAnswers")) {
                return gameController.getAnswers(data.getInt("Code"));
            } else if (Objects.equals(action, "RemovePlayer")) {
                return gameController.removePlayerFromGame(data.getInt("Code"), data.getString("Name"));
            } else if (Objects.equals(action, "GetQuestion")) {
                return gameController.getQuestion(data.getInt("Code"));
            }
        } catch (JSONException e) {
            return "400 check your body parameters."; // TODO something better
        }
        return "400 check your body parameters."; // TODO Something better
    }
}
