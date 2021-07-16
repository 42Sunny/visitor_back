package com.ftseoul.visitor.dto;

import com.ftseoul.visitor.data.Staff;
import com.ftseoul.visitor.data.Visitor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReserveListResponseDto {
    private Long id;

    private String place;

    private Staff staff;

    private List<Visitor> visitor;

    private String purpose;

    private LocalDateTime date;

    @Builder
    public ReserveListResponseDto(Long id, String place, Staff staff, List<Visitor> visitor, String purpose, LocalDateTime date) {
        this.id = id;
        this.place = place;
        this.staff = staff;
        this.visitor = visitor;
        this.purpose = purpose;
        this.date = date;
    }
}
