package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.data.Reserve;
import com.ftseoul.visitor.dto.ReserveDeleteRequestDto;
import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.dto.ReserveIdDto;
import com.ftseoul.visitor.dto.ReserveModifyDto;
import com.ftseoul.visitor.dto.ReserveResponseDto;
import com.ftseoul.visitor.dto.ReserveVisitorDto;
import com.ftseoul.visitor.dto.SearchReserveRequestDto;
import com.ftseoul.visitor.service.ReserveService;
import com.ftseoul.visitor.service.VisitorService;
import com.ftseoul.visitor.service.sns.SMSService;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReserveController {

    private final ReserveService reserveService;
    private final VisitorService visitorService;
    private final SMSService smsService;

    @GetMapping("/reserve/{id}")
    public Reserve findById(@PathVariable Long id) {
        return reserveService.findById(id);
    }

    @PostMapping("/reserves")
    public List<ReserveResponseDto> searchReserveList(@RequestBody SearchReserveRequestDto reserveRequestDto) {
        return reserveService.findAllByNameAndPhone(reserveRequestDto);
    }

    @DeleteMapping("/reserve")
    public boolean reserveDelete(@RequestParam Long reserve_id, @RequestBody ReserveDeleteRequestDto deleteRequestDto) {
        return reserveService.reserveDelete(reserve_id, deleteRequestDto);
    }

    @PutMapping("/reserve")
    public boolean reserveUpdate(@RequestBody ReserveModifyDto reserveModifyDto) {
        return reserveService.updateReserve(reserveModifyDto) && visitorService.updateVisitors(reserveModifyDto);
    }

    @Transactional
    @PostMapping(value = "/reserve/create")
    public ResponseEntity<ReserveIdDto> enrollReserve(@RequestBody ReserveVisitorDto reserveVisitorDto) {
        Reserve reserve = reserveService.saveReserve(reserveVisitorDto);
        List<Visitor> visitors = visitorService.saveVisitors(reserve.getId(), reserveVisitorDto.getVisitor());
        if (visitors != null) {
            smsService.sendMessages(visitors);
            return new ResponseEntity<>(new ReserveIdDto(reserve.getId()), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
