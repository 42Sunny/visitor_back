package com.ftseoul.visitor.controller;

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

    private final Seed seed;
    private final QRcodeService qRcodeService;

    @PostMapping("/qrcode")
    public ResponseEntity<Boolean> qrcodeCheck(@RequestBody QRCodePayload qrCodePayload) {
        log.info("Check qrcode text: {}", qrCodePayload.getCode());
        String originalText = null;
        try {
            originalText = seed.decryptUrl(qrCodePayload.getCode());
        } catch (IllegalArgumentException ex) {
            throw new ResourceNotFoundException("QRCode", "code", qrCodePayload.getCode());
        }
        Boolean result = qRcodeService.checkQRCode(originalText);
        if (!result) {
            log.error("Not a valid qrcode");
            throw new ResourceNotFoundException("QRCode", "code", qrCodePayload.getCode());
        }
        log.info("Valid qrcode: {}", originalText);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
