package com.ftseoul.visitor.dto.visitor.projection;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@NoArgsConstructor
public class CheckInVisitorDecrypt implements CheckInVisitor{
    private String checkInDate;
    private LocalDateTime checkIn;
    private String name;
    private String phone;
    private String staffName;
    private String staffPhone;
    private String staffDepartment;
    private String purpose;
    private String place;

    @QueryProjection
    public CheckInVisitorDecrypt(String checkInDate, LocalDateTime checkIn, String name,
        String phone, String staffName, String staffPhone, String staffDepartment,
        String purpose, String place) {
        this.checkInDate = checkInDate;
        this.checkIn = checkIn;
        this.name = name;
        this.phone = phone;
        this.staffName = staffName;
        this.staffPhone = staffPhone;
        this.staffDepartment = staffDepartment;
        this.purpose = purpose;
        this.place = place;
    }

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
    public String getStaffDepartment() {
        return this.staffDepartment;
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
