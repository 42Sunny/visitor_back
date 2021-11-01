package com.ftseoul.visitor.dto.staff;

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
public class StaffModifyDto implements Serializable {
    @NotNull(message = "Staff id를 입력 해주세요")
    private Long staffId;
    @NotNull(message = "수정할 이름을 입력 해주세요")
    private String name;
    @NotNull(message = "수정할 전화번호를 입력해주세요")
    private String phone;
    @NotNull(message = "소속을 입력해주세요")
    private String department;
}
