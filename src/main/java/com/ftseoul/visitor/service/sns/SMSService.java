package com.ftseoul.visitor.service.sns;

import com.ftseoul.visitor.data.Visitor;
import java.util.List;

public interface SMSService {
    public void sendMessage(String phone, Long reserveId);
    public void sendMessages(List<Visitor> visitors);
}
