package com.ftseoul.visitor.service.sns;

import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.dto.ReserveModifyDto;
import com.ftseoul.visitor.dto.StaffDto;
import java.time.LocalDateTime;
import java.util.List;

public interface SMSService {
    public void sendMessage(String phone, String value);
    public void sendMessage(StaffDto staffDto);
    public void sendMessages(List<Visitor> visitors, LocalDateTime date);
}
