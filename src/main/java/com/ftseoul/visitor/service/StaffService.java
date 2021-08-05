package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Staff;
import com.ftseoul.visitor.data.StaffRepository;
import com.ftseoul.visitor.dto.AddStaffRequestDto;
import com.ftseoul.visitor.dto.StaffDecryptDto;
import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StaffService {
    private final StaffRepository staffRepository;
    private final Seed seed;

    public Staff findById(Long id) {
        log.info("findById: " + id);
        return staffRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", id))
                .decrypt(seed);
    }

    public Staff findByName(String name) {
        log.info("Staff name: " + name);
        String encrypted = seed.encrypt(name);
        return staffRepository
                .findByName(encrypted)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "name", name))
                .decrypt(seed);
    }

    public boolean saveStaff(AddStaffRequestDto requestDto) {
        requestDto.encrypt(seed);
        Staff staff = requestDto.toEntity();
        log.info("save: " + staff);
        staffRepository.save(staff);
        return true;
    }

    public List<StaffDecryptDto> findAllStaff() {
        List<StaffDecryptDto> staffList = staffRepository.findAll().stream()
                .map(staff ->
                        StaffDecryptDto.builder().id(staff.getId())
                                .phone(staff.getPhone())
                                .name(staff.getName())
                                .build())
                .map(staffDecryptDto -> staffDecryptDto.decryptDto(seed))
                .collect(Collectors.toList());
        return staffList;
    }

    public StaffDecryptDto decrypt(Staff staff) {
        return StaffDecryptDto.builder()
                .id(staff.getId())
                .name(staff.getName())
                .phone(staff.getPhone())
                .build().decryptDto(seed);
    }

    public boolean existByName(String name) {
        log.info("Staff name: " + name);
        return staffRepository.existsStaffByName(name);
    }
}
