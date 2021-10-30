package com.ftseoul.visitor.dto.visitor.projection;

import java.time.LocalDateTime;

public interface CheckInVisitor {
    String getCheckInDate();
    LocalDateTime getCheckIn();
    String getName();
    String getPhone();
    String getStaffName();
    String getStaffPhone();
    String getPurpose();
    String getPlace();
}
