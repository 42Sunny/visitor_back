package com.ftseoul.visitor.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReserveDeleteRequestDto {
    private String phone;
    private String name;

    @Builder
    public ReserveDeleteRequestDto(String phone, String name) {
        this.phone = phone;
        this.name = name;
    }
}
