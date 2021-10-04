package com.ftseoul.visitor.service.sns;


public interface SMSService {
    void sendMessage(String phone, String message);
}
