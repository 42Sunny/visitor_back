package com.ftseoul.visitor.dto;

import com.ftseoul.visitor.data.Visitor;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StaffDto {
    private Long reserveId;

    private String phone;

    private LocalDateTime date;

    private List<Visitor> visitors;
}
