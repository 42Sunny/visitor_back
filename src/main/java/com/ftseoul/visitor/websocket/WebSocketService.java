package com.ftseoul.visitor.websocket;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
        log.info("Send to {}, message is {}", endpoint, message);
        LocalDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        String prefixDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        message = prefixDate + " " + message;
        simpMessagingTemplate.convertAndSend(endpoint, message);
    }

    @PostConstruct
    public void setMessageConverter() {
        simpMessagingTemplate.setMessageConverter(new StringMessageConverter());
    }
}
