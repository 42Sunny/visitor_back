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

    public static List<CompanyVisitorResponseDto> mockCompanyVisitorList(CompanyVisitorSearchDto companyVisitorSearchDto) {
        return List.of(
                CompanyVisitorResponseDto.builder()
                        .id(1L)
                        .companyName("웅진")
                        .name("체크아웃 안한 방문자")
                        .place("개포")
                        .checkIn(LocalDateTime.now())
                        .checkOut(null)
                        .build(),
                CompanyVisitorResponseDto.builder()
                        .id(3L)
                        .companyName("LG")
                        .name("어제 체크인한 방문자 2")
                        .place("개포")
                        .checkIn(LocalDateTime.now().minusDays(1L))
                        .checkOut(null)
                        .build(),
                CompanyVisitorResponseDto.builder()
                        .id(3L)
                        .companyName("웅진")
                        .name("체크아웃한 방문자")
                        .place("서초")
                        .checkIn(LocalDateTime.now().minusDays(2L))
                        .checkOut(LocalDateTime.now().minusDays(1L))
                        .build(),
                CompanyVisitorResponseDto.builder()
                        .id(4L)
                        .companyName("42")
                        .name("checkin과 checkout이 계산범위입니다.")
                        .place("서초")
                        .checkIn(companyVisitorSearchDto.getStart().atStartOfDay())
                        .checkOut(companyVisitorSearchDto.getEnd().atTime(23, 59, 59))
                        .build()
        );
    }
}
