package com.ftseoul.visitor.dto.payload;

import com.ftseoul.visitor.data.visitor.VisitorStatus;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VisitorStatusInfo implements Serializable {
    @NotNull(message = "방문자 고유번호를 입력해주세요")
    private Long id;
    @NotNull(message = "변경할 상태를 입력해주세요")
    private VisitorStatus status;
}
