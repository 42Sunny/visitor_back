package com.ftseoul.visitor.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reserve")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Reserve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String place;

    private Long targetStaff;

    private String visitorOrganization;

    private String visitorName;

    private String visitorPhone;

    private String purpose;

    private LocalDateTime date;

    public void updatePlace(String place) {
        this.place = place;
    }

    public void updateTargetStaff(Long targetStaff) {
        this.targetStaff = targetStaff;
    }

    public void updateVisitorOrganization(String visitorOrganization) {
        this.visitorOrganization = visitorOrganization;
    }

    public void updateVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public void updateVisitorPhone(String visitorPhone) {
        this.visitorPhone = visitorPhone;
    }

    public void updatePurpose(String purpose) {
        this.purpose = purpose;
    }

    public void updateDate(LocalDateTime date) {
        this.date = date;
    }
}
