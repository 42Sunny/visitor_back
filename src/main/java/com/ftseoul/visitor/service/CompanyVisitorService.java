package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Company;
import com.ftseoul.visitor.data.CompanyRepository;
import com.ftseoul.visitor.data.CompanyVisitor;
import com.ftseoul.visitor.data.CompanyVisitorRepository;
import com.ftseoul.visitor.data.visitor.VisitorStatus;
import com.ftseoul.visitor.dto.companyvisitor.CompanyVisitorRequestDto;
import com.ftseoul.visitor.dto.companyvisitor.CompanyVisitorResponseDto;
import com.ftseoul.visitor.dto.companyvisitor.CompanyVisitorSearchDto;
import com.ftseoul.visitor.exception.error.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyVisitorService {

    private final CompanyVisitorRepository companyVisitorRepository;
    private final CompanyRepository companyRepository;

    public List<CompanyVisitorResponseDto> findAllVisitorByDate(CompanyVisitorSearchDto companyVisitorSearchDto) {
        return companyVisitorRepository.findAllByCheckInTimeBetween(
                companyVisitorSearchDto.getStart().atStartOfDay(),
                companyVisitorSearchDto.getEnd().atTime(23, 59, 59),
                companyVisitorSearchDto.getPage()
        ).getContent()
                .stream().map(
                        companyVisitor -> {
                            Company company = companyRepository.findById(companyVisitor.getCompanyId())
                                    .orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyVisitor.getCompanyId()));
                            return companyVisitor.companyVisitorResponseDto(company.getName());
                        }
                ).collect(Collectors.toList());
    }

    public CompanyVisitor saveCompanyVisitor(CompanyVisitorRequestDto companyVisitorRequestDto) {
        Company company = companyRepository.findById(companyVisitorRequestDto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyVisitorRequestDto.getCompanyId()));
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
                        .orElseThrow(() -> new ResourceNotFoundException("CompanyVisitor", "id", visitorId))
                        .checkOut()
        );
        return null;
    }
}
