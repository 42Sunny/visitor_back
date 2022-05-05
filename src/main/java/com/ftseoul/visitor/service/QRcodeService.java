package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.DeviceRepository;
import com.ftseoul.visitor.data.ReserveRepository;
import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.data.VisitorRepository;
import com.ftseoul.visitor.data.visitor.VisitorStatus;
import com.ftseoul.visitor.dto.qrcode.QRCheckResponseDto;
import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.exception.error.InvalidDeviceException;
import com.ftseoul.visitor.exception.error.InvalidQRCodeException;
import com.ftseoul.visitor.websocket.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class QRcodeService {

    private final VisitorRepository visitorRepository;
    private final DeviceRepository deviceRepository;
    private final WebSocketService socketService;
    private final Seed seed;
    private final ReserveRepository reserveRepository;

    public String decodeQRText(String text) {
        String result;
        try {
            result = seed.decryptUrl(text);
        } catch (IllegalArgumentException ex) {
            throw new InvalidQRCodeException("code", text);
        }
        return result;
    }

    public QRCheckResponseDto checkQRCode(String text) {
        Visitor visitor = visitorRepository.findById(Long.parseLong(text))
            .orElseThrow(() -> new InvalidQRCodeException("code", text));
        return checkStatus(visitor);
    }

    private QRCheckResponseDto checkStatus(Visitor visitor) {
        QRCheckResponseDto result = null;
        String visitorName = seed.decrypt(visitor.getName());
        String message = visitorName + "님이 입실하셨습니다";


        /**
         * 0000... dummy 전화번호 방문자들 입실 처리 기능
         */
        boolean representativeFlag = checkRepresentativeVisitor(visitor);
        if (representativeFlag)
            return new QRCheckResponseDto("2000", "인증된 방문자", "입실");

        if (visitor.getStatus() == VisitorStatus.대기) {
            visitor.updateStatus(VisitorStatus.입실);
            visitor.checkIn();
            log.info(message);
            socketService.sendMessageToSubscriber("/visitor", message);
            visitorRepository.save(visitor);
            result = new QRCheckResponseDto("2000", "인증된 방문자", "입실");
        }
        else if (visitor.getStatus() == VisitorStatus.입실)
        {
            log.info(message);
            socketService.sendMessageToSubscriber("/visitor", message);
            result = new QRCheckResponseDto("2000", "인증된 방문자", "입실");
        }
        return result;
    }

    /**
     * Representative visitor 입실 처리 -> sendMessage
     * 같은 reserveID 가지는 Visitors 찾은 후 representative 제외 리스트 생성 -> 입실처리 (메세지 X)
     *
     * @param visitor
     * @return
     */
    private boolean checkRepresentativeVisitor(Visitor visitor){
        List<Visitor> findVisitors = visitorRepository.findAllByReserveId(visitor.getReserveId());

        if (isRepresentativeVisitor(findVisitors)){

            visitor.updateStatus(VisitorStatus.입실);
            visitor.checkIn();
            log.info(visitor.getName() + "님이 입실하였습니다.");
            socketService.sendMessageToSubscriber("/visitor", visitor.getName() + "님이 입실하였습니다.");
            visitorRepository.save(visitor);

            findVisitors = visitorRepository.findAllByReserveId(visitor.getReserveId()).stream()
                    .filter(visitor1 -> !visitor1.getPhone().equals(visitor.getPhone()))
                    .collect(Collectors.toList());

            findVisitors.forEach(
                    visitor1 -> {
                        if (visitor1.getStatus() == VisitorStatus.대기){
                            visitor1.updateStatus(VisitorStatus.입실);
                            visitor1.checkIn();
                            log.info(seed.decrypt(visitor1.getName()) + "님이 입실하셨습니다");
                            visitorRepository.save(visitor1);
                        }
                        else if (visitor1.getStatus() == VisitorStatus.입실){
                            log.info(seed.decrypt(visitor1.getName()) + "님이 입실하셨습니다");
                        }
                    }
            );
            return true;
        }
        else{
            return false;
        }
    }
    private boolean isRepresentativeVisitor(List<Visitor> visitors){

        Optional<Visitor> first = visitors.stream()
                .filter(visitor1 -> seed.encrypt(visitor1.getPhone()).equals("00000000000"))
                .findFirst();
        return first.isPresent();
    }
    public void checkAllowedDevice(String deviceId) {
        log.info("DeviceId is {},", deviceId);
        if (!deviceRepository.existsById(deviceId)){
            log.error("등록되지 않은 기기: {}", deviceId);
            throw new InvalidDeviceException(deviceId);
        }
        log.info("Allowed Device");
    }
}
