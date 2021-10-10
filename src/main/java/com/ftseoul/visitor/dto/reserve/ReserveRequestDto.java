package com.ftseoul.visitor.dto.reserve;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReserveRequestDto implements Serializable {
    @NotBlank(message = "전화번호를 입력해주세요")
    private String phone;
    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @Override
    public String toString() {
        return "ReserveDeleteRequestDto{" +
                "phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
