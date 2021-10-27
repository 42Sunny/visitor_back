package com.ftseoul.visitor.dto.visitor.projection;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public class CheckInVisitorDecrypt implements CheckInVisitor{
    private String checkInDate;
    private LocalDateTime checkIn;
    private String name;
    private String phone;


    @Override
    public String getCheckInDate() {
        return this.checkInDate;
    }

    @Override
    public LocalDateTime getCheckIn() {
        return this.checkIn;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getPhone() {
        return this.phone;
    }

}
