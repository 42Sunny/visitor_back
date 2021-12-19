package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.dto.company.CompanyRequestDto;
import com.ftseoul.visitor.dto.company.CompanyResponseDto;
import com.ftseoul.visitor.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/company")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping()
    public ResponseEntity<?> searchCompanyList() {
        return ResponseEntity.ok(
                companyService.findAllCompany()
        );
    }

    @PostMapping()
    public ResponseEntity<?> saveCompany(@Valid @RequestBody CompanyRequestDto companyRequestDto) {
        return ResponseEntity.ok(
                companyService.saveCompany(companyRequestDto).companyResponseDto().getName() + "가 등록되었습니다."
        );
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(
                companyService.deleteCompany(companyId)
        );
    }
}
