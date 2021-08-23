package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.data.visitor.VisitorStatus;
import com.ftseoul.visitor.dto.QRCheckResponseDto;
import com.ftseoul.visitor.dto.payload.QRCodePayload;
import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.exception.ResourceNotFoundException;
import com.ftseoul.visitor.service.QRcodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class QRcodeController {

    private final QRcodeService qRcodeService;

    @PostMapping("/qrcode")
    public ResponseEntity<?> qrcodeCheck(@RequestBody QRCodePayload qrCodePayload) {
        log.info("Check qrcode text: {}", qrCodePayload.getCode());
        String code = qRcodeService.decodeQRText(qrCodePayload.getCode());
        QRCheckResponseDto qrCheckResponseDto = qRcodeService.checkQRCode(code);
        return new ResponseEntity<>(qrCheckResponseDto, HttpStatus.OK);
    }

}
