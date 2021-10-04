package com.ftseoul.visitor.dto.error;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FrontErrorResponseDto implements Serializable {
    private String code;
    private String message;
}
