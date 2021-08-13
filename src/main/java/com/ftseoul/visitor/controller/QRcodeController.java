package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.exception.ResourceNotFoundException;
import com.ftseoul.visitor.service.QRcodeService;
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

    private final Seed seed;
    private final QRcodeService qRcodeService;

    @PostMapping("/qrcode")
    public ResponseEntity<Boolean> qrcodeCheck(String code) {
        log.info("Check qrcode text: {}", code);
        String originalText = seed.decryptUrl(code);
        Boolean result = qRcodeService.checkQRCode(originalText);
        if (!result) {
            log.error("Not a valid qrcode");
            throw new ResourceNotFoundException("QRCode", "code", code);
        }
        log.info("Valid qrcode: {}", originalText);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
