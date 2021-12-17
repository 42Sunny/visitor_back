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

    @Deprecated
    public static List<CompanyResponseDto> mockCompanyList() {
        return List.of(
                CompanyResponseDto.builder()
                        .id(1L)
                        .name("웅진")
                        .phone("010-1234-5678")
                        .build(),
                CompanyResponseDto.builder()
                        .id(2L)
                        .name("LG")
                        .phone("010-4321-8765")
                        .build(),
                CompanyResponseDto.builder()
                        .id(3L)
                        .name("Apple")
                        .phone("010-1234-5678")
                        .build()
                );
    }
}
