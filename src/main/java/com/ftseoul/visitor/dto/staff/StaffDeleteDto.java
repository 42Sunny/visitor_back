package com.ftseoul.visitor.dto.staff;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StaffDeleteDto implements Serializable {
    @NotNull(message = "staff id를 입력해주세요")
    private Long staffId;
}
