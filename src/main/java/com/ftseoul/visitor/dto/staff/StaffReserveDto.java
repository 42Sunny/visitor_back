package com.ftseoul.visitor.dto.staff;

import com.ftseoul.visitor.data.Visitor;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StaffReserveDto {
    private Long reserveId;

    private String phone;

    private String visitorPurpose;

    private String place;

    private LocalDateTime date;

    private List<Visitor> visitors;
}
