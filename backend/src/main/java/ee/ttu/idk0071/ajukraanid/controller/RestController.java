package ee.ttu.idk0071.ajukraanid.controller;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Random;


@org.springframework.web.bind.annotation.RestController
public class RestController {

    private GameController gameController = new GameController();

    @ResponseBody
    @RequestMapping("/index")
    public String getHTTPHead(@RequestHeader(value = "Content-Type") String httpHeader) {

        return "Request header: " + httpHeader;
    }

    @RequestMapping(method = RequestMethod.POST, value = "")
    public String getHTTPBody(@RequestBody String request) throws JSONException {
        JSONObject obj = new JSONObject(request);
        String action = obj.get("Action").toString();

        //DIS JUST FOR TESTING, IL CREATE A ID GIVING CLASS SOON ENOUGH DONT WORRY
        Random  rand = new Random();
        int randomNum = rand.nextInt((9999 - 1000) + 1) + 1000;

        if (Objects.equals(action, "NewGame")) {
            return gameController.addGame(randomNum) ? "Created a new game." : "Failed to create new game.";
        } else if (Objects.equals(action, "JoinGame")) {
            gameController.addPlayerToGame(obj.getInt("Code"), obj.get("Name").toString());
            // addPlayerToGame should also return boolean to know if it was successful
            return "Added player to game ... ";
        } else if (Objects.equals(action, "BeginGame")) {
            gameController.startGame(obj.getInt("Code"));
            return "Started game with code: " + obj.getInt("Code");
        } else if (Objects.equals(action, "FetchState")) {
            return "return new code";
        } else if (Objects.equals(action, "SubmitAnswer")) {
            return "return new code";
        } else if (Objects.equals(action, "GiveScore")) {
            return "200 OK";
        } else if (Objects.equals(action, "NewGame")) {
            return "return new code";
        }
        return "400 Bad Request";

    }

}


