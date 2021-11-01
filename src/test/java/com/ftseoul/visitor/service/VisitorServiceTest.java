package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Reserve;
import com.ftseoul.visitor.data.ReserveRepository;
import com.ftseoul.visitor.data.Staff;
import com.ftseoul.visitor.data.StaffRepository;
import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.data.VisitorRepository;
import com.ftseoul.visitor.dto.reserve.ReserveModifyDto;
import com.ftseoul.visitor.dto.visitor.VisitorDto;
import com.ftseoul.visitor.dto.visitor.VisitorModifyDto;
import com.ftseoul.visitor.encrypt.Seed;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
class VisitorServiceTest {
    @Autowired
    private Seed seed;

    @Autowired
    private ReserveRepository reserveRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private VisitorService visitorService;

    Staff savedStaff;

    Reserve savedReserve;

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
    }

    void finish() {
        reserveRepository.delete(savedReserve);
        staffRepository.delete(savedStaff);
    }

    @Test
    @Transactional
    void 방문자생성() {
        List<VisitorDto> toSave = new ArrayList<>();
        VisitorDto v1 = new VisitorDto(seed.encrypt("김수한무1"),
            seed.encrypt("01011111111"), "이노아카");
        VisitorDto v2 = new VisitorDto(seed.encrypt("김수한무2"),
            seed.encrypt("01022222222"), "이노아카");
        toSave.add(v1);
        toSave.add(v2);
        int resultCount = toSave.size();
        visitorService.saveVisitors(savedReserve.getId(), toSave);
        assertEquals(visitorRepository.findAllByReserveId(savedReserve.getId()).size(), resultCount);
    }

    @Test
    @Transactional
    void 방문자수정() {
        List<VisitorModifyDto> visitors = new ArrayList<>();

        VisitorModifyDto v1 = new VisitorModifyDto(savedReserve.getId(),
            seed.encrypt("김수한무1"),
            seed.encrypt("01011111111"),
            "이노아카", false);
        VisitorModifyDto v2 = new VisitorModifyDto(savedReserve.getId(),
            seed.encrypt("김수한무2"),
            seed.encrypt("01022222222"), "이노아카",false);
        VisitorModifyDto v3 = new VisitorModifyDto(savedReserve.getId(),
            seed.encrypt("김수한무3"),
            seed.encrypt("01033333333"), "이노아카",true);
        visitors.add(v1);
        visitors.add(v2);
        visitors.add(v3);

        long resultSize = visitors
            .stream()
            .filter(v -> v.isChanged())
            .count();

        ReserveModifyDto request = new ReserveModifyDto(savedReserve.getId(),
            savedReserve.getPlace(), savedStaff.getName(), savedReserve.getPurpose(),
            LocalDateTime.now(), visitors);

        List<Visitor> changedVisitors = visitorService.updateVisitors(request);

        assertEquals(changedVisitors.size(), resultSize);
        assertDoesNotThrow(() -> visitorRepository.findAllByReserveId(savedReserve.getId())
            .stream()
            .filter(v -> seed.decrypt(v.getName()).equals("김수한무3"))
            .findAny().get());
    }

    @Test
    @Transactional
    void 메세지생성() {
        String value = "a";
        String result = visitorService.createSMSMessage(value);

        String domain = "https://dev.vstr.kr";
        String message = "[이노베이션아카데미]\n"
            +"아래 링크 QR을 출입시 제시해주세요\n"
            +domain + "/" + value;

        assertEquals(result, message);
    }
}
