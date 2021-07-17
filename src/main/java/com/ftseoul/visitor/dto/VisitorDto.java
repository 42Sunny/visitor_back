package com.ftseoul.visitor.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VisitorDto implements Serializable {
    private Long reserve_id;

    private String name;

    private String phone;

    private String organization;

    @Override
    public String toString() {
        return "VisitorDto{" +
                "reserve_id=" + reserve_id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", organization='" + organization + '\'' +
                '}';
    }
}
