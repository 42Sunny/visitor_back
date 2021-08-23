package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.data.AdminRepository;
import com.ftseoul.visitor.dto.AdminLoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final AdminRepository adminRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/join")
    public boolean join(@RequestBody AdminLoginDto adminLoginDto) {
        log.info("join: " + adminLoginDto);
        adminLoginDto.setPassword(passwordEncoder.encode(adminLoginDto.getPassword()));
        adminRepository.save(adminLoginDto.toEntity());
        return true;
    }
}
