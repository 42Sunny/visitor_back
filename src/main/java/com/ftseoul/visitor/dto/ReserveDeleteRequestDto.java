package com.ftseoul.visitor.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReserveDeleteRequestDto implements Serializable {
    private String phone;
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
