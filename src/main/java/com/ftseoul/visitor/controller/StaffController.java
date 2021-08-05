package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.data.Staff;
import com.ftseoul.visitor.data.StaffRepository;
import com.ftseoul.visitor.dto.AddStaffRequestDto;
import com.ftseoul.visitor.dto.StaffDecryptDto;
import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.service.StaffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StaffController {
    private final Seed seed;
    private final StaffService staffService;

    @PostMapping("/staff")
    public boolean add_staff(@RequestBody AddStaffRequestDto requestDto) {
        log.info("Add Staff: " + requestDto);
        staffService.saveStaff(requestDto);
//        Staff staff = staffService.findByName(requestDto.getName());
//        log.info("staff add success: " + staffService.findByName(StaffDecryptDto.builder()
//                .id(staff.getId())
//                .name(staff.getName())
//                .phone(staff.getPhone())
//                .build().decryptDto(seed).getName()));
        return true;
    }

    @GetMapping("/staff")
    public List<StaffDecryptDto> staffListAll() {
        return staffService.findAllStaff();
    }
}
