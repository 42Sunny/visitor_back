package com.ftseoul.visitor.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class ReserveDeleteRequestDto implements Serializable {
    private String phone;
    private String name;

    @Builder
    public ReserveDeleteRequestDto(String phone, String name) {
        this.phone = phone;
        this.name = name;
    }
}
