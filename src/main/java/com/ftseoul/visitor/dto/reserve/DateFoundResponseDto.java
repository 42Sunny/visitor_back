package com.ftseoul.visitor.dto.reserve;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ftseoul.visitor.dto.visitor.VisitorInfoDecryptDto;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class DateFoundResponseDto implements Serializable {
    private Long id;

    private String place;

    private Long staffId;

    private String staffName;

    private String staffPhone;

    private String staffDepartment;

    private String purpose;

    private List<VisitorInfoDecryptDto> visitors;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime date;

    public DateFoundResponseDto(Long id, String place, Long staffId, String staffName,
        String staffPhone, String staffDepartment, String purpose, LocalDateTime date) {
        this.id = id;
        this.place = place;
        this.staffId = staffId;
        this.staffName = staffName;
        this.staffPhone = staffPhone;
        this.staffDepartment = staffDepartment;
        this.purpose = purpose;
        this.date = date;
    }
}
