package com.ftseoul.visitor.dto;

import com.ftseoul.visitor.encrypt.Seed;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StaffDecryptDto {
    private Long id;
    private String name;
    private String phone;

    @Builder
    public StaffDecryptDto(Long id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public StaffDecryptDto decryptDto(Seed seed) {
        this.name = seed.decrypt(this.name);
        this.phone = seed.decrypt(this.phone);
        return this;
    }
}
