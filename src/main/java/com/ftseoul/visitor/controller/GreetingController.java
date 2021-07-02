package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.service.GreetingService;
import com.ftseoul.visitor.websocket.Greeting;
import com.ftseoul.visitor.websocket.GreetingJPARepository;
import com.ftseoul.visitor.websocket.HelloMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GreetingController {

    private final GreetingService greetingService;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public List<Greeting> greeting(HelloMessage message) throws Exception{
        System.out.println(message.getName());
        Thread.sleep(1000);
        greetingService.save(message.getName());
        return greetingService.findAll();
//        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    @GetMapping("/api/v1/greeting-list")
    public List<Greeting> greetingList() {
        return greetingService.findAll();
    }
}
