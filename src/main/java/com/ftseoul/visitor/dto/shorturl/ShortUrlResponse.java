package com.ftseoul.visitor.dto.shorturl;

import java.io.Serializable;
import java.util.List;
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
public class ShortUrlResponse implements Serializable {
    private List<ShortUrlResponseDto> urlResponseList;
}
