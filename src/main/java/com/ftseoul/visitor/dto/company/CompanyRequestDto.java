package com.ftseoul.visitor.dto.company;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class CompanyRequestDto implements Serializable {

    private String name;
    private String phone;

    @Override
    public String toString() {
        return "CompanyRequestDto{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
