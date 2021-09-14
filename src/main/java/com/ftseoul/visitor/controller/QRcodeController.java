package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.dto.QRCheckResponseDto;
import com.ftseoul.visitor.service.QRcodeService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class QRcodeController {

    private final QRcodeService qRcodeService;

    @PostMapping("/qrcode")
    public ResponseEntity<?> qrcodeCheck(String code, HttpServletRequest request) {
        log.info("Check qrcode text: {}", code);
        String decodeQRText = qRcodeService.decodeQRText(code);
        log.info("decodeQRText is {}", decodeQRText);
        qRcodeService.checkAllowedDevice(request.getHeader("X-42Cadet-Device-Id"));
        QRCheckResponseDto qrCheckResponseDto = qRcodeService.checkQRCode(decodeQRText);
        return new ResponseEntity<>(qrCheckResponseDto, HttpStatus.OK);
    }
}
