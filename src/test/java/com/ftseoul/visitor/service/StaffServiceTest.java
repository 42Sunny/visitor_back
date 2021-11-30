package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Reserve;
import com.ftseoul.visitor.data.ReserveRepository;
import com.ftseoul.visitor.data.Staff;
import com.ftseoul.visitor.data.StaffRepository;
import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.dto.payload.Response;
import com.ftseoul.visitor.dto.staff.AddStaffRequestDto;
import com.ftseoul.visitor.dto.staff.StaffDecryptDto;
import com.ftseoul.visitor.dto.staff.StaffModifyDto;
import com.ftseoul.visitor.encrypt.Seed;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Rollback(value = true)
class StaffServiceTest {
    @Autowired
    private Seed seed;

    @Autowired
    private ReserveRepository reserveRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private StaffService staffService;

    Reserve savedReserve;

    Staff savedStaff;

    private final String NAME = "임시";

    @BeforeAll
    void setup() {
        init();
    }

    @AfterAll
    void cleanup() {
        finish();
    }


    void init() {
        Staff staff = Staff
            .builder()
            .name(seed.encrypt(NAME))
            .phone(seed.encrypt("01012345678"))
            .build();
        savedStaff = staffRepository.save(staff);

        Reserve reserve = Reserve
            .builder()
            .targetStaff(savedStaff.getId())
            .purpose("방문")
            .date(LocalDateTime.now())
            .place("개포")
            .build();
        savedReserve = reserveRepository.save(reserve);
    }

    void finish() {
        staffRepository.delete(savedStaff);
        reserveRepository.delete(savedReserve);
    }


    @Test
    @Transactional
    void 스태프이름조회() {
        Staff staff = staffService.findByName(seed.encrypt(NAME));
        assertEquals(staff.getId(), savedStaff.getId());
    }

    @Test
    @Transactional
    void 스태프저장() {
        final String toCheck = "staff42";
        AddStaffRequestDto toSave = new AddStaffRequestDto(toCheck, "01011111111", "시설관리팀");
        staffService.saveStaff(toSave);
        Optional<Staff> findStaff = staffRepository.findByName(seed.encrypt(toCheck));
        assertDoesNotThrow(() -> findStaff.get());
        assertEquals(staffService.decryptStaff(findStaff.get()).getName(), toCheck);
    }

    @Test
    @Transactional
    void 스태프수정() {
        final String expected = "수정된 이름";
        StaffModifyDto request = new StaffModifyDto(savedStaff.getId(), expected, "01011112222", "시설관리");
        staffService.modifyStaff(request);

        Optional<Staff> findStaff = staffRepository.findById(savedStaff.getId());
        assertDoesNotThrow(() -> findStaff.get());
        String toCheck = findStaff.get().decrypt(seed).getName();
        assertEquals(expected, toCheck);

        //staff id 가 조회 안되는 경우
        StaffModifyDto request2 = new StaffModifyDto(Long.valueOf(Integer.MAX_VALUE), expected, "01011112222", "시설관리");
        Response failResponse = staffService.modifyStaff(request2);
        assertEquals("4040", failResponse.getCode());

    }

    @Test
    @Transactional
    void 전체스태프조회() {
        List<StaffDecryptDto> result = staffService.findAllStaff();
        assertNotNull(result);
        assertNotEquals(result, 0);
    }

    @Test
    void 이름으로_존재여부_파악() {
        boolean result = staffService.existByName(seed.decrypt(savedStaff.getName()));
        assertTrue(result);
        boolean falseResult = staffService.existByName(seed.encrypt(UUID.randomUUID().toString()));
        assertFalse(falseResult);
    }

    @Test
    @Transactional
    void 스태프삭제() {
        Staff staff1 = Staff
            .builder()
            .name(seed.encrypt("staff1"))
            .phone(seed.encrypt("01012345678"))
            .build();
        Staff saveStaff = staffRepository.save(staff1);
        Response successResponse = staffService.deleteStaffById(saveStaff.getId());
        assertEquals(successResponse.getCode(), "2000");
        //참고 - 우리나라 인구수는 1억이 안 된다..
        Response falseResponse = staffService.deleteStaffById(Long.valueOf(Integer.MAX_VALUE));
        assertEquals(falseResponse.getCode(), "4040");
    }

    @Test
    @Transactional
    void 문자메세지형식() {
        final String domain = "https://dev.vstr.kr";
        final String shortUrl = "aAb1";

        String modifyTemplate = "[예약수정]\n" + "상세 확인: "+ domain + "/"  + shortUrl;
        assertEquals(modifyTemplate, staffService.createModifySMSMessage(null, shortUrl));

        List<Visitor> visitors = new ArrayList<>();
        visitors.add(Visitor
            .builder()
            .name(seed.encrypt(NAME))
            .phone(seed.encrypt("01011112323"))
            .organization("42")
            .reserve_id(savedReserve.getId())
            .build());

        LocalDateTime now = LocalDateTime.now();

        String result = staffService.createSaveSMSMessage(visitors, now, shortUrl);

        String saveTemplate = "[방문신청]\n"
            + now.format(DateTimeFormatter.ofPattern("MM/dd HH:mm")) + "\n"
            + NAME + "님" + "\n상세 확인: " + domain + "/" + shortUrl;

        assertEquals(saveTemplate, result);
    }
}
