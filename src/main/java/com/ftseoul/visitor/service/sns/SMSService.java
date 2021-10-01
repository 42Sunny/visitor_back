package com.ftseoul.visitor.service.sns;

import com.ftseoul.visitor.dto.ShortUrlDto;
import com.ftseoul.visitor.dto.StaffDto;
import java.util.List;

public interface SMSService {
    void sendMessage(String phone, String value);
    void sendMessage(StaffDto staffDto, String shortUrl);
    void sendMessages(List<ShortUrlDto> shortUrlDtoList, StaffDto staffReserveInfo);
}
