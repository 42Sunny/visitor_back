package com.ftseoul.visitor.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchReserveRequestDto {
    @NotBlank(message = "전화번호를 입력해주세요")
    private String phone;
    @NotBlank(message = "성함을 입력해주세요")
    private String name;

    @Builder
    public SearchReserveRequestDto(String phone, String name) {
        this.phone = phone;
        this.name = name;
    }

    @Override
    public String toString() {
        return "SearchReserveRequestDto{" +
                "phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
