package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.dto.DateFoundResponseDto;
import com.ftseoul.visitor.dto.DateRequestDto;
import com.ftseoul.visitor.service.InfoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class InfoController {

    private final InfoService infoService;

    @PostMapping("/info/reserve/date")
    public List<DateFoundResponseDto> findByDate(@RequestBody DateRequestDto date) {
        log.info("/reserve/date\n parameter: {}", date);
        return infoService.findAllByDate(date.getDate());
    }
}
