package com.ftseoul.visitor.dto.payload;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ErrorResponse implements Serializable {
    private String code;
    private String message;
}
