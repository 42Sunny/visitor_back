package com.ftseoul.visitor.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReserveUpdateDto {
    private String place;

    private Long targetStaff;

    private String visitorOrganization;

    private String visitorName;

    private String visitorPhone;

    private String purpose;

    private LocalDateTime date;

    public ReserveUpdateDto(String place, Long targetStaff, String visitorOrganization,
                            String visitorName, String visitorPhone, String purpose, LocalDateTime date) {
        this.place = place;
        this.targetStaff = targetStaff;
        this.visitorOrganization = visitorOrganization;
        this.visitorName = visitorName;
        this.visitorPhone = visitorPhone;
        this.purpose = purpose;
        this.date = date;
    }
}
