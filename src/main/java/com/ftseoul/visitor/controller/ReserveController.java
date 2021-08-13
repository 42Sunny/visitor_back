package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.data.Reserve;
import com.ftseoul.visitor.data.Staff;
import com.ftseoul.visitor.dto.*;
import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.service.ReserveService;
import com.ftseoul.visitor.service.StaffService;
import com.ftseoul.visitor.service.VisitorService;
import com.ftseoul.visitor.service.sns.SMSService;
import javax.transaction.Transactional;
import javax.validation.Valid;
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
    private final Seed seed;

    @GetMapping("/reserve/{id}")
    public ReserveListResponseDto findById(@PathVariable Long id) {
        log.info("/reserve/" + id + "\n");
        return reserveService.findById(id);
    }

    @PostMapping("/reserves")
    public List<ReserveListResponseDto> searchReserveList(@Valid @RequestBody SearchReserveRequestDto reserveRequestDto) {
        log.info("/reserves\n" + "parameter: " + reserveRequestDto);
        return reserveService.findReserveByVisitor(reserveRequestDto);
    }

    @DeleteMapping("/reserve")
    public boolean reserveDelete(Long reserve_id, @Valid @RequestBody ReserveDeleteRequestDto deleteRequestDto) {
        log.info("reserve delete\nid: " + reserve_id.toString() + ", dto: " + deleteRequestDto);
        return reserveService.reserveDelete(reserve_id, deleteRequestDto);
    }

    @PutMapping("/reserve")
    public boolean reserveUpdate(@Valid @RequestBody ReserveModifyDto reserveModifyDto) {
        log.info("reserve update: " + reserveModifyDto);
        return reserveService.updateReserve(reserveModifyDto);
    }

    @Transactional
    @PostMapping(value = "/reserve/create")
    public ResponseEntity<ReserveIdDto> enrollReserve(@Valid @RequestBody ReserveVisitorDto reserveVisitorDto) {
        log.info("/reserve/create\ndto: " + reserveVisitorDto);
        Reserve reserve = reserveService.saveReserve(reserveVisitorDto.encryptDto(seed));
        return new ResponseEntity<>(new ReserveIdDto(reserve.getId()), HttpStatus.CREATED);
    }
}
