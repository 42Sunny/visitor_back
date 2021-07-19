package com.ftseoul.visitor.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReserveModifyDto implements Serializable {
    private Long reserveId;

    private String place;

    private String targetStaffName;

    private String purpose;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime date;

    private List<VisitorModifyDto> visitor;

    @Override
    public String toString() {
        return "ReserveModifyDto{" +
                "reserveId=" + reserveId +
                ", place='" + place + '\'' +
                ", targetStaffName='" + targetStaffName + '\'' +
                ", purpose='" + purpose + '\'' +
                ", date=" + date +
                ", visitor=" + visitor +
                '}';
    }
}
