package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.dto.companyvisitor.CompanyVisitorRequestDto;
import com.ftseoul.visitor.dto.companyvisitor.CompanyVisitorSearchDto;
import com.ftseoul.visitor.service.CompanyVisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/company/visitor")
public class CompanyVisitorController {

    private final CompanyVisitorService companyVisitorService;

    @PostMapping("/date")
    public ResponseEntity<?> searchCompanyVisitor(@Valid @RequestBody CompanyVisitorSearchDto companyVisitorSearchDto) {
        return ResponseEntity.ok(companyVisitorService.findAllVisitorByDate(companyVisitorSearchDto));
    }

    @PostMapping()
    public ResponseEntity<?> saveCompanyVisitor(@Valid @RequestBody CompanyVisitorRequestDto companyVisitorRequestDto) {
        return ResponseEntity.ok(companyVisitorService.saveCompanyVisitor(companyVisitorRequestDto).getName() +
                "가 입실했습니다.");
    }

    @PatchMapping("/out/{visitorId}")
    public ResponseEntity<?> checkoutCompanyVisitor(@PathVariable Long visitorId) {
        return ResponseEntity.ok(companyVisitorService.checkOutCompanyVisitor(visitorId));
    }
}
