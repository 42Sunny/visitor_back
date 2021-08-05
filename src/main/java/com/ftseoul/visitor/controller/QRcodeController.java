package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.data.VisitorRepository;
import com.ftseoul.visitor.encrypt.Seed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class QRcodeController {

    private final VisitorRepository visitorRepository;
//    private final EncryptConfig config;
    private final Seed seed;

    @GetMapping("/qrcode-check")
    public String qrcodeCheck(String code) {
        return "test";
    }

    @GetMapping("/test")
    public void testencrypt() {
        System.out.println(seed.encrypt("01012345678"));
//        System.out.println("seed: " + s);
//        System.out.println("key: " + config.getKey());
    }
}
