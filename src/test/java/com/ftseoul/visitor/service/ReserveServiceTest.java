package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.*;
import com.ftseoul.visitor.dto.ReserveResponseDto;
import com.ftseoul.visitor.dto.SearchReserveRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReserveServiceTest {


    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReserveRepository reserveRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private ReserveService reserveService;

    @After
    public void tearDown() throws Exception {
        reserveRepository.deleteAll();
    }

    public Staff createStaff() {
        return Staff.builder()
                .name("홍길동")
                .phone("+8201012341234")
                .build();
    }

    public Reserve createReserve(Long staff) {
        return Reserve.builder()
                .date(LocalDateTime.now())
                .place("개포")
                .purpose("방문 목적")
                .targetStaff(staff)
                .build();
    }

    @Test
    public void Reserve_조회1() throws Exception {
        // given
        Staff staff = createStaff();
        staffRepository.save(staff);
        Reserve reserve = createReserve(staff.getId());
        reserveRepository.save(reserve);
        Visitor visitor = Visitor.builder()
                .name("방문객")
                .phone("+8201012345678")
                .reserve_id(reserve.getId())
                .organization("소속")
                .build();
        visitorRepository.save(visitor);

        // when
        reserveService.findAllByNameAndPhone(
                SearchReserveRequestDto.builder()
                        .name(visitor.getName())
                        .phone(visitor.getPhone())
                        .build());
        // then
//        assertThat()
    }
}