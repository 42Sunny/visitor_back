package com.ftseoul.visitor.dto.shorturl;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShortUrlDto implements Serializable {
    private String id;
    private String phone;
    private boolean isStaff;
}
