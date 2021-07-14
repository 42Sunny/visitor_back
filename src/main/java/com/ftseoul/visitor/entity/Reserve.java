package com.ftseoul.visitor.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "reserve")
@Getter
public class Reserve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String place;

    private Long targetStaff;

    private String visitorOrganization;

    private String visitorName;

    private String visitor;

    private String purpose;

    private LocalDateTime date;
}
