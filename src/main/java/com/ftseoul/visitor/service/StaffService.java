package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Staff;
import com.ftseoul.visitor.data.StaffRepository;
import com.ftseoul.visitor.dto.AddStaffRequestDto;
import com.ftseoul.visitor.dto.StaffDecryptDto;
import com.ftseoul.visitor.dto.StaffModifyDto;
import com.ftseoul.visitor.dto.payload.Response;
import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.exception.ResourceNotFoundException;
import java.util.Optional;
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
        return staffRepository
                .findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "name", name));
    }

    public boolean saveStaff(AddStaffRequestDto requestDto) {
        requestDto.encrypt(seed);
        Staff staff = requestDto.toEntity();
        log.info("save: " + staff);
        staffRepository.save(staff);
        return true;
    }

    public List<StaffDecryptDto> findAllStaff() {
        log.info("Find All Staffs");
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
        name = seed.encrypt(name);
        log.info("Staff name is : {}", name);
        return staffRepository.existsStaffByName(name);
    }

    public Response modifyStaff(StaffModifyDto staffModifyDto) {
        log.info("Modify Staff Called");
        Optional<Staff> foundStaff = staffRepository.findById(staffModifyDto.getId());
        if (foundStaff.isEmpty()) {
            return new Response("4040", "해당 스태프가 존재하지 않습니다");
        }
        Staff staff = foundStaff.get();
        log.info("Before Modify- Staff Name is {} and phone is {}", staff.getName(), staff.getPhone());
        staff.update(staffModifyDto.getName(), staffModifyDto.getPhone());
        staff.encrypt(seed);
        staffRepository.save(staff);
        log.info("After Modify- Staff Name is {} and phone is {}", staff.getName(), staff.getPhone());
        return new Response("2000", "스태프정보를 수정했습니다");
    }

    public Response deleteStaff(Long staffId) {
        log.info("Delete Staff Id: {}", staffId);
        staffRepository.deleteById(staffId);
        return new Response("2000", "해당 스태프를 삭제했습니다");
    }
}
