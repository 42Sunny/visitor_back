package com.ftseoul.visitor.dto;

import com.ftseoul.visitor.data.visitor.VisitorStatus;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateVisitorStatusDto implements Serializable {
    @NotNull(message = "방문자 고유번호를 입력해주세요")
    private Long visitorId;
    @NotNull(message = "변경할 상태를 입력해주세요")
    private VisitorStatus visitorStatus;
}
