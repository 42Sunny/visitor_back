package com.ftseoul.visitor.dto.companyvisitor;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CompanyVisitorResponseDto {
    private Long id;
    private String companyName;
    private String name;
    private String place;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    @Builder
    public CompanyVisitorResponseDto(Long id, String companyName, String name, String place, LocalDateTime checkIn, LocalDateTime checkOut) {
        this.id = id;
        this.companyName = companyName;
        this.name = name;
        this.place = place;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
}
