package pe.edu.pucp.mvc.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import pe.edu.pucp.mvc.models.Greeting;
import pe.edu.pucp.mvc.models.HelloMessage;

@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/pedidos")
    public Greeting greet(HelloMessage message) throws InterruptedException {
        //Thread.sleep(2000);
        return new Greeting("Hello");
    }
}
