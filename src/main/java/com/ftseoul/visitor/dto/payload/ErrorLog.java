package com.ftseoul.visitor.dto.payload;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ErrorLog {

    private String serviceName;

    private String ip;

    private String path;

    private String message;

    private String trace;

    private LocalDateTime errorDateTime = LocalDateTime.now();

    private String headerMap;

    private String parameterMap;

}
