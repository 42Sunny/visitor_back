package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.data.Reserve;
import com.ftseoul.visitor.data.Staff;
import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.dto.payload.Response;
import com.ftseoul.visitor.dto.reserve.ReserveIdDto;
import com.ftseoul.visitor.dto.reserve.ReserveListResponseDto;
import com.ftseoul.visitor.dto.reserve.ReserveModifyDto;
import com.ftseoul.visitor.dto.reserve.ReserveRequestDto;
import com.ftseoul.visitor.dto.reserve.ReserveVisitorDto;
import com.ftseoul.visitor.dto.shorturl.ShortUrlResponseDto;
import com.ftseoul.visitor.dto.staff.StaffReserveDto;
import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.service.ReserveService;
import com.ftseoul.visitor.service.ShortUrlService;
import com.ftseoul.visitor.service.StaffService;
import com.ftseoul.visitor.service.VisitorService;
import com.ftseoul.visitor.service.sns.SMSService;
import com.ftseoul.visitor.websocket.WebSocketService;
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
@RequestMapping("/v1")
public class ReserveController {

    private final ReserveService reserveService;
    private final StaffService staffService;
    private final ShortUrlService shortUrlService;
    private final SMSService smsService;
    private final WebSocketService socketService;
    private final VisitorService visitorService;
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

        List<Visitor> visitors = visitorService.findAllByReserveId(id);
        Staff staff = staffService.findById(reserveService.findStaffByReserveId(id));
        ReserveListResponseDto reserve = reserveService.findById(id);

        log.info("Staff name : {} ", staff.getName());
        smsService.sendMessage(seed.decrypt(staff.getPhone()), staffService.createDeleteSMSMessage(visitors,reserve.getDate()));
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
    @Transactional
    public boolean reserveUpdate(@Valid @RequestBody ReserveModifyDto reserveModifyDto) {
        Staff staff = staffService.findByName(seed.encrypt(reserveModifyDto.getTargetStaffName()));
        log.info("staff found: {}", staff);
        Reserve reserve = reserveService.updateReserve(reserveModifyDto, staff.getId());

        reserveModifyDto.encrypt(seed);
        List<Visitor> visitors = visitorService.updateVisitors(reserveModifyDto);
        log.info("Updated visitors: {}", visitors);

        StaffReserveDto staffReserveInfo = new StaffReserveDto(reserve.getId(), seed.decrypt(staff.getPhone()),
            reserveModifyDto.getPurpose(), reserveModifyDto.getPlace(),
            reserveModifyDto.getDate(), visitors);

        List<ShortUrlResponseDto> shortUrlList = shortUrlService.createShortUrls(visitors, staffReserveInfo);
        List<ShortUrlResponseDto> visitorShortUrls = shortUrlService.filterVisitorShortUrls(shortUrlList);
        ShortUrlResponseDto staffShortUrl = shortUrlService.filterStaffShortUrls(shortUrlList);

        visitorShortUrls.forEach(v -> smsService.sendMessage(v.getId(),visitorService.createSMSMessage(v.getValue())));
        smsService.sendMessage(seed.decrypt(staff.getPhone()), staffService.createModifySMSMessage(visitors, staffShortUrl.getValue()));
        log.info("Send text messages to visitors and staff");

        socketService.sendMessageToSubscriber("/visitor",
            "예약번호: " + reserveModifyDto.getReserveId() + " 예약이 수정되었습니다");
        return true;
    }

    @PostMapping(value = "/reserve/create")
    @Transactional
    public ResponseEntity<ReserveIdDto> saveReserve(@Valid @RequestBody ReserveVisitorDto reserveVisitorDto) {
        reserveVisitorDto = reserveVisitorDto.encryptDto(seed);
        Staff staff = staffService.findByName(reserveVisitorDto.getTargetStaffName());
        log.info("staff found: {}", staff);
        Reserve reserve = reserveService.saveReserve(reserveVisitorDto, staff.getId());
        List<Visitor> visitors = visitorService.saveVisitors(reserve.getId(), reserveVisitorDto.getVisitor());

        StaffReserveDto staffReserveInfo = new StaffReserveDto(reserve.getId(), staff.getPhone(),
            reserveVisitorDto.getPurpose(), reserveVisitorDto.getPlace(), reserveVisitorDto.getDate(),
            visitors);

        List<ShortUrlResponseDto> shortUrlList = shortUrlService.createShortUrls(visitors, staffReserveInfo);
        List<ShortUrlResponseDto> visitorShortUrls = shortUrlService.filterVisitorShortUrls(shortUrlList);
        ShortUrlResponseDto staffShortUrl = shortUrlService.filterStaffShortUrls(shortUrlList);

        visitorShortUrls.forEach(v -> smsService.sendMessage(v.getId(),visitorService.createSMSMessage(v.getValue())));
        smsService.sendMessage(seed.decrypt(staff.getPhone()), staffService.createSaveSMSMessage(visitors, reserve.getDate(), staffShortUrl.getValue()));
        log.info("Send message to Staff and Visitors");

        socketService.sendMessageToSubscriber("/visitor",
            "예약번호 :" + reserve.getId() + " 새로운 예약이 신청됐습니다");
        return new ResponseEntity<>(new ReserveIdDto(reserve.getId()), HttpStatus.CREATED);
    }
}
