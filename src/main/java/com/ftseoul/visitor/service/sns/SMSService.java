package com.ftseoul.visitor.service.sns;

public interface SMSService {
    public void sendMessage(String phone, String message);
}
