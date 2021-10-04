package com.ftseoul.visitor.dto.qrcode;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QRCheckResponseDto implements Serializable {
    private String code;
    private String message;
    private String status;
}
