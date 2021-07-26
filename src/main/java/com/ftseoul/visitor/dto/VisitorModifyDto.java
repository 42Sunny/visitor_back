package com.ftseoul.visitor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VisitorModifyDto implements Serializable {
    @NotBlank(message = "예약 아이디를 입력해주세요")
    private Long reserve_id;
    @NotBlank(message = "성함을 입력해주세요")
    private String name;
    @NotBlank(message = "전화번호를 입력해주세요")
    private String phone;
    @NotBlank(message = "소속을 입력해주세요")
    private String organization;

    @JsonProperty("isChanged")
    private boolean isChanged;

    @Override
    public String toString() {
        return "VisitorModifyDto{" +
            "reserve_id=" + reserve_id +
            ", name='" + name + '\'' +
            ", phone='" + phone + '\'' +
            ", organization='" + organization + '\'' +
            ", isChanged=" + isChanged +
            '}';
    }
}
