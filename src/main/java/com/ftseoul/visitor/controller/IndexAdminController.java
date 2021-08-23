package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.data.Role;
import com.ftseoul.visitor.dto.AdminLoginDto;
import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class IndexAdminController {

    private final StaffService staffService;
    private final Seed seed;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model) {
        System.out.println("Role\nname: " + Role.ADMIN.name()
        + "\nkey: " + Role.ADMIN.getKey());
        model.addAttribute("staffs", staffService.findAllStaff());
        String username = (String) httpSession.getAttribute("username");
        if (username != null) {
            model.addAttribute("userName", username);
        }
        return "index";
    }

    @GetMapping("/staff-add")
    public String staffSave() {
        return "staff-add";
    }

    @GetMapping("/joinform")
    public String joinform() {
        return "joinForm";
    }
}
