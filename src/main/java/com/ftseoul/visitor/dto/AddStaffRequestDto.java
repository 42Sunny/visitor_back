package com.ftseoul.visitor.dto;

import com.ftseoul.visitor.data.Staff;
import com.ftseoul.visitor.encrypt.Seed;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AddStaffRequestDto {
    private String name;
    private String phone;

    @Builder
    public AddStaffRequestDto(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public AddStaffRequestDto encrypt(Seed seed) {
        this.name = seed.encrypt(this.name);
        this.phone = seed.encrypt(this.phone);
        return this;
    }

    @Override
    public String toString() {
        return "AddStaffRequestDto{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public Staff toEntity() {
        return Staff.builder()
                .phone(this.phone)
                .name(this.name)
                .build();
    }
}
