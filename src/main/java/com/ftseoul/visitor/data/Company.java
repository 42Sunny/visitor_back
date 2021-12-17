package com.ftseoul.visitor.data;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String name;

    @Column(length = 200, nullable = false)
    private String phone;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Builder
    public Company(Long id, String name, String phone, Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.isDeleted = isDeleted;
    }
}
