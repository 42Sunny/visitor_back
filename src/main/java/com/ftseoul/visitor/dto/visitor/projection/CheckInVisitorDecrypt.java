package com.ftseoul.visitor.dto.visitor.projection;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public class CheckInVisitorDecrypt implements CheckInVisitor{
    private String checkInDate;
    private LocalDateTime checkIn;
    private String name;
    private String phone;
    private String staffName;
    private String staffPhone;
    private String purpose;
    private String place;


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

    @Override
    public String getStaffName() {
        return this.staffName;
    }

    @Override
    public String getStaffPhone() {
        return this.staffPhone;
    }

    @Override
    public String getPurpose() {
        return this.purpose;
    }

    @Override
    public String getPlace() {
        return this.place;
    }

}
