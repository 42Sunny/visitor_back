package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.dto.staff.AddStaffRequestDto;
import com.ftseoul.visitor.dto.staff.StaffDeleteDto;
import com.ftseoul.visitor.dto.staff.StaffModifyDto;
import com.ftseoul.visitor.dto.staff.StaffNameDto;
import com.ftseoul.visitor.dto.payload.Response;
import com.ftseoul.visitor.exception.error.ResourceNotFoundException;
import com.ftseoul.visitor.exception.error.WAuthUnAuthorizedException;
import com.ftseoul.visitor.filter.WAuthFilter;
import com.ftseoul.visitor.service.ReserveService;
import com.ftseoul.visitor.service.StaffService;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class StaffController {
    private final StaffService staffService;
    private final ReserveService reserveService;
    private final WAuthFilter wAuthFilter;

    @PostMapping("/admin/staff/save")
    public boolean addStaff(@RequestBody AddStaffRequestDto requestDto,
                            HttpServletRequest httpServletRequest) {
        if (!wAuthFilter.isAuthorized(httpServletRequest)) {
            throw new WAuthUnAuthorizedException();
        }
        log.info("Add Staff: " + requestDto);
        staffService.saveStaff(requestDto);
        return true;
    }

    @GetMapping("/admin/staff")
    public ResponseEntity<?> getAllStaff(HttpServletRequest httpServletRequest) {
        if (!wAuthFilter.isAuthorized(httpServletRequest)) {
            throw new WAuthUnAuthorizedException();
        }
        return ResponseEntity.ok(staffService.findAllStaff());
    }

    @PutMapping("/admin/staff")
    public ResponseEntity<?> updateStaff(@RequestBody StaffModifyDto staffModifyDto,
                                         HttpServletRequest httpServletRequest) {
        if (!wAuthFilter.isAuthorized(httpServletRequest)) {
            throw new WAuthUnAuthorizedException();
        }
        Response response = staffService.modifyStaff(staffModifyDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/admin/staff")
    @Transactional
    public ResponseEntity<?> deleteStaff(@RequestBody StaffDeleteDto dto,
                                         HttpServletRequest httpServletRequest) {
        if (!wAuthFilter.isAuthorized(httpServletRequest)) {
            throw new WAuthUnAuthorizedException();
        }
        Response response = staffService.deleteStaffById(dto.getStaffId());
        reserveService.deleteAllByStaffId(dto.getStaffId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/staff")
    public ResponseEntity<?> checkStaffExistByName(@RequestBody StaffNameDto staffNameDto,
                                                   HttpServletRequest httpServletRequest) {
        boolean result = staffService.existByName(staffNameDto.getStaffName());
        if (!result) {
            throw new ResourceNotFoundException("Staff", "StaffName", staffNameDto.getStaffName());
        }
        return new ResponseEntity<>(new Response("2000", "조회성공"), HttpStatus.OK);
    }
}
