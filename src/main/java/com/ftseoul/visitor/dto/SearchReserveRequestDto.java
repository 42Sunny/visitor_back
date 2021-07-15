package com.ftseoul.visitor.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchReserveRequestDto {
    private String phone;
    private String name;

    @Builder
    public SearchReserveRequestDto(String phone, String name) {
        this.phone = phone;
        this.name = name;
    }
}
