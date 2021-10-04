package com.ftseoul.visitor.dto.visitor;

import com.ftseoul.visitor.dto.payload.VisitorStatusInfo;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusResponseDto implements Serializable {
    private String code;
    private VisitorStatusInfo result;

}
