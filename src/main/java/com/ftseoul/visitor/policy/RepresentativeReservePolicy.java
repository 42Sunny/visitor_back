package com.ftseoul.visitor.policy;

import com.ftseoul.visitor.data.Reserve;
import com.ftseoul.visitor.data.Staff;
import com.ftseoul.visitor.data.Visitor;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RepresentativeReservePolicy implements ReservePolicy{

    private final ReserveService reserveService;
    private final StaffService staffService;
    private final ShortUrlService shortUrlService;
    private final SMSService smsService;
    private final WebSocketService socketService;
    private final VisitorService visitorService;
    private final Seed seed;

    @Override
    public ReserveType getType() {
        return ReserveType.REPRESENTATIVE;
    }

    @Override
    public Reserve saveReserve(ReserveVisitorDto reserveVisitorDto) {
        reserveVisitorDto = reserveVisitorDto.encryptDto(seed);
        Staff staff = staffService.findByName(reserveVisitorDto.getTargetStaffName());
        log.info("staff found: {}", staff);
        Reserve reserve = reserveService.saveReserveWithoutCheckDup(reserveVisitorDto, staff.getId());
        List<Visitor> visitors = visitorService.saveVisitors(reserve.getId(), reserveVisitorDto.getVisitor());

        StaffReserveDto staffReserveInfo = new StaffReserveDto(reserve.getId(), staff.getPhone(),
                reserveVisitorDto.getPurpose(), reserveVisitorDto.getPlace(), reserveVisitorDto.getDate(),
                visitors);

        /**
         * 더미 번호가 입력된 방문자 제거
         */
        visitors = visitors.stream()
                .filter(visitor -> !visitor.getPhone().equals("00000000000"))
                .collect(Collectors.toList());

        List<ShortUrlResponseDto> shortUrlList = shortUrlService.createShortUrls(visitors, staffReserveInfo);
        List<ShortUrlResponseDto> visitorShortUrls = shortUrlService.filterVisitorShortUrls(shortUrlList);
        ShortUrlResponseDto staffShortUrl = shortUrlService.filterStaffShortUrls(shortUrlList);


        visitorShortUrls.forEach(v -> smsService.sendMessage(v.getId(),visitorService.createSMSMessage(v.getValue())));
        smsService.sendMessage(seed.decrypt(staff.getPhone()), staffService.createSaveSMSMessage(visitors, reserve.getDate(), staffShortUrl.getValue()));
        log.info("Send message to Staff and Visitors");

        socketService.sendMessageToSubscriber("/visitor",
                "예약번호 :" + reserve.getId() + " 새로운 예약이 신청됐습니다");
        return reserve;
    }
}
