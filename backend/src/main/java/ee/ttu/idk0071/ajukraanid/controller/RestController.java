package ee.ttu.idk0071.ajukraanid.controller;


import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.atomic.AtomicLong;


@org.springframework.web.bind.annotation.RestController
public class RestController {



    @ResponseBody
    @RequestMapping("/index")
    public String getHTTPHead(@RequestHeader(value = "Content-Type") String httpHeader) {

        return "Request header: " + httpHeader;
    }

    @RequestMapping(method= RequestMethod.POST,  value = "index")
    public String getHTTPBody(@RequestBody String request)  {

        return "Request body: " + request;
    }

}


