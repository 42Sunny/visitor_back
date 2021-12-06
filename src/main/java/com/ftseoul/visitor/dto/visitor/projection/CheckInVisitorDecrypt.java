package com.ftseoul.visitor.dto.visitor.projection;

import com.ftseoul.visitor.data.visitor.VisitorStatus;
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
    private LocalDateTime reserveDate;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private long id;
    private String name;
    private String phone;
    private String organization;
    private VisitorStatus status;
    private String staffName;
    private String staffPhone;
    private String staffDepartment;
    private String purpose;
    private String place;

    @QueryProjection
    public CheckInVisitorDecrypt(LocalDateTime reserveDate, LocalDateTime checkIn, LocalDateTime checkOut, Long id,
                                 String name, String phone, String organization,
                                 VisitorStatus status, String staffName, String staffPhone,
                                 String staffDepartment, String purpose, String place) {
        this.reserveDate = reserveDate;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.organization = organization;
        this.status = status;
        this.staffName = staffName;
        this.staffPhone = staffPhone;
        this.staffDepartment = staffDepartment;
        this.purpose = purpose;
        this.place = place;
    }


    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public LocalDateTime getReserveDate() {
        return this.reserveDate;
    }

    @Override
    public LocalDateTime getCheckIn() {
        return this.checkIn;
    }

    @Override
    public LocalDateTime getCheckOut() {return this.checkOut;}

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getPhone() {
        return this.phone;
    }

    @Override
    public String getOrganization() {
        return this.organization;
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
    public VisitorStatus getStatus() {
        return this.status;
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
