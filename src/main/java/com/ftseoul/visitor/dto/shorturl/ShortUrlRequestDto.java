package com.ftseoul.visitor.dto.shorturl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ShortUrlRequestDto implements Serializable {
    private List<ShortUrlCreateDto> urlList = new ArrayList<>();

    public void addUrl(ShortUrlCreateDto url) {
        this.urlList.add(url);
    }
}
