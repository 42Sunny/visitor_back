package com.ftseoul.visitor.policy;

import com.ftseoul.visitor.data.Reserve;
import com.ftseoul.visitor.dto.reserve.ReserveVisitorDto;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class PresentativeReservePolicy implements ReservePolicy{

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
        return null;
    }
}
