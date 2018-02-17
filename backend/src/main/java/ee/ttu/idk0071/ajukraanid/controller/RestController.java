package ee.ttu.idk0071.ajukraanid.controller;


import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.atomic.AtomicLong;


@org.springframework.web.bind.annotation.RestController
public class RestController {


    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();


    @ResponseBody
    @RequestMapping("/index")
    public String getData(@RequestHeader(value = "Client") String httpHeaderValue) {

        System.out.println("Recieved following json: " + httpHeaderValue);
        // jajaja varsti teen gsoniga selle, prg lis testimiiseks
        String gamecode = httpHeaderValue.substring(httpHeaderValue.length() - 6, httpHeaderValue.length() - 2);

        // should return json type data.
        return "Starting a new game with game code: " + gamecode;
    }

}


