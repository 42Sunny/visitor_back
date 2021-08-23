package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.dto.ShortUrlCreateDto;
import com.ftseoul.visitor.dto.ShortUrlCreateListDto;
import com.ftseoul.visitor.dto.ShortUrlDto;
import com.ftseoul.visitor.dto.ShortUrlResponseDto;
import com.ftseoul.visitor.dto.ShortUrlResponseListDto;
import com.ftseoul.visitor.dto.StaffDto;
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

    public List<ShortUrlResponseDto> createUrls(List<ShortUrlDto> shortUrlDtoList) {
        log.info("Create Short Urls");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiKey);
        ShortUrlCreateListDto urlRequestObj = createUrlRequestJson(shortUrlDtoList);
        HttpEntity<ShortUrlCreateListDto> request = new HttpEntity<>(urlRequestObj, headers);
        ShortUrlResponseListDto shortUrlResponseListDto = restTemplate.postForObject(
            domain + "/urls", request, ShortUrlResponseListDto.class);
        return shortUrlResponseListDto.getUrlResponseList();
    }

    public ShortUrlCreateListDto createUrlRequestJson(List<ShortUrlDto> shortUrlDtoList) {
        ShortUrlCreateListDto result = new ShortUrlCreateListDto();
        for (ShortUrlDto shortUrlDto : shortUrlDtoList) {
            if (!shortUrlDto.isStaff()) {
                result.addUrl(new ShortUrlCreateDto(shortUrlDto.getPhone(),
                    qrPath + shortUrlDto.getId()));
            } else {
                result.addUrl(new ShortUrlCreateDto("staff", lookUpPath + shortUrlDto.getId()));
            }
        }
        return result;
    }

    public List<ShortUrlDto> createShortUrlDtoList(List<Visitor> visitors, StaffDto staffReserveInfo) {
        List<ShortUrlDto> shortUrlDtos = visitors
            .stream()
            .map(v -> new ShortUrlDto(seed.encryptUrl(v.getId().toString())
                , seed.decrypt(v.getPhone())
                , false
            ))
            .collect(Collectors.toList());
        shortUrlDtos.add(new ShortUrlDto(staffReserveInfo.getReserveId().toString(),
            staffReserveInfo.getPhone(), true));
        return shortUrlDtos;
    }
}
