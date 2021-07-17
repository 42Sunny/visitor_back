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
public class ReserveVisitorDto implements Serializable {
    private String place;

    private String targetStaffName;

    private String purpose;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime date;

    private List<VisitorDto> visitor;

    @Override
    public String toString() {
        return "ReserveVisitorDto{" +
                "place='" + place + '\'' +
                ", targetStaffName='" + targetStaffName + '\'' +
                ", purpose='" + purpose + '\'' +
                ", date=" + date +
                ", visitor=" + visitor +
                '}';
    }
}

