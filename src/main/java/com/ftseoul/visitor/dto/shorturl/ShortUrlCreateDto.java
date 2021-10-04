package com.ftseoul.visitor.dto.shorturl;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShortUrlCreateDto implements Serializable {
    private String id;
    private String originalUrl;
}
