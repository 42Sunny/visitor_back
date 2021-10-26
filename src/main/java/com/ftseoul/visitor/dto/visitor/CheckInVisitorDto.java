package com.ftseoul.visitor.dto.visitor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class CheckInVisitorDto {
    private LocalDate checkInDate;
    private LocalDateTime checkIn;
    private String name;
    private String phone;
    private int sum;
}
