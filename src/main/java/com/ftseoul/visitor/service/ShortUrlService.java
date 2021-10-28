package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.dto.shorturl.ShortUrlCreateDto;
import com.ftseoul.visitor.dto.shorturl.ShortUrlRequestDto;
import com.ftseoul.visitor.dto.shorturl.ShortUrlDto;
import com.ftseoul.visitor.dto.shorturl.ShortUrlResponse;
import com.ftseoul.visitor.dto.shorturl.ShortUrlResponseDto;
import com.ftseoul.visitor.dto.staff.StaffReserveDto;
import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.util.Constants;
import com.ftseoul.visitor.util.QRUtil;
import java.time.LocalDateTime;
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
            Constants.DOMAIN + "/urls", request, ShortUrlResponse.class);
        log.info("Short Url Lists: {}", shortUrlResponse);
        return shortUrlResponse.getUrlResponseList();
    }

    public ShortUrlRequestDto createJsonRequest(List<ShortUrlDto> shortUrls) {
        ShortUrlRequestDto result = new ShortUrlRequestDto();
        for (ShortUrlDto shortUrlDto : shortUrls) {
            if (!shortUrlDto.isStaff()) {
                result.addUrl(new ShortUrlCreateDto(shortUrlDto.getPhone(),
                    Constants.QRPATH + shortUrlDto.getId()));
            } else {
                result.addUrl(new ShortUrlCreateDto("staff", Constants.LOOKUPPATH + shortUrlDto.getId()));
            }
        }
        return result;
    }

    public List<ShortUrlResponseDto> createShortUrls(List<Visitor> visitors, StaffReserveDto staffReserveInfo) {
        LocalDateTime reserveDate = staffReserveInfo.getDate();

        List<ShortUrlDto> shortUrlDtos = visitors
            .stream()
            .map(v -> new ShortUrlDto(seed.encryptUrl(QRUtil.make(v.getId().toString(), reserveDate))
                , seed.decrypt(v.getPhone())
                , false
            ))
            .collect(Collectors.toList());
        //add staff info
        ShortUrlDto staffShortUrlDto = new ShortUrlDto(staffReserveInfo.getReserveId().toString(),
            staffReserveInfo.getPhone(), true);
        shortUrlDtos.add(staffShortUrlDto);
        return createUrls(shortUrlDtos);
    }

    public List<ShortUrlResponseDto> filterVisitorShortUrls(List<ShortUrlResponseDto> list) {
        return list
            .stream()
            .filter(url -> !url.getId().equals("staff"))
            .collect(Collectors.toList());
    }

    public ShortUrlResponseDto filterStaffShortUrls(List<ShortUrlResponseDto> list) {
        List<ShortUrlResponseDto> staffShortUrl = list
            .stream()
            .filter(url -> url.getId().equals("staff"))
            .collect(Collectors.toList());
        if (staffShortUrl.isEmpty()) {
            log.error("스테프 단축 URL을 만드는데 실패했습니다");
            return null;
        } else {
            return staffShortUrl.get(0);
        }
    }
}
