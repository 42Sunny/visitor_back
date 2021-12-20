package com.ftseoul.visitor.dto.companyvisitor;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
public class CompanyVisitorRequestDto implements Serializable {
    @NotNull(message = "companyId를 입력해주세요.")
    private Long companyId;
    @NotBlank(message = "이름을 입력해주세요")
    private String visitorName;
    @NotBlank(message = "장소를 입력해주세요")
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
