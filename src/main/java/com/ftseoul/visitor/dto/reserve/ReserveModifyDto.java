package com.ftseoul.visitor.dto.reserve;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ftseoul.visitor.dto.visitor.VisitorModifyDto;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
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
public class ReserveModifyDto implements Serializable {
    @NotNull(message = "예약 아이디를 입력해주세요")
    private long reserveId;
    @NotBlank(message = "장소를 입력해주세요")
    private String place;
    @NotBlank(message = "방문 직원의 이름을 입력해주세요")
    private String targetStaffName;
    @NotBlank(message = "방문 목적을 입력해주세요")
    private String purpose;

    @NotNull(message = "방문 시간을 입력해주세요")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime date;

    @Valid
    @NotNull(message = "방문자가 존재하지 않습니다")
    private List<VisitorModifyDto> visitor;

    @Override
    public String toString() {
        return "ReserveModifyDto{" +
                "reserveId=" + reserveId +
                ", place='" + place + '\'' +
                ", targetStaffName='" + targetStaffName + '\'' +
                ", purpose='" + purpose + '\'' +
                ", date=" + date +
                ", visitor=" + visitor +
                '}';
    }

    public ReserveModifyDto encrypt(Seed seed) {
        seed.encrypt(this.targetStaffName);
        for (VisitorModifyDto v:
             this.visitor) {
            v.updateNameAndPhone(seed.encrypt(v.getName())
                    , seed.encrypt(v.getPhone()));
        }
        return this;
    }
}
