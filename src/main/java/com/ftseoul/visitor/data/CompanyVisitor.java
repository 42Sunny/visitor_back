package com.ftseoul.visitor.data;

import com.ftseoul.visitor.data.visitor.VisitorStatus;
import com.ftseoul.visitor.dto.companyvisitor.CompanyVisitorResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class CompanyVisitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long companyId;

    @Column(length = 45, nullable = false)
    private String name;

    @Column(length = 10, nullable = false, columnDefinition = "VARCHAR(4) DEFAULT '입실'")
    @Enumerated(value = EnumType.STRING)
    private VisitorStatus status;

    @Column(length = 4, nullable = false)
    private String place;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP()")
    private LocalDateTime checkInTime;

    @Column
    private LocalDateTime checkOutTime;

    @Builder
    public CompanyVisitor(Long id, Long companyId, String name, VisitorStatus status, String place, LocalDateTime checkInTime, LocalDateTime checkOutTime) {
        this.id = id;
        this.companyId = companyId;
        this.name = name;
        this.status = status;
        this.place = place;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }

    public CompanyVisitor checkOut() {
        this.status = VisitorStatus.퇴실;
        this.checkOutTime = LocalDateTime.now();
        return this;
    }

    public CompanyVisitorResponseDto companyVisitorResponseDto(String companyName) {
        return CompanyVisitorResponseDto.builder()
                .id(this.id)
                .companyName(companyName)
                .name(this.name)
                .place(this.place)
                .checkIn(this.checkInTime)
                .checkOut(this.checkOutTime)
                .build();
    }
}
