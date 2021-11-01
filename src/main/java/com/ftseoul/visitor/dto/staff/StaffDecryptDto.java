package com.ftseoul.visitor.dto.staff;

import com.ftseoul.visitor.encrypt.Seed;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StaffDecryptDto {
    private Long id;
    private String name;
    private String phone;
    private String department;

    @Builder
    public StaffDecryptDto(Long id, String name, String phone, String department) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.department = department;
    }

    public StaffDecryptDto decryptDto(Seed seed) {
        this.name = seed.decrypt(this.name);
        this.phone = seed.decrypt(this.phone);
        return this;
    }
}
