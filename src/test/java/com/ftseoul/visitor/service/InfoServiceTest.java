package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Reserve;
import com.ftseoul.visitor.data.ReserveRepository;
import com.ftseoul.visitor.data.Staff;
import com.ftseoul.visitor.data.StaffRepository;
import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.data.VisitorRepository;
import com.ftseoul.visitor.data.visitor.VisitorStatus;
import com.ftseoul.visitor.dto.payload.VisitorStatusInfo;
import com.ftseoul.visitor.dto.reserve.DateFoundResponseDto;
import com.ftseoul.visitor.dto.visitor.UpdateVisitorStatusDto;
import com.ftseoul.visitor.encrypt.Seed;
import java.time.LocalDateTime;
import java.util.List;
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
class InfoServiceTest {
    @Autowired
    private InfoService infoService;

    @Autowired
    private Seed seed;

    @Autowired
    private ReserveRepository reserveRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    Staff savedStaff;

    Reserve savedReserve;

    Visitor savedVisitor;

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
            .name(seed.encrypt("abcde"))
            .phone(seed.encrypt("01012345678"))
            .department("시설관리")
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

        Visitor visitor = Visitor
            .builder()
            .name(seed.encrypt("홍길동님"))
            .organization("이노베이션아카데미")
            .reserve_id(savedReserve.getId())
            .phone(seed.encrypt("01011112222"))
            .build();
        savedVisitor = visitorRepository.save(visitor);
    }

    void finish() {
        reserveRepository.delete(savedReserve);
        visitorRepository.delete(savedVisitor);
        staffRepository.delete(savedStaff);
    }

    @Test
    @Transactional
    void 날짜별조회() {
        List<DateFoundResponseDto> list = infoService.findAllByDate(
            LocalDateTime.now().toLocalDate());
        assertNotEquals(list.size(), 0);
    }

    @Test
    @Transactional
    void 방문자상태변경() {
        UpdateVisitorStatusDto dto = new UpdateVisitorStatusDto
            (new VisitorStatusInfo(savedVisitor.getId(),
            VisitorStatus.퇴실));
        infoService.changeVisitorStatus(dto);
        assertEquals(VisitorStatus.퇴실, visitorRepository.findById(savedVisitor.getId()).get().getStatus());
    }
}
