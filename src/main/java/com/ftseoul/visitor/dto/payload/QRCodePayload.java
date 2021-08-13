package com.ftseoul.visitor.dto.payload;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QRCodePayload implements Serializable {
    private String code;
}
