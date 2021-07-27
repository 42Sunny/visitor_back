package com.ftseoul.visitor.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReserveDeleteRequestDto implements Serializable {
    @NotBlank(message = "전화번호를 입력해주세요")
    private String phone;
    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @Builder
    public ReserveDeleteRequestDto(String phone, String name) {
        this.phone = phone;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ReserveDeleteRequestDto{" +
                "phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
