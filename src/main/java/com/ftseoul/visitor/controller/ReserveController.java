package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.data.Reserve;
import com.ftseoul.visitor.dto.*;
import com.ftseoul.visitor.dto.payload.Response;
import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.service.ReserveService;
import com.ftseoul.visitor.websocket.WebSocketService;
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
    private final WebSocketService socketService;
    private final Seed seed;

    @GetMapping("/reserve/{id}")
    public ReserveListResponseDto findById(@PathVariable Long id) {
        log.info("/reserve/" + id + "\n");
        return reserveService.findById(id);
    }

    @DeleteMapping("/reserve/{id}")
    public Response deleteById(@PathVariable Long id) {
        log.info("DELETE /reserve/" + id.toString());
        socketService.sendMessageToSubscriber("/visitor", "예약번호: " + id + "예약이 삭제되었습니다");
        return reserveService.deleteById(id);
    }

    @PostMapping("/reserves")
    public List<ReserveListResponseDto> searchReserveList(@Valid @RequestBody ReserveRequestDto reserveRequestDto) {
        return reserveService.findReservesByNameAndPhone(reserveRequestDto);
    }

    @DeleteMapping("/reserve")
    public boolean reserveVisitorDelete(Long reserve_id, @Valid @RequestBody ReserveRequestDto deleteRequestDto) {
        return reserveService.visitorReserveDelete(reserve_id, deleteRequestDto);
    }

    @PutMapping("/reserve")
    public boolean reserveUpdate(@Valid @RequestBody ReserveModifyDto reserveModifyDto) {
        boolean result = reserveService.updateReserve(reserveModifyDto);
        socketService.sendMessageToSubscriber("/visitor",
            "예약번호: " + reserveModifyDto.getReserveId() + " 예약이 수정되었습니다");
        return result;
    }

    @PostMapping(value = "/reserve/create")
    public ResponseEntity<ReserveIdDto> enrollReserve(@Valid @RequestBody ReserveVisitorDto reserveVisitorDto) {
        Reserve reserve = reserveService.saveReserve(reserveVisitorDto.encryptDto(seed));
        socketService.sendMessageToSubscriber("/visitor",
            "예약번호 :" + reserve.getId() + " 새로운 예약이 신청됐습니다");
        return new ResponseEntity<>(new ReserveIdDto(reserve.getId()), HttpStatus.CREATED);
    }
}
