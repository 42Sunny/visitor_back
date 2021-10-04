package com.ftseoul.visitor.dto.visitor;

import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.encrypt.Seed;
import lombok.Builder;
import lombok.Getter;

@Getter
public class VisitorDecryptDto {
    private Long reserveId;

    private String name;

    private String phone;

    private String organization;

    @Builder
    public VisitorDecryptDto(Long reserveId, String name, String phone, String organization) {
        this.reserveId = reserveId;
        this.name = name;
        this.phone = phone;
        this.organization = organization;
    }

    public VisitorDecryptDto decryptDto(Seed seed) {
        this.name = seed.decrypt(this.name);
        this.phone = seed.decrypt(this.phone);
        return this;
    }

    public Visitor toEntity() {
        return Visitor.builder()
                .reserve_id(reserveId)
                .name(this.name)
                .phone(this.phone)
                .organization(organization)
                .build();
    }

    @Override
    public String toString() {
        return "VisitorDecryptDto{" +
                "reserveId=" + reserveId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", organization='" + organization + '\'' +
                '}';
    }
}
