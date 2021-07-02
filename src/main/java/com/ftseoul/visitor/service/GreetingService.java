package com.ftseoul.visitor.service;

import com.ftseoul.visitor.websocket.Greeting;
import com.ftseoul.visitor.websocket.GreetingJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GreetingService {

    private final GreetingJPARepository greetingJPARepository;

    public List<Greeting> findAll(){
        return greetingJPARepository.findAll();
    }

    @Transactional
    public void save(String message){
        Greeting greeting = new Greeting("Hello, " + HtmlUtils.htmlEscape(message) + "!");
        System.out.println(greeting.getContent().length());
        greetingJPARepository.save(greeting);
    }
}
