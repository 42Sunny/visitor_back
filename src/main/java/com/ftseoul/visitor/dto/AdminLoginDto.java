package com.ftseoul.visitor.dto;

import com.ftseoul.visitor.data.Admin;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class AdminLoginDto implements Serializable {
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

    @Override
    public String toString() {
        return "AdminLoginDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
