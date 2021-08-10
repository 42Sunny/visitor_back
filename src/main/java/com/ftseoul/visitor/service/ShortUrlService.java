package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.dto.ShortUrlCreateDto;
import com.ftseoul.visitor.dto.ShortUrlCreateListDto;
import com.ftseoul.visitor.dto.ShortUrlDto;
import com.ftseoul.visitor.dto.ShortUrlResponseDto;
import com.ftseoul.visitor.dto.ShortUrlResponseListDto;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpHead;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ShortUrlService {
    private final String domain = "Https://dev.vstr.kr";
    private final String qrPath = "https://visitor.dev.42seoul.io/qr/";

    public ShortUrlResponseListDto createUrls(List<ShortUrlDto> shortUrlDtoList) {
        log.info("Create Short Urls");
        RestTemplate restTemplate = new RestTemplate();
        ShortUrlCreateListDto urlRequestObj = createUrlRequestJson(shortUrlDtoList);
        ShortUrlResponseListDto shortUrlResponseListDto = restTemplate.postForObject(
            domain + "/urls", urlRequestObj, ShortUrlResponseListDto.class);
        return shortUrlResponseListDto;
    }

    public ShortUrlCreateListDto createUrlRequestJson(List<ShortUrlDto> shortUrlDtoList) {
        ShortUrlCreateListDto result = new ShortUrlCreateListDto();
        for (ShortUrlDto shortUrlDto : shortUrlDtoList) {
            result.addUrl(new ShortUrlCreateDto(shortUrlDto.getPhone(),
                qrPath + shortUrlDto.getId()));
        }
        return result;
    }
}
