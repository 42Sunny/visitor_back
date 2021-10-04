package com.ftseoul.visitor.dto.reserve;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ftseoul.visitor.dto.visitor.VisitorDecryptDto;
import com.ftseoul.visitor.dto.staff.StaffDecryptDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReserveListResponseDto {
    private Long id;

    private String place;

    private StaffDecryptDto staff;

    private List<VisitorDecryptDto> visitor;

    private String purpose;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime date;

    @Builder
    public ReserveListResponseDto(Long id, String place, StaffDecryptDto staff, List<VisitorDecryptDto> visitor, String purpose, LocalDateTime date) {
        this.id = id;
        this.place = place;
        this.staff = staff;
        this.visitor = visitor;
        this.purpose = purpose;
        this.date = date;
    }

    @Override
    public String toString() {
        return "ReserveListResponseDto{" +
                "id=" + id +
                ", place='" + place + '\'' +
                ", staff=" + staff +
                ", visitor=" + visitor +
                ", purpose='" + purpose + '\'' +
                ", date=" + date +
                '}';
    }
}
