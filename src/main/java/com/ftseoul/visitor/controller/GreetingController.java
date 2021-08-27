package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.service.GreetingService;
import com.ftseoul.visitor.websocket.Greeting;
import com.ftseoul.visitor.websocket.HelloMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GreetingController {

    private final GreetingService greetingService;

//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public Greeting greeting(HelloMessage message) throws Exception {
//        System.out.println(message.getName());
//        Thread.sleep(1000);
//        greetingService.save(message.getName());
//        return greetingService.findAll();
//        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
//    }

}
