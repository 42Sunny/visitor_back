package com.ftseoul.visitor.dto.company;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class CompanyResponseDto implements Serializable {

    private Long id;
    private String name;
    private String phone;

    @Builder
    public CompanyResponseDto(Long id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }
}
