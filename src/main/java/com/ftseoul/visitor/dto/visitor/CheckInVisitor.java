package com.ftseoul.visitor.dto.visitor;

import java.time.LocalDateTime;

public interface CheckInVisitor {
    String getCheckInDate();
    LocalDateTime getCheckIn();
    String getName();
    String getPhone();
    int getCount();
}
