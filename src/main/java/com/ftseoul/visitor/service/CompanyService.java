package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Company;
import com.ftseoul.visitor.data.CompanyRepository;
import com.ftseoul.visitor.dto.company.CompanyRequestDto;
import com.ftseoul.visitor.dto.company.CompanyResponseDto;
import com.ftseoul.visitor.exception.error.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<CompanyResponseDto> findAllCompany() {
        return companyRepository.findAllByIsDeletedIsFalse()
                .stream()
                .map(Company::companyResponseDto)
                .collect(Collectors.toList());
    }

    public Company saveCompany(CompanyRequestDto companyRequestDto) {
        return companyRepository.save(Company.builder()
                .name(companyRequestDto.getName())
                .phone(companyRequestDto.getPhone())
                .isDeleted(false)
                .build()
        );
    }

    public Void deleteCompany(Long companyId) {
        companyRepository.save(
                companyRepository.findById(companyId)
                        .orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId))
                        .LogicalDelete()
        );
        return null;
    }
}
