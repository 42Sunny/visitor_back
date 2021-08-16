package com.ftseoul.visitor.service.sns;

import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.dto.ShortUrlDto;
import com.ftseoul.visitor.dto.StaffDto;
import java.util.List;

public interface SMSService {
    public void sendMessage(String phone, String value);
    public void sendMessage(StaffDto staffDto, String shortUrl);
    public void sendMessages(List<ShortUrlDto> shortUrlDtoList, StaffDto staffReserveInfo);
}
