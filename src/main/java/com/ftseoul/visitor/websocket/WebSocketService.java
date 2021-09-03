package com.ftseoul.visitor.websocket;

import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebSocketService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendMessageToSubscriber(String endpoint, String message) {
        simpMessagingTemplate.convertAndSend(endpoint, message);
    }

    @PostConstruct
    public void setMessageConverter() {
        simpMessagingTemplate.setMessageConverter(new StringMessageConverter());
    }
}
