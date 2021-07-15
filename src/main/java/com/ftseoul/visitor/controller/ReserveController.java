package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.data.Reserve;
import com.ftseoul.visitor.dto.ReserveDeleteRequestDto;
import com.ftseoul.visitor.dto.ReserveResponseDto;
import com.ftseoul.visitor.dto.SearchReserveRequestDto;
import com.ftseoul.visitor.service.ReserveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReserveController {

    private final ReserveService reserveService;

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
}
