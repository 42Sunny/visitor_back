package com.ftseoul.visitor.dto.reserve;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ftseoul.visitor.dto.visitor.VisitorDto;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.ftseoul.visitor.encrypt.Seed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReserveVisitorDto implements Serializable {
    @NotBlank(message = "예약 장소를 입력해주세요")
    private String place;
    @NotBlank(message = "방문 직원 이름을 입력해주세요")
    private String targetStaffName;
    @NotBlank(message = "방문 목적을 입력해주세요")
    private String purpose;

    @NotNull(message = "방문 일시를 입력해주세요")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime date;

    @NotNull(message = "방문자는 최소 한명이상이어야 합니다")
    @Valid
    private List<VisitorDto> visitor;

    @Override
    public String toString() {
        return "ReserveVisitorDto{" +
                "place='" + place + '\'' +
                ", targetStaffName='" + targetStaffName + '\'' +
                ", purpose='" + purpose + '\'' +
                ", date=" + date +
                ", visitor=" + visitor +
                '}';
    }

    public ReserveVisitorDto encryptDto(Seed seed) {
        this.targetStaffName = seed.encrypt(targetStaffName);
        visitor = visitor.stream().map(visitorDto -> visitorDto.encryptDto(seed)).collect(Collectors.toList());
        return this;
    }
}

