package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.dto.qrcode.QRCheckResponseDto;
import com.ftseoul.visitor.service.QRcodeService;
import com.ftseoul.visitor.util.QRUtil;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class QRcodeController {

    private final QRcodeService qRcodeService;

    @PostMapping("/qrcode")
    public QRCheckResponseDto qrcodeCheck(String code, HttpServletRequest request) {
        log.info("Check qrcode text: {}", code);
        String decodeQRText = qRcodeService.decodeQRText(code);
        QRUtil.validateFormat(decodeQRText);

        String[] original = decodeQRText.split("\\.");
        String text = original[0];
        String timeStamp = original[1];

        QRUtil.withinToday(LocalDateTime.parse(timeStamp));

        log.info("decodeQRText is {}", decodeQRText);
        qRcodeService.checkAllowedDevice(request.getHeader("X-42Cadet-Device-Id"));
        return qRcodeService.checkQRCode(text);
    }
}
