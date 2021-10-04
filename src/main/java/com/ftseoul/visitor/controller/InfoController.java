package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.dto.reserve.DateFoundResponseDto;
import com.ftseoul.visitor.dto.reserve.DateRequestDto;
import com.ftseoul.visitor.dto.error.ErrorResponseDto;
import com.ftseoul.visitor.dto.visitor.UpdateStatusResponseDto;
import com.ftseoul.visitor.dto.visitor.UpdateVisitorStatusDto;
import com.ftseoul.visitor.dto.payload.Response;
import com.ftseoul.visitor.dto.payload.VisitorStatusInfo;
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
        log.info("/info/reserve/date\nparameter: {}", date);
        return infoService.findAllByDate(date.getDate());
    }
    @PutMapping("/info/visitor/status")
    public ResponseEntity<?> updateVisitorStatus(@RequestBody UpdateVisitorStatusDto dto) {
        log.info("/info/visitor/status\n parameter: {}", dto);
        VisitorStatusInfo result = infoService.changeVisitorStatus(dto);
        if (result == null) {
            return new ResponseEntity<ErrorResponseDto>(new ErrorResponseDto(new Response("4090",
                "다른 상태 값을 입력해주세요")), HttpStatus.OK);
        }
        return new ResponseEntity<UpdateStatusResponseDto>(new UpdateStatusResponseDto("2000", result), HttpStatus.OK);
    }
}
