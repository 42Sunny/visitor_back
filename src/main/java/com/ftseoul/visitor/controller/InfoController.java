package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.dto.DateFoundResponseDto;
import com.ftseoul.visitor.dto.DateRequestDto;
import com.ftseoul.visitor.dto.UpdateVisitorStatusDto;
import com.ftseoul.visitor.service.InfoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class InfoController {

    private final InfoService infoService;

    @PostMapping("/info/reserve/date")
    public List<DateFoundResponseDto> findByDate(@RequestBody DateRequestDto date) {
        log.info("/info/reserve/date\n parameter: {}", date);
        return infoService.findAllByDate(date.getDate());
    }
    @PutMapping("/info/visitor/status")
    public ResponseEntity<?> updateVisitorStatus(@RequestBody UpdateVisitorStatusDto dto) {
        log.info("/info/visitor/status\n parameter: {}", dto);
        boolean result = infoService.changeVisitorStatus(dto);
        if (!result) {
            return new ResponseEntity<String>("다른 상태값을 입력 해주세요", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<String>("변경에 성공했습니다", HttpStatus.OK);
    }
}
