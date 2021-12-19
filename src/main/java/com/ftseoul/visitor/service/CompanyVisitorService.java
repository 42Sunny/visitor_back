package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Company;
import com.ftseoul.visitor.data.CompanyRepository;
import com.ftseoul.visitor.data.CompanyVisitor;
import com.ftseoul.visitor.data.CompanyVisitorRepository;
import com.ftseoul.visitor.data.visitor.VisitorStatus;
import com.ftseoul.visitor.dto.companyvisitor.CompanyVisitorRequestDto;
import com.ftseoul.visitor.dto.companyvisitor.CompanyVisitorSearchDto;
import com.ftseoul.visitor.exception.error.CompanyNotFoundException;
import com.ftseoul.visitor.exception.error.CompanyVisitorNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyVisitorService {

    private final CompanyVisitorRepository companyVisitorRepository;
    private final CompanyRepository companyRepository;

    public List<CompanyVisitor> findAllVisitorByDate(CompanyVisitorSearchDto companyVisitorSearchDto) {
        return companyVisitorRepository.findAllByCheckInTimeBetween(
                companyVisitorSearchDto.getStart().atStartOfDay(),
                companyVisitorSearchDto.getEnd().atTime(23, 59, 59),
                companyVisitorSearchDto.getPage()
        ).getContent();
    }

    public CompanyVisitor saveCompanyVisitor(CompanyVisitorRequestDto companyVisitorRequestDto) {
        Company company = companyRepository.findById(companyVisitorRequestDto.getCompanyId())
                .orElseThrow(CompanyNotFoundException::new);
        return companyVisitorRepository.save(
                CompanyVisitor.builder()
                        .companyId(companyVisitorRequestDto.getCompanyId())
                        .name(companyVisitorRequestDto.getVisitorName())
                        .place(companyVisitorRequestDto.getPlace())
                        .checkInTime(LocalDateTime.now())
                        .status(VisitorStatus.입실)
                        .build()
        );
    }

    public Void checkOutCompanyVisitor(Long visitorId) {
        companyVisitorRepository.save(
                companyVisitorRepository.findById(visitorId)
                        .orElseThrow(CompanyVisitorNotFoundException::new)
                        .checkOut()
        );
        return null;
    }
}
