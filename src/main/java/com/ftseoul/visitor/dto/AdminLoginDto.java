package com.ftseoul.visitor.dto;

import com.ftseoul.visitor.data.Admin;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminLoginDto {
    private String username;
    private String password;

    @Builder
    public AdminLoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Admin toEntity() {
        return Admin.builder()
                .username(username)
                .password(password)
                .build();
    }
}
