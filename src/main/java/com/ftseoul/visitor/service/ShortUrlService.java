package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.dto.ShortUrlCreateDto;
import com.ftseoul.visitor.dto.ShortUrlCreateListDto;
import com.ftseoul.visitor.dto.ShortUrlDto;
import com.ftseoul.visitor.dto.ShortUrlResponseDto;
import com.ftseoul.visitor.dto.ShortUrlResponseListDto;
import com.ftseoul.visitor.dto.StaffShortUrlDto;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpHead;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ShortUrlService {
    private final String domain = "Https://dev.vstr.kr";
    private final String qrPath = "https://visitor.dev.42seoul.io/qr/";
    private final String lookUpPath = "https://visitor.dev.42seoul.io/lookup";

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
    public String createUrl(String reserveId) {
        log.info("Create Short Url");
        String originalUrl = lookUpPath; // + reserveId
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("originalUrl", originalUrl);
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<StaffShortUrlDto> shortUrlResponse = restTemplate.postForEntity(domain + "/url", request
            ,StaffShortUrlDto.class);
        return domain + "/" + shortUrlResponse.getBody().getValue();
    }

}
