package com.ftseoul.visitor.data;

import com.ftseoul.visitor.dto.company.CompanyResponseDto;
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

    @Column(nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean isDeleted = false;

    public Company LogicalDelete() {
        this.isDeleted = true;
        return this;
    }

    @Builder
    public Company(Long id, String name, String phone, Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.isDeleted = isDeleted;
    }

    public CompanyResponseDto companyResponseDto() {
        return CompanyResponseDto.builder()
                .id(this.id)
                .name(this.name)
                .phone(this.phone)
                .build();
    }
}
