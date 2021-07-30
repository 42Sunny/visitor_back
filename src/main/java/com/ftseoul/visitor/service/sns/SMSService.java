package com.ftseoul.visitor.service.sns;

import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.dto.ReserveModifyDto;
import com.ftseoul.visitor.dto.StaffDto;
import java.util.List;

public interface SMSService {
    public void sendMessage(String phone, Long reserveId, String QRCode);
    public void sendMessage(StaffDto staffDto);
    public void sendMessages(List<Visitor> visitors);
}
