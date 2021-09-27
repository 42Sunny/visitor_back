package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.dto.AddStaffRequestDto;
import com.ftseoul.visitor.dto.StaffDecryptDto;
import com.ftseoul.visitor.dto.StaffDeleteDto;
import com.ftseoul.visitor.dto.StaffModifyDto;
import com.ftseoul.visitor.dto.StaffNameDto;
import com.ftseoul.visitor.dto.payload.Response;
import com.ftseoul.visitor.exception.ResourceNotFoundException;
import com.ftseoul.visitor.service.StaffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StaffController {
    private final StaffService staffService;

    @PostMapping("/admin/staff/save")
    public boolean add_staff(@RequestBody AddStaffRequestDto requestDto) {
        log.info("Add Staff: " + requestDto);
        staffService.saveStaff(requestDto);
        return true;
    }

    @GetMapping("/admin/staff")
    public List<StaffDecryptDto> staffListAll() {
        return staffService.findAllStaff();
    }

    @PutMapping("/admin/staff")
    public ResponseEntity<Response> modifyStaff(@RequestBody StaffModifyDto staffModifyDto) {
        Response response = staffService.modifyStaff(staffModifyDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/admin/staff")
    public ResponseEntity<Response> deleteStaff(@RequestBody StaffDeleteDto dto) {
        Response response = staffService.deleteStaff(dto.getStaffId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/staff")
    public ResponseEntity<?> checkStaffByName(@RequestBody StaffNameDto staffNameDto) {
        boolean result = staffService.existByName(staffNameDto.getStaffName());
        if (!result) {
            throw new ResourceNotFoundException("Staff", "StaffName", staffNameDto.getStaffName());
        }
        return new ResponseEntity<>(new Response("2000", "조회성공"), HttpStatus.OK);
    }
}
