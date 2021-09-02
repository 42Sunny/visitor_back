package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.data.VisitorRepository;
import com.ftseoul.visitor.data.visitor.VisitorStatus;
import com.ftseoul.visitor.dto.QRCheckResponseDto;
import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.exception.InvalidQRCodeException;
import com.ftseoul.visitor.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class QRcodeService {

    private final VisitorRepository visitorRepository;
    private final Seed seed;

    public QRCheckResponseDto checkQRCode(String text) {
        Visitor visitor = visitorRepository.findById(Long.parseLong(text))
            .orElseThrow(() -> new InvalidQRCodeException("code", text));
        return checkQRCodeStatus(visitor);
    }

    public String decodeQRText(String text) {
        String result = null;
        try {
            result = seed.decryptUrl(text);
        } catch (IllegalArgumentException ex) {
            throw new InvalidQRCodeException("code", text);
        }
        return result;
    }

    public QRCheckResponseDto checkQRCodeStatus(Visitor visitor) {
        QRCheckResponseDto result = null;

        if (visitor.getStatus() == VisitorStatus.대기) {
            visitor.updateStatus(VisitorStatus.입실);
            visitor.updateCheckInTime(LocalDateTime.now());
            log.info("{}님이 입실 하셨습니다", seed.decrypt(visitor.getName()));
            visitorRepository.save(visitor);
            result = new QRCheckResponseDto("2000", "입실처리완료", "입실");
        }
        else if (visitor.getStatus() == VisitorStatus.입실)
        {
            visitor.updateStatus(VisitorStatus.퇴실);
            visitor.updateCheckOutTime(LocalDateTime.now());
            log.info("{}님이 퇴실 처리 되었습니다", seed.decrypt(visitor.getName()));
            visitorRepository.save(visitor);
            result = new QRCheckResponseDto("2000", "퇴실처리완료", "퇴실");
        }
        else if (visitor.getStatus() == VisitorStatus.퇴실)
        {
            log.info("이미 퇴실 처리된 방문자 입니다");
            result = new QRCheckResponseDto("4090", "이미 퇴실 처리된 방문자", "퇴실");
        }
        else if (visitor.getStatus() == VisitorStatus.만료)
        {
            log.info("QRCode의 기간이 만료되었습니다");
            result = new QRCheckResponseDto("4090", "기간만료 QR코드", "만료");
        }
        else
        {
            log.error("방문자 상태정보가 존재하지 않습니다");
        }
        return result;
    }
}
