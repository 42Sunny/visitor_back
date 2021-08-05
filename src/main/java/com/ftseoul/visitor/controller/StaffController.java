package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.dto.StaffNameDto;
import com.ftseoul.visitor.service.StaffService;
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
public class StaffController {
    private final StaffService staffService;

    @PostMapping("/staff")
    public ResponseEntity<Boolean> checkStaffByName(@RequestBody StaffNameDto staffNameDto) {
        log.info("To find staff name: {}", staffNameDto);
        boolean result = staffService.existByName(staffNameDto.getStaffName());
        if (result) {
            return new ResponseEntity<>(true, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
}
