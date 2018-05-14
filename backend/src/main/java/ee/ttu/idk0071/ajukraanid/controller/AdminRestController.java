package ee.ttu.idk0071.ajukraanid.controller;

import ee.ttu.idk0071.ajukraanid.guard.GuardException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static ee.ttu.idk0071.ajukraanid.message.Message.createErrorResponse;

@RestController
public class AdminRestController {

    private final GameController gameController;


    @Autowired
    private AdminRestController(GameController gameController) {
        this.gameController = gameController;
    }



    @RequestMapping(value = "/login", method = RequestMethod.OPTIONS)
    public ResponseEntity options(HttpServletResponse response) {
        System.out.println("OPTIONS /login called");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Allow", "POST,OPTIONS");
        return new ResponseEntity(HttpStatus.OK);
    }


    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/admin")
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
                case "GetQuestions":
                    return gameController.getPlainQuestions();
                case "AddQuestion":
                    return gameController.addPlainQuestion(data.getString("Text"));
                case "DeleteQuestion":
                    return gameController.deletePlainQuestion(data.getInt("Id"));
                case "UpdateQuestion":
                    return gameController.updatePlainQuestion(data.getInt("Id"), data.getString("Text"));
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
