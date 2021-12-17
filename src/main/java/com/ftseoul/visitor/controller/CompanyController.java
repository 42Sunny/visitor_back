package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.dto.company.CompanyRequestDto;
import com.ftseoul.visitor.dto.company.CompanyResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/company")
public class CompanyController {

    @GetMapping()
    public ResponseEntity<?> searchCompanyList() {
        return ResponseEntity.ok(CompanyResponseDto.mockCompanyList());
    }

    @PostMapping()
    public ResponseEntity<?> saveCompany(@RequestBody CompanyRequestDto companyRequestDto) {
        return ResponseEntity.ok(companyRequestDto.toString() + " post 요청 성공");
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(companyId + " delete 요청 성공");
    }
}
