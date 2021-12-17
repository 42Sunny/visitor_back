package com.ftseoul.visitor.dto.companyvisitor;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class CompanyVisitorRequestDto implements Serializable {
    private Long companyId;
    private String visitorName;
    private String place;

    @Override
    public String toString() {
        return "입실정보{" +
                "companyId=" + companyId +
                ", visitorName='" + visitorName + '\'' +
                ", place='" + place + '\'' +
                '}';
    }
}
