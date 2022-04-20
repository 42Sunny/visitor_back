package com.ftseoul.visitor.policy;

import com.ftseoul.visitor.data.*;
import com.ftseoul.visitor.dto.reserve.ReserveVisitorDto;
import com.ftseoul.visitor.dto.visitor.VisitorDto;
import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.service.ReserveService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Rollback(value = true)
class ReservePolicyTest {

    @Autowired
    ReservePolicyFactory reservePolicyFactory;

    @Autowired
    private Seed seed;

    @Autowired
    private ReserveRepository reserveRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private ReserveService reserveService;

    @Autowired
    private EntityManager em;

    @BeforeAll
    void setup() {
        init();
    }

    @AfterAll
    void cleanup() {
        finish();
    }

    Staff savedStaff;

    void init(){
        Staff staff = Staff
                .builder()
                .name(seed.encrypt("김길동"))
                .phone(seed.encrypt("01012345678"))
                .department("시설관리")
                .build();
        savedStaff = staffRepository.save(staff);
    }

    void finish(){
        reserveRepository.deleteAll();
        visitorRepository.deleteAll();
        staffRepository.delete(savedStaff);
    }


    @Test
    @DisplayName("예약_서비스_팩토리_테스트")
    void policyFactoryTest(){
        //given
        ReserveType defaultType = ReserveType.DEFAULT;
        ReserveType representativeType = ReserveType.REPRESENTATIVE;
        List<VisitorDto> mockVisitors = new ArrayList<>();
        VisitorDto visitor1 = new VisitorDto("visitor1", "00000000000", "집");
        VisitorDto visitor2 = new VisitorDto("visitor2", "00000000000", "집");

        mockVisitors.add(visitor1);
        mockVisitors.add(visitor2);


        //when
        ReserveVisitorDto temp = new ReserveVisitorDto("개포", "김길동", "테스트", LocalDateTime.now(), ReserveType.REPRESENTATIVE, mockVisitors);
        ReservePolicy defaultPolicy = reservePolicyFactory.getPolicy(temp.getRepresentative());
        Reserve reserve = defaultPolicy.saveReserve(temp);

        //then
        Assertions.assertThat(reserve.getPurpose()).isEqualTo("테스트");

        List<Visitor> visitors = visitorRepository.findAllByReserveId(reserve.getId());
        visitors.forEach(
                visitor -> Assertions.assertThat(seed.decrypt(visitor.getPhone())).isEqualTo("00000000000")
        );
    }
}
