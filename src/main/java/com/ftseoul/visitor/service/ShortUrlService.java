package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Staff;
import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.dto.ShortUrlCreateDto;
import com.ftseoul.visitor.dto.ShortUrlCreateListDto;
import com.ftseoul.visitor.dto.ShortUrlDto;
import com.ftseoul.visitor.dto.ShortUrlResponseDto;
import com.ftseoul.visitor.dto.ShortUrlResponseListDto;
import com.ftseoul.visitor.dto.StaffDto;
import com.ftseoul.visitor.dto.StaffShortUrlDto;
import com.ftseoul.visitor.encrypt.Seed;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
    private final Seed seed;

    public ShortUrlService(Seed seed) {
        this.seed = seed;
    }

    public List<ShortUrlResponseDto> createUrls(List<ShortUrlDto> shortUrlDtoList) {
        log.info("Create Short Urls");
        RestTemplate restTemplate = new RestTemplate();
        ShortUrlCreateListDto urlRequestObj = createUrlRequestJson(shortUrlDtoList);
        ShortUrlResponseListDto shortUrlResponseListDto = restTemplate.postForObject(
            domain + "/urls", urlRequestObj, ShortUrlResponseListDto.class);
        return shortUrlResponseListDto.getUrlResponseList();
    }

    public ShortUrlCreateListDto createUrlRequestJson(List<ShortUrlDto> shortUrlDtoList) {
        ShortUrlCreateListDto result = new ShortUrlCreateListDto();
        for (ShortUrlDto shortUrlDto : shortUrlDtoList) {
            if (!shortUrlDto.isStaff()) {
                result.addUrl(new ShortUrlCreateDto(shortUrlDto.getPhone(),
                    qrPath + shortUrlDto.getId()));
            } else {
                result.addUrl(new ShortUrlCreateDto("staff", lookUpPath));
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
