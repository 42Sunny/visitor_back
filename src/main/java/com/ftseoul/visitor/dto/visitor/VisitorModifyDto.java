package com.ftseoul.visitor.dto.visitor;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VisitorModifyDto implements Serializable {
    @NotNull(message = "예약 아이디를 입력해주세요")
    private long reserveId;
    @NotBlank(message = "성함을 입력해주세요")
    private String name;
    @NotBlank(message = "전화번호를 입력해주세요")
    private String phone;
    @NotBlank(message = "소속을 입력해주세요")
    private String organization;

    @JsonProperty("isChanged")
    private boolean isChanged;

    public void updateNameAndPhone(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "VisitorModifyDto{" +
            "reserveId =" + reserveId +
            ", name='" + name + '\'' +
            ", phone='" + phone + '\'' +
            ", organization='" + organization + '\'' +
            ", isChanged=" + isChanged +
            '}';
    }
}
