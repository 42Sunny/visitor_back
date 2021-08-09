package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.dto.AddStaffRequestDto;
import com.ftseoul.visitor.dto.StaffDecryptDto;
import com.ftseoul.visitor.dto.StaffNameDto;
import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.service.StaffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/staff/save")
    public boolean add_staff(@RequestBody AddStaffRequestDto requestDto) {
        log.info("Add Staff: " + requestDto);
        staffService.saveStaff(requestDto);
        return true;
    }

    @GetMapping("/staff")
    public List<StaffDecryptDto> staffListAll() {
        return staffService.findAllStaff();
    }

    @PostMapping("/staff")
    public ResponseEntity<Boolean> checkStaffByName(@RequestBody StaffNameDto staffNameDto) {
        log.info("To find staff name: {}", staffNameDto);
        boolean result = staffService.existByName(staffNameDto.getStaffName());
        if (result) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
}
