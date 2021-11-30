package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Staff;
import com.ftseoul.visitor.data.StaffRepository;
import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.dto.staff.AddStaffRequestDto;
import com.ftseoul.visitor.dto.staff.StaffDecryptDto;
import com.ftseoul.visitor.dto.staff.StaffModifyDto;
import com.ftseoul.visitor.dto.payload.Response;
import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.exception.ResourceNotFoundException;
import com.ftseoul.visitor.util.Constants;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        log.info("Staff Id: " + id);
        return staffRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Staff", "id", id));
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
        return staffRepository
            .findAll()
            .stream()
                .map(staff ->
                        StaffDecryptDto.builder().id(staff.getId())
                                .phone(staff.getPhone())
                                .name(staff.getName())
                                .department(staff.getDepartment())
                                .build())
                .map(staffDecryptDto -> staffDecryptDto.decryptDto(seed))
                .collect(Collectors.toList());
    }

    public StaffDecryptDto decryptStaff(Staff staff) {
        return StaffDecryptDto.builder()
                .id(staff.getId())
                .name(staff.getName())
                .phone(staff.getPhone())
                .department(staff.getDepartment())
                .build().decryptDto(seed);
    }

    public boolean existByName(String name) {
        name = seed.encrypt(name);
        log.info("Staff name is : {}", name);
        return staffRepository.existsStaffByName(name);
    }

    public Response modifyStaff(StaffModifyDto staffModifyDto) {
        log.info("Modify Staff Called");
        Optional<Staff> foundStaff = staffRepository.findById(staffModifyDto.getStaffId());

        if (foundStaff.isEmpty()) {
            return new Response("4040", "해당 스태프가 존재하지 않습니다");
        }

        Staff staff = foundStaff.get();
        log.info("Before Modify- Staff Name is {} and phone is {}", staff.getName(), staff.getPhone());
        staff.update(staffModifyDto.getName(), staffModifyDto.getPhone(), staffModifyDto.getDepartment());
        staff.encrypt(seed);
        staffRepository.save(staff);
        log.info("After Modify- Staff Name is {} and phone is {}", staff.getName(), staff.getPhone(), staff.getDepartment());
        return new Response("2000", "스태프정보를 수정했습니다");
    }

    public Response deleteStaffById(Long staffId) {
        log.info("Delete Staff Id: {}", staffId);
        try{
            staffRepository.deleteById(staffId);
        } catch (RuntimeException ex) {
            log.info(ex.getMessage());
            return new Response("4040", "해당 스태프가 존재하지 않습니다");
        }
        return new Response("2000", "해당 스태프를 삭제했습니다");
    }

    public String createSaveSMSMessage(List<Visitor> visitors, LocalDateTime dateTime, String shortUrl) {
        StringBuilder sb = new StringBuilder();
        sb.append("[방문신청]\n"
            + dateTime.format(DateTimeFormatter.ofPattern("MM/dd HH:mm")) + "\n");
        if (visitors != null && !visitors.isEmpty()) {
            long count = visitors.stream().count() - 1;
            String representor = seed.decrypt(visitors.get(0).getName());
            if (visitors.size() == 1) {
                sb.append(representor + "님");
            } else {
                sb.append(representor + "님 외 " + count + "명");
            }
        }
        sb.append("\n상세 확인: " + Constants.DOMAIN + "/" + shortUrl);
        return sb.toString();
    }

    public String createModifySMSMessage(List<Visitor> visitors, String shortUrl) {
        StringBuilder sb = new StringBuilder();
        sb.append("[예약수정]\n");
        if (visitors != null && !visitors.isEmpty()) {
            long count = visitors.stream().count() - 1;
            String representor = seed.decrypt(visitors.get(0).getName());
            if (visitors.size() == 1) {
                sb.append(representor + "님");
            } else {
                sb.append(representor + "님 외 " + count + "명");
            }
        }
        sb.append("상세 확인: "+ Constants.DOMAIN + "/"  + shortUrl);
        return sb.toString();
    }

    public String createDeleteSMSMessage(List<Visitor> visitors, LocalDateTime dateTime) {
        StringBuilder sb = new StringBuilder();
        sb.append("[예약취소]\n"
                + dateTime.format(DateTimeFormatter.ofPattern("MM/dd HH:mm")) + "\n");
        if (visitors != null && !visitors.isEmpty()) {
            long count = visitors.stream().count() - 1;
            String representor = seed.decrypt(visitors.get(0).getName());
            if (visitors.size() == 1) {
                sb.append(representor + "님");
            } else {
                sb.append(representor + "님 외 " + count + "명");
            }
        }
        return sb.toString();
    }
}
