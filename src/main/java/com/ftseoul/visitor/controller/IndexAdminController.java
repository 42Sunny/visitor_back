package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class IndexAdminController {

    private final StaffService staffService;
    private final Seed seed;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("staffs", staffService.findAllStaff());
        return "index";
    }

    @GetMapping("/staff-add")
    public String staffSave() {
        return "staff-add";
    }
}
