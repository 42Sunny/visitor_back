package com.ftseoul.visitor.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReserveUpdateDto {
    private String place;

    private Long targetStaff;

    private String purpose;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime date;

    public ReserveUpdateDto(String place, Long targetStaff, String purpose, LocalDateTime date) {
        this.place = place;
        this.targetStaff = targetStaff;
        this.purpose = purpose;
        this.date = date;
    }
}
