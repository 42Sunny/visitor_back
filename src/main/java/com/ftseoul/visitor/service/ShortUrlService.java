package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.dto.shorturl.ShortUrlCreateDto;
import com.ftseoul.visitor.dto.shorturl.ShortUrlRequestDto;
import com.ftseoul.visitor.dto.shorturl.ShortUrlDto;
import com.ftseoul.visitor.dto.shorturl.ShortUrlResponse;
import com.ftseoul.visitor.dto.shorturl.ShortUrlResponseDto;
import com.ftseoul.visitor.dto.staff.StaffDto;
import com.ftseoul.visitor.encrypt.Seed;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ShortUrlService {
    private final String domain = "Https://dev.vstr.kr";
    private final String qrPath = "https://visitor.dev.42seoul.io/qr/";
    private final String lookUpPath = "https://visitor.dev.42seoul.io/reserve-info/";
    private final Seed seed;

    @Value("${api-key}")
    private String apiKey;

    public ShortUrlService(Seed seed) {
        this.seed = seed;
    }

    public List<ShortUrlResponseDto> createUrls(List<ShortUrlDto> shortUrls) {
        log.info("Create Short Urls");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiKey);
        ShortUrlRequestDto urlRequestObj = createJsonRequest(shortUrls);
        HttpEntity<ShortUrlRequestDto> request = new HttpEntity<>(urlRequestObj, headers);
        ShortUrlResponse shortUrlResponse = restTemplate.postForObject(
            domain + "/urls", request, ShortUrlResponse.class);
        assert shortUrlResponse != null;
        return shortUrlResponse.getUrlResponseList();
    }

    public ShortUrlRequestDto createJsonRequest(List<ShortUrlDto> shortUrls) {
        ShortUrlRequestDto result = new ShortUrlRequestDto();
        for (ShortUrlDto shortUrlDto : shortUrls) {
            if (!shortUrlDto.isStaff()) {
                result.addUrl(new ShortUrlCreateDto(shortUrlDto.getPhone(),
                    qrPath + shortUrlDto.getId()));
            } else {
                result.addUrl(new ShortUrlCreateDto("staff", lookUpPath + shortUrlDto.getId()));
            }
        }
        return result;
    }

    public List<ShortUrlDto> createShortUrlDtos(List<Visitor> visitors, StaffDto staffReserveInfo) {
        List<ShortUrlDto> shortUrlDtos = visitors
            .stream()
            .map(v -> new ShortUrlDto(seed.encryptUrl(v.getId().toString())
                , seed.decrypt(v.getPhone())
                , false
            ))
            .collect(Collectors.toList());
        //add staff info
        ShortUrlDto staffShortUrlDto = new ShortUrlDto(staffReserveInfo.getReserveId().toString(),
            staffReserveInfo.getPhone(), true);
        shortUrlDtos.add(staffShortUrlDto);
        return shortUrlDtos;
    }
}
