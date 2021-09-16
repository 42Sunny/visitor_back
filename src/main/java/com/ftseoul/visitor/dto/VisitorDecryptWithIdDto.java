package com.ftseoul.visitor.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ftseoul.visitor.data.visitor.VisitorStatus;
import com.ftseoul.visitor.encrypt.Seed;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VisitorDecryptWithIdDto implements Serializable {
    private Long visitorId;

    private Long reserveId;

    private String name;

    private String phone;

    private String organization;

    private VisitorStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime checkInTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime checkOutTime;

    public VisitorDecryptWithIdDto decryptDto(Seed seed) {
        this.name = seed.decrypt(this.name);
        this.phone = seed.decrypt(this.phone);
        return this;
    }
}
