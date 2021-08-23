package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.dto.AdminLoginDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @PostMapping("/join")
    public String join(AdminLoginDto adminLoginDto) {

    }
}
