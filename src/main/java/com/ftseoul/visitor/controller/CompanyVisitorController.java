package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.dto.companyvisitor.CompanyVisitorRequestDto;
import com.ftseoul.visitor.dto.companyvisitor.CompanyVisitorResponseDto;
import com.ftseoul.visitor.dto.companyvisitor.CompanyVisitorSearchDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/company/visitor")
public class CompanyVisitorController {

    @PostMapping("/date")
    public ResponseEntity<?> searchCompanyVisitor(@RequestBody CompanyVisitorSearchDto companyVisitorSearchDto) {
        return ResponseEntity.ok(CompanyVisitorResponseDto.mockCompanyVisitorList(companyVisitorSearchDto));
    }

    @PostMapping()
    public ResponseEntity<?> saveCompanyVisitor(@RequestBody CompanyVisitorRequestDto companyVisitorRequestDto) {
        return ResponseEntity.ok(companyVisitorRequestDto.toString());
    }

    @PatchMapping("/out/{visitorId}")
    public ResponseEntity<?> checkoutCompanyVisitor(@PathVariable Long visitorId) {
        return ResponseEntity.ok(visitorId + " 가 체크아웃 했습니다.");
    }
}
