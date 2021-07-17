package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.data.Reserve;
import com.ftseoul.visitor.data.Staff;
import com.ftseoul.visitor.dto.*;
import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.service.ReserveService;
import com.ftseoul.visitor.service.StaffService;
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
    private final StaffService  staffService;
    private final SMSService smsService;

    @GetMapping("/reserve/{id}")
    public ReserveListResponseDto findById(@PathVariable Long id) {
        return reserveService.findById(id);
    }

    @PostMapping("/reserves")
    public List<ReserveListResponseDto> searchReserveList(@RequestBody SearchReserveRequestDto reserveRequestDto) {
        return reserveService.findReserveByVisitor(reserveRequestDto);
    }

    @DeleteMapping("/reserve")
    public boolean reserveDelete(Long reserve_id, @RequestBody ReserveDeleteRequestDto deleteRequestDto) {
        log.info("reserve delete");
        log.info("id: " + reserve_id.toString() + ", dto: " + deleteRequestDto.toString());
        return reserveService.reserveDelete(reserve_id, deleteRequestDto);
    }

    @PutMapping("/reserve")
    public boolean reserveUpdate(@RequestBody ReserveModifyDto reserveModifyDto) {
        log.info("reserve update");
        log.info(reserveModifyDto.toString());
        return reserveService.updateReserve(reserveModifyDto) && visitorService.updateVisitors(reserveModifyDto);
    }

    @Transactional
    @PostMapping(value = "/reserve/create")
    public ResponseEntity<ReserveIdDto> enrollReserve(@RequestBody ReserveVisitorDto reserveVisitorDto) {
        log.info("/reserve/create");
        log.info("dto: " + reserveVisitorDto.toString());
        Reserve reserve = reserveService.saveReserve(reserveVisitorDto);
        List<Visitor> visitors = visitorService.saveVisitors(reserve.getId(), reserveVisitorDto.getVisitor());
        log.info("target Staff name: " + reserveVisitorDto.getTargetStaffName());
        Staff staff = staffService.findByName(reserveVisitorDto.getTargetStaffName());
        if (visitors != null) {
            smsService.sendMessages(visitors);
            smsService.sendMessage(new StaffDto(reserve.getId(), staff.getPhone()));
            return new ResponseEntity<>(new ReserveIdDto(reserve.getId()), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
