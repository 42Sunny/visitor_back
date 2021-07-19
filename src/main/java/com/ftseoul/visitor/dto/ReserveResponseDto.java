package com.ftseoul.visitor.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ftseoul.visitor.data.Reserve;
import com.ftseoul.visitor.data.Staff;
import com.ftseoul.visitor.data.Visitor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReserveResponseDto {
    private Long id;

    private String place;

    private Staff staff;

    private Visitor visitor;

    private String purpose;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime date;

    @Builder
    public ReserveResponseDto(Long id, String place, Staff staff, Visitor visitor, String purpose, LocalDateTime date) {
        this.id = id;
        this.place = place;
        this.staff = staff;
        this.visitor = visitor;
        this.purpose = purpose;
        this.date = date;
    }
}
